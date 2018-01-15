/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-21	|  yinyuelin 	    |  create the file                       
 */

package com.xinwei.lte.web.lte.service;

import java.net.Socket;

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

public interface WebMasterService{
	
	/**
	 * 判断地址能否连接
	 * @param ip
	 * @param port
	 * @return
	 * @throws Exception
	 */
	public boolean judgeConnect(String ip,String port) throws Exception;
	
	/**
	 * 判断socket是否断连
	 * @param clientSocket
	 * @return
	 */
	public boolean isCloseConnect(Socket clientSocket);
	
	/**
	 * 重写oss-config.properties数据
	 * @param filePath
	 * @param ip
	 * @param port
	 * @param adapterXmlPath
	 * @param TVconfigXmlPath
	 * @throws Exception
	 */
	public void overwriteConf(String filePath,String ip,String port,String adapterXmlPath,String TVconfigXmlPath) throws Exception;
	
	/**
	 * 重连
	 * @throws Exception
	 */
	public void reConnect() throws Exception;
}
