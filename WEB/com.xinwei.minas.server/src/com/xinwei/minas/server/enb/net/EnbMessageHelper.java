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
 * eNB基站消息处理助手
 * 
 * @author fanhaoyu
 * 
 */

public class EnbMessageHelper {

	/**
	 * 根据字节流解析传输层消息
	 * 
	 * @param buf
	 * @return
	 */
	public static EnbTransportMessage parse(byte[] buf) {
		EnbTransportMessage message = new EnbTransportMessage();
		int offset = 0;
		// 版本：2个字节
		message.setVersion(ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// 应用传输层字节长度：2个字节
		message.setTransportLen(ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		// 应用报文层字节总长度：4个字节
		int appLength = ByteUtils.toInt(buf, offset, 4);
		message.setTotalAppLen(appLength);
		offset += 4;
		// 消息标识：4个字节，用于唯一标示一条应用层消息，传输层拆包组包使用
		message.setMessageId(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		// 总包数：4个字节
		message.setTotalPacketNum(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		// 包序号：4个字节，从0开始计数
		message.setPacketNo(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		byte[] payload = new byte[appLength];
		System.arraycopy(buf, offset, payload, 0, appLength);
		message.setPayload(payload);

		return message;
	}

	/**
	 * 将传输层消息编码成字节流
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
	 * 将应用层消息编码成字节流
	 * 
	 * @param message
	 * @return
	 * @throws BizException
	 */
	public static byte[] encode(EnbAppMessage message) throws Exception {
		TlvFieldUtil util = new TlvFieldUtil(EnbModule.getInstance()
				.getTagCollection());
		// 将TLV部分编码
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
	 * 拆分消息
	 * 
	 * @param message
	 * @return
	 * @throws BizException
	 */
	public static List<EnbTransportMessage> splitMessage(EnbAppMessage message)
			throws Exception {
		List<EnbTransportMessage> transMsgList = new ArrayList<EnbTransportMessage>();
		// 将应用层消息编码
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
			// 如果是最后一个包
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
	 * 合并传输层消息
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
		// 解析应用层消息
		return parseAppMessage(byteStream.toByteArray());
	}

	/**
	 * 对应用层字节码流进行解析，转换为应用层消息模型
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

		// 解析消息体
		TlvFieldUtil util = new TlvFieldUtil(EnbModule.getInstance()
				.getTagCollection());
		Map<Integer, List<Object>> fieldMap = util.parse(buffer);
		for (Integer tagId : fieldMap.keySet()) {
			appMessage.addTagListValue(tagId, fieldMap.get(tagId));
		}
		return appMessage;
	}

	public static boolean receiveAllSubPackets(EnbTransportMessage[] messageList) {
		// 如果消息列表为空，判定为未收齐
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
	 * 解析应答结果
	 * 
	 * @param resp
	 */
	public static void parseResponse(EnbAppMessage resp) throws Exception {
		if (resp == null) {
			throw new Exception("[Unknown error.]");
		}
		if (!resp.isSuccessful()) {
			String errMsg = resp.getStringValue(TagConst.ERR_MSG);
			// throw new Exception("[eNB回应错误信息]:" + errMsg);
			throw new Exception(OmpAppContext.getMessage("enb_return_err_msg")
					+ ":" + errMsg);
		}
	}

}
