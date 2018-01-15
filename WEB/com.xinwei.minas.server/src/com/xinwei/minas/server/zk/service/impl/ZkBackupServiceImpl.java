/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.service.impl;

import java.io.File;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;

import org.apache.log4j.Logger;

import com.xinwei.minas.core.facade.MinasClientFacade;
import com.xinwei.minas.server.core.secu.service.LoginUserCache;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.platform.CallbackScript;
import com.xinwei.minas.server.zk.backup.ZkBackupManager;
import com.xinwei.minas.server.zk.backup.ZkBackupTaskScheduler;
import com.xinwei.minas.server.zk.backup.ZkBackupUtil;
import com.xinwei.minas.server.zk.dao.ZkBackupTaskDAO;
import com.xinwei.minas.server.zk.net.ZkClusterConnector;
import com.xinwei.minas.server.zk.net.ZkClusterConnectorManager;
import com.xinwei.minas.server.zk.net.ZkNodeHelper;
import com.xinwei.minas.server.zk.net.impl.ZkClusterConnectorZkClientImpl;
import com.xinwei.minas.server.zk.service.SagClusterBizService;
import com.xinwei.minas.server.zk.service.ZkBackupService;
import com.xinwei.minas.zk.core.basic.ZkBackup;
import com.xinwei.minas.zk.core.basic.ZkBackupConstant;
import com.xinwei.minas.zk.core.basic.ZkBackupTask;
import com.xinwei.minas.zk.core.basic.ZkClusterConstant;
import com.xinwei.minas.zk.core.facade.ZkCallbackFacade;
import com.xinwei.minas.zk.core.xnode.common.ZkNode;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant;
import com.xinwei.omp.core.utils.DateUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * NK集群数据备份服务实现
 * 
 * @author fanhaoyu
 * 
 */

public class ZkBackupServiceImpl implements ZkBackupService {

	private static final Logger logger = Logger
			.getLogger(ZkBackupServiceImpl.class);

	private ZkBackupTaskDAO backupTaskDAO;

	private ZkClusterConnectorManager connectorManager;

	private ZkBackupManager backupManager;

	public ZkBackupTaskDAO getBackupTaskDAO() {
		return backupTaskDAO;
	}

	public void setBackupTaskDAO(ZkBackupTaskDAO backupTaskDAO) {
		this.backupTaskDAO = backupTaskDAO;
	}

	public ZkClusterConnectorManager getConnectorManager() {
		return connectorManager;
	}

	public void setConnectorManager(ZkClusterConnectorManager connectorManager) {
		this.connectorManager = connectorManager;
	}

	public ZkBackupManager getBackupManager() {
		return backupManager;
	}

	public void setBackupManager(ZkBackupManager backupManager) {
		this.backupManager = backupManager;
	}

	@Override
	public void createBackup(String backupName, int createType)
			throws RemoteException, Exception {
		// 如果备份空间超出500M，则抛出异常
		double totalSpace = ZkBackupUtil
				.calBackupSpaceSize(ZkBackupConstant.BACKUP_DIR);
		if (totalSpace > ZkBackupConstant.MAX_SPACE_SIZE) {
			throw new Exception(MessageFormat.format(
					OmpAppContext.getMessage("create_backup_failed"),
					OmpAppContext.getMessage("backup_space_over_maxsize")));
		}
		ZkClusterConnector connector = connectorManager
				.getConnector(ZkClusterConstant.ZKCLUSTER_ID);
		if (connector == null) {
			throw new Exception(MessageFormat.format(OmpAppContext
					.getMessage("create_backup_failed"), OmpAppContext
					.getMessage("conn_to_specified_nk_cluster_not_existing")));
		}
		if (connector.getConnectState() == ZkClusterConstant.STATE_DISCONNECTED) {
			throw new Exception(
					OmpAppContext
							.getMessage("conn_to_specified_nk_cluster_not_existing"));
		}
		ZkNode sagRoot = connector.getSagRoot();
		// 如果无SAG群组节点
		if (sagRoot.getChildren().size() == 0) {
			throw new Exception(
					MessageFormat.format(
							OmpAppContext.getMessage("create_backup_failed"),
							OmpAppContext
									.getMessage("ZkBackupService.no_available_data_for_backup")));
		}
		// 如果是通过备份任务自动创建，备份名的命名规则为：“备份-当前时间”
		if (createType == ZkBackup.CREATETYPE_AUTO) {
			Date date = new Date(System.currentTimeMillis());
			backupName += DateUtils.getStringByYYYYMMDDHHMMSS(date);

			logger.info("create backup " + backupName + " automatically");
			ZkBackup backup = ZkBackupUtil.createBackupOnDisk(backupName,
					sagRoot);
			backupManager.addBackup(backup);
			logger.info("finish create backup");

			ZkBackupTask task = ZkBackupTaskScheduler.getInstance().getTask();
			// 更新备份任务上次执行时间
			long briefTime = DateUtils.getBriefTimeFromMillisecondTime(date
					.getTime());
			task.setLastTime(DateUtils.getStandardTimeFromBriefTime(briefTime));
			backupTaskDAO.modifyBackupTask(task);
		} else {
			// 手动创建备份
			logger.info("create backup " + backupName + " manually");

			ZkBackup backup = ZkBackupUtil.createBackupOnDisk(backupName,
					sagRoot);
			backupManager.addBackup(backup);
		}
	}

