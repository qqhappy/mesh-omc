/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-28	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.facade.conf;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.model.Mo;

/**
 * 
 * MO��������
 * 
 * @author chenjunhua
 * 
 */

public interface MoBasicFacade extends Remote {

	/**
	 * ��������MO
	 * 
	 * @return
	 */
	public List<Mo> queryAll() throws RemoteException, Exception;

	/**
	 * ��ȡָ��moId�� MO
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Mo queryByMoId(Long moId) throws RemoteException, Exception;

	/**
	 * ��ȡָ�����͵�MO
	 * 
	 * @param moTypeId
	 *            ����
	 * @return
	 */
	public List<Mo> queryByType(int moTypeId) throws RemoteException, Exception;

	/**
	 * ����MO
	 * 
	 * @param mo
	 *            ���ܶ���
	 * @param regionId
	 *            �����������
	 */
	public void add(Mo mo, long regionId) throws RemoteException, Exception;

	/**
	 * �޸�MO
	 * 
	 * @param mo
	 */
	public void modify(Mo mo) throws RemoteException, Exception;

	/**
	 * ɾ��ָ��ID��MO
	 * 
	 * @param moId
	 */
	public void delete(Long moId) throws RemoteException, Exception;

	/**
	 * �޸�MO����״̬
	 * 
	 * @param mo
	 */
	public void changeManageState(Mo mo) throws RemoteException, Exception;

}
