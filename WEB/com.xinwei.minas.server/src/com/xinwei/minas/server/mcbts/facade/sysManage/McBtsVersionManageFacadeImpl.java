/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-19	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.McBtsVersionManageFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsCodeDownloadTask;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersionHistory;
import com.xinwei.minas.server.mcbts.service.sysManage.McBtsVersionManageService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 
 * @author chenshaohua
 * 
 */

@SuppressWarnings("serial")
public class McBtsVersionManageFacadeImpl extends UnicastRemoteObject implements
		McBtsVersionManageFacade {

	private Log log = LogFactory.getLog(McBtsVersionManageFacadeImpl.class);

	private McBtsVersionManageService service;

	private SequenceService sequenceService;

	protected McBtsVersionManageFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(McBtsVersionManageService.class);
		sequenceService = AppContext.getCtx().getBean(SequenceService.class);
	}

	@Override
	public List<McBtsVersion> queryAll() throws RemoteException, Exception {
		return service.queryAll();
	}

	@Override
	public void delete(OperObject operObject, McBtsVersion mcBtsVersion)
			throws RemoteException, Exception {
		// 若没有使用则删除数据库
		service.delete(mcBtsVersion);
	}

	@Override
	public void saveOrUpdate(OperObject operObject, McBtsVersion mcbtsVersion)
			throws RemoteException, Exception {
		if (mcbtsVersion.getIdx() == null) {
			mcbtsVersion.setIdx(sequenceService.getNext());
		}
		try {
			service.saveOrUpdate(mcbtsVersion);
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * 下载基站软件
	 * 
	 * @param mo
	 * @param btsId
	 * @param mcBtsVersion
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public int download(OperObject operObject, Mo mo, Long btsId,
			McBtsVersion mcBtsVersion) throws RemoteException, Exception {
		return service.download(mo, btsId, mcBtsVersion);
	}

	/**
	 * 基于基站的类型查询基站版本
	 * 
	 * @param btsType
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public List<McBtsVersion> queryByBtsType(Integer btsType)
			throws RemoteException, Exception {
		return service.queryByBtsType(btsType);
	}

	/**
	 * 基于基站ID查询所有这个基站的版本下载记录
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public List<McBtsVersionHistory> queryDownloadHistory(Long btsId)
			throws RemoteException, Exception {
		return service.queryDownloadHistory(btsId);
	}

	/**
	 * 获取最后一条任务的状态(时间,状态)
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public McBtsCodeDownloadTask getLatestStatus(Long btsId)
			throws RemoteException, Exception {
		return service.getLatestStatus(btsId);
	}

	@Override
	public Map<Long, String> queryCurrentDownloadTasks()
			throws RemoteException, Exception {
		return service.queryCurrentDownloadTasks();
	}

	/**
	 * 删除某个基站下的所有下载记录
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public int deleteAllHistory(Long btsId) throws RemoteException, Exception {
		return service.deleteAllHistory(btsId);
	}

	/**
	 * 删除某个历史记录
	 * 
	 * @param history
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public int deleteHistory(McBtsVersionHistory history)
			throws RemoteException, Exception {
		return service.deleteHistory(history);
	}

	/**
	 * 向基站发送升级请求
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public void upgrade(OperObject operObject, Long moId, Integer ho_type)
			throws RemoteException, Exception {
		service.upgrade(moId, ho_type);
	}

	@Override
	public void add(String fileName, byte[] fileContent)
			throws RemoteException, Exception {
		service.add(fileName, fileContent);
	}

	@Override
	public boolean deleteFile(String fileName) throws RemoteException,
			Exception {
		return service.deleteFile(fileName);
	}

	public void add(McBtsVersion mcBtsVersion) throws RemoteException,
			Exception {
		// 设置idx
		if (mcBtsVersion.getIdx() == null) {
			mcBtsVersion.setIdx(sequenceService.getNext());
		}
		service.add(mcBtsVersion);
	}
}
