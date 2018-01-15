/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-29	| jiayi 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import com.xinwei.minas.mcbts.core.model.sysManage.ChannelComparableMode;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * 基站兼容模式配置服务接口
 * 
 * @author jiayi
 * 
 */

public interface ChannelComparableModeService extends ICustomService{

	/**
	 * 配置指定基站的基站兼容模式
	 * 
	 * @param moId 基站的MO Id
	 * 
	 * @param config 基站兼容模式信息
	 * 
	 * @throws Exception
	 */
	public void config(long moId, ChannelComparableMode config)
			throws Exception;

	/**
	 * 配置系统的基站兼容模式
	 * 
	 * @param config 基站兼容模式信息
	 * 
	 * @throws Exception
	 */
	public void config(ChannelComparableMode config) throws Exception;

	/**
	 * 从网管数据库查询兼容模式配置信息
	 * 
	 * @return ChannelComparableMode 基站兼容模式信息
	 * 
	 * @throws Exception
	 */
	public ChannelComparableMode queryFromEMS() throws Exception;

	/**
	 * 从基站查询兼容模式配置信息
	 * 
	 * @param moId 基站的MO Id
	 * 
	 * @return ChannelComparableMode 基站兼容模式信息
	 * 
	 * @throws Exception
	 */
	public ChannelComparableMode queryFromNE(long moId) throws Exception;

}
