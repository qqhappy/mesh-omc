/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-3-21	| jiayi		 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.proxy.layer1.impl;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer1.CalibrationResult;
import com.xinwei.minas.server.mcbts.proxy.layer1.CalibResultProxy;

/**
 * 
 * У׼���ҵ�����Proxy
 * 
 * @author jiayi
 * 
 */

public class CalibResultProxyImpl implements CalibResultProxy {

	public CalibResultProxyImpl() {
	}

	/**
	 * ��ѯ��Ԫҵ������
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public CalibrationResult query(Long moId) throws Exception {
		// TODO: ��ѯ����У׼���ʵ��
		return null;
	}

	/**
	 * ��ѯУ׼�����ʷ����
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public List<CalibrationResult> queryHistory(Long moId) throws Exception {
		// TODO: ��ѯ����У׼���ʵ��
		List<CalibrationResult> result = new ArrayList<CalibrationResult>();
		return result;
	}
}
