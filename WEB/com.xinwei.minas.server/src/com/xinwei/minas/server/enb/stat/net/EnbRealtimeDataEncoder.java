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
 * 实时性能数据编码接口类
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbRealtimeDataEncoder {

	public byte[] encode(long moId, List<EnbRealtimeItemData> itemDataList)
			throws Exception;

}
