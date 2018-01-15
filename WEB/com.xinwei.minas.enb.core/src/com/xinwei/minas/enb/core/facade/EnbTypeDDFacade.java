/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.util.List;

import com.xinwei.minas.enb.core.model.EnbTypeDD;

/**
 * 
 * eNB类型数据字段Facade
 * 
 * @author chenjunhua
 * 
 */

public interface EnbTypeDDFacade extends Remote{

	/**
	 * 查找所有基站类型数据字典
	 * @return
	 * @throws Exception
	 */
	public List<EnbTypeDD> queryAllTypeDDs() throws Exception;
	
	/**
	 * 查找指定类型ID的基站类型数据字段
	 * @param enbTypeId
	 * @return
	 * @throws Exception
	 */
	public EnbTypeDD queryTypeDDById(int enbTypeId) throws Exception;
	
}
