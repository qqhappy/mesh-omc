/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-17	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.Simulcast;

/**
 * 
 * ͬ����Դ����ӿ�
 * 
 * 
 * @author tiance
 * 
 */

public interface SimulcastManageFacade extends Remote {
	/**
	 * ��ȡͬ����Դ�б�
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<Simulcast> queryAll() throws RemoteException, Exception;

	/**
	 * ���ݲ�ѯ����ID�µ�����ͬ����Դ
	 * 
	 * @param districtId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<Simulcast> queryByDistrictId(long districtId)
			throws RemoteException, Exception;

	/**
	 * �������޸�ͬ����Դ
	 * 
	 * @param simulcast
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void saveOrUpdate(OperObject operObject, Simulcast simulcast) throws RemoteException,
			Exception;

	/**
	 * ɾ��ͬ����Դ
	 * 
	 * @param simulcast
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void delete(OperObject operObject, Simulcast simulcast) throws RemoteException, Exception;

	/**
	 * ��ѯͬ����״̬
	 * 
	 * @return
	 */
	public boolean[] querySyncStatus() throws RemoteException, Exception;

	/**
	 * ��ͬ����Դ����ͬ��
	 * <p>
	 * toSync[0]Ϊ��վ��·��Ϣ,toSync[1]Ϊͬ����Դ��Ϣ
	 * </p>
	 * <p>
	 * Ŀǰֻ֧��ͬ����Դ��Ϣ,֮����ʵ�ֻ�վ��·��Ϣ��ͬ��
	 * </p>
	 * 
	 * @param toSync
	 *            : Ϊtrueʱͬ��,false��ͬ��
	 */
	@Loggable
	public void sync(OperObject operObject, boolean[] toSync) throws RemoteException, Exception;
}