	@Override
	public ZkBackup queryBackup(String backupName) throws RemoteException,
			Exception {
		return backupManager.getBackup(backupName);
	}

	@Override
	public List<ZkBackup> queryAllBackups() throws RemoteException, Exception {
		return backupManager.getBackupList();
	}

	@Override
	public void refreshBackups() throws RemoteException, Exception {
		backupManager.clearBackupList();
		backupManager.initialize();
	}

	@Override
	public void deleteBackup(String backupName) throws RemoteException,
			Exception {
		byte flag = ZkBackupUtil.deleteBackupOnDisk(backupManager
				.getBackup(backupName));
		// 删除成功
		if (flag == ZkBackupUtil.DEL_SUCCESS) {
			backupManager.removeBackup(backupName);
		} else if (flag == ZkBackupUtil.ERROR_CODE_BACKUP_MISSING) {
			// 备份丢失
			backupManager.removeBackup(backupName);
			throw new Exception(
					MessageFormat.format(OmpAppContext
							.getMessage("ZkBackupService.backup_not_exist"),
							backupName));
		} else if (flag == ZkBackupUtil.ERROR_CODE_DEL_FAILED) {
			throw new Exception(MessageFormat.format(
					OmpAppContext.getMessage("ZkBackupService.delete_failed"),
					backupName));
		}
	}

	@Override
	public void recoverBackup(long zkClusterId, String backupName)
			throws RemoteException, Exception {
		ZkClusterConnectorZkClientImpl connector = (ZkClusterConnectorZkClientImpl) connectorManager
				.getConnector(zkClusterId);
		if (connector == null) {
			throw new Exception(
					OmpAppContext
							.getMessage("conn_to_specified_nk_cluster_not_existing"));
		}
		if (connector.getConnectState() == ZkClusterConstant.STATE_DISCONNECTED) {
			throw new Exception(
					OmpAppContext
							.getMessage("conn_to_specified_nk_cluster_not_existing"));
		}
		// 获取备份中的SAG组列表
		ZkBackup backup = backupManager.getBackup(backupName);
		List<ZkNode> sagGroupList = ZkBackupUtil.getSagGroupsofBackup(backup);

		logger.info("Recover backup " + backupName);
		Lock lock = connector.getLock();
		if (!lock.tryLock()) {
			throw new Exception(
					OmpAppContext.getMessage("connection_is_occupied"));
		}
		try {
			ZkNode sagRoot = connector.getSagRoot();
			// 取消sagRoot监听
			ZkNodeHelper.deregisterOneNodeListener(connector, sagRoot);
			SagClusterBizService service = AppContext.getCtx().getBean(
					SagClusterBizService.class);
			// 递归删除sagRoot换粗中的所有SAG群组及其子节点
			List<ZkNode> list = new CopyOnWriteArrayList<ZkNode>(
					sagRoot.getChildren());
			for (ZkNode sagGroup : list) {
				service.deleteZkNodeRecursive(zkClusterId, sagGroup);
			}
			// 递归添加备份中的SAG群组列表
			for (ZkNode sagGroup : sagGroupList) {
				service.addZkNodeRecursive(zkClusterId, sagGroup,
						ZkNodeConstant.CREATE_MODE_PERSISTENT);
			}
			// 恢复sagRoot监听
			ZkNodeHelper.registerOneNodeListener(connector, sagRoot);
		} finally {
			lock.unlock();
		}

		// 通知客户端刷新界面
		LoginUserCache.getInstance().callback(new CallbackScript() {
			public void execute(MinasClientFacade minasClientFacade)
					throws Exception {
				ZkCallbackFacade zkCallbackFacade = minasClientFacade
						.getFacade(ZkCallbackFacade.class);
				if (zkCallbackFacade != null) {
					zkCallbackFacade
							.notifyZkClientRefresh(ZkNodeConstant.SAG_ROOT_PATH);
				}
			}
		});
	}

