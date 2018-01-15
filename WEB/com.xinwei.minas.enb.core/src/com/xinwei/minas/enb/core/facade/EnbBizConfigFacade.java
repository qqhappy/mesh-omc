/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;

import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;

/**
 * 
 * eNB通用网元配置门面
 * 
 * @author chenjunhua
 * 
 */

public interface EnbBizConfigFacade extends Remote {

	/**
	 * 查询指定网元的表数据
	 * 
	 * @param moId
	 *            网元Id
	 * @param tableName
	 *            表名
	 * @param conditon
	 *            查询条件
	 * @return
	 * @throws Exception
	 */
	public XBizTable queryFromEms(Long moId, String tableName,
			XBizRecord conditon) throws Exception;

	/**
	 * 增加或更新指定网元的表数据
	 * 
	 * @param moId
	 *            网元Id
	 * @param tableName
	 *            表名
	 * @param bizRecord
	 *            记录
	 * @throws Exception
	 */
	public void addOrUpdate(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception;

	/**
	 * 删除指定网元的表数据
	 * 
	 * @param moId
	 *            网元Id
	 * @param tableName
	 *            表名
	 * @param conditon
	 *            条件
	 * @throws Exception
	 */
	public void delete(Long moId, String tableName, XBizRecord conditon)
			throws Exception;

}
