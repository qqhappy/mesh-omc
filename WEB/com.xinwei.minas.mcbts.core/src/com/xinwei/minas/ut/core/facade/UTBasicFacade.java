/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-29	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.ut.core.model.UTBacthUpgradeResult;
import com.xinwei.minas.ut.core.model.UTBatchUpgradeQueryModel;
import com.xinwei.minas.ut.core.model.UTCondition;
import com.xinwei.minas.ut.core.model.UTQueryResult;
import com.xinwei.minas.ut.core.model.UserTerminal;

/**
 * 
 * �ն˻�������ӿ�
 * 
 * 
 * @author tiance
 * 
 */

public interface UTBasicFacade extends Remote {
	/**
	 * ��������HLR��ѯ�ն�
	 * 
	 * @param utc
	 * @return
	 * @throws Exception
	 */
	public UTQueryResult queryUTByCondition(UTCondition utc)
			throws RemoteException, Exception;

	/**
	 * �����ݿ��ѯ�����ն�����
	 * 
	 * @return
	 */
	public List<TerminalVersion> queryUTTypes() throws RemoteException,
			Exception;

	/**
	 * ���ĳ���ն˵�����״̬
	 * 
	 * @param pid
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public UserTerminal queryUTByPid(String pid) throws RemoteException,
			Exception;
	/**
	 * ����ն����������Ľ��
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<UTBacthUpgradeResult> queryUTBathUpgradeAll()throws RemoteException,Exception;
    /**
     * ����ն������������ܼ�¼����
     * flag--true  �����ɹ��ļ�¼����    flag--false����ʧ�ܵļ�¼����
     * @return
     * @throws Exception
     * @throws Exception
     */
	public Integer queryUTBatchUpgradeTotalCounts(Integer flag)throws RemoteException,Exception;
	/**
	 * �������Ĳ�ѯ�ն����������Ľ����
	 * @param queryModel
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<UTBacthUpgradeResult> queryUTBatchUpgradeByCondition(UTBatchUpgradeQueryModel queryModel) throws RemoteException,Exception;
	/**
	 * �洢�ն��������
	 * @param utResult
	 */
	public void saveUTBatchUpgradeResult(UTBacthUpgradeResult utResult)throws RemoteException,Exception;
	/**
	 * ����ɾ���ն��������
	 * @param utResults
	 * @throws RemoteException
	 * @throws Exception
	 */
    public void batchDeleteResults(List<UTBacthUpgradeResult> utResults)throws RemoteException,Exception;
    /**
     * �����ݿ��ѯ�ж��Ƿ�����������־
     * @param utResult
     * @return
     * @throws RemoteException
     * @throws Exception
     */
    public Integer queryIsUpgradingFlag(UTBacthUpgradeResult utResult) throws RemoteException,Exception;
}
