/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-9	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.Business;

/**
 * 
 * 普通service的上层抽象接口
 * 
 * @author chenshaohua
 * 
 */

public interface ICustomService {

	/**
	 * 从EMS同步数据到基站
	 * 
	 * @param moId
	 *            网元Id
	 * @throws Exception
	 */
	public void syncFromEMSToNE(Long moId) throws Exception;

	/**
	 * 从基站同步数据到EMS
	 * 
	 * @param moId
	 *            网元Id
	 * @throws Exception
	 */
	public void syncFromNEToEMS(Long moId) throws Exception;

	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception;

	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception;
}
