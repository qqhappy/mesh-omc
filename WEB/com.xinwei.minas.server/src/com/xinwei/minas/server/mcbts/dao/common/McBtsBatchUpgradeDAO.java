/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-22	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.UpgradeInfo;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 基站批量升级持久化的接口
 * 
 * 
 * @author tiance
 * 
 */

public interface McBtsBatchUpgradeDAO extends GenericDAO<UpgradeInfo, Long> {

	/**
	 * 保存一个基站批量升级的列表到数据库
	 * 
	 * @param list
	 */
	public void saveAll(List<UpgradeInfo> list);

	/**
	 * 基于moId查询基站批量升级信息
	 * 
	 * @param moId
	 */
	public UpgradeInfo queryByMoId(long moId);

	/**
	 * 查询一个尚未启动的任务,按scheduledTime和idx排序
	 * 
	 * @return 尚未执行的任务
	 */
	public UpgradeInfo queryFreeUpgradeInfo();

	/**
	 * 查询可以被归档的升级任务
	 * 
	 * @return 可以被归档的任务列表
	 */
	public List<UpgradeInfo> queryToArchive();

	/**
	 * 删除要归档的升级任务
	 * 
	 * @param listToArchive
	 */
	public void deleteArchive(List<UpgradeInfo> listToArchive);

	/**
	 * 获取要升级某个版本软件的任务列表
	 * 
	 * @return
	 */
	public List<UpgradeInfo> queryUpgradingInfoByVersion(String version);
}
