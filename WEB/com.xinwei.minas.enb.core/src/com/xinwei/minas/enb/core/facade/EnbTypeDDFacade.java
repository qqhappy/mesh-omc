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
 * eNB���������ֶ�Facade
 * 
 * @author chenjunhua
 * 
 */

public interface EnbTypeDDFacade extends Remote{

	/**
	 * �������л�վ���������ֵ�
	 * @return
	 * @throws Exception
	 */
	public List<EnbTypeDD> queryAllTypeDDs() throws Exception;
	
	/**
	 * ����ָ������ID�Ļ�վ���������ֶ�
	 * @param enbTypeId
	 * @return
	 * @throws Exception
	 */
	public EnbTypeDD queryTypeDDById(int enbTypeId) throws Exception;
	
}
