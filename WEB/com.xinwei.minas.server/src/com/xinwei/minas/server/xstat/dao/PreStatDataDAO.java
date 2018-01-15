/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.dao;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.xstat.core.model.PreStatItem;

/**
 * 
 * Ԥͳ�����ݳ־û��ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface PreStatDataDAO {

	/**
	 * ����Ԥͳ������
	 * 
	 * @param item
	 * @param preStatType
	 *            Ԥͳ�����ͣ��ο�StatConstants�е�Ԥͳ�����Ͷ���
	 * @throws Exception
	 */
	public void savePreStatItem(PreStatItem item, int preStatType)
			throws Exception;

	/**
	 * ��������Ԥͳ������
	 * 
	 * @param itemList
	 * @param preStatType
	 *            Ԥͳ�����ͣ��ο�StatConstants�е�Ԥͳ�����Ͷ���
	 * @throws Exception
	 */
	public void savePreStatItem(List<PreStatItem> itemList, int preStatType)
			throws Exception;

	/**
	 * ����������ѯСʱԤͳ������
	 * 
	 * @param entityMap
	 *            ��ʽ:key-moId,value=List[entityType#entityOid]
	 * @param itemList
	 *            ͳ�����б���ʽ:itemType#itemId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public List<PreStatItem> queryHourPreStatData(
			Map<Long, List<String>> entityMap, List<String> itemList,
			long startTime, long endTime) throws Exception;

	/**
	 * ����������ѯ��Ԥͳ������
	 * 
	 * @param entityMap
	 *            ��ʽ:key-moId,value=List[entityType#entityOid]
	 * @param itemList
	 *            ͳ�����б���ʽ:itemType#itemId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public List<PreStatItem> queryDayPreStatData(
			Map<Long, List<String>> entityMap, List<String> itemList,
			long startTime, long endTime) throws Exception;

}
