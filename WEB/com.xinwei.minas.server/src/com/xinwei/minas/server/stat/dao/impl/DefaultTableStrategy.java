/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-2-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.stat.dao.TableStrategy;

/**
 * 
 * 默认分表策略-按数据的时间类型和基站moId进行分表
 * 
 * @author fanhaoyu
 * 
 */

public class DefaultTableStrategy implements TableStrategy {

	private static final String TABLE_NAME_PREFIX = "mcbts_stat_data_{0}_{1}";

	private static final String[] TIME_TYPE_NAME = new String[] { "realtime",
			"daily", "week", "month", "year" };

	private static final int TABLE_COUNT = 5;

	@Override
	public String getTableName(int timeType, long btsId) {
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		long moId = mcBts.getMoId();
		int index = (int) (moId % TABLE_COUNT);
		return MessageFormat.format(TABLE_NAME_PREFIX,
				TIME_TYPE_NAME[timeType], index);
		// StringBuilder tableBuilder = new StringBuilder();
		// tableBuilder.append(TABLE_NAME_PREFIX).append("_")
		// .append(TIME_TYPE_NAME[timeType]).append("_").append(index);
		// return tableBuilder.toString();
	}

	@Override
	public List<String> getAllTables() {
		return null;
	}

	@Override
	public List<String> getTables(int condition) {
		List<String> tableList = new ArrayList<String>();
		for (int i = 0; i < TABLE_COUNT; i++) {
			tableList.add(MessageFormat.format(TABLE_NAME_PREFIX,
					TIME_TYPE_NAME[condition], i));
		}
		return tableList;
	}

}
