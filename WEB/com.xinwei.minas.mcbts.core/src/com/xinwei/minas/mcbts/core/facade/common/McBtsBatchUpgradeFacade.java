/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-14	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.common.UpgradeInfo;
import com.xinwei.minas.mcbts.core.model.common.UpgradeInfoArchive;

/**
 * 
 * ��վ������������Ľӿ�
 * 
 * @author tiance
 * 
 */

public interface McBtsBatchUpgradeFacade extends Remote {

	/**
	 * ��ӻ�վ������������
	 * 
	 * @param list
	 *            ��ӵĻ�վ������
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void addTask(OperObject operObject, List<UpgradeInfo> list)
			throws RemoteException, Exception;

	/**
	 * ��ȡ���л�վ�������������Map
	 * 
	 * @return ���Map��btsTypeΪKey; �ڲ�Map�Ի�վmoIdΪKey
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Map<Integer, Map<Long, UpgradeInfo>> queryAll()
			throws RemoteException, Exception;

	/**
	 * ��ȡ���л�վ���������Map
	 * 
	 * @return ���Map��btsTypeΪKey; �ڲ�ΪUpgradeInfo���б�
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Map<Integer, List<UpgradeInfo>> queryAll2() throws RemoteException,
			Exception;

	/**
	 * ��ȡ���ڽ�������������
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<UpgradeInfo> queryUpgrading() throws RemoteException, Exception;

	/**
	 * ��ֹ��վ��������������
	 * 
	 * @param list
	 *            ����ֹ�����������б�
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void terminate(OperObject operObject, List<UpgradeInfo> list)
			throws RemoteException, Exception;

	/**
	 * ���Ѿ�������������й鵵
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void archive(OperObject operObject) throws RemoteException,
			Exception;

	/**
	 * ��ȡÿ����վ������һ���鵵
	 * 
	 * @return �鵵����������б�
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Map<Integer, List<UpgradeInfoArchive>> queryLatestArchive()
			throws RemoteException, Exception;

	/**
	 * ��ȡһ����վ��������ʷ��¼
	 * 
	 * @param moId
	 * @return һ����վ��������ʷ��¼
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<UpgradeInfoArchive> queryArchiveByMoId(long moId)
			throws RemoteException, Exception;

	/**
	 * ��ȡ��Ҫ����ĳ���汾����������б�
	 * 
	 * @param version
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<UpgradeInfo> queryUpgradingInfoByVersion(String version)
			throws RemoteException, Exception;
}
