/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.stat.service;

import com.xinwei.minas.stat.core.model.MonitorItem;

/**
 * 
 * ʵʱ���ܼ�ط���ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface RealTimePerfMonitorService {

	/**
	 * ������ĳ������ļ���
	 * 
	 * @param sessionId
	 * @param item
	 * @throws Exception
	 */
	public void start(String sessionId, MonitorItem item) throws Exception;

	/**
	 * ֹͣ��ĳ������ļ���
	 * 
	 * @param sessionId
	 * @param item
	 * @throws Exception
	 */
	public void stop(String sessionId, MonitorItem item) throws Exception;

	/**
	 * �ͻ�����������������
	 * 
	 * @param sessionId
	 * @param item
	 * @throws Exception
	 */
	public void handshake(String sessionId, MonitorItem item) throws Exception;

}
