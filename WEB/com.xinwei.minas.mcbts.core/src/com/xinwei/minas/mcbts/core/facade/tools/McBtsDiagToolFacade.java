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
 * 诊断门面类
 * 
 * @author tiance
 * 
 */

public interface McBtsDiagToolFacade extends Remote {
	/**
	 * 获取基站的信息: 1.公网ip 2.公网L3端口 3.用户名 4.密码
	 */
	public BtsDiagParameter queryBtsDiagParameter(Mo mo) throws RemoteException, Exception;
}
