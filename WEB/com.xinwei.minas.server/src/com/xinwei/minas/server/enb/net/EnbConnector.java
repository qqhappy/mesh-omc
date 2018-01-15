/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.net;

import java.util.concurrent.TimeoutException;




/**
 * 
 * eNB ͨ�Ŵ���
 * 
 * @author chenjunhua
 * 
 */

public interface EnbConnector {
	
	
	/**
	 * ������Ԫ��Ϣ֪ͨ������
	 * 
	 * @param listener
	 *            ��Ԫ��Ϣ֪ͨ������
	 */
	public void addListener(EnbNotifyListener listener);

	/**
	 * ɾ����Ԫ��Ϣ֪ͨ������
	 * 
	 * @param listener
	 *            ��Ԫ��Ϣ֪ͨ������
	 */
	public void removeListener(EnbNotifyListener listener);

	/**
	 * ֪ͨ��Ԫ��Ϣ֪ͨ������
	 * 
	 * @param enbAppMessage
	 *            ֪ͨ��Ϣ
	 */
	public void nofityListener(EnbAppMessage enbAppMessage);

	/**
	 * ͬ������
	 * 
	 * @param enbAppMessage
	 * @return
	 * @throws TimeoutException
	 * @throws Exception
	 */
	public EnbAppMessage syncInvoke(EnbAppMessage enbAppMessage)
			throws TimeoutException, Exception;

	/**
	 * ָ����ʱʱ��ͬ������
	 * 
	 * @param enbAppMessage
	 * @param timeout
	 *            ��ʱʱ�䣬��λ������
	 * @return
	 * @throws TimeoutException
	 * @throws Exception
	 */
	public EnbAppMessage syncInvoke(EnbAppMessage enbAppMessage, long timeout)
			throws TimeoutException, Exception;

	/**
	 * �첽����
	 * 
	 * @param enbAppMessage
	 * @throws Exception
	 */
	public void asyncInvoke(EnbAppMessage enbAppMessage) throws Exception;
}
