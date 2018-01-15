/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

/**
 * eNB�澯����ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbAlarmService {

	/**
	 * ��ָ����Ԫ���и澯ͬ������
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void syncAlarm(long moId) throws Exception;

}
