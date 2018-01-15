/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| zhaolingling 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.server.mcbts.service.ICustomService;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhaolingling
 * 
 */

public interface PreventBroadcastService  extends ICustomService{
	/**
	 * 查询全部配置
	 * 
	 * @return
	 */
	public List<GenericBizData> queryAll();
	/**
	 * 向所有基站配置
	 * 
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configAll(GenericBizData config) throws RemoteException,
			Exception;
	
	/**
	 * 向指定基站配置数据
	 * 
	 * @param moId
	 * @param config
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(Long moId, GenericBizData config) throws RemoteException, Exception;

	/**
	 * 向基站查询
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public GenericBizData queryFromNE(Long moId) throws RemoteException,
			Exception;
}
