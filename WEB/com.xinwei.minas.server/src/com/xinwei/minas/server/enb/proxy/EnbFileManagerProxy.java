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
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����:Enb�ļ�����
 * </p>
 * 
 * @author qiwei
 * 
 */

public interface EnbFileManagerProxy {
	/**
	 * Enb��վ�汾�ļ�����
	 * @param btsId
	 * @param genericBizData
	 * @throws Exception
	 */
	public void enbVersionDownloadConfig(Long btsId, GenericBizData genericBizData)
			throws Exception;
	/**
	 * Enb��վ�汾����
	 * @param enbData 
	 * 
	 */
	public void upgrade(Long enbId, GenericBizData enbData) throws Exception;
}
