/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-28	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.dao;

import java.util.List;

import com.xinwei.minas.server.xstat.model.TableStrategyModel;

/**
 * 
 * ͳ�����ݳ־û��ֱ����
 * 
 * @author fanhaoyu
 * 
 */

public interface StatDataTableStrategy {

	/**
	 * ��ȡĿ�����ݿ�ͱ�
	 * 
	 * @param moId
	 * @param startTime
	 *            ʱ���ʽΪ:YYYYMMDDhhmmss
	 * @param endTime
	 *            ʱ���ʽΪ:YYYYMMDDhhmmss
	 * @return
	 */
	public List<TableStrategyModel> getOriginalTarget(Long moId,
			Long startTime, Long endTime);

	/**
	 * ����ʱ���ȡĿ�����ݿ���
	 * 
	 * @param time
	 *            ʱ���ʽΪ:YYYYMMDDhhmmss
	 * @return
	 */
	public String getTargetDataBase(long time);

	/**
	 * ��ȡСʱԤͳ�����ݵ�Ŀ�����ݿ�ͱ�
	 * 
	 * @param moId
	 * @param startTime
	 *            ʱ���ʽΪ:YYYYMMDDhhmmss
	 * @param endTime
	 *            ʱ���ʽΪ:YYYYMMDDhhmmss
	 * @return
	 */
	public List<TableStrategyModel> getPreOneHourTarget(Long moId,
			Long startTime, Long endTime);

	/**
	 * ��ȡ��Ԥͳ�����ݵ�Ŀ�����ݿ�ͱ�
	 * 
	 * @param moId
	 * @param startTime
	 *            ʱ���ʽΪ:YYYYMMDDhhmmss
	 * @param endTime
	 *            ʱ���ʽΪ:YYYYMMDDhhmmss
	 * @return
	 */
	public List<TableStrategyModel> getPreOneDayTarget(Long moId,
			Long startTime, Long endTime);

	/**
	 * ������ʼ����ʱ���ȡĿ�����ݿ��б�
	 * <p>
	 * ���startTimeΪ20120201000000������ʱ��Ϊ20120401000000���򷵻ؽ��Ϊ201202,201203
	 * </p>
	 * <p>
	 * ���startTimeΪ20120206000000������ʱ��Ϊ20120401000000���򷵻ؽ��Ϊ201202,201203
	 * </p>
	 * <p>
	 * ���startTimeΪ20120206000000������ʱ��Ϊ20120406000000���򷵻ؽ��Ϊ201202,201203
	 * </p>
	 * 
	 * @param startTime
	 *            ʱ���ʽΪ:YYYYMMDDhhmmss
	 * @param endTime
	 *            ʱ���ʽΪ:YYYYMMDDhhmmss
	 * @return
	 */
	public List<String> getTargetDataBaseList(long startTime, long endTime);

	/**
	 * ��ȡĿ�����ݿ�ͱ�
	 * 
	 * @param moId
	 * @param time
	 *            ʱ���ʽΪ:YYYYMMDDhhmmss
	 * @param itemId
	 *            ͳ����ID
	 * @return
	 */
	public TableStrategyModel getOriginalTarget(Long moId, Long time);

	/**
	 * ��ȡСʱԤͳ�����ݵ�Ŀ�����ݿ�ͱ�
	 * 
	 * @param moId
	 * @param time
	 *            ʱ���ʽΪ:YYYYMMDDhhmmss
	 * @return
	 */
	public TableStrategyModel getPreOneHourTarget(Long moId, Long time);

	/**
	 * ��ȡ��Ԥͳ�����ݵ�Ŀ�����ݿ�ͱ�
	 * 
	 * @param moId
	 * @param time
	 *            ʱ���ʽΪ:YYYYMMDDhhmmss
	 * @return
	 */
	public TableStrategyModel getPreOneDayTarget(Long moId, Long time);
}
