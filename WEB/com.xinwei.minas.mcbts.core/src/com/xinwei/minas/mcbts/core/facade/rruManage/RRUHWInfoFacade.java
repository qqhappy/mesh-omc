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
 * RRUӲ����Ϣ��ѯ����ӿ�
 * 
 * @author chenshaohua
 * 
 */

// TODO:����
public interface RRUHWInfoFacade extends Remote {

	public McBtsSN queryHWInfoFromNE(Long moId) throws RemoteException,
			Exception;
}
