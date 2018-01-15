/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-7	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import com.xinwei.minas.core.model.EnbCellStart;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizSceneTable;

/**
 * 
 * enb小区配置服务
 * 
 * @author chenlong
 * 
 */

public interface EnbCellService {
	
	/**
	 * 小区开站向导,增加小区
	 * @param enb
	 * @param enbCellStart
	 * @throws Exception
	 */
	public void add(long moId, EnbCellStart enbCellStart) throws Exception;
	/**
	 * 小区开站向导,修改小区
	 * @param enb
	 * @param enbCellStart
	 * @throws Exception
	 */
	public void update(long moId, EnbCellStart enbCellStart) throws Exception;
	
	/**
	 * 根据enb查询开站场景及对应A类参数的数据
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public XBizSceneTable queryByMoId(long moId) throws Exception;
	
	/**
	 * 删除小区所有信息
	 * @param moId
	 * @param cid
	 * @throws Exception
	 */
	public void delete(long moId, int cid) throws Exception;
	
	/**
	 * 根据小区ID和网元ID查询小区信息
	 * @param moId
	 * @param cid
	 * @return
	 * @throws Exception
	 */
	public XBizRecord queryByCid(long moId, int cid) throws Exception;
	
}
