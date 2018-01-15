/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.util.List;

import com.xinwei.minas.enb.core.model.EnbNeighbourRecord;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * eNB�������÷���ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbNeighbourService {

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
	 * @param moId
	 * @param record
	 * @throws Exception
	 */
	public void addNeighbour(long moId, EnbNeighbourRecord record)
			throws Exception;

	/**
	 * ����ָ����Ԫ��������¼�����֮ǰ��Ϊ��������Ϊ����������Ҫ����ӦeNB������ϵ���е���Ӧ��¼ɾ�������֮ǰ����������Ϊ��Ϊ������
	 * ��Ҫ�ڶ�ӦeNB������ϵ���������Ӧ��¼
	 * 
	 * @param moId
	 * @param record
	 * @throws Exception
	 */
	public void updateNeighbour(long moId, EnbNeighbourRecord record)
			throws Exception;

	/**
	 * ɾ��ָ����Ԫ��������¼�������Ϊ����������Ҫ����ӦeNB������ϵ���е���Ӧ��¼ɾ��
	 * 
	 * @param moId
	 * @param record
	 * @throws Exception
	 */
	public void deleteNeighbour(long moId, EnbNeighbourRecord record)
			throws Exception;

	/**
	 * С�������������С����ʶ�����������������Ƶ�ı�ʱ����Ҫ���С����������С������������ϵ����ָ��������ܼ�¼
	 * 
	 * @param moId
	 * @param cellParaRecord
	 *            С���������¼
	 * @throws Exception
	 */
	public void syncCellConfigToAll(long moId, XBizRecord cellParaRecord)
			throws Exception;

}
