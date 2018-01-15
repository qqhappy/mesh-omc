/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-2	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.model.corenet.EnbGlobalConfig;
import com.xinwei.minas.enb.core.model.corenet.TaModel;
import com.xinwei.omp.core.model.biz.PagingCondition;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * eNBȫ���������ݷ���ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbGlobalConfigService {

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
	public Map<Object, String> configEnbGlobalConfig(EnbGlobalConfig config)
			throws Exception;

	/**
	 * ��Ӹ�������
	 * 
	 * @param taModel
	 * @throws Exception
	 */
	public void addTaItem(TaModel taModel) throws Exception;

	/**
	 * �޸ĸ�������
	 * 
	 * @param taModel
	 * @throws Exception
	 */
	public void modifyTaItem(TaModel taModel) throws Exception;

	/**
	 * ɾ����������
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void deleteTaItem(int id) throws Exception;

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
	 * @return
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
