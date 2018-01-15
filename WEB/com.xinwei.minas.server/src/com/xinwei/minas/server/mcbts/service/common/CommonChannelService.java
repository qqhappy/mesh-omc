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
 * ������Ϣ������ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface CommonChannelService {
	/**
	 * ��������ͬ����Ϣ
	 * 
	 * @param synInfo
	 */
	public void saveCommonChannelSynInfos(List<CommonChannelSynInfo> synInfos);
}
