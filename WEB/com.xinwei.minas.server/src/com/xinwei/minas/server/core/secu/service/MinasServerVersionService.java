/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-27	| fangping 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.service;

import java.rmi.RemoteException;

import com.xinwei.minas.core.model.secu.MinasServerVersion;

/**
 * 
 * ¿‡ºÚ“™√Ë ˆ
 * 
 * <p>
 * ¿‡œÍœ∏√Ë ˆ
 * </p> 
 * 
 * @author fangping
 * 
 */

public interface MinasServerVersionService {
	public MinasServerVersion getMinasServerVersion() throws RemoteException,
			Exception ;
	}
