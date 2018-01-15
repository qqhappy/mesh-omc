/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-14	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.common.UpgradeInfo;
import com.xinwei.minas.mcbts.core.model.common.UpgradeInfoArchive;

/**
 * 
 * 基站批量升级服务的类
 * 
 * @author tiance
 * 
 */

public interface McBtsBatchUpgradeService {
	/**
	 * 添加基站批量升级任务
	 * 
	 * @param list
	 *            添加的基站的任务
	 * @throws Exception
	 */
	public void addTask(List<UpgradeInfo> list) throws Exception;

	/**
	 * 获取所有基站批量升级任务的Map
	 * 
	 * @return 所有升级任务的Map,key为基站的moId
	 * @throws Exception
	 */
	public Map<Integer, Map<Long, UpgradeInfo>> queryAll() throws Exception;

	/**
	 * 获取所有基站升级任务的Map
	 * 
	 * @return 外层Map以btsType为Key; 内层为UpgradeInfo的列表
	 * @throws Exception
	 */
	public Map<Integer, List<UpgradeInfo>> queryAll2() throws Exception;

	/**
	 * 获取正在进行升级的任务
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<UpgradeInfo> queryUpgrading() throws Exception;

	/**
	 * 终止基站批量升级的任务
	 * 
	 * @param list
	 *            待终止的升级任务列表
	 * @throws Exception
	 */
	public void terminate(List<UpgradeInfo> list) throws Exception;

	/**
	 * 对已经结束的任务进行归档
	 * 
	 * @throws Exception
	 */
	public void archive() throws Exception;

	/**
	 * 获取每个基站的最新一个归档
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, List<UpgradeInfoArchive>> queryLatestArchive()
			throws Exception;

	/**
	 * 获取一个基站的所有历史记录
	 * 
	 * @return 一个基站的所有历史记录
	 */
	public List<UpgradeInfoArchive> queryArchiveByMoId(long moId)
			throws Exception;

	/**
	 * 获取将要升级某个版本软件的任务列表
	 * 
	 * @param version
	 * @return
	 * @throws Exception
	 */
	public List<UpgradeInfo> queryUpgradingInfoByVersion(String version)
			throws Exception;
}
