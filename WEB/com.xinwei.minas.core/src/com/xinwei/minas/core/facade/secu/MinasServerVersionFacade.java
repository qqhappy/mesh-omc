/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-27	| fangping 	| 	create the file                       
 */

package com.xinwei.minas.core.facade.secu;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.model.secu.MinasServerVersion;

/**
 * 
 * 获取网管平台服务器版本模型的门面接口
 * 
 * 
 * @author fangping
 * 
 */

public interface MinasServerVersionFacade extends Remote {
	public MinasServerVersion getMinasServerVersion() throws RemoteException,
			Exception;
}
