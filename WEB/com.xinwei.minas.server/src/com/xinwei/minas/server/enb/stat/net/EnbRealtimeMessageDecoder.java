/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-8	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.net;

import java.util.List;

import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;
import com.xinwei.minas.server.enb.net.EnbAppMessage;

/**
 * 
 * 实时性能消息解码器接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbRealtimeMessageDecoder {

	public List<EnbRealtimeItemData> decode(EnbAppMessage appMessage)
			throws Exception;

}
