/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-23	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.common.UpgradeInfo;
import com.xinwei.minas.mcbts.core.model.common.UpgradeInfoArchive;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 基站批量升级归档持久层接口
 * 
 * 
 * @author tiance
 * 
 */

public interface McBtsBatchUpgradeArchiveDAO extends
		GenericDAO<UpgradeInfoArchive, Long> {

	/**
	 * 归档一批升级任务
	 * 
	 * @param list
	 */
	public void saveAll(List<UpgradeInfoArchive> list);

	/**
	 * 获取每个基站的最新一个归档
	 * 
	 * @return
	 */
	public List<UpgradeInfoArchive> queryLatestArchive();

	/**
	 * 获取一个基站的所有历史记录
	 * 
	 * @return
	 */
	public List<UpgradeInfoArchive> queryArchiveByMoId(long moId);
}
