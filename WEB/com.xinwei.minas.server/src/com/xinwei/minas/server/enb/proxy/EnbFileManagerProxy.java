/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-22	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.proxy;

import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述:Enb文件管理
 * </p>
 * 
 * @author qiwei
 * 
 */

public interface EnbFileManagerProxy {
	/**
	 * Enb基站版本文件下载
	 * @param btsId
	 * @param genericBizData
	 * @throws Exception
	 */
	public void enbVersionDownloadConfig(Long btsId, GenericBizData genericBizData)
			throws Exception;
	/**
	 * Enb基站版本升级
	 * @param enbData 
	 * 
	 */
	public void upgrade(Long enbId, GenericBizData enbData) throws Exception;
}
