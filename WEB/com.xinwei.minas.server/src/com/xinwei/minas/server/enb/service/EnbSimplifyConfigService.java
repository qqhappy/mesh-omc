/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * eNB简易配置服务接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbSimplifyConfigService {

	/**
	 * 添加单板，如果为RRU单板，需要指定连接的光口号，并添加拓扑表记录
	 * 
	 * @param moId
	 * @param boardRecord
	 * @param fiberPort
	 * @throws Exception
	 */
	public void addBoard(long moId, XBizRecord boardRecord, Integer fiberPort)
			throws Exception;

	/**
	 * 修改单板，如果为RRU单板，需要指定连接的光口号，并修改拓扑表记录
	 * 
	 * @param moId
	 * @param boardRecord
	 * @param fiberPort
	 * @throws Exception
	 */
	public void updateBoard(long moId, XBizRecord boardRecord, Integer fiberPort)
			throws Exception;

	/**
	 * 删除单板，同时删除拓扑表中相应记录
	 * 
	 * @param moId
	 * @param boardRecord
	 * @throws Exception
	 */
	public void deleteBoard(long moId, XBizRecord boardRecord) throws Exception;

	/**
	 * 更新小区参数表记录
	 * 
	 * @param moId
	 * @param cellParaRecord
	 * @throws Exception
	 */
	public void updateCellPara(long moId, XBizRecord cellParaRecord)
			throws Exception;

	/**
	 * 删除小区参数表记录，同时将该小区相关的其他参数删除
	 * 
	 * @param moId
	 * @param cellParaRecord
	 * @throws Exception
	 */
	public void deleteCellPara(long moId, XBizRecord cellParaRecord)
			throws Exception;
}
