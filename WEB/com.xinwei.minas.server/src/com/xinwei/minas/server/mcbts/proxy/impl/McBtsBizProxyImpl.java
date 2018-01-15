/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.impl;

import java.text.MessageFormat;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * McBtsҵ��Э��������ʵ��
 * 
 * @author chenjunhua
 * 
 */

public class McBtsBizProxyImpl implements McBtsBizProxy {

	private static final Log log = LogFactory.getLog(McBtsBizProxyImpl.class);

	private McBtsConnector connector;

	public McBtsBizProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	/**
	 * ��ѯ��Ԫҵ������
	 * 
	 * @param genericBizData
	 *            ��Ԫҵ������
	 * @return ��¼��
	 * @throws Exception
	 */
	public GenericBizData query(Long moId, GenericBizData genericBizData)
			throws Exception {
		// ��ģ��ת��Ϊ��Ԫ��Ϣ
		McBtsMessage request = McBtsBizProxyHelper.convertModelToRequest(moId,
				genericBizData, McBtsConstants.OPERATION_QUERY);
		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage response = connector.syncInvoke(request);
			// �������
			McBtsBizProxyHelper.parseResult(response);
			// ����Ԫ��Ϣת��Ϊģ��
			GenericBizData result = McBtsBizProxyHelper.convertResponseToModel(
					genericBizData.getBizName(), response,
					McBtsConstants.OPERATION_QUERY);
			return result;
		} catch (TimeoutException e) {
			log.error("���վ��ѯ\"" + genericBizData.getBizName() + "\"ʱ������ʱ����", e);
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time")
							+ e.getLocalizedMessage());
		} catch (UnsupportedOperationException e) {
			log.error(OmpAppContext.getMessage("unsupported_biz_operation")
					+ ":" + genericBizData.getBizName());
			throw new UnsupportedOperationException(
					OmpAppContext.getMessage("unsupported_biz_operation"));
		} catch (Exception e) {
			log.error("��ѯ" + genericBizData.getBizName() + "ʱ������ѯ����", e);
			throw new Exception(OmpAppContext.getMessage("mcbts_query_error")
					+ e.getLocalizedMessage());
		}
	}

	/**
	 * ������Ԫҵ������
	 * 
	 * @param genericBizData
	 *            ��Ԫҵ������
	 * @throws Exception
	 */
	public void config(Long moId, GenericBizData genericBizData)
			throws Exception {

		McBtsMessage request = null;
		try {
			// ��ģ��ת��Ϊ��Ԫ��Ϣ
			request = McBtsBizProxyHelper.convertModelToRequest(moId,
					genericBizData, McBtsConstants.OPERATION_CONFIG);
			if (genericBizData.getTransactionId() != 0) {
				request.setTransactionId(genericBizData.getTransactionId());
			}
		} catch (Exception e) {
			log.error("����ת����Ϣ��ʱ�����" + genericBizData.getBizName(), e);
			throw new Exception(e);
		}
		
		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage response = connector.syncInvoke(request);
			// �������
			McBtsBizProxyHelper.parseResult(response);
		} catch (TimeoutException e) {
			log.error("��վ����ʱ������ʱ����,ҵ��:" + genericBizData.getBizName(), e);
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		} catch (UnsupportedOperationException e) {
			throw new UnsupportedOperationException(
					OmpAppContext.getMessage("unsupported_biz_operation")
							+ McBtsBizProxyHelper.getProtocolDesc(
									genericBizData,
									McBtsConstants.OPERATION_CONFIG));
		} catch (Exception e) {
			log.error("��վ����ʱ�������ô���,ҵ��:" + genericBizData.getBizName(), e);
			throw new Exception(OmpAppContext.getMessage("mcbts_config_error")
					+ e.getLocalizedMessage());
		}
	}

	public static void main(String[] args) {
		// String a = "65534";
		// int ia = Integer.parseInt(a);
		// short sa = (short) ia;
		// System.out.println(ia);
		// System.out.println(sa);
		// byte[] body = new byte[2];
		// ByteArrayUtil.putShort(body, 0, sa);
		// System.out.println(ByteArrayUtil.toHexString(body));
		// byte[] b = new byte[] { -1 };
		// System.out.println(ByteArrayUtil.toHexString(b));

		byte[] buf = new byte[] { -1, -1, -1, -1 };
		long value = ByteUtils.toLong(buf, 0, 2);
		System.out.println(value);

		String msg = OmpAppContext.getMessage("wrong") + ":{0}, {1}, {2}";
		String result = MessageFormat.format(msg, "xxx", 123, 3.33f);
		System.out.println(result);

	}

	// /**
	// * ������Ԫҵ������
	// *
	// * @param moBizData
	// * ��Ԫҵ������
	// * @return ��¼��
	// * @throws Exception
	// */
	// public MoBizData query(MoBizData moBizData) throws Exception {
	// // У������
	// if (moBizData.getRecords().isEmpty()) {
	// throw new Exception("��ѯ������������.");
	// }
	// String bizName = moBizData.getBizName();
	// // ��ģ��ת��Ϊ��Ԫ��Ϣ
	// McBtsMessage request = convertModelToRequest(moBizData);
	// // ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
	// McBtsMessage response = connector.syncInvoke(request);
	// // �������
	// parseResult(response);
	// // ����Ԫ��Ϣת��Ϊģ��
	// MoBizData result = this.convertResponseToModel(bizName, response);
	// return result;
	// }

	// /**
	// * ������Ԫҵ������
	// *
	// * @param moBizData
	// * ��Ԫҵ������
	// * @throws Exception
	// */
	// public void config(MoBizData moBizData) throws Exception {
	// // У������
	// if (moBizData.getRecords().isEmpty()) {
	// throw new Exception("����������������.");
	// }
	// // ��ģ��ת��Ϊ��Ԫ��Ϣ
	// McBtsMessage request = convertModelToRequest(moBizData);
	// try {
	// // ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
	// McBtsMessage response = connector.syncInvoke(request);
	// // �������
	// parseResult(response);
	// } catch (TimeoutException e) {
	// throw new Exception("��վӦ��ʱ.");
	// }
	// }

	// /**
	// * ��ҵ��ģ��ת��Ϊ��Ԫ��Ϣ
	// *
	// * @param moBizData
	// * @return
	// * @throws Exception
	// */
	// private McBtsMessage convertModelToRequest(MoBizData moBizData)
	// throws Exception {
	// McBtsMessage message = new McBtsMessage();
	// String bizName = moBizData.getBizName();
	// String operation = McBtsConstants.OPERATION_CONFIG;
	// McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
	// .getProtocolMetas().getProtocolMetaBy(bizName, operation);
	// if (protocolMeta == null) {
	// String msg = "δ֪��ҵ����� [{0}/{1}]\n���������ļ��Ƿ���ȷ.";
	// msg = MessageFormat.format(msg, bizName, operation);
	// throw new Exception(msg);
	// }
	// MoBizRecord record = moBizData.getRecords().get(0);
	// McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
	// .getHeader().getItem();
	// McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getRequest()
	// .getBody().getItem();
	// // �����Ϣͷ
	// this.fillHeader(message, record, headerItems);
	// // �����Ϣ��
	// this.fillBody(message, record, bodyItems);
	// return message;
	// }
	//
	//
	//
	// /**
	// * �����Ϣͷ
	// *
	// * @param message
	// * @param record
	// * @param headerItems
	// */
	// private void fillHeader(McBtsMessage message, MoBizRecord record,
	// McBtsProtocolHeaderItemMeta[] headerItems) {
	// for (McBtsProtocolHeaderItemMeta item : headerItems) {
	// String itemName = item.getName();
	// String itemValue = item.getValue();
	// if (itemName.equals(McBtsConstants.PROTOCOL_MSG_AREA)) {
	// message.setMsgArea(Integer.parseInt(itemValue));
	// } else if (itemName.equals(McBtsConstants.PROTOCOL_MA)) {
	// message.setMa(Integer.parseInt(itemValue));
	// } else if (itemName.equals(McBtsConstants.PROTOCOL_MOC)) {
	// if (itemValue.toLowerCase().startsWith("0x")) {
	// // 16����
	// message.setMoc(Integer.parseInt(itemValue.substring(2), 16));
	// } else {
	// message.setMoc(Integer.parseInt(itemValue));
	// }
	// } else if (itemName.equals(McBtsConstants.PROTOCOL_ACTION_TYPE)) {
	// message.setActionType(Integer.parseInt(itemValue));
	// }
	// }
	// // ���û�վID
	// Long moId = record.getMoId();
	// Long btsId = this.getBtsIdByMoId(moId);
	// message.setBtsId(btsId);
	// }
	//
	// /**
	// * �����Ϣ��
	// *
	// * @param message
	// * @param record
	// * @param bodyItems
	// */
	// private void fillBody(McBtsMessage message, MoBizRecord record,
	// McBtsProtocolBodyItemMeta[] bodyItems) {
	// byte[] buf = new byte[4096];
	// int offset = 0;
	// for (McBtsProtocolBodyItemMeta item : bodyItems) {
	// String name = item.getName();
	// String type = item.getType();
	// int length = Integer.parseInt(item.getLength());
	// String value = record.getItemValue(name);
	// if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)
	// || type.equals(McBtsConstants.TYPE_SIGNED_NUMBER)) {
	// // ��ֵ�ͣ������޷��ź��з�������
	// ByteUtils.putNumber(buf, offset, value,
	// length);
	// }
	// else if (type.equals(McBtsConstants.TYPE_RESERVE)) {
	// // do nothing
	// }
	// else if (type.equals(McBtsConstants.TYPE_IPv4)) {
	// ByteUtils.putIp(buf, offset, value);
	// }
	// offset += length;
	// }
	// message.setContent(buf, 0, offset);
	// }

	// /**
	// * ����Ԫ��Ϣת��Ϊҵ��ģ��
	// *
	// * @param moBizData
	// * @return
	// * @throws Exception
	// */
	// private MoBizData convertResponseToModel(String bizName, McBtsMessage
	// message)
	// throws Exception {
	// MoBizData bizData = new MoBizData();
	// bizData.setBizName(bizName);
	// byte[] buf = message.getContent();
	// String operation = McBtsConstants.OPERATION_QUERY;
	// McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
	// .getProtocolMetas().getProtocolMetaBy(bizName, operation);
	// if (protocolMeta == null) {
	// String msg = "δ֪��ҵ����� [{0}/{1}]\n���������ļ��Ƿ���ȷ.";
	// msg = MessageFormat.format(msg, bizName, operation);
	// throw new Exception(msg);
	// }
	// McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
	// .getHeader().getItem();
	// McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getRequest()
	// .getBody().getItem();
	// MoBizRecord record = new MoBizRecord();
	// // ������Ϣ��
	// this.parseBody(record, buf, bodyItems);
	// bizData.addRecord(record);
	// return bizData;
	// }
	//
	//
	// /**
	// * ������Ϣ��
	// * @param record
	// * @param buf
	// * @param bodyItems
	// */
	// private void parseBody(MoBizRecord record, byte[] buf,
	// McBtsProtocolBodyItemMeta[] bodyItems) {
	// int offset = 0;
	// for (McBtsProtocolBodyItemMeta item : bodyItems) {
	// String name = item.getName();
	// String type = item.getType();
	// int length = Integer.parseInt(item.getLength());
	// String value = "";
	// if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)) {
	// // �޷�����
	// value = String.valueOf(ByteUtils.toUnsignedNumber(buf, offset, length));
	// }
	// else if (type.equals(McBtsConstants.TYPE_SIGNED_NUMBER)) {
	// // �з�����
	// value = String.valueOf(ByteUtils.toSignedNumber(buf, offset, length));
	// }
	// if (!type.equals(McBtsConstants.TYPE_RESERVE)) {
	// record.addItemValue(name, value);
	// }
	// offset += length;
	// }
	// }

}
