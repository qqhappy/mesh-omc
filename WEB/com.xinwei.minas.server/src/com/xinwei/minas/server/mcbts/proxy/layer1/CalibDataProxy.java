/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-3-21	| jiayi		 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.proxy.layer1;

import com.xinwei.minas.mcbts.core.model.layer1.CalibrationDataInfo;

/**
 * У׼��������ҵ�����Proxy
 * 
 * @author jiayi
 * 
 */
public interface CalibDataProxy {

	/**
	 * ��ѯ��Ԫҵ������
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public CalibrationDataInfo query(Long moId) throws Exception;

	/**
	 * ������Ԫҵ������
	 * 
	 * @param moId
	 * @param setting
	 * @throws Exception
	 */
	public void config(Long moId, CalibrationDataInfo setting) throws Exception;
}
