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
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����:��վ�ļ����� proxy��ʵ��
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
		// ת����Ϣ
		request = convertModelToMessage(enbId, genericBizData);
		// ������Ϣ
		config(request, genericBizData);
	}

	/**
	 * ������ģ�ʹ�������Ϣ��(��վ�汾��������)
	 * 
	 * @param enbId
	 * @param data
	 * @return
	 */
	private EnbAppMessage convertModelToMessage(Long enbId, GenericBizData data) {
		EnbAppMessage message = new EnbAppMessage();
		// Ӧ�ò��ͷ
		message.setEnbId(enbId);
		message.setMa(EnbMessageConstants.MA_FILE);
		message.setMoc(EnbMessageConstants.MOC_ENB_VERSION_DOWNLOAD);
		message.setActionType(EnbMessageConstants.ACTION_CONFIG);
		message.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		// ��Ϣ��
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
		// ��ģ��ת��Ϊ��Ԫ��Ϣ
		request = convertModelToUpgradeMessage(enbId, genericBizData);
		// ������Ϣ
		config(request, genericBizData);
	}

	/**
	 * ת����վ�����ķ���
	 * 
	 * @param enbId
	 * @param genericBizData
	 * @return
	 */
	private EnbAppMessage convertModelToUpgradeMessage(Long enbId,
			GenericBizData genericBizData) {
		EnbAppMessage message = new EnbAppMessage();
		// Ӧ�ò��ͷ
		message.setEnbId(enbId);
		message.setMa(EnbMessageConstants.MA_FILE);
		message.setMoc(EnbMessageConstants.MOC_ENB_VERSION_UPGRADE);
		message.setActionType(EnbMessageConstants.ACTION_CONFIG);
		message.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		// ��Ϣ��
		message.addTagValue(TagConst.SWITCH_SOFTWARE_TYPE, genericBizData
				.getProperty(EnbConstantUtils.SW_TYPE).getValue());
		return message;
	}

	/**
	 * ������Ϣ�������ĵײ㷽��
	 * 
	 * @throws Exception
	 */
	public void config(EnbAppMessage request, GenericBizData genericBizData)
			throws Exception {
		try {
			// ��ģ��ת��Ϊ��Ԫ��Ϣ
			if (genericBizData.getTransactionId() != 0) {
				request.setTransactionId(genericBizData.getTransactionId());
			}
		} catch (Exception e) {
			log.error("����ת����Ϣ��ʱ�����" + genericBizData.getBizName(), e);
			throw new Exception(e);
		}

		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			if (enbConnector != null) {
				EnbAppMessage resp = enbConnector.syncInvoke(request);
				// ����Ӧ����
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
