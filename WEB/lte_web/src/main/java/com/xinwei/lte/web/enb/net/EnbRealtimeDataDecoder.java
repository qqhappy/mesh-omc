/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-14	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.net;

import java.util.List;

import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;

/**
 * 
 * eNB实时性能数据解码器接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbRealtimeDataDecoder {

	public List<EnbRealtimeItemData> decode(byte[] dataBytes) throws Exception;

}
