/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.util.Map;

import com.xinwei.omp.core.model.meta.XList;

/**
 * 
 * eNB扩展业务服务接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbExtBizService {

	/**
	 * 重启指定eNB
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void reset(long moId) throws Exception;

	/**
	 * 重启指定eNB的指定单板
	 * 
	 * @param moId
	 * @param rackId
	 * @param shelfId
	 * @param boardId
	 * @throws Exception
	 */
	public void reset(long moId, int rackId, int shelfId, int boardId)
			throws Exception;

	/**
	 * 导出开站参数
	 * 
	 * @param moId
	 * @return 返回开站参数内容，由前台生成文件
	 * @throws Exception
	 */
	public String exportActiveData(long moId) throws Exception;

	/**
	 * 学习eNB数据配置，包括支持哪些表和表中的哪些字段
	 * 
	 * @param moId
	 * @param reStudy
	 *            如果数据配置已存在，是否重新学习
	 * @throws Exception
	 */
	public void studyEnbDataConfig(long moId, boolean reStudy) throws Exception;

	/**
	 * 查询自学习数据配置
	 * 
	 * @return key=version,value=dataConfig
	 * @throws Exception
	 */
	public Map<String, Map<String, XList>> queryStudyDataConfig()
			throws Exception;

	/**
	 * 将指定基站除开站参数以外的数据恢复到模板数据
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void recoverDefaultData(long moId) throws Exception;
}