	@Override
	public void createBackupTask(ZkBackupTask task) throws RemoteException,
			Exception {
		backupTaskDAO.createBackupTask(task);
	}

	@Override
	public void deleteBackupTask(int taskId) throws RemoteException, Exception {
		backupTaskDAO.deleteBackupTask(taskId);
	}

	@Override
	public void modifyBackupTask(ZkBackupTask task) throws RemoteException,
			Exception {
		backupTaskDAO.modifyBackupTask(task);
	}

	@Override
	public ZkBackupTask queryBackupTask(int taskId) throws RemoteException,
			Exception {
		return backupTaskDAO.queryBackupTask(taskId);
	}

	@Override
	public List<ZkBackupTask> queryBackupTasks() throws RemoteException,
			Exception {
		return backupTaskDAO.queryBackupTasks();
	}

	@Override
	public void openBackupTask(ZkBackupTask task) throws RemoteException,
			Exception {
		ZkBackupTaskScheduler scheduler = ZkBackupTaskScheduler.getInstance();
		scheduler.setTask(task);
		scheduler.start();
		// 存库
		task.setState(ZkBackupTask.STATE_OPEN);
		backupTaskDAO.modifyBackupTask(task);
	}

	@Override
	public void closeBackupTask(ZkBackupTask task) throws RemoteException,
			Exception {
		ZkBackupTaskScheduler.getInstance().stop();
		// 存库
		task.setState(ZkBackupTask.STATE_CLOSED);
		backupTaskDAO.modifyBackupTask(task);
	}

	@Override
	public void releaseBackupSpace() throws RemoteException, Exception {

		List<ZkBackup> backupList = backupManager.getBackupList();
		// 所有备份按创建时间排序，旧的在前
		Collections.sort(backupList, new Comparator<ZkBackup>() {
			@Override
			public int compare(ZkBackup b1, ZkBackup b2) {
				return (int) (b1.getCreateTime().getTime() - b2.getCreateTime()
						.getTime());
			}
		});

		List<ZkBackup> backupDel = new LinkedList<ZkBackup>();
		double sizeCount = 0;
		// 根据要释放空间的大小确定要删除的备份
		for (ZkBackup backup : backupList) {
			sizeCount += backup.getSpaceSize();
			backupDel.add(backup);
			if (sizeCount >= ZkBackupConstant.RELEASE_LIMIT)
				break;
		}
		// 开始删除
		for (ZkBackup backup : backupDel) {
			ZkBackup temp = backupManager.getBackup(backup.getName());
			byte flag = ZkBackupUtil.deleteBackupOnDisk(temp);
			if (flag == ZkBackupUtil.DEL_SUCCESS) {
				backupManager.removeBackup(backup.getName());
			} else if (flag == ZkBackupUtil.ERROR_CODE_BACKUP_MISSING) {
				backupManager.removeBackup(backup.getName());
				throw new Exception(MessageFormat.format(OmpAppContext
						.getMessage("ZkBackupService.backup_not_exist"), backup
						.getName()));
			} else if (flag == ZkBackupUtil.ERROR_CODE_DEL_FAILED) {
				throw new Exception(MessageFormat.format(OmpAppContext
						.getMessage("ZkBackupService.delete_failed"), backup
						.getName()));
			}
		}
	}

	@Override
	public Map<String, byte[]> downloadBackup(String backupName)
			throws RemoteException, Exception {
		HashMap<String, byte[]> fileMap = new HashMap<String, byte[]>();

		String filePath = new StringBuffer()
				.append(ZkBackupConstant.BACKUP_DIR_NAME)
				.append(File.separator).append(backupName)
				.append(File.separator).toString();
		for (String fileName : backupManager.getBackup(backupName)
				.getFileList()) {
			// 获取备份中SAG群组文件字节流
			byte[] fileBytes = ZkBackupUtil.getBackupFileBytes(filePath
					+ fileName);
			// 放入Map
			fileMap.put(fileName, fileBytes);
		}
		return fileMap;
	}

	@Override
	public double queryTotalBackupSpace() throws RemoteException, Exception {
		return ZkBackupUtil.calBackupSpaceSize(ZkBackupConstant.BACKUP_DIR);
	}
}