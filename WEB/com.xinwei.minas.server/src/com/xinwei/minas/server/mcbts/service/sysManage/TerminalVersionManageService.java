/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-18	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.mcbts.core.model.sysManage.TDLHistoryKey;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersionType;
import com.xinwei.minas.mcbts.core.model.sysManage.UTCodeDownloadTask;

/**
 * 
 * 终端版本管理服务接口
 * 
 * 
 * @author tiance
 * 
 */

public interface TerminalVersionManageService {

	/**
	 * 获取服务器上的所有终端版本的列表
	 * 
	 */
	public List<TerminalVersion> queryAll();

	/**
	 * 根据typeId获取某个终端的所有版本
	 * 
	 */
	public List<TerminalVersion> queryByTypeId(Integer typeId);

	/**
	 * 从服务器上删除一个终端版本
	 */
	public void delete(TerminalVersion terminalVersion);

	/**
	 * 根据typeId和version,从服务器获取某个终端版本信息
	 */
	public TerminalVersion queryByTypeIdAndVersion(Integer typeId,
			String version, int fileType);

	/**
	 * 更新终端版本
	 */
	public int modify(TerminalVersion terminalVersion);

	/**
	 * 添加终端版本
	 */
	public int add(TerminalVersion terminalVersion);

	/**
	 * 将终端版本下载到FTP服务器,并通知基站获取终端版本
	 */
	public int download(Mo mo, Long btsId, TerminalVersion terminalVersion)
			throws Exception;

	/**
	 * 返回从基站返回的下载终端版本的所有结果
	 * 
	 * @param mcBtsId
	 * @param terminalVersion
	 * @return
	 */
	public Map<TDLHistoryKey, UTCodeDownloadTask> queryDownloadHistory(
			Long mcBtsId);

	/**
	 * 通过TDLHistoryKey从后台服务中查询一个下载版本的最新状态
	 * 
	 * @param key
	 * @return
	 */
	public UTCodeDownloadTask getLatestStatus(Long btsId);

	/**
	 * 删除历史记录里的记录
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 */
	public int deleteHistory(Long btsId, UTCodeDownloadTask task);

	/**
	 * 删除某个网元的所有历史记录
	 * 
	 * @param btsId
	 * @return
	 */
	public int deleteAllHis(Long btsId);
	
	/**
	 * 查询所有终端版本类型
	 * 
	 */
	public List<TerminalVersionType> queryAllType();

	/**
	 * 增加终端版本类型
	 * @param model
	 * @return
	 */
	public void addType(TerminalVersionType model);

	/**
	 * 更新终端版本类型
	 */
	public int modifyType(TerminalVersionType model) throws RemoteException,
			Exception;

	/**
	 * 删除终端版本类型
	 */
	public void deleteType(TerminalVersionType model) throws RemoteException,
			Exception;
	
}
