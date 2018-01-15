/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.facade.conf;

import java.net.InetSocketAddress;
import java.rmi.Remote;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.conf.KeyDesc;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XCollection;
import com.xinwei.omp.core.model.meta.XMetaRef;

/**
 * 
 * 通用网元配置门面
 * 
 * @author chenjunhua
 * 
 */

public interface XMoBizConfigFacade extends Remote {
	
	/**
	 * 查询指定网元的表数据
	 * 
	 * @param moId
	 *            网元Id
	 * @param bizName
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public XBizTable queryFromEms(Long moId, String bizName,
			XBizRecord condition) throws Exception;
	
	/**
	 * 从网管查询指定网元的表数据
	 * 
	 * @param moId
	 *            网元Id
	 * @param tableName
	 *            表名
	 * @param condition
	 *            条件
	 * @param queryStatus
	 *            如果基站在线，是否向基站查询状态字段的值
	 * @return
	 * @throws Exception
	 */
	public XBizTable queryFromEms(Long moId, String tableName,
			XBizRecord condition, boolean queryStatus) throws Exception;
	
	/**
	 * 从网元查询表数据
	 * 
	 * @param moId
	 *            网元Id
	 * @param tableName
	 *            表名
	 * @return
	 * @throws Exception
	 */
	public XBizTable queryFromNe(Long moId, String tableName) throws Exception;
	
	/**
	 * 增加指定网元的表数据
	 * 
	 * @param moId
	 *            网元Id
	 * @param bizName
	 *            表名
	 * @param bizRecord
	 *            记录
	 * @throws Exception
	 */
	@Loggable
	public void add(Long moId, String bizName, XBizRecord bizRecord)
			throws Exception;
	
	/**
	 * 更新指定网元的表数据
	 * 
	 * @param moId
	 *            网元Id
	 * @param bizName
	 *            表名
	 * @param bizRecord
	 *            记录
	 * @throws Exception
	 */
	@Loggable
	public void update(Long moId, String bizName, XBizRecord bizRecord)
			throws Exception;
	
	/**
	 * 删除指定网元的表数据
	 * 
	 * @param moId
	 *            网元Id
	 * @param bizName
	 *            表名
	 * @param bizKey
	 *            条件
	 * @throws Exception
	 */
	@Loggable
	public void delete(Long moId, String bizName, XBizRecord bizKey)
			throws Exception;
	
	/**
	 * 按应用定义查询数据
	 * 
	 * @param moId
	 *            网元Id
	 * @param metaRefList
	 *            引用定义列表
	 * @return KeyDesc列表
	 * @throws Exception
	 */
	public List<KeyDesc> queryByMetaRef(Long moId, List<XMetaRef> metaRefList)
			throws Exception;
	
	/**
	 * 查询UI映射(Key:版本号 , Value:功能集合)
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Map<String, XCollection> queryUIMap(int moTypeId, int subTypeId)
			throws Exception;
	
	/**
	 * 根据网元查询网管的网络地址
	 * 
	 * @return 网管与指定类型的网元通信的网络地址(IP地址和端口)
	 * @throws Exception
	 */
	public InetSocketAddress queryEmsNetAddress(Long moId) throws Exception;
	
	/**
	 * 根据网元类型获取字段等级配置
	 * 
	 * @param moType
	 *            网元类型
	 * @return key:version, value:[key:tableName, value:[key:level,
	 *         value=fieldList]]
	 * @throws Exception
	 */
	public Map<String, Map<String, Map<String, List<String>>>> getTableFieldLevelConfig(
			int moType) throws Exception;
	
	/**
	 * 根据网元类型获取业务配置
	 * 
	 * @param moTypeId
	 * @return key:version, value=Map[key:bizName, value=fieldNameList]
	 * @throws Exception
	 */
	public Map<String, Map<String, List<String>>> queryBizConfig(int moTypeId)
			throws Exception;
	
	/**
	 * 查询指定网元类型支持的协议版本号
	 * 
	 * @param moTypeId
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, List<String>> querySupportedProtocolVersion(int moTypeId)
			throws Exception;
	
	/**
	 * 查询指定网管列表的所有业务数据
	 * 
	 * @param moTypeId
	 * @param moIdList
	 * @return
	 * @throws Exception
	 */
	public Map<Long, List<XBizTable>> queryDataByMoIdList(int moTypeId,
			List<Long> moIdList) throws Exception;
	
	/**
	 * 比较并同步网管数据到网元
	 * 
	 * @param moId
	 * @throws Exception
	 */
	@Loggable
	public void compareAndSyncEmsDataToNe(Long moId) throws Exception;
	
	/**
	 * 比较并同步网元数据到网管
	 * 
	 * @param moId
	 * @throws Exception
	 */
	@Loggable
	public void compareAndSyncNeDataToEms(Long moId) throws Exception;
	
	/**
	 * 获取一个空闲的小区物理ID
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getFreePci(int enbType, String protocolVersion) throws Exception;
	
	/**
	 * 获取一个空闲的逻辑根序列
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getFreeRsi(int enbType, String protocolVersion) throws Exception;
}
