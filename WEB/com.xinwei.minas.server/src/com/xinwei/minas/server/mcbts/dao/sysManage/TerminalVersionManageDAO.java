/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-18	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.sysManage.TDLHistoryKey;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersionType;
import com.xinwei.minas.mcbts.core.model.sysManage.UTCodeDownloadTask;

/**
 * 
 * 终端版本管理Dao接口
 * 
 * @author tiance
 * 
 */

public interface TerminalVersionManageDAO {
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
	 * 根据typeId查询终端版本
	 */
	public TerminalVersion queryByTypeIdAndVersion(Integer typeId,
			String version, int fileType);

	/**
	 * 向服务器加入新增的终端版本
	 * 
	 */
	public int add(TerminalVersion terminalVersion);

	/**
	 * 从服务器删除终端版本
	 * 
	 */
	public int delete(TerminalVersion terminalVersion);

	/**
	 * 修改数据库中的某个终端版本
	 * 
	 */
	public int update(TerminalVersion terminalVersion);

	/**
	 * 从terminal_version_type表中查询是否已经存在typeId相应的内容 如果没有,需要提醒用户先加入相应的内容
	 * 
	 */
	public int queryForType(Integer typeId);

	/**
	 * 获取全部下载历史
	 * 
	 * @return 基于btsId, version存储的MAP
	 */
	public Map<TDLHistoryKey, UTCodeDownloadTask> queryHistroy(Long btsId);

	/**
	 * 添加到历史记录中
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 */
	public int insertToHistory(Long btsId, UTCodeDownloadTask task);

	/**
	 * 从历史记录中删除
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 */
	public int deleteFromHistory(Long btsId, UTCodeDownloadTask task);

	/**
	 * 删除某个网元的所有历史记录
	 * 
	 * @param btsId
	 * @return
	 */
	public int deleteAllFromHistory(Long btsId);

	/**
	 * 修改某个历史记录
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 */
	public int updateHistory(Long btsId, UTCodeDownloadTask task);

	/**
	 * 通过TDLHistoryKey检索某一个history记录
	 * 
	 * @param key
	 * @return
	 */
	public UTCodeDownloadTask queryHistoryByKey(TDLHistoryKey key);

	/**
	 * 查询所有终端版本类型
	 * @return
	 */
	public List<TerminalVersionType> queryAllType();
	
	/**
	 * 添加终端版本类型
	 */
	public int addType(TerminalVersionType model);
	/**
	 * 更新终端版本类型
	 */
	public int modifyType(TerminalVersionType model);

	/**
	 * 删除终端版本类型
	 */
	public int deleteType(int typeId);

}
