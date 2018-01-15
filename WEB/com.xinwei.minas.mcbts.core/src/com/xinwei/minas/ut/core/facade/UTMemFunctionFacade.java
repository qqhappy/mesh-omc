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
 * MEM��������ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface UTMemFunctionFacade extends Remote {

	
	/**
	 * �����ն���Ϣ�ϱ�
	 * @param memSingalReport
	 * @throws Exception
	 */
	@Loggable
	public void configMemSingalReport(OperObject operObject, Long moId, MemSingalReport memSingalReport)throws Exception;
	


	/**
	 * ��ѯ�ն��ϱ���Ϣ
	 * @param operObject
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@Loggable
	public MemSingalReport queryMemSingalReport(OperObject operObject, Long pid) throws Exception;
	
	/**
	 * ��ѯ�ն��������
	 * @param operObject
	 * @param moId
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	@Loggable
	public List<UTLayer3Param> queryMemLayer3Param(OperObject operObject, Long moId, Long pid) throws Exception;
}
