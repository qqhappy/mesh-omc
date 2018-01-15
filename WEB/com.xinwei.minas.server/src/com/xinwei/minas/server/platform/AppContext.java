/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-3-14	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.platform;

import org.springframework.context.ApplicationContext;

import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * Application上下文
 * 
 * @author chenjunhua
 * 
 */

public class AppContext {


	/**
	 * 获取国际化资源信息
	 * 
	 * @param key
	 * @return
	 */
	public static String getMessage(String key) {
		return getMessage(key, new Object[] {});
	}

	/**
	 * 获取国际化资源信息
	 * 
	 * @param key
	 * @param tokenValue
	 * @return
	 */
	public static String getMessage(String key, Object[] tokenValue) {
		return OmpAppContext.getMessage(key, tokenValue);
	}

	/**
	 * 获取SpringApp Context
	 * @return
	 */
	public static ApplicationContext getCtx() {
		return OmpAppContext.getCtx();
	}
	
	
}
