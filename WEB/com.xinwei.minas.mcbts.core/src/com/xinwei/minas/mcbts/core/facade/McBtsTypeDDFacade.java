/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;

/**
 * 
 * 基站类型数据字典门面
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsTypeDDFacade extends Remote {
	/**
	 * 查询所有的基站类型数据字典定义
	 * 
	 * @return
	 */
	public List<McBtsTypeDD> queryAll() throws RemoteException;

	/**
	 * 根据基站类型编码获取相应的基站类型数据字典定义
	 * 
	 * @param mcBtsType
	 *            基站类型编码
	 * @return
	 */
	public McBtsTypeDD queryByTypeId(int mcBtsType) throws RemoteException;
}
