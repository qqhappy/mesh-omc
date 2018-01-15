/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.net;

import java.util.List;

import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;

/**
 * 
 * 实时性能数据发送器接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbRealtimeDataSender {

	/**
	 * 发送指定网元的性能数据列表
	 * 
	 * @param moId
	 * @param itemList
	 * @throws Exception
	 */
	public void sendData(long moId, List<EnbRealtimeItemData> itemDataList)
			throws Exception;

}
