/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-10	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.server.core.conf.net.model.TlvFieldUtil;
import com.xinwei.minas.server.enb.EnbModule;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB��վ��Ϣ��������
 * 
 * @author fanhaoyu
 * 
 */

public class EnbMessageHelper {

	/**
	 * �����ֽ��������������Ϣ
	 * 
	 * @param buf
	 * @return
	 */
	public static EnbTransportMessage parse(byte[] buf) {
		EnbTransportMessage message = new EnbTransportMessage();
		int offset = 0;
		// �汾��2���ֽ�
		message.setVersion(ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// Ӧ�ô�����ֽڳ��ȣ�2���ֽ�
		message.setTransportLen(ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// Ӧ�ñ��Ĳ��ֽ��ܳ��ȣ�4���ֽ�
		int appLength = ByteUtils.toInt(buf, offset, 4);
		message.setTotalAppLen(appLength);
		offset += 4;
		// ��Ϣ��ʶ��4���ֽڣ�����Ψһ��ʾһ��Ӧ�ò���Ϣ������������ʹ��
		message.setMessageId(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		// �ܰ�����4���ֽ�
		message.setTotalPacketNum(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		// ����ţ�4���ֽڣ���0��ʼ����
		message.setPacketNo(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		byte[] payload = new byte[appLength];
		System.arraycopy(buf, offset, payload, 0, appLength);
		message.setPayload(payload);

		return message;
	}

	/**
	 * ���������Ϣ������ֽ���
	 * 
	 * @param message
	 * @return
	 */
	public static byte[] encode(EnbTransportMessage message) {
		byte[] msgBytes = new byte[EnbTransportMessage.HEADER_LEN
				+ message.getPayload().length];
		int offset = 0;
		ByteUtils.putNumber(msgBytes, offset,
				String.valueOf(message.getVersion()), 2);
		offset += 2;
		ByteUtils.putNumber(msgBytes, offset,
				String.valueOf(message.getTransportLen()), 2);
		offset += 2;
		ByteUtils.putNumber(msgBytes, offset,
				String.valueOf(message.getTotalAppLen()), 4);
		offset += 4;
		ByteUtils.putNumber(msgBytes, offset,
				String.valueOf(message.getMessageId()), 4);
		offset += 4;
		ByteUtils.putNumber(msgBytes, offset,
				String.valueOf(message.getTotalPacketNum()), 4);
		offset += 4;
		ByteUtils.putNumber(msgBytes, offset,
				String.valueOf(message.getPacketNo()), 4);
		offset += 4;
		System.arraycopy(message.getPayload(), 0, msgBytes, offset,
				message.getPayload().length);
		return msgBytes;
	}

	/**
	 * ��Ӧ�ò���Ϣ������ֽ���
	 * 
	 * @param message
	 * @return
	 * @throws BizException
	 */
	public static byte[] encode(EnbAppMessage message) throws Exception {
		TlvFieldUtil util = new TlvFieldUtil(EnbModule.getInstance()
				.getTagCollection());
		// ��TLV���ֱ���
		byte[] content = util.encode(message.getFieldMap());
		byte[] msgBytes = new byte[EnbAppMessage.HEADER_LEN + content.length];
		int offset = 0;
		ByteUtils.putLong(msgBytes, offset, message.getEnbId());
		offset += 8;
		ByteUtils.putNumber(msgBytes, offset,
				String.valueOf(message.getTransactionId()), 4);
		offset += 4;
		ByteUtils.putNumber(msgBytes, offset, String.valueOf(message.getMa()),
				2);
		offset += 2;
		ByteUtils.putNumber(msgBytes, offset, String.valueOf(message.getMoc()),
				2);
		offset += 2;
		ByteUtils.putNumber(msgBytes, offset,
				String.valueOf(message.getActionType()), 1);
		offset += 1;
		ByteUtils.putNumber(msgBytes, offset,
				String.valueOf(message.getMessageType()), 1);
		offset += 1;
		System.arraycopy(content, 0, msgBytes, offset, content.length);
		return msgBytes;
	}

	/**
	 * �����Ϣ
	 * 
	 * @param message
	 * @return
	 * @throws BizException
	 */
	public static List<EnbTransportMessage> splitMessage(EnbAppMessage message)
			throws Exception {
		List<EnbTransportMessage> transMsgList = new ArrayList<EnbTransportMessage>();
		// ��Ӧ�ò���Ϣ����
		byte[] msgBytes = encode(message);

		int packetNum = msgBytes.length / EnbMessageConstants.SINGLE_MAX_LEN;
		if (msgBytes.length % EnbMessageConstants.SINGLE_MAX_LEN != 0) {
			packetNum++;
		}
		for (int i = 0; i < packetNum; i++) {
			EnbTransportMessage transMsg = new EnbTransportMessage();
			transMsg.setMessageId(message.getTransactionId());

			transMsg.setVersion(0);
			transMsg.setTotalAppLen(msgBytes.length);
			transMsg.setTotalPacketNum(packetNum);
			transMsg.setPacketNo(i);

			int offset = EnbMessageConstants.SINGLE_MAX_LEN * i;
			int payloadLength = EnbMessageConstants.SINGLE_MAX_LEN;
			// ��������һ����
			if (i == packetNum - 1) {
				payloadLength = msgBytes.length - offset;
			}
			byte[] payload = new byte[payloadLength];
			System.arraycopy(msgBytes, offset, payload, 0, payloadLength);
			transMsg.setPayload(payload);

			transMsg.setTransportLen(EnbTransportMessage.HEADER_LEN
					+ payloadLength);

			transMsgList.add(transMsg);
		}

		return transMsgList;
	}

	/**
	 * �ϲ��������Ϣ
	 * 
	 * @param messageList
	 * @return
	 * @throws Exception
	 */
	public static EnbAppMessage mergeMessage(EnbTransportMessage[] messageList)
			throws Exception {

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		for (EnbTransportMessage transMsg : messageList) {
			try {
				byteStream.write(transMsg.getPayload());
			} catch (IOException e) {
				throw new BizException("merge message failed.");
			}
		}
		// ����Ӧ�ò���Ϣ
		return parseAppMessage(byteStream.toByteArray());
	}

	/**
	 * ��Ӧ�ò��ֽ��������н�����ת��ΪӦ�ò���Ϣģ��
	 * @param msgBytes
	 * @return
	 * @throws Exception
	 */
	private static EnbAppMessage parseAppMessage(byte[] msgBytes)
			throws Exception {
		EnbAppMessage appMessage = new EnbAppMessage();
		int offset = 0;
		appMessage.setEnbId(ByteUtils.toLong(msgBytes, offset, 8));
		offset += 8;
		appMessage.setTransactionId(ByteUtils.toInt(msgBytes, offset, 4));
		offset += 4;
		appMessage.setMa(ByteUtils.toInt(msgBytes, offset, 2));
		offset += 2;
		appMessage.setMoc(ByteUtils.toInt(msgBytes, offset, 2));
		offset += 2;
		appMessage.setActionType(ByteUtils.toInt(msgBytes, offset, 1));
		offset += 1;
		appMessage.setMessageType(ByteUtils.toInt(msgBytes, offset, 1));
		offset += 1;

		ByteBuffer buffer = ByteBuffer.wrap(msgBytes, offset, msgBytes.length
				- offset);

		// ������Ϣ��
		TlvFieldUtil util = new TlvFieldUtil(EnbModule.getInstance()
				.getTagCollection());
		Map<Integer, List<Object>> fieldMap = util.parse(buffer);
		for (Integer tagId : fieldMap.keySet()) {
			appMessage.addTagListValue(tagId, fieldMap.get(tagId));
		}
		return appMessage;
	}

	public static boolean receiveAllSubPackets(EnbTransportMessage[] messageList) {
		// �����Ϣ�б�Ϊ�գ��ж�Ϊδ����
		if (messageList == null)
			return false;
		if (messageList.length == 1)
			return true;
		for (int i = 0; i < messageList.length; i++) {
			if (messageList[i] == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ����Ӧ����
	 * 
	 * @param resp
	 */
	public static void parseResponse(EnbAppMessage resp) throws Exception {
		if (resp == null) {
			throw new Exception("[Unknown error.]");
		}
		if (!resp.isSuccessful()) {
			String errMsg = resp.getStringValue(TagConst.ERR_MSG);
			// throw new Exception("[eNB��Ӧ������Ϣ]:" + errMsg);
			throw new Exception(OmpAppContext.getMessage("enb_return_err_msg")
					+ ":" + errMsg);
		}
	}

}
