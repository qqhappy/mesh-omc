/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-3-21	| jiayi		 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.proxy.layer1;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer1.CalibrationResult;

/**
 * У׼���ҵ�����Proxy
 * 
 * @author jiayi
 * 
 */
public interface CalibResultProxy {

	/**
	 * ��ѯ���ý������
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public CalibrationResult query(Long moId) throws Exception;

	/**
	 * ��ѯ���ý������
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<CalibrationResult> queryHistory(Long moId) throws Exception;
}
