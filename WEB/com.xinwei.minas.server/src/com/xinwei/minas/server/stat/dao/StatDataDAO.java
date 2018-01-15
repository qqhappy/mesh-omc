/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-22	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.dao;

import java.util.List;

import com.xinwei.minas.stat.core.model.StatData;

/**
 * 
 * ͳ������DAO�ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface StatDataDAO {

	/**
	 * �洢ͳ�������б�
	 * 
	 * @param dataList
	 * @throws Exception
	 */

	public void saveData(List<StatData> dataList) throws Exception;

	/**
	 * �洢ͳ������
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void saveData(StatData data) throws Exception;

	/**
	 * ��ѯָ��ʱ����ڵ�ͳ������
	 * 
	 * @param begintime
	 *            --ͳ�ƿ�ʼʱ��
	 * @param endtime
	 *            --ͳ�ƽ���ʱ��
	 * @param timeType
	 *            --ͳ������
	 * @return --ͳ�������б�
	 * @throws Exception
	 */
	public List<StatData> getData(long begintime, long endtime, int timeType)
			throws Exception;

	/**
	 * ��ѯָ����վ��ָ��ʱ����ڵ�ͳ������
	 * 
	 * @param btsId
	 *            --��վ���
	 * @param begintime
	 *            --ͳ�ƿ�ʼʱ��
	 * @param endtime
	 *            --ͳ�ƽ���ʱ��
	 * @param type
	 *            --ͳ������
	 * @return --ͳ�������б�
	 * @throws Exception
	 */
	public List<StatData> getData(long btsId, long begintime, long endtime,
			int timeType) throws Exception;

	/**
	 * ɾ��ָ��ʱ�����ͳ������
	 * 
	 * @param beginTime
	 *            --ͳ�ƿ�ʼʱ��
	 * @param endTime
	 *            --ͳ�ƽ���ʱ��
	 * @param timeType
	 *            --ͳ������
	 * @return --ɾ���ļ�¼��
	 * @throws Exception
	 */
	public int deleteData(long beginTime, long endTime, int timeType)
			throws Exception;

	/**
	 * ɾ��ĳʱ���֮ǰ������ͳ������
	 * 
	 * @param endTime
	 *            --ͳ�ƽ���ʱ��
	 * @param timeType
	 *            --ͳ������
	 * @return --ɾ���ļ�¼��
	 * @throws Exception
	 */
	public int deleteData(long endTime, int timeType) throws Exception;

}
