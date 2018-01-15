/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-1	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;

/**
 * �ն���������ӿ�
 * @author zhuxiaozhan
 *
 */
public interface TerminalRestartFacade extends Remote {
	/**
	 * �����ն���������
	 * @param modId
	 * @param eid
	 */
	@Loggable
	public void restartConfig(OperObject operObject, Long moId, String eid) throws RemoteException, Exception;
}
