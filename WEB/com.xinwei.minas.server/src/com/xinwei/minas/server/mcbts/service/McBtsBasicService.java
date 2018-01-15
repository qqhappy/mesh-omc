/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsCondition;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * McBts����ҵ�����ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsBasicService extends ICustomService {

	/**
	 * ����һ����վ
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public void add(McBts mcBts) throws Exception;

	/**
	 * �޸�һ����վ
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public void modify(McBts mcBts) throws Exception;

	/**
	 * ɾ��һ����վ
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void delete(Long moId) throws Exception;

	/**
	 * �޸Ĺ���״̬
	 * 
	 * @param moId
	 * @param manageState
	 * @throws Exception
	 */
	public void changeManageState(Long moId, ManageState manageState)
			throws Exception;

	/**
	 * ��ѯMcBts������Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public McBts queryByMoId(Long moId) throws Exception;

	/**
	 * ����BtsId��ѯMcBts��Ϣ
	 * 
	 * @param btsId
	 * @return
	 * @throws Exception
	 */
	public McBts queryByBtsId(Long btsId) throws Exception;

	/**
	 * ������ͬ��Ƶ���ѯMcBts
	 * 
	 * @param freq
	 * @return
	 * @throws Exception
	 */
	public List<McBts> queryBySameFreq(int freq) throws Exception;

	// /**
	// * ����McBts������Ϣ
	// *
	// * @param mcBts
	// * @throws Exception
	// */
	// public void config(McBts mcBts) throws Exception;

	/**
	 * ��ѯ���л�վ
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<McBts> queryAll() throws Exception;

	/**
	 * ����վ���Ͳ�ѯ��վ
	 * 
	 * @param btsType
	 * @return
	 * @throws Exception
	 */
	public List<McBts> queryByBtsType(int btsType) throws Exception;

	/**
	 * ��ȡ��moIdΪkey�Ļ�վmap
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Long, McBts> getMapByMoId() throws Exception;

	/**
	 * ��ȡ��btsIdΪkey�Ļ�վmap
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Long, McBts> getMapByBtsId() throws Exception;

	/**
	 * ����������ѯ��վ ��������
	 * 
	 * @param mcBtsCondition
	 * @return
	 * @throws Exception
	 * @author liuzhongyan
	 */
	public PagingData<McBts> queryAllByCondition(McBtsCondition mcBtsCondition)
			throws Exception;

	/**
	 * ��ѯ���з��������Ļ�վ
	 * 
	 * @param condition
	 *            ��ѯ����
	 * @return ���������Ļ�վ�б�
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryMcBtsBy(McBtsCondition condition) throws Exception;

	/**
	 * ���ݻ�վMoId�б��ѯһ�л�վ������״̬
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryByMoIdList(List<Long> moIds) throws Exception;

	/**
	 * ���ݻ�վ�ĵ�ǰ״̬���»�վ��������
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public void updateMcBtsCache(McBts mcBts) throws Exception;

	void fillExportList(Business business) throws Exception;

}
