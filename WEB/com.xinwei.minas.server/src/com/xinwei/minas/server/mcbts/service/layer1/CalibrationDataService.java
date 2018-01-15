/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer1.CalibrationDataInfo;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * У׼��������ҵ�����ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface CalibrationDataService extends ICustomService {

	/**
	 * ��ѯУ׼����������Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public CalibrationDataInfo queryCalibrationDataConfigByMoId(Long moId)
			throws Exception;

//	/**
//	 * ����У׼����������Ϣ
//	 * 
//	 * @param locationArea
//	 * @throws Exception
//	 */
//	public void config(Long moId, CalibrationDataInfo setting) throws Exception;

	/**
	 * ����У׼����������Ϣ
	 * 
	 * @param moId
	 * @param setting
	 * @param isSyncConfig
	 *            �Ƿ�ͬ�������ڽӱ�
	 * @throws Exception
	 */
	public void config(Long moId, CalibrationDataInfo setting,
			boolean isSyncConfig) throws Exception;

	/**
	 * ����Ԫ���У׼����������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public CalibrationDataInfo queryDeviceCalibrationDataConfigByMoId(Long moId)
			throws Exception;
}
