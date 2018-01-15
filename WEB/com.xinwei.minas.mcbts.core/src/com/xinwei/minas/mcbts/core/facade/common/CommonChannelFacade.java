/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.common;

import java.rmi.Remote;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.CommonChannelSynInfo;

/**
 * 
 * 网归信息门面接口
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface CommonChannelFacade extends Remote{
	/**
	 * 保存网归同步信息
	 * 
	 * @param synInfo
	 */
	public void saveCommonChannelSynInfo(List<CommonChannelSynInfo> synInfos) throws Exception;
}
