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
 * HLR UDP底层连接接口
 * 
 * @author chenjunhua
 * 
 */

public interface HlrUdpConnector {

	/**
	 * 增加网元消息通知侦听器
	 * 
	 * @param listener
	 *            网元消息通知侦听器
	 */
	public void addListener(HlrUdpNotifyListener listener);

	/**
	 * 删除网元消息通知侦听器
	 * 
	 * @param listener
	 *            网元消息通知侦听器
	 */
	public void removeListener(HlrUdpNotifyListener listener);

	/**
	 * 通知网元消息通知侦听器
	 * 
	 * @param message
	 *            通知消息
	 */
	public void nofityListener(HlrUdpMessage message);
	
	
	
	/**
	 * 增加OSS消息通知侦听器
	 * 
	 * @param listener
	 *            OSS消息通知侦听器
	 */
	public void addOssListener(HlrUdpNotifyListener listener);

	/**
	 * 删除OSS消息通知侦听器
	 * 
	 * @param listener
	 *            OSS消息通知侦听器
	 */
	public void removeOssListener(HlrUdpNotifyListener listener);

	/**
	 * 通知OSS消息通知侦听器
	 * 
	 * @param message
	 *            通知消息
	 */
	public void nofityOssListener(HlrUdpMessage message);
	
	
	/**
	 * 同步调用接口方法
	 * 
	 * @param request
	 *            请求消息
	 */
	public List<HlrUdpMessage> syncInvoke(HlrUdpMessage request) throws TimeoutException, Exception;

	/**
	 * 异步调用接口方法
	 * 
	 * @param request
	 *            请求消息
	 */
	public void asyncInvoke(HlrUdpMessage request) throws Exception;
}
