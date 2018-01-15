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
 * McBTS底层连接接口
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsConnector {

	/**
	 * 初始化
	 * @throws Exception
	 */
	public void initialize() throws Exception;
	
	/**
	 * 增加网元消息通知侦听器
	 * 
	 * @param listener
	 *            网元消息通知侦听器
	 */
	public void addListener(McBtsNotifyListener listener);

	/**
	 * 删除网元消息通知侦听器
	 * 
	 * @param listener
	 *            网元消息通知侦听器
	 */
	public void removeListener(McBtsNotifyListener listener);

	/**
	 * 通知网元消息通知侦听器
	 * 
	 * @param mcBtsMessage
	 *            通知消息
	 */
	public void nofityListener(McBtsMessage mcBtsMessage);
	
	
	/**
	 * 同步调用接口方法
	 * 
	 * @param request
	 *            请求消息
	 */
	public McBtsMessage syncInvoke(McBtsMessage request) throws TimeoutException, Exception;

	/**
	 * 异步调用接口方法
	 * 
	 * @param mcBtsMessage
	 *            请求消息
	 */
	public void asyncInvoke(McBtsMessage mcBtsMessage) throws Exception;
}
