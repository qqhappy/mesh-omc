/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * XMeta助手
 * 
 * @author chenjunhua
 * 
 */

public class XMetaUtils {
	
	/**
	 * 替换资源
	 * @param key
	 * @param resourceBundle
	 * @return
	 */
	public static String replaceResource(String key, ResourceBundle resourceBundle) {
		if (key.startsWith("%")) {
			key = key.substring(1);
			key = getString(key, resourceBundle);
		}
		return key;
	}
	

	/**
	 * 获取资源
	 * @param key
	 * @param resourceBundle
	 * @return
	 */
	private static String getString(String key, ResourceBundle resourceBundle) {
		try {
			String ss = resourceBundle.getString(key);
			return ss;
			
		} catch (Exception e) {
			return "!" + key + "!";
		}
	}
	
	/**
	 * 替换mcbts-ui.xml中标签 ui->biz->item->property->value中的资源
	 * @param value
	 * @param resourceBundle
	 * @return
	 */
	public static String replacePropertyResource(String value, ResourceBundle resourceBundle){
		Pattern p = Pattern.compile("\\((.*?)\\)");
		Matcher m = p.matcher(value);
		while(m.find()){
			String innerBracket = m.group(1);
			if(innerBracket.startsWith("%")){
				innerBracket = innerBracket.substring(1);
				innerBracket = getString(innerBracket, resourceBundle);
				value = value.replace(m.group(1), innerBracket);
			}
		}
		return value;
	}
}
