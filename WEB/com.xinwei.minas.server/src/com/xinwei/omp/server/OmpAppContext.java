/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-1-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.server;

import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.env.Environment;

import com.xinwei.system.util.PropertiesUtils;

/**
 * 
 * Application上下文
 * 
 * @author chenjunhua
 * 
 */

public class OmpAppContext implements ApplicationContextAware {

	private static ApplicationContext ctx;

	private static Environment env;

	private static Locale locale = Locale.getDefault();

	@Override
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		this.ctx = ctx;		
		locale = (Locale) ctx.getBean("platform.locale");
		// 设置RMI服务对外提供的IP地址
		String serverIp = OmpAppContext.getPropertyByName("platform.server.ip");
		if (!StringUtils.isEmpty(serverIp)) {
			System.setProperty("java.rmi.server.hostname", serverIp);
		}
	}
	
	
	@Autowired
	private void setEnv(Environment env) {
		OmpAppContext.env = env;
	}

	/**
	 * 根据属性名获取properties文件中对应的属性值
	 * 
	 * @param name
	 * @return
	 */
	public static String getPropertyByName(String name) {		
		if (env.getProperty(name) != null) {
			return env.getProperty(name);
		} else {
			Properties p = (Properties)ctx.getBean("settings");
			return p.getProperty(name);
		}
	}

	public static String getPropertyByName(String name, String beanName) {
		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(beanName)) {
			return null;
		}
		Properties p = (Properties) ctx.getBean(beanName);
		return p.getProperty(name);
	}

	public static String Parse(String filePath, String key) {
		InputStream is = null;
		try {
			is = PropertiesUtils.class.getClassLoader().getResourceAsStream(
					filePath);
			Properties properties = new Properties();
			properties.load(is);
			return properties.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

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
		try {
			return ctx.getMessage(key, tokenValue, locale);
		} catch (NoSuchMessageException e) {
			return "${" + key + "}";
		}
	}

	public static ApplicationContext getCtx() {
		return ctx;
	}


	public static Locale getLocale() {
		return locale;
	}
	
	
}
