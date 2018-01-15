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

import com.xinwei.minas.stat.core.model.StatDetail;

/**
 * ͳ����ϸ��Ϣ�ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface StatDetailDAO {

	/**
	 * �洢�����ͳ����ϸ��Ϣ
	 * 
	 * @param detail
	 * @throws Exception
	 */
	public void saveOrUpdate(StatDetail detail) throws Exception;

	/**
	 * ȡ�����һ�Σ�ָ��ͳ��������ͳ�Ƴɹ���ͳ����ϸ��Ϣ
	 * 
	 * @param timeType
	 *            --ͳ������
	 * @return --ͳ����ϸ
	 * @throws Exception
	 */
	public StatDetail getMaxSuccessStatDetail(int timeType) throws Exception;

	/**
	 * ȡ��ĳ��ͳ���������һ��ͳ�ƽ���ʱ��
	 * 
	 * @param timeType
	 *            --ͳ������
	 * @return
	 * @throws Exception
	 */
	public long getLatestStatTime(int timeType) throws Exception;

	/**
	 * �����Ͳ���ʧ�ܵ�ͳ����Ϣ
	 * 
	 * @param timeType
	 *            --ͳ������
	 * @return --ͳ����ϸ
	 * @throws Exception
	 */
	public List<StatDetail> getFailedStatDetail(int timeType) throws Exception;

	/**
	 * ȡ��ָ��ͳ�����͡�ͳ�ƽ������ָ��ͳ�ƽ���ʱ��֮ǰ��ͳ����ϸ�б�
	 * 
	 * @param timeType
	 *            --ͳ������
	 * @param latestStatTime
	 *            --ͳ�ƽ���ʱ��
	 * @param flag
	 *            --�����ʶ
	 * @return --ͳ����ϸ�б�
	 * @throws Exception
	 */
	public List<StatDetail> getStatDetailBeforeSpecTime(int timeType,
			long latestStatTime, int flag) throws Exception;

	/**
	 * ȡ��ĳָ��ͳ��������ָ��ʱ��ǰ������ͳ����ϸ
	 * 
	 * @param timeType
	 *            --ͳ������
	 * @param latestStatTime
	 *            --ͳ�ƽ���ʱ��
	 * @return --ͳ����ϸ�б�
	 * @throws Exception
	 */
	public List<StatDetail> getFailedStatDetailBeforeSpecTime(int timeType,
			long latestStatTime) throws Exception;

	/**
	 * �h��ָ��ͳ�����ͣ�ͳ�ƽ���ʱ�䣬ͳ�ƽ����ͳ����ϸ
	 * 
	 * @param timeType
	 *            --ͳ������
	 * @param endtime
	 *            --ͳ�ƽ���ʱ��
	 * @param flag
	 *            --ͳ�ƽ����ʶ
	 * @throws Exception
	 */
	public void deleteStatDetail(int timeType, long endtime, int flag)
			throws Exception;
}
