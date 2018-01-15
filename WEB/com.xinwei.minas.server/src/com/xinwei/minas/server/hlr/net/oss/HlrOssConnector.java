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
 * HLR OSS 通信连接器
 * 
 * @author chenjunhua
 * 
 */
public interface HlrOssConnector {

	/**
	 * 业务消息同步调用接口
	 * 
	 * @param hlrOssBizMessage
	 *            业务消息
	 * @return
	 * @throws TimeoutException
	 * @throws Exception
	 */
	public HlrOssBizMessage syncInvoke(HlrOssBizMessage hlrOssBizMessage)
			throws TimeoutException, Exception;

	/**
	 * 业务消息同步调用接口
	 * 
	 * @param hlrOssBizMessage
	 *            业务消息
	 * @param timeout
	 *            超时时间（单位：秒）
	 * @return
	 * @throws TimeoutException
	 * @throws Exception
	 */
	public HlrOssBizMessage syncInvoke(HlrOssBizMessage hlrOssBizMessage, int timeout)
			throws TimeoutException, Exception;

	/**
	 * 异步调用接口
	 * 
	 * @param hlrOssBizMessage
	 *            业务消息
	 * @throws Exception
	 */
	public void asyncInvoke(HlrOssBizMessage hlrOssBizMessage) throws Exception;

}
