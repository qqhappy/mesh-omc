/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-19	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.enb.core.model.EnbAsset;
import com.xinwei.minas.enb.core.model.EnbAssetCondition;
import com.xinwei.minas.enb.core.model.EnbAssetHistory;
import com.xinwei.omp.core.model.biz.PagingData;


/**
 * 
 * �ʲ�����
 * 
 * @author chenlong
 * 
 */

public interface EnbAssetFacade extends Remote {
	
	/**
	 * ��������ѯ�ʲ���Ϣ
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@Loggable
	public PagingData<EnbAsset> queryByCondition(EnbAssetCondition condition) throws Exception;
	
	
	/**
	 * ��������ѯ��ʷ�ʲ���Ϣ
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@Loggable
	public PagingData<EnbAssetHistory> queryHistoryByCondition(EnbAssetCondition condition) throws Exception;
	
	
	/**
	 * ȷ��ͣ���ʲ�
	 * @param assetHistory
	 * @throws BizException
	 */
	@Loggable
	public void confirmStop(EnbAssetHistory assetHistory) throws Exception;
	
	
	/**
	 * �޸�
	 * @param enbAsset
	 * @throws Exception
	 */
	public void update(EnbAsset enbAsset) throws Exception;
	
	
}
