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
 * ��ͨservice���ϲ����ӿ�
 * 
 * @author chenshaohua
 * 
 */

public interface ICustomService {

	/**
	 * ��EMSͬ�����ݵ���վ
	 * 
	 * @param moId
	 *            ��ԪId
	 * @throws Exception
	 */
	public void syncFromEMSToNE(Long moId) throws Exception;

	/**
	 * �ӻ�վͬ�����ݵ�EMS
	 * 
	 * @param moId
	 *            ��ԪId
	 * @throws Exception
	 */
	public void syncFromNEToEMS(Long moId) throws Exception;

	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception;

	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception;
}
