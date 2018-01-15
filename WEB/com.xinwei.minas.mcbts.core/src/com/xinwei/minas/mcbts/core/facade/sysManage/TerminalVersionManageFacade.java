/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-18	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.TDLHistoryKey;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersionType;
import com.xinwei.minas.mcbts.core.model.sysManage.UTCodeDownloadTask;

/**
 * 
 * 终端版本管理门面
 * 
 * 
 * @author tiance
 * 
 */

public interface TerminalVersionManageFacade extends Remote {

	/**
	 * 获取服务器上的所有终端版本的列表
	 */
	public List<TerminalVersion> queryAll() throws RemoteException, Exception;

	/**
	 * 根据typeId获取某个终端的所有版本
	 * 
	 * @param typeId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<TerminalVersion> queryByTypeId(int typeId)
			throws RemoteException, Exception;

	/**
	 * 从服务器上删除一个终端版本
	 */
	@Loggable
	public void delete(OperObject operObject, TerminalVersion terminalVersion) throws RemoteException,
			Exception;

	/**
	 * 根据typeId和version,从服务器获取某个终端版本信息
	 */
	public TerminalVersion queryByTypeIdAndVersion(Integer typeId,
			String version, int fileType) throws RemoteException, Exception;

	/**
	 * 更新终端版本
	 */
	public int modify(TerminalVersion terminalVersion) throws RemoteException,
			Exception;

	/**
	 * 添加终端版本
	 */
	@Loggable
	public int add(OperObject operObject, TerminalVersion terminalVersion) throws RemoteException,
			Exception;

	/**
	 * 将终端版本下载到FTP服务器,并通知基站获取终端版本
	 */
	@Loggable
	public int download(OperObject operObject, Mo mo, Long btsId, TerminalVersion terminalVersion)
			throws RemoteException, Exception;

	/**
	 * 返回从基站返回的下载终端版本的结果
	 * 
	 * @param mcBtsId
	 * @param terminalVersion
	 * @return
	 */
	public Map<TDLHistoryKey, UTCodeDownloadTask> queryDownloadHistory(
			Long mcBtsId) throws RemoteException, Exception;

	/**
	 * 通过TDLHistoryKey从后台服务中查询一个下载版本的最新状态
	 * 
	 * @param key
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public UTCodeDownloadTask getLatestStatus(Long btsId)
			throws RemoteException, Exception;

	/**
	 * 删除历史记录里的记录
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public int deleteHistory(Long btsId, UTCodeDownloadTask task)
			throws RemoteException, Exception;

	/**
	 * 删除某个网元的所有历史记录
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public int deleteAllHis(Long btsId) throws RemoteException, Exception;
	
	/**
	 * 查询所有终端版本类型
	 */
	public List<TerminalVersionType> queryAllType() throws RemoteException,
			Exception;
	
	/**
	 * 添加终端版本类型
	 */
	@Loggable
	public void addType(OperObject operObject, TerminalVersionType model) throws RemoteException, Exception;
	/**
	 * 更新终端版本类型
	 */
	@Loggable
	public int modifyType(OperObject operObject, TerminalVersionType model) throws RemoteException,
			Exception;

	/**
	 * 删除终端版本类型
	 */
	@Loggable
	public void deleteType(OperObject operObject, TerminalVersionType model) throws RemoteException,
			Exception;
}
