/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.xstat.service;

import java.util.List;

/**
 * 
 * ͳ������ʵ�崦��ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface StatEntityProcessor<T> {

	/**
	 * ����ͳ������ʵ���б�
	 * 
	 * @param entities
	 * @throws Exception
	 */
	public void process(List<T> entities) throws Exception;

}
