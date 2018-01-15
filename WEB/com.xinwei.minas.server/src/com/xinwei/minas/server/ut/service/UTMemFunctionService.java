/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.service;

import java.util.List;

import com.xinwei.minas.ut.core.model.MemSingalReport;
import com.xinwei.minas.ut.core.model.UTLayer3Param;

/**
 * 
 * MEM功能服务接口
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface UTMemFunctionService {
	/**
	 * 配置终端信息上报
	 * @param moId
	 * @param memSingalReport
	 * @throws Exception
	 */
	public void configMemSingalReport( Long moId, MemSingalReport memSingalReport)throws Exception;
	
	/**
	 * 查询终端上报信息
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	public MemSingalReport queryMemSingalReport(Long pid) throws Exception;
	
	/**
	 * 查询终端三层参数
	 * @param moId
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	public List<UTLayer3Param> queryMemLayer3Param(Long moId, Long pid) throws Exception;
}
