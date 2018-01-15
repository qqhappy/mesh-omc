/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-10	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * 常量导入类
 * 
 * @author fanhaoyu
 * 
 */

public class StatConstantImporter {

	/**
	 * 返回 常量：描述
	 * 
	 * @param pathname
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> importConstants(String pathname)
			throws Exception {
		return importConstants(new FileInputStream(pathname));
	}

	/**
	 * 返回 常量：描述
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> importConstants(InputStream inputStream)
			throws IOException {

		Properties properties = new Properties();
		properties.load(inputStream);
		Enumeration<String> keys = (Enumeration<String>) properties
				.propertyNames();
		Map<String, String> map = new HashMap<String, String>();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			map.put(properties.getProperty(key), key);
		}
		return map;
	}

	public static void main(String[] args) {
		String pathname = "D:\\_WorkBox\\eNB_Stat\\统计项常量.properties";

		try {
			Map<String, String> map = importConstants(pathname);
			System.out.println(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
