/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.service;

import java.util.List;

import com.xinwei.minas.ut.core.model.MemSingalReport;
import com.xinwei.minas.ut.core.model.UTLayer3Param;

/**
 * 
 * MEM���ܷ���ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface UTMemFunctionService {
	/**
	 * �����ն���Ϣ�ϱ�
	 * @param moId
	 * @param memSingalReport
	 * @throws Exception
	 */
	public void configMemSingalReport( Long moId, MemSingalReport memSingalReport)throws Exception;
	
	/**
	 * ��ѯ�ն��ϱ���Ϣ
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	public MemSingalReport queryMemSingalReport(Long pid) throws Exception;
	
	/**
	 * ��ѯ�ն��������
	 * @param moId
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	public List<UTLayer3Param> queryMemLayer3Param(Long moId, Long pid) throws Exception;
}
