/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.proxy;

/**
 * 
 * eNBʵʱ���ܼ�ش���ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbRealtimeMonitorProxy {

	/**
	 * ��ʼ�ϱ�ʵʱ��������
	 * 
	 * @param enbId
	 * @param intervalFlag
	 * @throws Exception
	 */
	public void start(long enbId, int intervalFlag) throws Exception;

	/**
	 * ֹͣ�ϱ�ʵʱ��������
	 * 
	 * @param enbId
	 * @throws Exception
	 */
	public void stop(long enbId) throws Exception;

}
