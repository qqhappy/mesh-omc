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
 * Application������
 * 
 * @author chenjunhua
 * 
 */

public class AppContext {


	/**
	 * ��ȡ���ʻ���Դ��Ϣ
	 * 
	 * @param key
	 * @return
	 */
	public static String getMessage(String key) {
		return getMessage(key, new Object[] {});
	}

	/**
	 * ��ȡ���ʻ���Դ��Ϣ
	 * 
	 * @param key
	 * @param tokenValue
	 * @return
	 */
	public static String getMessage(String key, Object[] tokenValue) {
		return OmpAppContext.getMessage(key, tokenValue);
	}

	/**
	 * ��ȡSpringApp Context
	 * @return
	 */
	public static ApplicationContext getCtx() {
		return OmpAppContext.getCtx();
	}
	
	
}
