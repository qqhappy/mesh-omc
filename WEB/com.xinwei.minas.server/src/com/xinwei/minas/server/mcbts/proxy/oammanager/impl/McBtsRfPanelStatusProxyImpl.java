package com.xinwei.minas.server.mcbts.proxy.oammanager.impl;

import java.text.MessageFormat;
import java.util.concurrent.TimeoutException;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.McBtsRfPanelStatus;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.oammanager.McBtsRfPanelStatusProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * McBtsRfPanelStatusProxyImplproxy实现
 * 
 * @author fangping
 * 
 */

public class McBtsRfPanelStatusProxyImpl implements McBtsRfPanelStatusProxy {

	public final static String ERROR_QUERY_FAILED = "query failed";

	private McBtsConnector connector;

	@Override
	public McBtsRfPanelStatus queryRfPanelStatus(Long moId) throws Exception,
			UnsupportedOperationException {
		McBtsMessage request = convertModelToRequest1(moId,
				McBtsConstants.OPERATION_QUERY);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			// 判断结构合不合法
			McBtsBizProxyHelper.parseResult(response);
			// 将网元消息转换为模型//此处的first Param must be the same as config file
			// TO DO
			McBtsRfPanelStatus Status = convertResponseToModel(response,
					McBtsConstants.OPERATION_QUERY);

			return Status;
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

	private McBtsRfPanelStatus convertResponseToModel(McBtsMessage response,
			String operation) throws Exception {
		byte[] buf = response.getContent();
		McBtsRfPanelStatus mcbtsStatus = new McBtsRfPanelStatus(
				response.getBtsId(), buf);
		return mcbtsStatus;

	}

	// private McBtsRfPanelStatus convertResponseToModel(String bizName,
	// McBtsMessage response, String operation) throws Exception {
	// McBtsRfPanelStatus mcbtsStatus = new McBtsRfPanelStatus();
	// byte[] buf = response.getContent();
	// McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
	// .getProtocolMetas().getProtocolMetaBy(bizName, operation);
	// if (protocolMeta == null) {
	// String msg = OmpAppContext.getMessage("unknown_biz_operation");
	// msg = MessageFormat.format(msg, bizName, operation);
	// throw new Exception(msg);
	// }
	// McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getResponse()
	// .getBody().getItem();
	// // 解析从基站查询出来的数据对应的消息体
	// parseBody(mcbtsStatus, buf, bodyItems);
	// return mcbtsStatus;
	//
	// }

	// // parse 消息体
	// private void parseBody(McBtsRfPanelStatus mcbtsStatus, byte[] buf,
	// McBtsProtocolBodyItemMeta[] bodyItems) {
	// int offset = 0;
	// if (bodyItems == null) {
	// return;
	// }
	// for (McBtsProtocolBodyItemMeta item : bodyItems) {
	// String name = item.getName();
	// String type = item.getType();
	// int length = Integer.parseInt(item.getLength());
	// Object value = null;
	// if (type.equalsIgnoreCase(McBtsConstants.TYPE_UNSIGNED_NUMBER)) {
	// // 无符号数
	// value = String.valueOf(ByteUtils.toUnsignedNumber(buf, offset,
	// length));
	// } else if (type.equalsIgnoreCase(McBtsConstants.TYPE_SIGNED_NUMBER)) {
	// // 有符号数
	// value = String.valueOf(ByteUtils.toSignedNumber(buf, offset,
	// length));
	// }
	// offset += length;
	//
	// // 频综板状态4
	// // 频综板硬件版本号
	// if (name.equals("Synhardwareversion")) {
	// mcbtsStatus.setSynhardwareversion(Integer.parseInt(value
	// .toString()));
	// }
	// // 频综板软件版本号
	//
	// if (name.equals("Synsoftwareversion")) {
	// mcbtsStatus.setSynsoftwareversion(Integer.parseInt(value
	// .toString()));
	// }
	// // 晶振偏差
	// if (name.equals("TCXOoffset")) {
	// mcbtsStatus.setTCXOoffset(Integer.parseInt(value.toString()));
	// }
	// // 频综板接收出错次数
	// if (name.equals("SYNReceivingErrorCounter")) {
	// mcbtsStatus.setSYNReceivingErrorCounter(Integer.parseInt(value
	// .toString()));
	// }
	//
	// // 射频板状态8
	// // 硬件版本号
	//
	// if (name.equals("hardwareVersion")) {
	// mcbtsStatus.setHardwareVersion(Integer.parseInt(value
	// .toString()));
	// }
	// // 软件版本号
	//
	// if (name.equals("softwareVersion")) {
	// mcbtsStatus.setSoftwareVersion(Integer.parseInt(value
	// .toString()));
	// }
	// // 板电压值
	//
	// if (name.equals("BoardVoltageValue")) {
	// mcbtsStatus.setBoardVoltageValue(Integer.parseInt(value
	// .toString()));
	// }
	// // 板电流值
	//
	// if (name.equals("BoardCurrentValue")) {
	// mcbtsStatus.setBoardCurrentValue(Integer.parseInt(value
	// .toString()));
	// }
	// // 塔放电压值
	//
	// if (name.equals("TTAVoltageValue")) {
	// mcbtsStatus.setTTAVoltageValue(Integer.parseInt(value
	// .toString()));
	// }
	// // 塔放电流值
	//
	// if (name.equals("TTACurrentValue")) {
	// mcbtsStatus.setTTACurrentValue(Integer.parseInt(value
	// .toString()));
	// }
	// // 发送功率
	//
	// if (name.equals("TransmitPowerValue")) {
	// mcbtsStatus.setTransmitPowerValue(Integer.parseInt(value
	// .toString()));
	// }
	// // 天线接收出错次数
	// if (name.equals("ReceivingErrorCounter")) {
	// mcbtsStatus.setReceivingErrorCounter(Integer.parseInt(value
	// .toString()));
	// }
	// }
	// }

	private McBtsMessage convertModelToRequest1(Long moId, String operation)
			throws Exception {
		McBtsMessage message = new McBtsMessage();
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas()
				.getProtocolMetaBy("mcbts_RfPanelStatus", operation);
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
