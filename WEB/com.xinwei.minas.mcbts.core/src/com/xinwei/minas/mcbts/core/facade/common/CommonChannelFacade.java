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
 * ������Ϣ����ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface CommonChannelFacade extends Remote{
	/**
	 * ��������ͬ����Ϣ
	 * 
	 * @param synInfo
	 */
	public void saveCommonChannelSynInfo(List<CommonChannelSynInfo> synInfos) throws Exception;
}
