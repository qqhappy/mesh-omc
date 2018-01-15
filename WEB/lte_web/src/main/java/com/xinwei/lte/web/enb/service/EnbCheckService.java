/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-11-3	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.xinwei.lte.web.enb.model.check.HealthCheckShow;
import com.xinwei.minas.enb.core.model.Enb;


/**
 * 
 * 基站健康检查服务
 * 
 * 
 * @author chenlong
 * 
 */
public interface EnbCheckService {
	
	/**
	 * 对基站进行健康检查
	 * @param enbList
	 */
	public int doCheck(List<Enb> enbList,String realPath);
	
	/**
	 * 查询检查结果文件
	 * @param request
	 * @return
	 */
	public List<HealthCheckShow> queryCheckFile(String realPath);
	
}
