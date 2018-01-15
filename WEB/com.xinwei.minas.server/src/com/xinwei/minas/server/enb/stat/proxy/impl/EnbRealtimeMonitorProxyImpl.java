/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.proxy.impl;

import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbConnector;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.net.EnbMessageHelper;
import com.xinwei.minas.server.enb.net.TagConst;
import com.xinwei.minas.server.enb.stat.proxy.EnbRealtimeMonitorProxy;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB实时性能监控代理接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbRealtimeMonitorProxyImpl implements EnbRealtimeMonitorProxy {

	private Log log = LogFactory.getLog(EnbRealtimeMonitorProxyImpl.class);

	private EnbConnector enbConnector;

	@Override
	public void start(long enbId, int intervalFlag) throws Exception {
		EnbAppMessage startMessage = createStartMessage(enbId, intervalFlag);

		try {
			// 调低底层通信层发送消息, 同步等待应答
			if (enbConnector != null) {
				EnbAppMessage resp = enbConnector.syncInvoke(startMessage);
				// 解析应答结果
				EnbMessageHelper.parseResponse(resp);
			}
		} catch (TimeoutException e) {
			log.error("send start monitor request timeout.");
			throw new Exception(OmpAppContext.getMessage("enb_reply_over_time"));
		} catch (Exception e) {
			log.error("send start monitor request failed.", e);
			throw new Exception(
					OmpAppContext.getMessage("start_monitor_enb_failed")
							+ e.getLocalizedMessage());
		}
	}

	@Override
	public void stop(long enbId) throws Exception {
		EnbAppMessage stopMessage = createStopMessage(enbId);

		try {
			// 调低底层通信层发送消息, 同步等待应答
			if (enbConnector != null) {
				EnbAppMessage resp = enbConnector.syncInvoke(stopMessage);
				// 解析应答结果
				EnbMessageHelper.parseResponse(resp);
			}
		} catch (TimeoutException e) {
			log.error("send stop monitor request timeout.");
			throw new Exception(OmpAppContext.getMessage("enb_reply_over_time"));
		} catch (Exception e) {
			log.error("send stop monitor request failed.", e);
			throw new Exception(
					OmpAppContext.getMessage("stop_monitor_enb_failed")
							+ e.getLocalizedMessage());
		}
	}

	/**
	 * 创建实时性能监控开始消息
	 * 
	 * @param enbId
	 * @param intervalFlag
	 *            上报时间间隔
	 * @return
	 */
	private EnbAppMessage createStartMessage(long enbId, int intervalFlag) {
		EnbAppMessage appMessage = new EnbAppMessage();
		appMessage.setEnbId(enbId);
		appMessage.setMa(EnbMessageConstants.MA_PERF);
		appMessage
				.setMoc(EnbMessageConstants.MOC_REALTIME_MONITOR_START_REQ_AND_RES);
		appMessage.setActionType(EnbMessageConstants.ACTION_OTHERS);
		appMessage.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		appMessage.addTagValue(TagConst.REALTIME_INTERVAL, intervalFlag);
		return appMessage;
	}

	/**
	 * 创建停止监控消息
	 * 
	 * @param enbId
	 * @return
	 */
	private EnbAppMessage createStopMessage(long enbId) {
		EnbAppMessage appMessage = new EnbAppMessage();
		appMessage.setEnbId(enbId);
		appMessage.setMa(EnbMessageConstants.MA_PERF);
		appMessage
				.setMoc(EnbMessageConstants.MOC_REALTIME_MONITOR_STOP_REQ_AND_RES);
		appMessage.setActionType(EnbMessageConstants.ACTION_OTHERS);
		appMessage.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		return appMessage;
	}

	public void setEnbConnector(EnbConnector enbConnector) {
		this.enbConnector = enbConnector;
	}
}
