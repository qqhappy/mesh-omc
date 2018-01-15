/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-8	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.rruManage;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.common.McBtsSN;

/**
 * 
 * RRU硬件信息查询门面接口
 * 
 * @author chenshaohua
 * 
 */

// TODO:备用
public interface RRUHWInfoFacade extends Remote {

	public McBtsSN queryHWInfoFromNE(Long moId) throws RemoteException,
			Exception;
}
