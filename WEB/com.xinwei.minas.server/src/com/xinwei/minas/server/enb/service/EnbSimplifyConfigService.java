/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * eNB�������÷���ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbSimplifyConfigService {

	/**
	 * ��ӵ��壬���ΪRRU���壬��Ҫָ�����ӵĹ�ںţ���������˱��¼
	 * 
	 * @param moId
	 * @param boardRecord
	 * @param fiberPort
	 * @throws Exception
	 */
	public void addBoard(long moId, XBizRecord boardRecord, Integer fiberPort)
			throws Exception;

	/**
	 * �޸ĵ��壬���ΪRRU���壬��Ҫָ�����ӵĹ�ںţ����޸����˱��¼
	 * 
	 * @param moId
	 * @param boardRecord
	 * @param fiberPort
	 * @throws Exception
	 */
	public void updateBoard(long moId, XBizRecord boardRecord, Integer fiberPort)
			throws Exception;

	/**
	 * ɾ�����壬ͬʱɾ�����˱�����Ӧ��¼
	 * 
	 * @param moId
	 * @param boardRecord
	 * @throws Exception
	 */
	public void deleteBoard(long moId, XBizRecord boardRecord) throws Exception;

	/**
	 * ����С���������¼
	 * 
	 * @param moId
	 * @param cellParaRecord
	 * @throws Exception
	 */
	public void updateCellPara(long moId, XBizRecord cellParaRecord)
			throws Exception;

	/**
	 * ɾ��С���������¼��ͬʱ����С����ص���������ɾ��
	 * 
	 * @param moId
	 * @param cellParaRecord
	 * @throws Exception
	 */
	public void deleteCellPara(long moId, XBizRecord cellParaRecord)
			throws Exception;
}
