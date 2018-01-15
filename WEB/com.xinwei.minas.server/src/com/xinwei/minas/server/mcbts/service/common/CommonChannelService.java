/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.CommonChannelSynInfo;

/**
 * 
 * 网归信息服务类接口
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface CommonChannelService {
	/**
	 * 保存网归同步信息
	 * 
	 * @param synInfo
	 */
	public void saveCommonChannelSynInfos(List<CommonChannelSynInfo> synInfos);
}
