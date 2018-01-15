/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-14	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.District;

/**
 * 
 * �����������ӿ�
 * 
 * 
 * @author tiance
 * 
 */

public interface DistrictManageFacade extends Remote {
	/**
	 * ��ѯ���еĵ�����Ϣ
	 * 
	 * @return
	 */
	public List<District> queryAll() throws RemoteException, Exception;

	/**
	 * ������߸���һ��������Ϣ
	 * 
	 * @param district
	 */
	@Loggable
	public void saveOrUpdate(OperObject operObject, District district) throws RemoteException,
			Exception;

	/**
	 * ɾ��һ��������Ϣ
	 * 
	 * @param district
	 */
	@Loggable
	public void delete(OperObject operObject, District district) throws RemoteException, Exception;
}
