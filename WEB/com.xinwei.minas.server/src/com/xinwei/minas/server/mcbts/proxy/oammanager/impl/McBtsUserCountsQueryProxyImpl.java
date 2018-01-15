package com.xinwei.minas.server.mcbts.proxy.oammanager.impl;

import java.text.MessageFormat;
import java.util.concurrent.TimeoutException;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.McBtsUserCountsQuery;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.oammanager.McBtsUserCountsQueryProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;
/**
 * 
 * 基站序列号proxy实现
 * 
 * @author fangping
 * McBtsUserCountsQuery
 */

public class McBtsUserCountsQueryProxyImpl implements McBtsUserCountsQueryProxy {

	public final static String ERROR_QUERY_FAILED = "query failed";

	private McBtsConnector connector;

	public McBtsUserCountsQuery queryUserCountsQuery(Long moId) throws Exception,
			UnsupportedOperationException {
		McBtsMessage request = convertModelToRequest1(moId,
				McBtsConstants.OPERATION_QUERY);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			// 判断结构合不合法
			McBtsBizProxyHelper.parseResult(response);
			// 将网元消息转换为模型
			McBtsUserCountsQuery mcbtsStatus = convertResponseToModel("mcBtsUserCounts",
					response, McBtsConstants.OPERATION_QUERY);

			return mcbtsStatus;
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time")
							+ e.getLocalizedMessage());
		} catch (UnsupportedOperationException e) {
			throw new Exception(
					OmpAppContext.getMessage("unsupported_biz_operation"));
		} catch (Exception e) {
			throw new Exception(OmpAppContext.getMessage("mcbts_query_error")
					+ e.getLocalizedMessage());
		}
	}

	private McBtsUserCountsQuery convertResponseToModel(String bizName,
			McBtsMessage response, String operation) throws Exception {
		McBtsUserCountsQuery mcbtsStatus = new McBtsUserCountsQuery();
		byte[] buf = response.getContent();
 		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas().getProtocolMetaBy(bizName, operation);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, bizName, operation);
			throw new Exception(msg);
		}
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getResponse()
				.getBody().getItem();
		// 解析消息体
		parseBody(mcbtsStatus, buf, bodyItems);
		return mcbtsStatus;

	}

	private void parseBody(McBtsUserCountsQuery mcbtsStatus, byte[] buf,
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
				// 无符号数
				value = String.valueOf(ByteUtils.toUnsignedNumber(buf, offset,
						length));
			} else if (type.equalsIgnoreCase(McBtsConstants.TYPE_SIGNED_NUMBER)) {
				// 有符号数
				value = String.valueOf(ByteUtils.toSignedNumber(buf, offset,
						length));
			}
			offset += length;
			
			if (name.equals("registeredUserNum")) {
				mcbtsStatus.setRegisteredUserNum(Integer.parseInt(value
						.toString()));
			}
			if (name.equals("activeUserNum")) {
				mcbtsStatus.setActiveUserNum(Integer.parseInt(value
						.toString()));
			}
		}
	}

	private McBtsMessage convertModelToRequest1(Long moId, String operation)
			throws Exception {
		McBtsMessage message = new McBtsMessage();
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas().getProtocolMetaBy("mcBtsUserCounts", operation);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, operation);
			throw new Exception(msg);
		}
		// 获取元数据
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
				.getHeader().getItem();
		// 填充消息头
		this.fillHeader(message, moId, headerItems);
		return message;
	}

	private void fillHeader(McBtsMessage message, Long moId,
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
					// 16进制
					message.setMoc(Integer.parseInt(itemValue.substring(2), 16));
				} else {
					message.setMoc(Integer.parseInt(itemValue));
				}
			} else if (itemName.equals(McBtsConstants.PROTOCOL_ACTION_TYPE)) {
				message.setActionType(Integer.parseInt(itemValue));
			}
		}
		// 设置基站ID
		Long btsId = this.getBtsIdByMoId(moId);
		message.setBtsId(btsId);
	}

	private Long getBtsIdByMoId(Long moId) {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		return mcBts.getBtsId();
	}

	public McBtsConnector getConnector() {
		return connector;
	}

	public void setConnector(McBtsConnector connector) {
		this.connector = connector;
	}
}
