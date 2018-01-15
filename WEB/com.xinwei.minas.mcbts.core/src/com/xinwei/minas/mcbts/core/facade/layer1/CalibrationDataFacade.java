/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.layer1;

import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationDataInfo;

/**
 * 
 * У׼��������ҵ������
 * 
 * @author chenjunhua
 * 
 */

public interface CalibrationDataFacade extends MoBizFacade<CalibrationDataInfo> {

	/**
	 * ��ѯУ׼����������Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public CalibrationDataInfo queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * ����У׼���ͻ�����Ϣ
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Long moId, CalibrationDataInfo data)
			throws RemoteException, Exception;

	/**
	 * ����У׼���ͻ�����Ϣ
	 * 
	 * @param moId
	 * @param data
	 * @param isSyncConfig
	 *            �Ƿ�ͬ�������ڽӱ�
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(Long moId, CalibrationDataInfo data, boolean isSyncConfig)
			throws RemoteException, Exception;

}
