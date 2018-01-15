/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-9-8	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.lte.web.enb.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.xinwei.lte.web.enb.cache.HttpSessionRegistry;

/**
 * 
 * HttpSessionListener实现类，用于侦听HttpSession的创建和销毁事件
 * 
 * @author chenjunhua
 * 
 */
public class MinasHttpSessionListener implements HttpSessionListener {


	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		HttpSessionRegistry.getInstance().add(session);

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		HttpSessionRegistry.getInstance().remove(session.getId());

	}


}
