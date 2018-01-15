/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-14	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.District;

/**
 * 
 * 地域管理服务接口
 * 
 * 
 * @author tiance
 * 
 */

public interface DistrictManageService {
	/**
	 * 查询所有的地域信息
	 * 
	 * @return
	 */
	public List<District> queryAll();

	/**
	 * 插入或者更新一个地域信息
	 * 
	 * @param district
	 */
	public void saveOrUpdate(District district);

	/**
	 * 删除一个地域信息
	 * 
	 * @param district
	 */
	public void delete(District district);
}
