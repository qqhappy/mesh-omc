/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-12	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.model.FullTableConfigInfo;

/**
 * 
 * 整表配置业务门面接口
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author zhuxiaozhan
 * 
 */
public interface EnbFullTableConfigFacade extends Remote {
	/**
	 * 进行整表配置
	 * 
	 * @param moId
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Long moId) throws Exception;

	/**
	 * 删除指定的整表配置信息
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void delete(FullTableConfigInfo data) throws Exception;

	/**
	 * 查询指定eNB的整表配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public FullTableConfigInfo queryByMoId(Long moId) throws Exception;

	/**
	 * 查询所有的整表配置信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<FullTableConfigInfo> queryAll() throws Exception;

	/**
	 * 根据配置状态查询整表配置信息
	 * 
	 * @param status
	 *            ：成功、失败、配置中
	 * @return
	 * @throws Exception
	 */
	public List<FullTableConfigInfo> queryByStatus(int status) throws Exception;
}
