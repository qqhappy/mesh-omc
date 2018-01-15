/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * eNB简易配置接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbSimplifyConfigFacade extends Remote {

	/**
	 * 添加单板，如果为RRU单板，需要指定连接的光口号，并添加拓扑表记录
	 * 
	 * @param operObject
	 * @param moId
	 * @param boardRecord
	 * @param fiberPort
	 * @throws Exception
	 */
	@Loggable
	public void addBoard(OperObject operObject, long moId,
			XBizRecord boardRecord, Integer fiberPort) throws Exception;

	/**
	 * 修改单板，如果为RRU单板，需要指定连接的光口号，并修改拓扑表记录
	 * 
	 * @param operObject
	 * @param moId
	 * @param boardRecord
	 * @param fiberPort
	 * @throws Exception
	 */
	@Loggable
	public void updateBoard(OperObject operObject, long moId,
			XBizRecord boardRecord, Integer fiberPort) throws Exception;

	/**
	 * 删除单板，同时删除拓扑表中相应记录
	 * 
	 * @param operObject
	 * @param moId
	 * @param boardRecord
	 * @throws Exception
	 */
	@Loggable
	public void deleteBoard(OperObject operObject, long moId,
			XBizRecord boardRecord) throws Exception;

	/**
	 * 更新小区参数表记录
	 * 
	 * @param operObject
	 * @param moId
	 * @param cellParaRecord
	 * @throws Exception
	 */
	@Loggable
	public void updateCellPara(OperObject operObject, long moId,
			XBizRecord cellParaRecord) throws Exception;

	/**
	 * 删除小区参数表记录，同时将该小区相关的其他参数删除
	 * 
	 * @param operObject
	 * @param moId
	 * @param cellParaRecord
	 * @throws Exception
	 */
	public void deleteCellPara(OperObject operObject, long moId,
			XBizRecord cellParaRecord) throws Exception;

}
