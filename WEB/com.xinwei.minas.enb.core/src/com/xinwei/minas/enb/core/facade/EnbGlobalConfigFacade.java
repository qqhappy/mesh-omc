/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-2	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.model.corenet.EnbGlobalConfig;
import com.xinwei.minas.enb.core.model.corenet.TaModel;
import com.xinwei.omp.core.model.biz.PagingCondition;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * eNBȫ���������ݷ���������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbGlobalConfigFacade extends Remote {

	/**
	 * ��ѯeNBȫ������
	 * 
	 * @return
	 * @throws Exception
	 */
	public EnbGlobalConfig queryEnbGlobalConfig() throws Exception;

	/**
	 * ����eNBȫ������
	 * 
	 * @param config
	 * @return ���÷�����Ϣ
	 * @throws Exception
	 */
	@Loggable
	public Map<Object, String> configEnbGlobalConfig(OperObject operObject,
			EnbGlobalConfig config) throws Exception;

	/**
	 * ��Ӹ�������
	 * 
	 * @param taModel
	 * @throws Exception
	 */
	@Loggable
	public void addTaItem(OperObject operObject, TaModel taModel)
			throws Exception;

	/**
	 * �޸ĸ�������
	 * 
	 * @param taModel
	 * @throws Exception
	 */
	@Loggable
	public void modifyTaItem(OperObject operObject, TaModel taModel)
			throws Exception;

	/**
	 * ɾ����������
	 * 
	 * @param id
	 * @throws Exception
	 */
	@Loggable
	public void deleteTaItem(OperObject operObject, int id) throws Exception;

	/**
	 * ��ID��ѯ���������¼
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public TaModel queryTaItemById(int id) throws Exception;

	/**
	 * ��ѯ���и�������
	 * 
	 * @throws Exception
	 */
	public List<TaModel> queryAllTaItems() throws Exception;

	/**
	 * ���շ�ҳ������ѯ������������
	 * 
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public PagingData<TaModel> queryTaItems(PagingCondition condition)
			throws Exception;

}
