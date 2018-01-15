/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-07-31	| chenjunhua 	| 	create the file                    
 */
package com.xinwei.minas.server.hlr.net.oss;

import java.util.concurrent.TimeoutException;

import com.xinwei.minas.server.hlr.net.oss.model.HlrOssBizMessage;

/**
 * HLR OSS ͨ��������
 * 
 * @author chenjunhua
 * 
 */
public interface HlrOssConnector {

	/**
	 * ҵ����Ϣͬ�����ýӿ�
	 * 
	 * @param hlrOssBizMessage
	 *            ҵ����Ϣ
	 * @return
	 * @throws TimeoutException
	 * @throws Exception
	 */
	public HlrOssBizMessage syncInvoke(HlrOssBizMessage hlrOssBizMessage)
			throws TimeoutException, Exception;

	/**
	 * ҵ����Ϣͬ�����ýӿ�
	 * 
	 * @param hlrOssBizMessage
	 *            ҵ����Ϣ
	 * @param timeout
	 *            ��ʱʱ�䣨��λ���룩
	 * @return
	 * @throws TimeoutException
	 * @throws Exception
	 */
	public HlrOssBizMessage syncInvoke(HlrOssBizMessage hlrOssBizMessage, int timeout)
			throws TimeoutException, Exception;

	/**
	 * �첽���ýӿ�
	 * 
	 * @param hlrOssBizMessage
	 *            ҵ����Ϣ
	 * @throws Exception
	 */
	public void asyncInvoke(HlrOssBizMessage hlrOssBizMessage) throws Exception;

}
