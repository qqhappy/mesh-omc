/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-11	| zhaolingling 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1;

import java.io.File;

import com.xinwei.minas.mcbts.core.model.layer1.CalibrationResult;

/**
 * 
 * У׼�������ӿ�
 * 
 * 
 * @author zhaolingling
 * 
 */

public interface CalibrationResultService {

	/**
	 * �ṩ��У׼��������չ�����߷��ͺͽ��յ�У׼�����ķ���
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public CalibrationResult getCalibrationResult(Long moId) throws Exception;

	/**
	 * ��ѯУ׼�����Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public Object[] queryByMoId(Long moId) throws Exception;

	/**
	 * �������ݿ�
	 * 
	 * @param file
	 * @throws Exception
	 */
	public void updateCalibGenericConfig(Long moId, File file) throws Exception;
}