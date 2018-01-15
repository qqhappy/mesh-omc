/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-12	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.model.EnbNeighbourRecord;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * eNB������ϵ��������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbNeighbourFacade extends Remote {

	/**
	 * ��ѯָ����Ԫ����������¼
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<EnbNeighbourRecord> queryNeighbourRecords(long moId)
			throws Exception;

	/**
	 * ��������ѯ������¼
	 * 
	 * @param moId
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public EnbNeighbourRecord queryNeighbourRecord(long moId,
			XBizRecord condition) throws Exception;

	/**
	 * ���ָ����Ԫ��������ϵ���¼�������¼����С�������С����Ϊ�������򽫼�¼���������Ԫ������ϵ���
	 * ����Ҫ����������eNB��������ϵ������Ӷ�Ӧ��¼
	 * 
	 * @param operObject
	 * @param moId
	 * @param record
	 * @throws Exception
	 */
	@Loggable
	public void addNeighbour(OperObject operObject, long moId,
			EnbNeighbourRecord record) throws Exception;

	/**
	 * ����ָ����Ԫ��������¼�����֮ǰ��Ϊ��������Ϊ����������Ҫ����ӦeNB������ϵ���е���Ӧ��¼ɾ�������֮ǰ����������Ϊ��Ϊ������
	 * ��Ҫ�ڶ�ӦeNB������ϵ���������Ӧ��¼
	 * 
	 * @param operObject
	 * @param moId
	 * @param record
	 * @throws Exception
	 */
	@Loggable
	public void updateNeighbour(OperObject operObject, long moId,
			EnbNeighbourRecord record) throws Exception;

	/**
	 * ɾ��ָ����Ԫ��������¼�������Ϊ����������Ҫ����ӦeNB������ϵ���е���Ӧ��¼ɾ��
	 * 
	 * @param operObject
	 * @param moId
	 * @param record
	 * @throws Exception
	 */
	@Loggable
	public void deleteNeighbour(OperObject operObject, long moId,
			EnbNeighbourRecord record) throws Exception;

}
