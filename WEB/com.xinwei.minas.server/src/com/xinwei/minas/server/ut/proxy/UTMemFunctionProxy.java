/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.proxy;

import java.util.List;

import com.xinwei.minas.ut.core.model.UTLayer3Param;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * MEM���ܴ���ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface UTMemFunctionProxy {
	
	/**
	 * ��ѯ�ն��������
	 * @param moId
	 * @param genericBizData
	 * @return
	 * @throws Exception
	 */
	public List<UTLayer3Param> queryMemLayer3Param(Long moId, GenericBizData genericBizData) throws Exception;
}
