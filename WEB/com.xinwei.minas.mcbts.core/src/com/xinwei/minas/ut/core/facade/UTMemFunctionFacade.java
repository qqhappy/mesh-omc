/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-12	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.facade;

import java.rmi.Remote;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.ut.core.model.MemSingalReport;
import com.xinwei.minas.ut.core.model.UTLayer3Param;

/**
 * 
 * MEM功能门面接口
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface UTMemFunctionFacade extends Remote {

	
	/**
	 * 配置终端信息上报
	 * @param memSingalReport
	 * @throws Exception
	 */
	@Loggable
	public void configMemSingalReport(OperObject operObject, Long moId, MemSingalReport memSingalReport)throws Exception;
	


	/**
	 * 查询终端上报信息
	 * @param operObject
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@Loggable
	public MemSingalReport queryMemSingalReport(OperObject operObject, Long pid) throws Exception;
	
	/**
	 * 查询终端三层参数
	 * @param operObject
	 * @param moId
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@Loggable
	public List<UTLayer3Param> queryMemLayer3Param(OperObject operObject, Long moId, Long pid) throws Exception;
}
