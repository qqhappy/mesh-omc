/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-11	| zhaolingling 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.layer1;

import java.rmi.RemoteException;

import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationResult;

/**
 * 
 * У׼�������ӿ�
 * 
 * 
 * @author zhaolingling
 * 
 */

public interface CalibrationResultFacade extends MoBizFacade<CalibrationResult> {

	/**
	 * ��ѯУ׼����������Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public Object[] queryByMoId(Long moId) throws RemoteException, Exception;

}
