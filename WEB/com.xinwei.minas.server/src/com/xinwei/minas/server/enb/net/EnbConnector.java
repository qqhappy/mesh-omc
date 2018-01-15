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
 * eNB 通信代理
 * 
 * @author chenjunhua
 * 
 */

public interface EnbConnector {
	
	
	/**
	 * 增加网元消息通知侦听器
	 * 
	 * @param listener
	 *            网元消息通知侦听器
	 */
	public void addListener(EnbNotifyListener listener);

	/**
	 * 删除网元消息通知侦听器
	 * 
	 * @param listener
	 *            网元消息通知侦听器
	 */
	public void removeListener(EnbNotifyListener listener);

	/**
	 * 通知网元消息通知侦听器
	 * 
	 * @param enbAppMessage
	 *            通知消息
	 */
	public void nofityListener(EnbAppMessage enbAppMessage);

	/**
	 * 同步调用
	 * 
	 * @param enbAppMessage
	 * @return
	 * @throws TimeoutException
	 * @throws Exception
	 */
	public EnbAppMessage syncInvoke(EnbAppMessage enbAppMessage)
			throws TimeoutException, Exception;

	/**
	 * 指定超时时间同步调用
	 * 
	 * @param enbAppMessage
	 * @param timeout
	 *            超时时间，单位：毫秒
	 * @return
	 * @throws TimeoutException
	 * @throws Exception
	 */
	public EnbAppMessage syncInvoke(EnbAppMessage enbAppMessage, long timeout)
			throws TimeoutException, Exception;

	/**
	 * 异步调用
	 * 
	 * @param enbAppMessage
	 * @throws Exception
	 */
	public void asyncInvoke(EnbAppMessage enbAppMessage) throws Exception;
}
