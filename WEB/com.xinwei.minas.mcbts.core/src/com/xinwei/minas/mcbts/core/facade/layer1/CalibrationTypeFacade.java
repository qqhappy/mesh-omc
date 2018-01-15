/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.layer1;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationType;

/**
 * 
 * У׼��������ҵ������
 * 
 * @author chenjunhua
 * 
 */

public interface CalibrationTypeFacade extends MoBizFacade<CalibrationType> {

	/**
	 * ��ѯУ׼����������Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public CalibrationType queryByMoId(Long moId) throws RemoteException, Exception;

	/**
	 * ����У׼���ͻ�����Ϣ
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, CalibrationType data) throws RemoteException, Exception;
	
}
