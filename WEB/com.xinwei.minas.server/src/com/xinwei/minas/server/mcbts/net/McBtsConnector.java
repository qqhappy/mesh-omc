/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.net;

import java.util.concurrent.TimeoutException;

/**
 * 
 * McBTS�ײ����ӽӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsConnector {

	/**
	 * ��ʼ��
	 * @throws Exception
	 */
	public void initialize() throws Exception;
	
	/**
	 * ������Ԫ��Ϣ֪ͨ������
	 * 
	 * @param listener
	 *            ��Ԫ��Ϣ֪ͨ������
	 */
	public void addListener(McBtsNotifyListener listener);

	/**
	 * ɾ����Ԫ��Ϣ֪ͨ������
	 * 
	 * @param listener
	 *            ��Ԫ��Ϣ֪ͨ������
	 */
	public void removeListener(McBtsNotifyListener listener);

	/**
	 * ֪ͨ��Ԫ��Ϣ֪ͨ������
	 * 
	 * @param mcBtsMessage
	 *            ֪ͨ��Ϣ
	 */
	public void nofityListener(McBtsMessage mcBtsMessage);
	
	
	/**
	 * ͬ�����ýӿڷ���
	 * 
	 * @param request
	 *            ������Ϣ
	 */
	public McBtsMessage syncInvoke(McBtsMessage request) throws TimeoutException, Exception;

	/**
	 * �첽���ýӿڷ���
	 * 
	 * @param mcBtsMessage
	 *            ������Ϣ
	 */
	public void asyncInvoke(McBtsMessage mcBtsMessage) throws Exception;
}
