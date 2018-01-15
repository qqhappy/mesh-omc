/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.common.impl;

import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.common.RealTimePerfProxy;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * 基站实时性能请求代理实现
 * 
 * @author fanhaoyu
 * 
 */

public class RealTimePerfProxyImpl implements RealTimePerfProxy {

	private McBtsConnector connector;

	public RealTimePerfProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	@Override
	public void queryAsync(long moId, GenericBizData genericBizData)
			throws Exception {
		// 将模型转换为网元消息
		McBtsMessage request = McBtsBizProxyHelper.convertModelToRequest(moId,
				genericBizData, McBtsConstants.OPERATION_QUERY);
		// 调低底层通信层发送消息, 同步等待应答
		connector.asyncInvoke(request);
	}

}
