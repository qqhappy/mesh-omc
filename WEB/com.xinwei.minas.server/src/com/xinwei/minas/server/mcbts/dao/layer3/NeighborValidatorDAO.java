/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-18	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.ArrayList;
import java.util.Map;

/**
 * 
 * 邻接表校验Dao接口
 * 
 * @author tiance
 * 
 */

public interface NeighborValidatorDAO {

	/**
	 * 查询所有基站邻接表个数信息
	 */
	public Map<Integer, String> queryNeighborCounts();

	/**
	 * 查询所有基站附加邻接表个数信息
	 */
	public Map<Integer, String> queryAppendNeighborCounts();

	/**
	 * 查询所有基站频点及前导序列号信息
	 */
	public Map<String, Map<Integer, String>> queryBtsFreqSeqInfo();
	
	/**
	 * 查询同频同导的基站信息
	 * @return
	 */
	public Map<String, ArrayList<Long>> querySameFreqSeqInfo();
}
