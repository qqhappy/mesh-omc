/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.service;

import java.io.File;
import java.util.List;

/**
 * 
 * 统计数据文件解析接口
 * 
 * @author fanhaoyu
 * 
 */

public interface StatFileParser {

	/**
	 * 从性能统计文件中解析出统计实体列表
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List parse(File file) throws Exception;

	/**
	 * 从字节流中解析出统计实体列表
	 * 
	 * @param fileContent
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List parse(byte[] fileContent) throws Exception;

	/**
	 * 获取数据文件上报时间，返回millionSecond
	 * 
	 * @param fileName
	 * @return
	 */
	public long getDataTime(String fileName);
}
