/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.xstat.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * ͳ�����ݲ�ѯ����
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class StatDataQueryCondition implements Serializable {

	/**
	 * ͳ�Ƽ����15����
	 */
	public static final int INTERVAL_FIFTEEN_MINUTES = 1;

	/**
	 * ͳ�Ƽ����1Сʱ
	 */
	public static final int INTERVAL_ONE_HOUR = 2;

	/**
	 * ͳ�Ƽ����1��
	 */
	public static final int INTERVAL_ONE_DAY = 3;

	private long startTime;

	private long endTime;

	private int interval;

	private List<String> itemList;

	private Map<Long, List<String>> entityMap = new HashMap<Long, List<String>>();

	public long getStartTime() {
		return startTime;
	}

	/**
	 * �������ݿ�ʼʱ�䣬��ʽYYYYMMDDhhmmss
	 * 
	 * @param startTime
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	/**
	 * �������ݽ���ʱ�䣬��ʽYYYYMMDDhhmmss
	 * 
	 * @param endTime
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getInterval() {
		return interval;
	}

	/**
	 * ����ͳ�Ƽ��
	 * 
	 * @param interval
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * ����Ҫ��ѯ��ͳ�����б���ʽ��ͳ��������#ͳ����ID
	 * 
	 * @param itemList
	 */
	public void setItemList(List<String> itemList) {
		this.itemList = itemList;
	}

	public List<String> getItemList() {
		return itemList;
	}

	/**
	 * ���Ҫ��ѯ��ʵ���б���ʽ��entityType#entityOid
	 * 
	 * @param entityList
	 */
	public void addEntity(long moId, String entityType, String entityOid) {
		List<String> entityList = entityMap.get(moId);
		if (entityList == null) {
			entityList = new ArrayList<String>();
			entityMap.put(moId, entityList);
		}
		entityList.add(entityType + StatConstants.DEFAULT_SPLIT + entityOid);
	}

	public Map<Long, List<String>> getEntityMap() {
		return entityMap;
	}

}
