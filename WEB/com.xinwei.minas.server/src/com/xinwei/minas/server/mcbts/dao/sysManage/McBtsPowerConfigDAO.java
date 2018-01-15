/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-19	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage;

import java.util.HashSet;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.PowerSupply;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述:操作基站和电源的关系
 * </p>
 * 
 * @author qiwei
 * 
 */

public interface McBtsPowerConfigDAO {
	/**
	 * 根据基站moid查询所对应电源
	 * 
	 */
	public PowerSupply queryByMoId(long moid);

	/**
	 * 存储电源和基站对应关系；
	 */
	public void saveByMoIdAndPowerId(long idx, PowerSupply powerSupply);

	/**
	 * 根据电源信息查询对应基站的moId集合
	 */
	public HashSet<Long> queryMcbtsByPowerSupply(PowerSupply powerSupply);

	/**
	 * 删除基站时删除对应关系
	 */
	public void deleteRelationByMoId(long moid);
}
