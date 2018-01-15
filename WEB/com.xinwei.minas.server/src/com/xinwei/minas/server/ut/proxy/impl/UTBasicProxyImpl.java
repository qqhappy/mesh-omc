/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-30	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.proxy.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.hlr.net.udp.HlrUdpConnector;
import com.xinwei.minas.server.hlr.net.udp.HlrUdpMessage;
import com.xinwei.minas.server.ut.proxy.UTBasicProxy;
import com.xinwei.minas.server.ut.proxy.UTBasicProxyHelper;
import com.xinwei.minas.ut.core.model.UTCondition;
import com.xinwei.minas.ut.core.model.UTQueryResult;
import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.core.utils.PhoneNumberUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * McBts基本业务代理
 * 
 * 
 * @author tiance
 * 
 */

public class UTBasicProxyImpl implements UTBasicProxy {

	private Log log = LogFactory.getLog(UTBasicProxyImpl.class);

	private String hlrIp;

	private int hlrPort;

	private HlrUdpConnector connector;

	public UTBasicProxyImpl(HlrUdpConnector connector) {
		super();
		this.connector = connector;
	}

	public void setHlrIp(String hlrIp) {
		this.hlrIp = hlrIp;
	}

	public void setHlrPort(int hlrPort) {
		this.hlrPort = hlrPort;
	}

	@Override
	public UTQueryResult queryUTByCondition(UTCondition utc) throws Exception {
		HlrUdpMessage request = UTBasicProxyHelper
				.convertFromConditionToMessage(utc, hlrIp, hlrPort);

		request.setHlrIp(hlrIp);
		request.setHlrPort(hlrPort);
		request.setMessageType(HlrUdpMessage.QUERY_USER_REQ);

		List<HlrUdpMessage> response = null;
		try {
			response = connector.syncInvoke(request);
		} catch (TimeoutException e) {
			throw new TimeoutException(OmpAppContext.getMessage("shlr_response_time_out"));
		} catch (Exception e) {
			log.error("Error sending request of ut-querying to hlr.", e);
		}

		UTQueryResult result = UTBasicProxyHelper
				.convertFromMessageToModel(response);

		if (result != null) {
			// 获取总页数
			int pageNum = result.getTotalNumInHlr() / utc.getQueryCount();
			pageNum += (result.getTotalNumInHlr() % utc.getQueryCount() == 0 ? 0
					: 1);
			result.setPageNum(pageNum);

			// 获取当前页数
			int curPage = utc.getPageShift() / utc.getQueryCount() + 1;
			result.setCurPage(curPage);
		}

		return result;
	}

}
