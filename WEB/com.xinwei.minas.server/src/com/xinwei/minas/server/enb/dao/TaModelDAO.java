/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-2	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao;

import java.util.List;

import com.xinwei.minas.enb.core.model.corenet.TaModel;
import com.xinwei.omp.core.model.biz.PagingCondition;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * ��������DAO�ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface TaModelDAO {

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
