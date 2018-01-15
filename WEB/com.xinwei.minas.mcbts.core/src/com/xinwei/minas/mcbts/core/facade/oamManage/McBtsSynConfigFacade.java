/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-3	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.oamManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;

/**
 * 
 * ͬ����������
 * 
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsSynConfigFacade extends Remote {

	/**
	 * ͬ��EMS���ݵ���վ
	 * 
	 * @param restudy
	 *            �Ƿ���Ҫ��ѧϰ
	 * @param moId
	 *            ��վ����ԪID
	 * @return ʧ��ҵ���б�
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public List<String> config(OperObject operObject, Integer restudy, Long moId)
			throws RemoteException, Exception;

	/**
	 * �ӻ�վͬ�����ݵ�EMS
	 * 
	 * @param moId
	 *            ��վ����ԪID
	 * @return ʧ�ܵ�ҵ���б�
	 * @throws Exception
	 */
	@Loggable
	public List<String> syncFromNEToEMS(OperObject operObject, Long moId) throws RemoteException,
			Exception;

}
