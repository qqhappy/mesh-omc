/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-14	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.McBts;

/**
 * 
 * ��վ����ģʽ����ӿ�
 * 
 * @author tiance
 * 
 */

public interface McBtsModeFacade extends Remote {
	public McBts queryMcBtsMode(McBts mcbts) throws RemoteException, Exception;
}
