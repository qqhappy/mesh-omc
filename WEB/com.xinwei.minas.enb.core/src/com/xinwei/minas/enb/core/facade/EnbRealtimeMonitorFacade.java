/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.util.List;

import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemConfig;

/**
 * 
 * ʵʱ���ܼ�ط���������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbRealtimeMonitorFacade extends Remote {

	/**
	 * ��ѯʵʱ����ͳ����������Ϣ
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<EnbRealtimeItemConfig> queryItemConfig() throws Exception;

	/**
	 * ��ʼ���ĳeNB
	 * 
	 * @param sessionId
	 * @param moId
	 * @throws Exception
	 */
	public void startMonitor(String sessionId, long moId) throws Exception;

	/**
	 * ֹͣ���ĳeNB
	 * 
	 * @param sessionId
	 * @param moId
	 * @throws Exception
	 */
	public void stopMonitor(String sessionId, long moId) throws Exception;
}
