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
 * McBts业务协议适配器实现
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
	 * 查询网元业务数据
	 * 
	 * @param genericBizData
	 *            网元业务数据
	 * @return 记录集
	 * @throws Exception
	 */
	public GenericBizData query(Long moId, GenericBizData genericBizData)
			throws Exception {
		// 将模型转换为网元消息
		McBtsMessage request = McBtsBizProxyHelper.convertModelToRequest(moId,
				genericBizData, McBtsConstants.OPERATION_QUERY);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			// 解析结果
			McBtsBizProxyHelper.parseResult(response);
			// 将网元消息转换为模型
			GenericBizData result = McBtsBizProxyHelper.convertResponseToModel(
					genericBizData.getBizName(), response,
					McBtsConstants.OPERATION_QUERY);
			return result;
		} catch (TimeoutException e) {
			log.error("向基站查询\"" + genericBizData.getBizName() + "\"时发生超时错误", e);
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time")
							+ e.getLocalizedMessage());
		} catch (UnsupportedOperationException e) {
			log.error(OmpAppContext.getMessage("unsupported_biz_operation")
					+ ":" + genericBizData.getBizName());
			throw new UnsupportedOperationException(
					OmpAppContext.getMessage("unsupported_biz_operation"));
		} catch (Exception e) {
			log.error("查询" + genericBizData.getBizName() + "时发生查询出错", e);
			throw new Exception(OmpAppContext.getMessage("mcbts_query_error")
					+ e.getLocalizedMessage());
		}
	}

	/**
	 * 配置网元业务数据
	 * 
	 * @param genericBizData
	 *            网元业务数据
	 * @throws Exception
	 */
	public void config(Long moId, GenericBizData genericBizData)
			throws Exception {

		McBtsMessage request = null;
		try {
			// 将模型转换为网元消息
			request = McBtsBizProxyHelper.convertModelToRequest(moId,
					genericBizData, McBtsConstants.OPERATION_CONFIG);
			if (genericBizData.getTransactionId() != 0) {
				request.setTransactionId(genericBizData.getTransactionId());
			}
		} catch (Exception e) {
			log.error("配置转换消息的时候出错" + genericBizData.getBizName(), e);
			throw new Exception(e);
		}
		
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			// 解析结果
			McBtsBizProxyHelper.parseResult(response);
		} catch (TimeoutException e) {
			log.error("基站配置时发生超时错误,业务:" + genericBizData.getBizName(), e);
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		} catch (UnsupportedOperationException e) {
			throw new UnsupportedOperationException(
					OmpAppContext.getMessage("unsupported_biz_operation")
							+ McBtsBizProxyHelper.getProtocolDesc(
									genericBizData,
									McBtsConstants.OPERATION_CONFIG));
		} catch (Exception e) {
			log.error("基站配置时发生配置错误,业务:" + genericBizData.getBizName(), e);
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
	// * 根据网元业务数据
	// *
	// * @param moBizData
	// * 网元业务数据
	// * @return 记录集
	// * @throws Exception
	// */
	// public MoBizData query(MoBizData moBizData) throws Exception {
	// // 校验输入
	// if (moBizData.getRecords().isEmpty()) {
	// throw new Exception("查询请求数据有误.");
	// }
	// String bizName = moBizData.getBizName();
	// // 将模型转换为网元消息
	// McBtsMessage request = convertModelToRequest(moBizData);
	// // 调低底层通信层发送消息, 同步等待应答
	// McBtsMessage response = connector.syncInvoke(request);
	// // 解析结果
	// parseResult(response);
	// // 将网元消息转换为模型
	// MoBizData result = this.convertResponseToModel(bizName, response);
	// return result;
	// }

	// /**
	// * 配置网元业务数据
	// *
	// * @param moBizData
	// * 网元业务数据
	// * @throws Exception
	// */
	// public void config(MoBizData moBizData) throws Exception {
	// // 校验输入
	// if (moBizData.getRecords().isEmpty()) {
	// throw new Exception("配置请求数据有误.");
	// }
	// // 将模型转换为网元消息
	// McBtsMessage request = convertModelToRequest(moBizData);
	// try {
	// // 调低底层通信层发送消息, 同步等待应答
	// McBtsMessage response = connector.syncInvoke(request);
	// // 解析结果
	// parseResult(response);
	// } catch (TimeoutException e) {
	// throw new Exception("基站应答超时.");
	// }
	// }

	// /**
	// * 将业务模型转换为网元消息
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
	// String msg = "未知的业务操作 [{0}/{1}]\n请检查配置文件是否正确.";
	// msg = MessageFormat.format(msg, bizName, operation);
	// throw new Exception(msg);
	// }
	// MoBizRecord record = moBizData.getRecords().get(0);
	// McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
	// .getHeader().getItem();
	// McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getRequest()
	// .getBody().getItem();
	// // 填充消息头
	// this.fillHeader(message, record, headerItems);
	// // 填充消息体
	// this.fillBody(message, record, bodyItems);
	// return message;
	// }
	//
	//
	//
	// /**
	// * 填充消息头
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
	// // 16进制
	// message.setMoc(Integer.parseInt(itemValue.substring(2), 16));
	// } else {
	// message.setMoc(Integer.parseInt(itemValue));
	// }
	// } else if (itemName.equals(McBtsConstants.PROTOCOL_ACTION_TYPE)) {
	// message.setActionType(Integer.parseInt(itemValue));
	// }
	// }
	// // 设置基站ID
	// Long moId = record.getMoId();
	// Long btsId = this.getBtsIdByMoId(moId);
	// message.setBtsId(btsId);
	// }
	//
	// /**
	// * 填充消息体
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
	// // 数值型（包括无符号和有符号数）
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
	// * 将网元消息转换为业务模型
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
	// String msg = "未知的业务操作 [{0}/{1}]\n请检查配置文件是否正确.";
	// msg = MessageFormat.format(msg, bizName, operation);
	// throw new Exception(msg);
	// }
	// McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
	// .getHeader().getItem();
	// McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getRequest()
	// .getBody().getItem();
	// MoBizRecord record = new MoBizRecord();
	// // 解析消息体
	// this.parseBody(record, buf, bodyItems);
	// bizData.addRecord(record);
	// return bizData;
	// }
	//
	//
	// /**
	// * 分析消息体
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
	// // 无符号数
	// value = String.valueOf(ByteUtils.toUnsignedNumber(buf, offset, length));
	// }
	// else if (type.equals(McBtsConstants.TYPE_SIGNED_NUMBER)) {
	// // 有符号数
	// value = String.valueOf(ByteUtils.toSignedNumber(buf, offset, length));
	// }
	// if (!type.equals(McBtsConstants.TYPE_RESERVE)) {
	// record.addItemValue(name, value);
	// }
	// offset += length;
	// }
	// }

}
