/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;

/**
 * 
 * ��վ���������ֵ�����
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsTypeDDFacade extends Remote {
	/**
	 * ��ѯ���еĻ�վ���������ֵ䶨��
	 * 
	 * @return
	 */
	public List<McBtsTypeDD> queryAll() throws RemoteException;

	/**
	 * ���ݻ�վ���ͱ����ȡ��Ӧ�Ļ�վ���������ֵ䶨��
	 * 
	 * @param mcBtsType
	 *            ��վ���ͱ���
	 * @return
	 */
	public McBtsTypeDD queryByTypeId(int mcBtsType) throws RemoteException;
}
