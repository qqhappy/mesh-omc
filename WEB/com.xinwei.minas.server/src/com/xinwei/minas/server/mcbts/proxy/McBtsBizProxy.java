/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy;

import java.rmi.RemoteException;

import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * McBtsҵ��Э��������
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsBizProxy {
//	/**
//	 * ������Ԫҵ������
//	 * 
//	 * @param moBizData
//	 *            ��Ԫҵ������
//	 * @return ��¼��
//	 * @throws Exception
//	 */
//	public MoBizData query(MoBizData moBizData) throws Exception;
//
//	/**
//	 * ������Ԫҵ������
//	 * 
//	 * @param moBizData
//	 *            ��Ԫҵ������
//	 * @throws Exception
//	 */
//	public void config(MoBizData moBizData) throws Exception;
	
	/**
	 * ��ѯ��Ԫҵ������
	 * 
	 * @param genericBizData
	 *            ��Ԫҵ������
	 * @return ��¼��
	 * @throws Exception
	 */
	public GenericBizData query(Long moId, GenericBizData genericBizData) throws Exception;
	
	/**
	 * ������Ԫҵ������
	 * 
	 * @param genericBizData
	 *            ��Ԫҵ������
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(Long moId, GenericBizData genericBizData) throws Exception ;

}
