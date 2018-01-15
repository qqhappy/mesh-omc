/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer1;

import com.xinwei.minas.mcbts.core.model.layer1.McBtsAntennaLock;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 天线闭锁配置DAO接口
 * 
 * @author fanhaoyu
 * 
 */

public interface McbtsAntennaLockDAO extends GenericDAO<McBtsAntennaLock, Long> {

	/**
	 * 根据moId查询天线闭锁配置信息
	 * 
	 * @param moId
	 */
	public McBtsAntennaLock queryByMoId(Long moId);

}