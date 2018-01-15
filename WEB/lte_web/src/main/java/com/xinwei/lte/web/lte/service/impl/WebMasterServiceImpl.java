/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-21	|  yinyuelin 	    |  create the file                       
 */

package com.xinwei.lte.web.lte.service.impl;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xinwei.hlr.HLRModule;
import com.xinwei.lte.web.lte.service.WebMasterService;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author yinyuelin
 * 
 */
@Service("webMasterService")
public class WebMasterServiceImpl implements WebMasterService{

	@Resource
	private OssAdapter ossAdapter;
	
	private int time = 2000;
	@Override
	public boolean judgeConnect(String ip, String port) throws Exception{	

		 Socket socket = null;
		 boolean result = false;
		 try{
			 socket = new Socket();
			 socket.setReceiveBufferSize(1024 * 1024);// 设置缓冲区大小
			 InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, Integer.parseInt(port));
			 
			 socket.connect(inetSocketAddress,time); // 设置应答响应时间	 
			 
			 if(isCloseConnect(socket)){
				 result = false;
			 }else{
				 result = true;
			 }
		}catch (Exception e){
			throw e;
		}finally{
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		 
		
		return result;
	}

	@Override
	public boolean isCloseConnect(Socket clientSocket)
	{
		boolean b = false;
		if (null == clientSocket) {
			return b = true;
		}
		if (clientSocket.isClosed()) {
			return b = true;
		}
		if (!clientSocket.isConnected()) {
			return b = true;
		}
		if (clientSocket.isOutputShutdown()) {
			return b = true;
		}
		if (clientSocket.isInputShutdown()) {
			return b = true;
		}
		return b;
	}

	@Override
	public void overwriteConf(String filePath, String ip, String port,
			String adapterXmlPath, String TVconfigXmlPath) throws Exception
	{
		BufferedOutputStream buff = null;
		try
		{
			FileOutputStream out = new FileOutputStream(filePath);
			buff = new BufferedOutputStream(out);			
			StringBuffer write = new StringBuffer();			
			write.append("ip="+ip);
			write.append("\r\n");
			write.append("port="+port);
			write.append("\r\n");
			write.append("adapterXmlPath="+adapterXmlPath);
			write.append("\r\n");
			write.append("TVconfigXmlPath="+TVconfigXmlPath);
			buff.write(write.toString().getBytes("utf-8"));
			buff.flush();
		}
		catch (Exception e){
			throw e;
		}finally{
			try{
				buff.close();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void reConnect() throws Exception
	{
		//重连
		HLRModule hlrModule = HLRModule.getInstance();
		hlrModule.destroy();
		ossAdapter.initialize();	
		
	}
	
}
