/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.dao;

import com.xinwei.minas.server.platform.generic.dao.GenericDAO;
import com.xinwei.minas.ut.core.model.MemSingalReport;

/**
 * 
 * 终端调试信息上报DAO接口
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface MemSingalReportDAO extends GenericDAO<MemSingalReport, Long>{
	/**
	 * 根据终端的pid查询
	 * @param pid
	 * @return
	 */
	public MemSingalReport queryByPid(Long pid);
}
