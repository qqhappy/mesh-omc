/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-2	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.tools;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.mcbts.core.model.tools.BtsDiagParameter;

/**
 * 
 * ���������
 * 
 * @author tiance
 * 
 */

public interface McBtsDiagToolFacade extends Remote {
	/**
	 * ��ȡ��վ����Ϣ: 1.����ip 2.����L3�˿� 3.�û��� 4.����
	 */
	public BtsDiagParameter queryBtsDiagParameter(Mo mo) throws RemoteException, Exception;
}
