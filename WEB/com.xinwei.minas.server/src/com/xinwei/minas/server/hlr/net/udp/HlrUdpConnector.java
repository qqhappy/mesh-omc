/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.hlr.net.udp;

import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * 
 * HLR UDP�ײ����ӽӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface HlrUdpConnector {

	/**
	 * ������Ԫ��Ϣ֪ͨ������
	 * 
	 * @param listener
	 *            ��Ԫ��Ϣ֪ͨ������
	 */
	public void addListener(HlrUdpNotifyListener listener);

	/**
	 * ɾ����Ԫ��Ϣ֪ͨ������
	 * 
	 * @param listener
	 *            ��Ԫ��Ϣ֪ͨ������
	 */
	public void removeListener(HlrUdpNotifyListener listener);

	/**
	 * ֪ͨ��Ԫ��Ϣ֪ͨ������
	 * 
	 * @param message
	 *            ֪ͨ��Ϣ
	 */
	public void nofityListener(HlrUdpMessage message);
	
	
	
	/**
	 * ����OSS��Ϣ֪ͨ������
	 * 
	 * @param listener
	 *            OSS��Ϣ֪ͨ������
	 */
	public void addOssListener(HlrUdpNotifyListener listener);

	/**
	 * ɾ��OSS��Ϣ֪ͨ������
	 * 
	 * @param listener
	 *            OSS��Ϣ֪ͨ������
	 */
	public void removeOssListener(HlrUdpNotifyListener listener);

	/**
	 * ֪ͨOSS��Ϣ֪ͨ������
	 * 
	 * @param message
	 *            ֪ͨ��Ϣ
	 */
	public void nofityOssListener(HlrUdpMessage message);
	
	
	/**
	 * ͬ�����ýӿڷ���
	 * 
	 * @param request
	 *            ������Ϣ
	 */
	public List<HlrUdpMessage> syncInvoke(HlrUdpMessage request) throws TimeoutException, Exception;

	/**
	 * �첽���ýӿڷ���
	 * 
	 * @param request
	 *            ������Ϣ
	 */
	public void asyncInvoke(HlrUdpMessage request) throws Exception;
}
