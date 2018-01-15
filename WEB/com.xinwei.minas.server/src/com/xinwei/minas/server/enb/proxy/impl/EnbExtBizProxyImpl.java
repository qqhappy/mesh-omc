/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.proxy.impl;

import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbConnector;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.net.EnbMessageHelper;
import com.xinwei.minas.server.enb.net.TagConst;
import com.xinwei.minas.server.enb.proxy.EnbExtBizProxy;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB扩展业务代理接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbExtBizProxyImpl implements EnbExtBizProxy {

	private static Log log = LogFactory.getLog(EnbExtBizProxyImpl.class);

	private EnbConnector enbConnector;

	@Override
	public void reset(long enbId) throws Exception {
		// 创建消息
		EnbAppMessage resetMessage = createResetMessage(enbId,
				EnbMessageConstants.RESET_TYPE_BTS);
		// 发送消息
		sendResetMessage(resetMessage);
	}

	@Override
	public void reset(long enbId, int rackId, int shelfId, int boardId)
			throws Exception {
		// 创建消息
		EnbAppMessage resetMessage = createResetMessage(enbId,
				EnbMessageConstants.RESET_TYPE_BOARD);
		resetMessage.addTagValue(TagConst.RACK_NO, rackId);
		resetMessage.addTagValue(TagConst.SHELF_NO, shelfId);
		resetMessage.addTagValue(TagConst.SLOT_NO, boardId);
		// 发送消息
		sendResetMessage(resetMessage);
	}

	@Override
	public String studyEnbDataConfig(long enbId) throws Exception {
		EnbAppMessage studyMessage = createStudyMessage(enbId);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			if (enbConnector != null) {
				EnbAppMessage resp = enbConnector
						.syncInvoke(studyMessage, 5000);
				return resp.getStringValue(TagConst.DATA_CONFIG);
			}
		} catch (TimeoutException e) {
			log.error("send study eNB data config request timeout.");
			throw new Exception(OmpAppContext.getMessage("enb_reply_over_time"));
		} catch (Exception e) {
			log.error("send study eNB data config request failed.", e);
			throw new Exception(
					OmpAppContext.getMessage("study_dataconfig_failed")
							+ e.getLocalizedMessage());
		}
		return "";
	}

	/**
	 * 发送复位消息
	 * 
	 * @param resetMessage
	 * @throws Exception
	 */
	private void sendResetMessage(EnbAppMessage resetMessage) throws Exception {
		try {
			// 调低底层通信层发送消息, 同步等待应答
			if (enbConnector != null) {
				EnbAppMessage resp = enbConnector.syncInvoke(resetMessage);
				// 解析应答结果
				EnbMessageHelper.parseResponse(resp);
			}
		} catch (TimeoutException e) {
			log.error("send eNB reset request timeout.");
			throw new Exception(OmpAppContext.getMessage("enb_reply_over_time"));
		} catch (Exception e) {
			log.error("send eNB reset request failed.", e);
			throw new Exception(OmpAppContext.getMessage("reset_enb_failed")
					+ e.getLocalizedMessage());
		}
	}

	/**
	 * 创建复位消息
	 * 
	 * @param enbId
	 * @param resetType
	 *            复位类型，基站级或单板级
	 * @return
	 */
	private EnbAppMessage createResetMessage(long enbId, int resetType) {
		EnbAppMessage appMessage = new EnbAppMessage();
		appMessage.setEnbId(enbId);
		appMessage.setMa(EnbMessageConstants.MA_CONF);
		appMessage.setMoc(EnbMessageConstants.MOC_RESET);
		appMessage.setActionType(EnbMessageConstants.ACTION_OTHERS);
		appMessage.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		appMessage.addTagValue(TagConst.RESET_TYPE, resetType);
		return appMessage;
	}

	/**
	 * 创建自学习请求消息
	 * 
	 * @param enbId
	 * @return
	 */
	private EnbAppMessage createStudyMessage(long enbId) {
		EnbAppMessage appMessage = new EnbAppMessage();
		appMessage.setEnbId(enbId);
		appMessage.setMa(EnbMessageConstants.MA_CONF);
		appMessage
				.setMoc(EnbMessageConstants.MOC_ENB_DATA_CONFIG_STUDY_REQ_AND_RES);
		appMessage.setActionType(EnbMessageConstants.ACTION_OTHERS);
		appMessage.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		return appMessage;
	}

	public void setEnbConnector(EnbConnector enbConnector) {
		this.enbConnector = enbConnector;
	}

}
