/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-24	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.xstat.core.util;

import com.xinwei.minas.xstat.core.model.PreStatItem;
import com.xinwei.minas.xstat.core.model.StatItem;

/**
 * 
 * 统计项助手
 * 
 * @author fanhaoyu
 * 
 */

public class StatItemUtil {

	public static PreStatItem convertToPreStatItem(StatItem statItem) {
		PreStatItem preStatItem = new PreStatItem();
		preStatItem.setMoId(statItem.getMoId());
		preStatItem.setEntityType(statItem.getEntityType());
		preStatItem.setEntityOid(statItem.getEntityOid());
		preStatItem.setStatTime(statItem.getStartTime());
		preStatItem.setItemId(statItem.getItemId());
		preStatItem.setItemType(statItem.getItemType());
		preStatItem.setStatValue(statItem.getStatValue());

		return preStatItem;
	}
}
