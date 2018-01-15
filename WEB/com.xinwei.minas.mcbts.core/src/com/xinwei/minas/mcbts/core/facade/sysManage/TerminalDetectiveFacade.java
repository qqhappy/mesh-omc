/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-5	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * @author zhuxiaozhan
 * 
 */
public interface TerminalDetectiveFacade extends Remote {

	/**
	 * ÷’∂ÀÃΩ≤‚«Î«Û
	 * @param moId
	 * @param eid
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public UserTerminal detectiveQuery(OperObject operObject, Long moId, String eid)
			throws RemoteException, Exception;
}
