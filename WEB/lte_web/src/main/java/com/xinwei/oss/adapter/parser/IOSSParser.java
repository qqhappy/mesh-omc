/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-23	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.parser;

import com.xinwei.oss.adapter.model.meta.OssBizItem;

/**
 * 
 * 对象解析器接口，负责把前端对象与后台对象的转化
 * 
 * @author chenshaohua
 * 
 */

public interface IOSSParser {

	/**
	 * 把前端对象转化为后台对象
	 * 
	 * @param object
	 *            前端封装的对象
	 * @return 后台对象
	 */
	public Object parse(Object object, OssBizItem item);

	/**
	 * 把后台对象转化为前端对象
	 * 
	 * @param object
	 *            后台对象
	 * @return 前端对象
	 */
	public Object unParse(Object object, OssBizItem item);
}
