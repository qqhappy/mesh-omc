/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.proxy;

import java.util.List;

import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XList;

/**
 * 
 * 通用网元配置Proxy
 * 
 * @author chenjunhua
 * 
 */

public interface EnbBizConfigProxy {

	/**
	 * 根据表名和记录生成插入sql语句
	 * 
	 * @param version
	 * @param tableName
	 * @param bizRecord
	 * @return
	 */
	public String generateInsertSql(int enbTypeId, String version, String tableName,
			XBizRecord bizRecord);

	/**
	 * 查询指定网元的表数据
	 * 
	 * @param moId
	 * @param tableName
	 * @param fieldMetas
	 * @return
	 * @throws Exception
	 */
	public XBizTable query(Long moId, String tableName, List<XList> fieldMetas)
			throws Exception;

	/**
	 * 增加指定网元的表数据
	 * 
	 * @param moId
	 *            网元ID
	 * @param tableName
	 *            业务名
	 * @param bizRecord
	 *            记录
	 * @throws Exception
	 */
	public void add(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception;

	/**
	 * 修改指定网元的表数据
	 * 
	 * @param moId
	 *            网元ID
	 * @param tableName
	 *            业务名
	 * @param bizRecord
	 *            记录
	 * @throws Exception
	 */
	public void update(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception;

	/**
	 * 删除指定网元的表数据
	 * 
	 * @param moId
	 *            网元ID
	 * @param tableName
	 *            业务名
	 * @param bizKey
	 *            条件
	 * @throws Exception
	 */
	public void delete(Long moId, String tableName, XBizRecord bizKey)
			throws Exception;

	/**
	 * 整表业务配置. flag为true代表整表配置，为false代表整表反构
	 * 
	 * @param enId
	 * @param data
	 * @param bizFlag
	 * @throws Exception
	 */
	public void fullTableConfig(Long enbId, GenericBizData data, int bizFlag)
			throws Exception;
}
