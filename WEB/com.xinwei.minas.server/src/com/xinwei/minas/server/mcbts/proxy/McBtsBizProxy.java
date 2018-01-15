/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy;

import java.rmi.RemoteException;

import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * McBts业务协议适配器
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsBizProxy {
//	/**
//	 * 根据网元业务数据
//	 * 
//	 * @param moBizData
//	 *            网元业务数据
//	 * @return 记录集
//	 * @throws Exception
//	 */
//	public MoBizData query(MoBizData moBizData) throws Exception;
//
//	/**
//	 * 配置网元业务数据
//	 * 
//	 * @param moBizData
//	 *            网元业务数据
//	 * @throws Exception
//	 */
//	public void config(MoBizData moBizData) throws Exception;
	
	/**
	 * 查询网元业务数据
	 * 
	 * @param genericBizData
	 *            网元业务数据
	 * @return 记录集
	 * @throws Exception
	 */
	public GenericBizData query(Long moId, GenericBizData genericBizData) throws Exception;
	
	/**
	 * 配置网元业务数据
	 * 
	 * @param genericBizData
	 *            网元业务数据
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(Long moId, GenericBizData genericBizData) throws Exception ;

}
