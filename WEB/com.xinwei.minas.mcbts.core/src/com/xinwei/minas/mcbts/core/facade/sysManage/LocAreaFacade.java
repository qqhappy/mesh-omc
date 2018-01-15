/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-29	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.LocationArea;

/**
 * 
 * λ��������ӿ�
 * 
 * 
 * @author chenshaohua
 * 
 */

public interface LocAreaFacade extends Remote {

	/**
	 * ��ѯȫ����¼
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LocationArea> queryAll() throws RemoteException, Exception;

	/**
	 * ���������λ������Ϣ
	 * 
	 * @param la
	 */
	@Loggable
	public void saveOrUpdate(OperObject operObject, LocationArea la) throws RemoteException, Exception;

	/**
	 * ɾ��λ����
	 * 
	 * @param la
	 */
	@Loggable
	public void delete(OperObject operObject, LocationArea la) throws RemoteException, Exception;;

	/**
	 * ��������
	 * 
	 */
	public void config(List<LocationArea> locationAreaList)
			throws RemoteException, Exception;

	/**
	 * ��ѯ����locationId
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	// public List<Long> queryAllLocationId() throws RemoteException, Exception;
}
