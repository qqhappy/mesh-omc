/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.xstat.core.model;

/**
 * 
 * 类简要描述
 * 
 * @author fanhaoyu
 * 
 */

public class StatConstants {

	// StatItem相关
	public static final String POINT = ".";

	/**
	 * entityType和entityOid的分隔符
	 */
	public static final String DEFAULT_SPLIT = "#";

	public static final String COLLECT_TYPE_ADD = "ADD";

	public static final String COLLECT_TYPE_AVERAGE = "AVERAGE";

	/**
	 * 预统计类型，小时
	 */
	public static final int PRE_STAT_TYPE_HOUR = 1;

	/**
	 * 预统计类型，天
	 */
	public static final int PRE_STAT_TYPE_DAY = 2;

}
