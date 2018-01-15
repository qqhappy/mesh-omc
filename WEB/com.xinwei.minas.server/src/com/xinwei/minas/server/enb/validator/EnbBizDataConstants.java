/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-4-11	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.validator;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * eNB 业务数据常量类 
 * 
 * @author chenjunhua
 * 
 */

public class EnbBizDataConstants {
	
	private static Map<Integer, Integer> bandwidthRbMap = new HashMap();
	
	static {
		bandwidthRbMap.put(0, 6);
		bandwidthRbMap.put(1, 15);
		bandwidthRbMap.put(2, 25);
		bandwidthRbMap.put(3, 50);
		bandwidthRbMap.put(4, 75);
		bandwidthRbMap.put(5, 100);
	}

	/**
	 * 根据带宽代表的数字（0-5），获取对应的最大RB
	 * @param bandwidthNum 带宽代表的数字（0-5）
	 * @return 对应的最大RB
	 */
	public static int getMaxRbByBandwidth(int bandwidthNum) {
		//(0)1.4M(6RB)|(1)3M(15RB)|(2)5M(25RB)|(3)10M(50RB)|(4)15M(75RB)|(5)20M(100RB)
		try {
			return bandwidthRbMap.get(bandwidthNum);
		} catch (Exception e) {
			return 0;
		}		
	}
	
}
