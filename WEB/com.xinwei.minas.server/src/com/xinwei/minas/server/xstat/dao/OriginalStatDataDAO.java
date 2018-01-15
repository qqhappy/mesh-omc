/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-23	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.dao;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.xstat.core.model.StatItem;

/**
 * 
 * ����ͳ��ʵ��־û��ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface OriginalStatDataDAO {

	/**
	 * ��������ͳ������ʵ��
	 * 
	 * @param statItemList
	 * @throws Exception
	 */
	public void save(List<StatItem> statItemList) throws Exception;

	/**
	 * ��moId����ʼ����ʱ���ѯ
	 * 
	 * @param moId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	public List<StatItem> queryBy(long moId, long startTime, long endTime)
			throws Exception;

	/**
	 * ɾ��ָ��ʱ����ڵ�����ͳ������
	 * 
	 * @param startTime
	 * @param endTime
	 * @throws Exception
	 */
	public void delete(long startTime, long endTime) throws Exception;

	/**
	 * ����������ѯԭʼ����
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
	public List<StatItem> queryBy(Map<Long, List<String>> entityMap,
			List<String> itemList, long startTime, long endTime)
			throws Exception;

}
