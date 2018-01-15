/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao;

import java.util.List;

import com.xinwei.minas.core.model.conf.KeyDesc;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizSceneTable;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XMetaRef;

/**
 * 
 * 通用网元配置DAO
 * 
 * @author chenjunhua
 * 
 */

public interface EnbBizConfigDAO {
	
	/**
	 * 查询指定网元的表数据
	 * 
	 * @param moId
	 *            网元ID
	 * @param tableName
	 *            表名
	 * @param condition
	 *            条件
	 * @return
	 * @throws Exception
	 */
	public XBizTable query(Long moId, String tableName, XBizRecord condition)
			throws Exception;
	
	/**
	 * 按主键查询指定网元的数据
	 * 
	 * @param moId
	 *            网元ID
	 * @param tableName
	 *            表名
	 * @param bizKey
	 *            表主键
	 * @return
	 * @throws Exception
	 */
	public XBizRecord queryByKey(Long moId, String tableName, XBizRecord bizKey)
			throws Exception;
	
	/**
	 * 增加指定网元的表数据
	 * 
	 * @param moId
	 *            网元ID
	 * @param tableName
	 *            表名
	 * @param bizRecord
	 *            记录
	 * @throws Exception
	 */
	public void add(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception;
	
	/**
	 * 增加指定网元的表数据
	 * 
	 * @param moId
	 *            网元ID
	 * @param tableName
	 *            表名
	 * @param bizKey
	 *            主键
	 * @param bizRecord
	 *            记录
	 * @throws Exception
	 */
	public void add(Long moId, String tableName, XBizRecord bizKey,
			XBizRecord bizRecord) throws Exception;
	
	/**
	 * 批量增加指定网元数据
	 * 
	 * @param moId
	 */
	public void batchAdd(Long moId, List<XBizTable> tableList) throws Exception;
	
	/**
	 * 更新指定网元的表数据
	 * 
	 * @param moId
	 *            网元ID
	 * @param tableName
	 *            表名
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
	 *            表名
	 * @param bizKey
	 *            条件，如果条件为null，则删除表中所有记录
	 * @throws Exception
	 */
	public void delete(Long moId, String tableName, XBizRecord bizKey)
			throws Exception;
	
	/**
	 * 删除指定网元的所有业务数据
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void deleteAll(Long moId) throws Exception;
	
	/**
	 * 删除指定网元的指定业务数据
	 * 
	 * @param moId
	 * @param tableName
	 * @throws Exception
	 */
	public void deleteAll(Long moId, String tableName) throws Exception;
	
	/**
	 * 按应用定义查询数据
	 * 
	 * @param moId
	 *            网元ID
	 * @param metaRefList
	 *            引用定义列表
	 * @return KeyDesc列表
	 * @throws Exception
	 */
	public List<KeyDesc> queryByMetaRef(Long moId, List<XMetaRef> metaRefList)
			throws Exception;
	
	/**
	 * 新增小区开站场景数据
	 * 
	 * @param moId
	 * @param cid
	 * @param bizRecord
	 * @throws Exception
	 */
	public void addScene(long moId, int cid, XBizRecord bizRecord)
			throws Exception;
	
	/**
	 * 查询enb下所有小区场景数据
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public XBizSceneTable querySceneByMoId(long moId) throws Exception;
	
	/**
	 * 根据moId和cid查询小区场景数据
	 * 
	 * @param moId
	 * @param cid
	 * @return
	 * @throws Exception
	 */
	public XBizRecord querySceneByMoIdAndCid(long moId, int cid)
			throws Exception;
	
	/**
	 * 删除小区场景数据
	 * 
	 * @param moId
	 * @param cid
	 * @throws Exception
	 */
	public void deleteScene(long moId, int cid) throws Exception;
	
	/**
	 * 更新小区场景数据
	 * 
	 * @param moId
	 * @param cid
	 * @throws Exception
	 */
	public void updateScene(long moId, int cid, XBizRecord bizRecord)
			throws Exception;
	
}
