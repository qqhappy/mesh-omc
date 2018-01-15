/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-4	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbCondition;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * eNB������Ϣ����ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbBasicService {

	/**
	 * ����һ����վ
	 * 
	 * @param enb
	 * @throws Exception
	 */
	@Loggable
	public void add(Enb enb) throws Exception;

	/**
	 * �޸�һ����վ
	 * 
	 * @param enb
	 * @throws Exception
	 */
	@Loggable
	public void modify(Enb enb) throws Exception;

	/**
	 * ɾ��һ����վ
	 * 
	 * @param moId
	 * @throws Exception
	 */
	@Loggable
	public void delete(Long moId) throws Exception;

	/**
	 * �޸Ĺ���״̬
	 * 
	 * @param moId
	 * @param manageState
	 * @throws Exception
	 */
	@Loggable
	public void changeManageState(Long moId, ManageState manageState)
			throws Exception;

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
	 * @throws Exception
	 */
	public PagingData<Enb> queryAllByCondition(EnbCondition condition)
			throws Exception;

	/**
	 * ���ݻ�վMoId�б��ѯһ�л�վ������״̬
	 * 
	 * @param moIds
	 * @return
	 * @throws Exception
	 */
	public List<Enb> queryByMoIdList(List<Long> moIds) throws Exception;

}
