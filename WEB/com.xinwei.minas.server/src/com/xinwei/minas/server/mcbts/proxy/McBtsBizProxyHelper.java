/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy;

import java.text.MessageFormat;
import java.util.List;

import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.GenericBizRecord;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * McBts Proxy ����
 * 
 * @author chenjunhua
 * 
 */

public class McBtsBizProxyHelper {
	/**
	 * ��ҵ��ģ��ת��Ϊ��Ԫ��Ϣ
	 * 
	 * @param bizData
	 * @return
	 * @throws Exception
	 */
	public static McBtsMessage convertModelToRequest(Long moId,
			GenericBizData bizData, String operation) throws Exception {
		McBtsMessage message = new McBtsMessage();
		String bizName = bizData.getBizName();
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas().getProtocolMetaBy(bizName, operation);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, bizName, operation);
			throw new Exception(msg);
		}
		// ��ȡԪ����
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
				.getHeader().getItem();
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getRequest()
				.getBody().getItem();
		// �����Ϣͷ
		fillHeader(message, moId, headerItems);
		// �����Ϣ��
		List<GenericBizRecord> records = bizData.getRecords();
		if (!records.isEmpty()) {
			GenericBizRecord record = records.get(0);
			fillBody(message, record, bodyItems);
		}
		return message;
	}

	/**
	 * ����Э���ж����Э������
	 * 
	 * @param bizData
	 * @param operation
	 * @return
	 */
	public static String getProtocolDesc(GenericBizData bizData,
			String operation) {
		String bizName = bizData.getBizName();
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas().getProtocolMetaBy(bizName, operation);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, bizName, operation);
			return msg;
		}

		return protocolMeta.getDesc();
	}

	/**
	 * ����Ԫ��Ϣת��Ϊҵ��ģ��
	 * 
	 * @param moBizData
	 * @return
	 * @throws Exception
	 */
	public static GenericBizData convertResponseToModel(String bizName,
			McBtsMessage message, String operation) throws Exception {
		GenericBizData bizData = new GenericBizData(bizName);
		byte[] buf = message.getContent();
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas().getProtocolMetaBy(bizName, operation);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, bizName, operation);
			throw new Exception(msg);
		}
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getResponse()
				.getHeader().getItem();
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getResponse()
				.getBody().getItem();
		// ������Ϣ��
		parseBody(bizData, buf, bodyItems);
		return bizData;
	}

	/**
	 * �������
	 * 
	 * @param response
	 * @throws Exception
	 */
	public static void parseResult(McBtsMessage response) throws Exception {
		if (!response.isSuccessful()) {
			String reason = "";
			int resultCode = response.getActionResult();
			if (resultCode != 1) {
				StringBuilder sbuf = new StringBuilder();
				sbuf.append(Integer.toHexString(resultCode).toLowerCase());
				while (sbuf.length() < 4) {
					sbuf.insert(0, '0');
				}
				sbuf.insert(0, "mcbts_error_code_0x");
				reason = OmpAppContext.getMessage(sbuf.toString());
				if (reason.contains(sbuf.toString())) {
					reason = OmpAppContext
							.getMessage("mcbts_error_code_unknow");
				}
			}
			String errorMsg = OmpAppContext.getMessage("operation_failed");
			errorMsg = MessageFormat.format(errorMsg, reason);
			throw new BizException(errorMsg);
		}
	}

	/**
	 * ������Ϣ��
	 * 
	 * @param record
	 * @param buf
	 * @param bodyItems
	 */
	private static void parseBody(GenericBizData bizData, byte[] buf,
			McBtsProtocolBodyItemMeta[] bodyItems) {
		int offset = 0;
		if (bodyItems == null) {
			return;
		}
		for (McBtsProtocolBodyItemMeta item : bodyItems) {
			String name = item.getName();
			String type = item.getType();
			int length = Integer.parseInt(item.getLength());
			Object value = null;
			if (type.equalsIgnoreCase(McBtsConstants.TYPE_UNSIGNED_NUMBER)) {
				// �޷�����
				value = ByteUtils.toUnsignedNumber(buf, offset, length);
			} else if (type.equalsIgnoreCase(McBtsConstants.TYPE_SIGNED_NUMBER)) {
				// �з�����
				value = ByteUtils.toSignedNumber(buf, offset, length);
			} else if (type.equalsIgnoreCase(McBtsConstants.TYPE_IPv4)) {
				// IPv4
				value = ByteUtils.toIp(buf, offset, length);
			} else if (type.equalsIgnoreCase(McBtsConstants.TYPE_STRING)) {
				// String
				String charsetName = McBtsConstants.CHARSET_US_ASCII;
				value = ByteUtils.toString(buf, offset, length, charsetName);
			} else if (type
					.equalsIgnoreCase(McBtsConstants.TYPE_TRIMMED_STRING)) {
				// ����ͬtoStringһ��,ֻ�ǽ��ȥ�����ߵĿո�
				String charsetName = McBtsConstants.CHARSET_US_ASCII;
				value = ByteUtils.toTrimmedString(buf, offset, length,
						charsetName);
			} else if (type.equalsIgnoreCase(McBtsConstants.TYPE_VAR_STRING)) {
				// �䳤String
				// ��ȡ�䳤�ַ�������
				int strLen = ByteUtils.toInt(buf, offset, length);
				offset += length;
				String charsetName = McBtsConstants.CHARSET_US_ASCII;
				value = ByteUtils.toString(buf, offset, strLen, charsetName);
				// ����ƫ��������
				length = strLen;
			} else if (type.equalsIgnoreCase(McBtsConstants.TYPE_HEX_STRING)) {
				value = ByteUtils.toHexString(buf, offset, length);
			} else if (type.equalsIgnoreCase(McBtsConstants.TYPE_RESERVE)) {
				value = ByteUtils.toUnsignedNumber(buf, offset, length);
			}
			offset += length;
			GenericBizProperty property = new GenericBizProperty();
			property.setName(name);
			property.setValue(value);
			bizData.addProperty(property);
		}
	}

	/**
	 * �����Ϣͷ
	 * 
	 * @param message
	 * @param record
	 * @param headerItems
	 */
	public static void fillHeader(McBtsMessage message, Long moId,
			McBtsProtocolHeaderItemMeta[] headerItems) {
		for (McBtsProtocolHeaderItemMeta item : headerItems) {
			String itemName = item.getName();
			String itemValue = item.getValue();
			if (itemName.equals(McBtsConstants.PROTOCOL_MSG_AREA)) {
				message.setMsgArea(Integer.parseInt(itemValue));
			} else if (itemName.equals(McBtsConstants.PROTOCOL_MA)) {
				message.setMa(Integer.parseInt(itemValue));
			} else if (itemName.equals(McBtsConstants.PROTOCOL_MOC)) {
				if (itemValue.toLowerCase().startsWith("0x")) {
					// 16����
					message.setMoc(Integer.parseInt(itemValue.substring(2), 16));
				} else {
					message.setMoc(Integer.parseInt(itemValue));
				}
			} else if (itemName.equals(McBtsConstants.PROTOCOL_ACTION_TYPE)) {
				message.setActionType(Integer.parseInt(itemValue));
			}
		}
		// ���û�վID
		Long btsId = getBtsIdByMoId(moId);
		message.setBtsId(btsId);
	}

	/**
	 * �����Ϣ��
	 * 
	 * @param message
	 * @param record
	 * @param bodyItems
	 */
	public static void fillBody(McBtsMessage message, GenericBizRecord record,
			McBtsProtocolBodyItemMeta[] bodyItems) {
		byte[] buf = new byte[4096];
		int offset = 0;
		for (McBtsProtocolBodyItemMeta item : bodyItems) {
			String name = item.getName();
			String type = item.getType();
			int length = Integer.parseInt(item.getLength());
			GenericBizProperty property = record.getPropertyValue(name);
			if (type.equalsIgnoreCase(McBtsConstants.TYPE_UNSIGNED_NUMBER)
					|| type.equalsIgnoreCase(McBtsConstants.TYPE_SIGNED_NUMBER)) {
				// ��ֵ�ͣ������޷��ź��з�������
				ByteUtils.putNumber(buf, offset,
						property.getValue().toString(), length);
			} else if (type.equalsIgnoreCase(McBtsConstants.TYPE_RESERVE)) {
				// �����ֶ�
				// do nothing
			} else if (type.equalsIgnoreCase(McBtsConstants.TYPE_IPv4)) {
				// IPv4
				ByteUtils.putIp(buf, offset, property.getValue().toString());
			} else if (type.equalsIgnoreCase(McBtsConstants.TYPE_STRING)) {
				// ����String
				String charsetName = McBtsConstants.CHARSET_US_ASCII;
				char fillChar = '\0';
				ByteUtils.putString(buf, offset,
						property.getValue().toString(), length, fillChar,
						charsetName);
			} else if (type.equalsIgnoreCase(McBtsConstants.TYPE_VAR_STRING)) {
				// �䳤String
				String str = property.getValue().toString();
				int strLen = str.length();
				// �ַ�������
				offset = ByteUtils.putNumber(buf, offset,
						String.valueOf(strLen), length);
				// �ַ���
				String charsetName = McBtsConstants.CHARSET_US_ASCII;
				char fillChar = '\0';
				ByteUtils.putString(buf, offset, str, strLen, fillChar,
						charsetName);
				// ����ƫ��������
				length = strLen;
			} else if (type.equalsIgnoreCase(McBtsConstants.TYPE_HEX_STRING)) {
				String str = property.getValue().toString();

				ByteUtils.putHexString(buf, offset, str);
			}
			offset += length;
		}
		message.setContent(buf, 0, offset);
	}

	/**
	 * ����MO ID��ȡ��վID
	 * 
	 * @param moId
	 * @return
	 */
	private static Long getBtsIdByMoId(Long moId) {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		return mcBts.getBtsId();
	}
}
