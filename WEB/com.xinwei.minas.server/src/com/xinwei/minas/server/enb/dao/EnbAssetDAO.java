/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-20	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao;

import java.util.List;

import com.xinwei.minas.enb.core.model.EnbAsset;
import com.xinwei.minas.enb.core.model.EnbAssetCondition;
import com.xinwei.minas.enb.core.model.EnbAssetHistory;
import com.xinwei.omp.core.model.biz.PagingData;


/**
 * 
 * �ʲ���ϢDAO
 * 
 * @author chenlong
 * 
 */

public interface EnbAssetDAO {
	
	/**
	 * ����
	 * @param enbAsset
	 * @throws Exception
	 */
	public void add(EnbAsset enbAsset) throws Exception;
	
	/**
	 * �޸�
	 * @param enbAsset
	 * @throws Exception
	 */
	public void update(EnbAsset enbAsset) throws Exception;
	
	/**
	 * ɾ��
	 * @param productionSN
	 * @throws Exception
	 */
	public void delete(EnbAsset enbAsset) throws Exception;
	
	/**
	 * ��������ID��ѯ�ʲ���¼
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public EnbAsset queryById(long id) throws Exception;
	
	/**
	 * ��ѯ����
	 * @return
	 * @throws Exception
	 */
	public List<EnbAsset> queryAll() throws Exception;
	
	/**
	 * ����һ���ʲ���ʷ��¼
	 * @param assetHistory
	 * @throws Exception
	 */
	public void addHistory(EnbAssetHistory assetHistory) throws Exception; 
	
	/**
	 * ��������ѯ�ʲ���Ϣ
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public PagingData<EnbAsset> queryByCondition(EnbAssetCondition condition) throws Exception;
	
	/**
	 * ��������ѯ��ʷ�ʲ���Ϣ
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public PagingData<EnbAssetHistory> queryHistoryByCondition(EnbAssetCondition condition) throws Exception;

	/**
	 * ɾ���û�վ�������ʲ�
	 * @param enbId
	 */
	public void deleteEnbAll(Long enbId);
	
	
	
	
}
