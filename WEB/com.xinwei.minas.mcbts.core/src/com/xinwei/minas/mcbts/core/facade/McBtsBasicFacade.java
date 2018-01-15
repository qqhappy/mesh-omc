/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsCondition;
import com.xinwei.minas.sxc.core.model.SxcBasic;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * McBts����ҵ������
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsBasicFacade extends Remote {

	/**
	 * ����һ����վ
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	@Loggable
	public void add(OperObject operObject, McBts mcBts) throws RemoteException,
			Exception;

	/**
	 * �޸�һ����վ
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	@Loggable
	public void modify(OperObject operObject, McBts mcBts)
			throws RemoteException, Exception;

	/**
	 * ɾ��һ����վ
	 * 
	 * @param moId
	 * @throws Exception
	 */
	@Loggable
	public void delete(OperObject operObject, Long moId)
			throws RemoteException, Exception;

	/**
	 * �޸Ĺ���״̬
	 * 
	 * @param moId
	 * @param manageState
	 * @throws Exception
	 */
	@Loggable
	public void changeManageState(OperObject operObject, Long moId,
			ManageState manageState) throws RemoteException, Exception;

	/**
	 * ��ѯMcBts������Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public McBts queryByMoId(Long moId) throws RemoteException, Exception;

	/**
	 * ����BtsId��ѯMcBts��Ϣ
	 * 
	 * @param btsId
	 * @return
	 * @throws Exception
	 */
	public McBts queryByBtsId(Long btsId) throws RemoteException, Exception;

	/**
	 * ������ͬ��Ƶ���ѯMcBts
	 * 
	 * @param freq
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryBySameFreq(int freq) throws RemoteException,
			Exception;

	// /**
	// * ����McBts������Ϣ
	// *
	// * @param mcBts
	// * @throws Exception
	// */
	// public void config(McBts mcBts) throws RemoteException, Exception;

	/**
	 * ��ѯ���л�վ
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryAll() throws RemoteException, Exception;

	/**
	 * ����վ���Ͳ�ѯ��վ
	 * 
	 * @param btsType
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryByBtsType(int btsType) throws RemoteException,
			Exception;

	/**
	 * ��ȡ��moIdΪkey�Ļ�վmap
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Long, McBts> getMapByMoId() throws RemoteException, Exception;

	/**
	 * ��ȡ��btsIdΪkey�Ļ�վmap
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Long, McBts> getMapByBtsId() throws RemoteException, Exception;

	/**
	 * ����������ѯ��վ
	 * 
	 * @param mcBtsCondition
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public PagingData<McBts> queryAllByCondition(McBtsCondition mcBtsCondition)
			throws RemoteException, Exception;

	/**
	 * ��ѯ���з��������Ļ�վ
	 * 
	 * @param condition
	 *            ��ѯ����
	 * @return ���������Ļ�վ�б�
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryMcBtsBy(McBtsCondition condition)
			throws RemoteException, Exception;

	/**
	 * ���ݻ�վMoId�б��ѯһ�л�վ������״̬
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryByMoIdList(List<Long> moIds)
			throws RemoteException, Exception;

	/**
	 * ����SAG��Ϣ
	 * 
	 * @param oldSxc
	 *            ��SAG��Ϣ
	 * @param newSxc
	 *            ��SAG��Ϣ
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void updateSagInfo(SxcBasic oldSxc, SxcBasic newSxc)
			throws RemoteException, Exception;

	/**
	 * �����Է��ֵĻ�վ�б�
	 * 
	 * @return �Է��ֵĻ�վ�б�
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryAutomaticFindMcBts() throws RemoteException,
			Exception;

	/**
	 * ɾ��ָ�����Է��ֻ�վ
	 * 
	 * @param btsId
	 *            ��վID
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void deleteAutomaticFindMcBts(Long btsId) throws RemoteException,
			Exception;

	/**
	 * ���ݻ�վ�ĵ�ǰ״̬���»�վ��������
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public void updateMcBtsCache(OperObject operObject, McBts mcBts)
			throws Exception;

}
