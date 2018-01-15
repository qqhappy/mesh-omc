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
 * ��վʵʱ�����������ʵ��
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
		// ��ģ��ת��Ϊ��Ԫ��Ϣ
		McBtsMessage request = McBtsBizProxyHelper.convertModelToRequest(moId,
				genericBizData, McBtsConstants.OPERATION_QUERY);
		// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
		connector.asyncInvoke(request);
	}

}
