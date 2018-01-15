/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.powerSupply;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;
import EDU.oswego.cs.dl.util.concurrent.TimeoutException;

/**
 * 
 * 电源连接器
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class PowerSupplyTcpConnector extends IoHandlerAdapter{
	Log log = LogFactory.getLog(PowerSupplyTcpConnector.class);
	
	//socket connector
	private NioSocketConnector connector;
	
	// 超时时长（单位：毫秒）
	private static final long TIME_OUT = 2500;
	
	//The client session cache
	private static final ConcurrentHashMap<String, IoSession> sessionCache = new ConcurrentHashMap<String, IoSession>(); 
	
	// The FutureResult cache which builded for each session;
	private static final ConcurrentHashMap<String, FutureResult> futureCache = new ConcurrentHashMap<String, FutureResult>(); 
	
	public static final String DEST_IP = "destIp";
	
	public static final String DEST_PORT = "destPort";
	
	private static final PowerSupplyTcpConnector instance = new PowerSupplyTcpConnector();
	
	private PowerSupplyTcpConnector() {
		connector = new NioSocketConnector();
		connector.setHandler(this);
	}

	/**
	 * Get the instance of the PowerSupplyTcpConnector
	 * @return
	 */
	public static  PowerSupplyTcpConnector getInstance() {
		return instance;
	}
	

	/**
	 * 发送消息
	 * 
	 * @param message
	 * @return 返回应答消息
	 */
	public byte[] sendMessage(byte[] message, String ip, int port) {
		String key = createSessionCacheKey(ip, port);
		IoSession session = sessionCache.get(key);
		if (session == null || !session.isConnected()) {
			ConnectFuture connFuture = connector.connect(new InetSocketAddress(
					ip, port));
			connFuture.awaitUninterruptibly();
			session = connFuture.getSession();
			session.setAttribute(DEST_IP, ip);
			session.setAttribute(DEST_PORT, port);
			sessionCache.put(key, session);
		}
		
		FutureResult response = futureCache.get(key);
		if (response == null) {
			response = new FutureResult();
			futureCache.put(key, response);
		}
		
		IoBuffer buffer = IoBuffer.allocate(message.length);
		buffer.put(message);
		buffer.flip();
		session.write(buffer);
		session.getConfig().setUseReadOperation(true);
		try {
			return (byte[]) response.timedGet(TIME_OUT);
		} catch (TimeoutException e) {
			log.error(e.getMessage());
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		} catch (InvocationTargetException e) {
			log.error(e.getMessage());
		}
		
		return null;
	}
	
	
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		ByteBuffer rBuffer = ((IoBuffer)message).buf();
		int size = rBuffer.limit();
    	byte[] data = rBuffer.array();
    	byte[] rp = new byte[size];
    	for (int i = 0; i < size; i ++) {
    		rp[i] = data[i];
    	}
    	String ip = (String) session.getAttribute(DEST_IP);
    	int port = (Integer) session.getAttribute(DEST_PORT);
    	String key = createSessionCacheKey(ip, port);
    	FutureResult response = futureCache.get(key);
    	if (response != null) {
    		response.set(rp);
    	}
	}
	 
	/**
	 * close the communication session
	 * @param ip
	 * @param port
	 */
	public void close(String ip, int port) {
//		if (connector != null && connector.isActive()) {
//			connector.dispose(true);
//		}
		String key = createSessionCacheKey(ip, port);
		IoSession session = sessionCache.get(key);
		if (session != null && session.isConnected()) {
			session.close(true);
		}
		sessionCache.remove(key);
		futureCache.remove(key);
	}
	
	
	/**
	 * create the key for session cache
	 * @param ip
	 * @param port
	 * @return
	 */
	public String createSessionCacheKey(String ip, int port) {
		return ip + ":" + port;
	}
	
	
	/**
	 * Get the FutureResult for recording the response
	 * @param key
	 * @return
	 */
	public FutureResult getResponseFuture(String key) {
		return futureCache.get(key);
	}
	
	
	public static void main(String args[])throws Exception {
		byte[] data = { 0x7E, 0x34, 0x35, 0x34, 0x30, 0x30};
		byte[] rp = instance.sendMessage(data, "172.16.24.187", 18567);
		if (rp != null) {
			for (int i = 0; i < rp.length; i++) {
	    		System.out.print(rp[i]);
	    		System.out.print(" ");
	    	}
			System.out.print("\n");
		} else {
			System.out.println("zxz响应超时");
		}
		instance.close("172.16.24.187", 18567);
	}
	
}
