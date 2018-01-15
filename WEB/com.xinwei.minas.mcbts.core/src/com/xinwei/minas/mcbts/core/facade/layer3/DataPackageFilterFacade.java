/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-19	| fangping 	| 	create the file                       
 */
package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.common.DataPackageFilter;

public interface DataPackageFilterFacade extends Remote {

	/**
	 * ��ȡ��������
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public int queryFilterType() throws RemoteException, Exception;

	/**
	 * �����ݿ��ѯ������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<DataPackageFilter> queryAllFormEMS() throws RemoteException,
			Exception;

	/**
	 * ���վ��ѯ���ݰ�����׼��
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Object[] queryFromNE(Long moId) throws RemoteException, Exception;

	/**
	 * �������ݰ����˹�������ݿ�,���ж��Ƿ��·������л�վ
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, int filterType, List<DataPackageFilter> filterList)
			throws RemoteException, Exception;

	/**
	 * ���û�վ�����ݰ����˹���
	 * 
	 * @param moId
	 * @param filterList
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Long moId, int filterType,
			List<DataPackageFilter> filterList) throws RemoteException,
			Exception;

}
