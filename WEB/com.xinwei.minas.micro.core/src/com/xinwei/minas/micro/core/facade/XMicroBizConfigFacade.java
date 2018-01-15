/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.micro.core.facade;

import java.rmi.Remote;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.EnbCellStart;
import com.xinwei.minas.core.model.EnbSceneDataShow;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizSceneTable;

/**
 * 
 * 通用网元配置门面
 * 
 * @author chenjunhua
 * 
 */

public interface XMicroBizConfigFacade extends Remote {
	
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
	 * 增加小区
	 * @param moId
	 * @param enbCellStart
	 * @throws Exception
	 */
	public void addSence(long moId, EnbCellStart enbCellStart) throws Exception;
	/**
	 * 修改小区
	 * @param moId
	 * @param enbCellStart
	 * @throws Exception
	 */
	public void updateSence(long moId, EnbCellStart enbCellStart) throws Exception;
	
	/**
	 * 查询小区开站信息
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public XBizSceneTable querySceneData(Long moId) throws Exception;
	
	/**
	 * 根据小区ID查询小区信息
	 * @param moId
	 * @param cid
	 * @return
	 * @throws Exception
	 */
	public XBizRecord querySceneDataByCid(Long moId,int cid) throws Exception;
	
	/**
	 * 删除小区信息
	 * @param moId
	 * @param cid
	 * @throws Exception
	 */
	public void deleteScene(Long moId,int cid) throws Exception;
	
	/**
	 * 查询场景显示数据
	 * @return
	 * @throws Exception
	 */
	public EnbSceneDataShow querySceneDataShow() throws Exception;
	
	
}
