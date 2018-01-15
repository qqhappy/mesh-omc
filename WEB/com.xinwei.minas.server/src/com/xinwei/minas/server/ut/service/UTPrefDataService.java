/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.service;

import com.xinwei.minas.ut.core.model.UTPerfData;

/**
 * 
 *�鿴�ն��������ݷ���ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface UTPrefDataService {
	/**
	 * ��ѯ�ն�������������
	 * @param moId
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	public UTPerfData query(Long moId, Long pid) throws Exception;
}
