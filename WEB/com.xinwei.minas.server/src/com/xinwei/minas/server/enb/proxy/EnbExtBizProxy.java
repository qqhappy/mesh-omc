/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.proxy;

/**
 * 
 * eNB扩展业务代理接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbExtBizProxy {

	/**
	 * 重启指定eNB
	 * 
	 * @param enbId
	 * @throws Exception
	 */
	public void reset(long enbId) throws Exception;

	/**
	 * 重启指定eNB的指定单板
	 * 
	 * @param enbId
	 * @param rackId
	 * @param shelfId
	 * @param boardId
	 * @throws Exception
	 */
	public void reset(long enbId, int rackId, int shelfId, int boardId)
			throws Exception;

	/**
	 * 学习eNB数据配置，包括支持哪些表和表中的哪些字段
	 * 
	 * @param enbId
	 * @return
	 * @throws Exception
	 */
	public String studyEnbDataConfig(long enbId) throws Exception;

}
