package com.xinwei.minas.server.zk.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.zk.service.ZkBackupService;
import com.xinwei.minas.zk.core.basic.ZkBackup;
import com.xinwei.minas.zk.core.basic.ZkBackupTask;
import com.xinwei.minas.zk.core.facade.ZkBackupFacade;

@SuppressWarnings("serial")
public class ZkBackupFacadeImpl extends UnicastRemoteObject implements
		ZkBackupFacade {

	private ZkBackupService service;

	protected ZkBackupFacadeImpl() throws RemoteException {
		super();
		service = AppContext.getCtx().getBean(ZkBackupService.class);
	}

	@Override
	public void createBackup(String backupName, int createType) throws RemoteException,
			Exception {
		service.createBackup(backupName, createType);
	}

	@Override
	public ZkBackup queryBackup(String backupName) throws RemoteException,
			Exception {
		return service.queryBackup(backupName);
	}

	@Override
	public List<ZkBackup> queryAllBackups() throws RemoteException, Exception {
		return service.queryAllBackups();
	}

	@Override
	public void refreshBackups() throws RemoteException, Exception {
		service.refreshBackups();		
	}

	@Override
	public void deleteBackup(String backupName) throws RemoteException,
			Exception {
		service.deleteBackup(backupName);
	}

	@Override
	public void recoverBackup(long zkClusterId, String backupName) throws RemoteException,
			Exception {
		service.recoverBackup(zkClusterId, backupName);
	}

	@Override
	public void createBackupTask(ZkBackupTask task) throws RemoteException,
			Exception {
		service.createBackupTask(task);
	}

	@Override
	public void deleteBackupTask(int taskId) throws RemoteException, Exception {
		service.deleteBackupTask(taskId);
	}

	@Override
	public void modifyBackupTask(ZkBackupTask task) throws RemoteException,
			Exception {
		service.modifyBackupTask(task);
	}

	@Override
	public ZkBackupTask queryBackupTask(int taskId) throws RemoteException,
			Exception {
		return service.queryBackupTask(taskId);
	}

	@Override
	public List<ZkBackupTask> queryBackupTasks() throws RemoteException,
			Exception {
		return service.queryBackupTasks();
	}

	@Override
	public void openBackupTask(ZkBackupTask task) throws RemoteException,
			Exception {
		service.openBackupTask(task);		
	}

	@Override
	public void closeBackupTask(ZkBackupTask task) throws RemoteException,
			Exception {
		service.closeBackupTask(task);
	}

	@Override
	public void releaseBackupSpace() throws RemoteException, Exception {
		service.releaseBackupSpace();
	}

	@Override
	public Map<String, byte[]> downloadBackup(String backupName) throws RemoteException,
			Exception {
		return service.downloadBackup(backupName);
	}

	@Override
	public double queryTotalBackupSpace() throws RemoteException, Exception {
		return service.queryTotalBackupSpace();
	}
}