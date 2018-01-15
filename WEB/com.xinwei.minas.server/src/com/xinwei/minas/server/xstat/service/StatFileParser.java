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
 * ͳ�������ļ������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface StatFileParser {

	/**
	 * ������ͳ���ļ��н�����ͳ��ʵ���б�
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List parse(File file) throws Exception;

	/**
	 * ���ֽ����н�����ͳ��ʵ���б�
	 * 
	 * @param fileContent
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List parse(byte[] fileContent) throws Exception;

	/**
	 * ��ȡ�����ļ��ϱ�ʱ�䣬����millionSecond
	 * 
	 * @param fileName
	 * @return
	 */
	public long getDataTime(String fileName);
}
