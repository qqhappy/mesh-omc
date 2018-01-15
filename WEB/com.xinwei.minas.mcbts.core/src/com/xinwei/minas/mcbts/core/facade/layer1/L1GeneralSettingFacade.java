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
import com.xinwei.minas.mcbts.core.model.layer1.L1GeneralSetting;

/**
 * 
 * L1��������ҵ������
 * 
 * @author chenjunhua
 * 
 */

public interface L1GeneralSettingFacade extends MoBizFacade<L1GeneralSetting> {

	/**
	 * ��ѯL1����������Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public L1GeneralSetting queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * ����L1���û�����Ϣ
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, L1GeneralSetting data) throws RemoteException, Exception;

	/**
	 * ����L1���û�����Ϣ
	 * 
	 * @param data
	 * @param isSyncConfig
	 *            �Ƿ�ͬ�������ڽӱ�
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(L1GeneralSetting data, boolean isSyncConfig)
			throws RemoteException, Exception;

}
