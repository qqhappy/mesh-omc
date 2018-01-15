/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * eNB�������ýӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbSimplifyConfigFacade extends Remote {

	/**
	 * ��ӵ��壬���ΪRRU���壬��Ҫָ�����ӵĹ�ںţ���������˱��¼
	 * 
	 * @param operObject
	 * @param moId
	 * @param boardRecord
	 * @param fiberPort
	 * @throws Exception
	 */
	@Loggable
	public void addBoard(OperObject operObject, long moId,
			XBizRecord boardRecord, Integer fiberPort) throws Exception;

	/**
	 * �޸ĵ��壬���ΪRRU���壬��Ҫָ�����ӵĹ�ںţ����޸����˱��¼
	 * 
	 * @param operObject
	 * @param moId
	 * @param boardRecord
	 * @param fiberPort
	 * @throws Exception
	 */
	@Loggable
	public void updateBoard(OperObject operObject, long moId,
			XBizRecord boardRecord, Integer fiberPort) throws Exception;

	/**
	 * ɾ�����壬ͬʱɾ�����˱�����Ӧ��¼
	 * 
	 * @param operObject
	 * @param moId
	 * @param boardRecord
	 * @throws Exception
	 */
	@Loggable
	public void deleteBoard(OperObject operObject, long moId,
			XBizRecord boardRecord) throws Exception;

	/**
	 * ����С���������¼
	 * 
	 * @param operObject
	 * @param moId
	 * @param cellParaRecord
	 * @throws Exception
	 */
	@Loggable
	public void updateCellPara(OperObject operObject, long moId,
			XBizRecord cellParaRecord) throws Exception;

	/**
	 * ɾ��С���������¼��ͬʱ����С����ص���������ɾ��
	 * 
	 * @param operObject
	 * @param moId
	 * @param cellParaRecord
	 * @throws Exception
	 */
	public void deleteCellPara(OperObject operObject, long moId,
			XBizRecord cellParaRecord) throws Exception;

}
