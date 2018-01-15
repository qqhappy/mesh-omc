/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao;

import java.util.List;

import com.xinwei.minas.enb.core.model.FullTableConfigInfo;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 整表配置业务DAO层接口
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface EnbFullTableConfigDAO extends GenericDAO<FullTableConfigInfo, Long> {
	
	/**
	 * 根据moId查询整表配置信息
	 * @param moId
	 * @return
	 */
	public FullTableConfigInfo queryByMoId(Long moId);
	
	/**
	 * 根据配置任务状态查询
	 * @param status
	 * @return
	 */
	public List<FullTableConfigInfo> queryByStatus(int status);
	
}
