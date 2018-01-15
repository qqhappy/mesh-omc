/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-29	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.LocationArea;

/**
 * 
 * 位置区服务接口
 * 
 * 
 * @author chenshaohua
 * 
 */

public interface LocAreaService {

	/**
	 * 查询全部记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<LocationArea> queryAll();

	/**
	 * 新增或更新位置区信息
	 * 
	 * @param la
	 */
	public void saveOrUpdate(LocationArea la);

	/**
	 * 删除一个位置区信息
	 * 
	 * @param la
	 */
	public void delete(LocationArea la);

	/**
	 * 保存配置
	 * 
	 * @param locationAreaList
	 * @throws Exception
	 */
	public void config(List<LocationArea> locationAreaList) throws Exception;

}
