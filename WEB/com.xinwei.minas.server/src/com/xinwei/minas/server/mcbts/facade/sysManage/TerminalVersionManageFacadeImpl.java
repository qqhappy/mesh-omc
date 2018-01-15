/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-18	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.TerminalVersionManageFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.TDLHistoryKey;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersionType;
import com.xinwei.minas.mcbts.core.model.sysManage.UTCodeDownloadTask;
import com.xinwei.minas.server.mcbts.service.sysManage.TerminalVersionManageService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 终端版本管理
 * 
 * @author tiance
 * 
 */

public class TerminalVersionManageFacadeImpl extends UnicastRemoteObject
		implements TerminalVersionManageFacade {

	private final TerminalVersionManageService terminalVersionManageService;

	protected TerminalVersionManageFacadeImpl() throws RemoteException {
		super();
		terminalVersionManageService = AppContext.getCtx().getBean(
				TerminalVersionManageService.class);
	}

	/**
	 * 获取服务器上的所有终端版本的列表
	 * 
	 */
	@Override
	public List<TerminalVersion> queryAll() throws RemoteException, Exception {
		return terminalVersionManageService.queryAll();
	}

	/**
	 * 根据typeId获取某个终端的所有版本
	 * 
	 * @param typeId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public List<TerminalVersion> queryByTypeId(int typeId)
			throws RemoteException, Exception {
		return terminalVersionManageService.queryByTypeId(typeId);
	}

	/**
	 * 从服务器上删除一个终端版本
	 */
	@Override
	public void delete(OperObject operObject, TerminalVersion terminalVersion) throws RemoteException,
			Exception {
		terminalVersionManageService.delete(terminalVersion);
	}

	/**
	 * 根据typeId和version,从服务器获取某个终端版本信息
	 */
	@Override
	@Deprecated
	// 已改成从服务器端验证
	public TerminalVersion queryByTypeIdAndVersion(Integer typeId,
			String version, int fileType) throws RemoteException, Exception {
		return terminalVersionManageService.queryByTypeIdAndVersion(typeId,
				version, fileType);
	}

	/**
	 * 更新终端版本
	 */
	@Override
	public int modify(TerminalVersion terminalVersion) throws RemoteException,
			Exception {
		return terminalVersionManageService.modify(terminalVersion);
	}

	/**
	 * 添加终端版本
	 */
	@Override
	public int add(OperObject operObject, TerminalVersion terminalVersion) throws RemoteException,
			Exception {
		return terminalVersionManageService.add(terminalVersion);
	}

	/**
	 * 将终端版本下载到FTP服务器,并通知基站获取终端版本
	 */
	@Override
	public int download(OperObject operObject, Mo mo, Long btsId, TerminalVersion terminalVersion)
			throws RemoteException, Exception {
		return terminalVersionManageService
				.download(mo, btsId, terminalVersion);
	}

	/**
	 * 返回从基站返回的下载终端版本的结果
	 * 
	 * @param mcBtsId
	 * @param terminalVersion
	 * @return
	 */
	@Override
	public Map<TDLHistoryKey, UTCodeDownloadTask> queryDownloadHistory(
			Long mcBtsId) throws RemoteException, Exception {
		return terminalVersionManageService.queryDownloadHistory(mcBtsId);
	}

	/**
	 * 通过TDLHistoryKey从后台服务中查询一个下载版本的最新状态
	 * 
	 * @param key
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public UTCodeDownloadTask getLatestStatus(Long btsId)
			throws RemoteException, Exception {
		return terminalVersionManageService.getLatestStatus(btsId);
	}

	/**
	 * 删除历史记录里的记录
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */

	public int deleteHistory(Long btsId, UTCodeDownloadTask task)
			throws RemoteException, Exception {
		return terminalVersionManageService.deleteHistory(btsId, task);
	}

	/**
	 * 删除某个网元的所有历史记录
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public int deleteAllHis(Long btsId) throws RemoteException, Exception {
		return terminalVersionManageService.deleteAllHis(btsId);
	}

	/**
	 * 查询所有终端版本类型
	 */
	@Override
	public List<TerminalVersionType> queryAllType() throws RemoteException,
			Exception {
		return terminalVersionManageService.queryAllType();
	}

	/**
	 * 添加终端版本类型
	 */
	@Override
	public void addType(OperObject operObject, TerminalVersionType model) throws RemoteException,
			Exception {
		terminalVersionManageService.addType(model);

	}

	/**
	 * 更新终端版本类型
	 */
	@Override
	public int modifyType(OperObject operObject, TerminalVersionType model) throws RemoteException,
			Exception {
		return terminalVersionManageService.modifyType(model);
	}

	/**
	 * 删除终端版本类型
	 */
	@Override
	public void deleteType(OperObject operObject, TerminalVersionType model) throws RemoteException,
			Exception {
		terminalVersionManageService.deleteType(model);
	}
}
