/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-17	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.Simulcast;

/**
 * 
 * 同播资源管理服务接口
 * 
 * @author tiance
 * 
 */

public interface SimulcastManageService {
	/**
	 * 获取同播资源列表
	 * 
	 * @return
	 */
	public List<Simulcast> queryAll();

	/**
	 * 根据查询地域ID下的所有同播资源
	 * 
	 * @param districtId
	 * @return
	 */
	public List<Simulcast> queryByDistrictId(long districtId);

	/**
	 * 新增或修改同播资源
	 * 
	 * @param simulcast
	 */
	public void saveOrUpdate(Simulcast simulcast);

	/**
	 * 设置同播资源的基站链路信息为未同步
	 */
	public void setSimulMcBtsLinkUnSync();

	/**
	 * 删除同播资源
	 * 
	 * @param simulcast
	 */
	public void delete(Simulcast simulcast);

	/**
	 * 查询同步的状态
	 * 
	 * @return
	 */
	public boolean[] querySyncStatus();

	/**
	 * 对同播资源进行同步
	 * <p>
	 * toSync[0]为基站链路信息,toSync[1]为同播资源信息
	 * </p>
	 * <p>
	 * 目前只支持同播资源信息,之后再实现基站链路信息的同步
	 * </p>
	 * 
	 * 
	 * @param toSync
	 *            : 为true时同步,false不同步
	 * @throws Exception
	 *             : 返回未成功同步的SAG ID列表
	 */
	public void sync(boolean[] toSync) throws Exception;
}
