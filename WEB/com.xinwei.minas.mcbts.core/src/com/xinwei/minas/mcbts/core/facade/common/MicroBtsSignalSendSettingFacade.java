/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.common;

import java.rmi.RemoteException;

import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.mcbts.core.model.common.MicroBtsSignalSendSetting;

/**
 * 
 * ����С��վ�źŷ��ͷ�ʽ����
 * 
 * @author fanhaoyu
 * 
 */

public interface MicroBtsSignalSendSettingFacade extends
		MoBizFacade<MicroBtsSignalSendSetting> {

	public static final String MICRO_SIGNAL_BIZ = "microbts_signal_send";

	/**
	 * ��ѯС��վ��Ϣ���ͷ�ʽ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public MicroBtsSignalSendSetting queryByMoId(Long moId)
			throws RemoteException, Exception;

	/**
	 * ����С��վ��Ϣ���ͷ�ʽ
	 * 
	 * @param setting
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(MicroBtsSignalSendSetting setting)
			throws RemoteException, Exception;
}