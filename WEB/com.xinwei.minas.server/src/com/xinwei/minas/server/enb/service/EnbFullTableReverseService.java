/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-20	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

/**
 * 
 * ����������ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface EnbFullTableReverseService {
	
	/**
	 * ��������������
	 * @param moId
	 * @throws Exception
	 */
	public void config(Long moId) throws Exception;
}
