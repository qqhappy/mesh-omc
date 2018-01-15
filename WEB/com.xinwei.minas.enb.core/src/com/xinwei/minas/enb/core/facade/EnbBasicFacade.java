/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbCondition;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * ENodeB������������ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface EnbBasicFacade extends Remote {

	/**
	 * ����һ����վ
	 * 
	 * @param enb
	 * @throws Exception
	 */
	@Loggable
	public void add(OperObject operObject, Enb enb) throws Exception;

	/**
	 * �޸�һ����վ
	 * 
	 * @param enb
	 * @throws Exception
	 */
	@Loggable
	public void modify(OperObject operObject, Enb enb) throws Exception;

	/**
	 * ɾ��һ����վ
	 * 
	 * @param moId
	 * @throws Exception
	 */
	@Loggable
	public void delete(OperObject operObject, Long moId) throws Exception;

	/**
	 * �޸Ĺ���״̬
	 * 
	 * @param moId
	 * @param manageState
	 * @throws Exception
	 */
	@Loggable
	public void changeManageState(OperObject operObject, Long moId,
			ManageState manageState) throws Exception;

	/**
	 * ��ѯϵͳ������eNB��վ
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Enb> queryAllEnb() throws Exception;

	/**
	 * ��ѯ��վ������Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public Enb queryByMoId(Long moId) throws Exception;

	/**
	 * ����enbId��ѯ��վ��Ϣ
	 * 
	 * @param enbId
	 * @return
	 * @throws Exception
	 */
	public Enb queryByEnbId(Long enbId) throws Exception;

	/**
	 * ����������ѯ��վ
	 * 
	 * @param condition
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public PagingData<Enb> queryAllByCondition(EnbCondition condition)
			throws Exception;

	/**
	 * ���ݻ�վMoId�б��ѯһ�л�վ������״̬
	 * 
	 * @param moIds
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<Enb> queryByMoIdList(List<Long> moIds) throws RemoteException,
			Exception;
}
