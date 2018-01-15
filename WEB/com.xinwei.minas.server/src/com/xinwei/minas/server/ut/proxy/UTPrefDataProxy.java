/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.proxy;

import com.xinwei.minas.ut.core.model.UTPerfData;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * �鿴�ն��������ݴ���ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface UTPrefDataProxy {
	/**
	 * ��ѯ�ն�������������
	 * @param moId
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public UTPerfData query(Long moId, GenericBizData genericBizData) throws Exception;
}
