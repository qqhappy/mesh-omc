/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-12	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.model.FullTableConfigInfo;

/**
 * 
 * 整表配置业务服务接口
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author zhuxiaozhan
 * 
 */

public interface EnbFullTableConfigService {
	/**
	 * 进行整表配置
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void config(Long moId) throws Exception;

	/**
	 * 删除指定的整表配置信息
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void delete(FullTableConfigInfo data) throws Exception;

	/**
	 * 查询指定eNB的整表配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public FullTableConfigInfo queryByMoId(Long moId) throws Exception;

	/**
	 * 查询所有的整表配置信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<FullTableConfigInfo> queryAll() throws Exception;

	/**
	 * 根据配置状态查询整表配置信息
	 * 
	 * @param status
	 *            ：成功、失败、配置中
	 * @return
	 * @throws Exception
	 */
	public List<FullTableConfigInfo> queryByStatus(int status) throws Exception;

	/**
	 * 为指定网元按目标版本格式生成整表配置文件,并放到FTP上,返回生成的文件名称
	 * 
	 * @param moId
	 * @param targetVersion
	 * @return
	 * @throws Exception
	 */
	public String generateFullTableSqlFile(Long moId, String targetVersion)
			throws Exception;

	/**
	 * 为指定网元按照目标版本格式生成所有表的整表配置的sql脚本列表
	 * 
	 * @param moId
	 * @param targetVersion
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<String>> generateFullTableSql(Long moId,
			String targetVersion) throws Exception;

	/**
	 * 为指定网元按照目标版本格式生成指定表的整表配置的sql脚本列表
	 * 
	 * @param moId
	 * @param targetVersion
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<String> generateFullTableSql(Long moId, String targetVersion,
			String tableName) throws Exception;

	/**
	 * 保存更新整表配置业务信息
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void saveUpdate(FullTableConfigInfo data) throws Exception;

}
