/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-22	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.proxy.impl;

import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbConnector;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.net.EnbMessageHelper;
import com.xinwei.minas.server.enb.net.TagConst;
import com.xinwei.minas.server.enb.proxy.EnbFileManagerProxy;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述:基站文件管理 proxy的实现
 * </p>
 * 
 * @author qiwei
 * 
 */

public class EnbFileManagerProxyImpl implements EnbFileManagerProxy {

	private Log log = LogFactory.getLog(EnbFileManagerProxyImpl.class);

	private EnbConnector enbConnector;

	public void setEnbConnector(EnbConnector enbConnector) {
		this.enbConnector = enbConnector;
	}

	@Override
	public void enbVersionDownloadConfig(Long enbId,
			GenericBizData genericBizData) throws Exception {
		EnbAppMessage request = null;
		// 转换消息
		request = convertModelToMessage(enbId, genericBizData);
		// 发送消息
		config(request, genericBizData);
	}

	/**
	 * 将数据模型传化成消息；(基站版本升级配置)
	 * 
	 * @param enbId
	 * @param data
	 * @return
	 */
	private EnbAppMessage convertModelToMessage(Long enbId, GenericBizData data) {
		EnbAppMessage message = new EnbAppMessage();
		// 应用层包头
		message.setEnbId(enbId);
		message.setMa(EnbMessageConstants.MA_FILE);
		message.setMoc(EnbMessageConstants.MOC_ENB_VERSION_DOWNLOAD);
		message.setActionType(EnbMessageConstants.ACTION_CONFIG);
		message.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		// 消息体
		message.addTagValue(TagConst.FTP_IP,
				data.getProperty(EnbConstantUtils.FTP_IP).getValue());

		message.addTagValue(TagConst.FTP_PORT,
				data.getProperty(EnbConstantUtils.FTP_PORT).getValue());

		message.addTagValue(TagConst.FTP_USER_NAME,
				data.getProperty(EnbConstantUtils.FTP_USER_NAME).getValue());

		message.addTagValue(TagConst.FTP_PASSWORD,
				data.getProperty(EnbConstantUtils.FTP_PASSWORD).getValue());

		message.addTagValue(TagConst.FILE_DIRECTORY,
				data.getProperty(EnbConstantUtils.FILE_DIRECTORY).getValue());

		message.addTagValue(TagConst.SOFTWARE_TYPE,
				data.getProperty(EnbConstantUtils.SOFTWARE_TYPE).getValue());

		message.addTagValue(TagConst.VERSION,
				data.getProperty(EnbConstantUtils.VERSION).getValue());

		message.addTagValue(TagConst.DATA_FILE_DIRECTORY,
				data.getProperty(EnbConstantUtils.DATA_FILE_DIRECTORY)
						.getValue());

		message.addTagValue(TagConst.DATA_FILE_NAME,
				data.getProperty(EnbConstantUtils.DATA_FILE_NAME).getValue());

		return message;
	}

	@Override
	public void upgrade(Long enbId, GenericBizData genericBizData)
			throws Exception {
		EnbAppMessage request = null;
		// 将模型转换为网元消息
		request = convertModelToUpgradeMessage(enbId, genericBizData);
		// 发送消息
		config(request, genericBizData);
	}

	/**
	 * 转换基站升级的方法
	 * 
	 * @param enbId
	 * @param genericBizData
	 * @return
	 */
	private EnbAppMessage convertModelToUpgradeMessage(Long enbId,
			GenericBizData genericBizData) {
		EnbAppMessage message = new EnbAppMessage();
		// 应用层包头
		message.setEnbId(enbId);
		message.setMa(EnbMessageConstants.MA_FILE);
		message.setMoc(EnbMessageConstants.MOC_ENB_VERSION_UPGRADE);
		message.setActionType(EnbMessageConstants.ACTION_CONFIG);
		message.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		// 消息体
		message.addTagValue(TagConst.SWITCH_SOFTWARE_TYPE, genericBizData
				.getProperty(EnbConstantUtils.SW_TYPE).getValue());
		return message;
	}

	/**
	 * 发送消息并解析的底层方法
	 * 
	 * @throws Exception
	 */
	public void config(EnbAppMessage request, GenericBizData genericBizData)
			throws Exception {
		try {
			// 将模型转换为网元消息
			if (genericBizData.getTransactionId() != 0) {
				request.setTransactionId(genericBizData.getTransactionId());
			}
		} catch (Exception e) {
			log.error("配置转换消息的时候出错" + genericBizData.getBizName(), e);
			throw new Exception(e);
		}

		try {
			// 调低底层通信层发送消息, 同步等待应答
			if (enbConnector != null) {
				EnbAppMessage resp = enbConnector.syncInvoke(request);
				// 解析应答结果
				EnbMessageHelper.parseResponse(resp);
			}

		} catch (TimeoutException e) {
			log.error("send message to eNB failed.", e);
			throw new Exception(OmpAppContext.getMessage("enb_reply_over_time"));
		} catch (UnsupportedOperationException e) {
			throw new UnsupportedOperationException(
					OmpAppContext.getMessage("unsupported_biz_operation")
							+ genericBizData.getBizName());
		} catch (Exception e) {
			log.error("send message to eNB failed.", e);
			throw new Exception(OmpAppContext.getMessage("operation_faield")
					+ e.getLocalizedMessage());
		}
	}
}
