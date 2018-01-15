/**
 * 
 */
package com.xinwei.minas.server.mcbts.proxy.layer3.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlan;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlanAttach;
import com.xinwei.minas.mcbts.core.model.layer3.WrappedVlan;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer3.QinQProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.GenericBizRecord;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * @author chenshaohua
 * 
 */
public class QinQProxyImpl implements QinQProxy {

	private McBtsConnector connector;

	public QinQProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	public void config(Long moId, GenericBizData data, McBtsVlanAttach attach)
			throws Exception {
		McBtsMessage request = convertModelToRequest(moId, data, attach,
				McBtsConstants.OPERATION_CONFIG);
		try {
			McBtsMessage response = connector.syncInvoke(request);
			McBtsBizProxyHelper.parseResult(response); // 解析结果
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		} catch (Exception e) {
			throw new Exception(OmpAppContext.getMessage("mcbts_config_error")
					+ e.getLocalizedMessage());
		}
	}

	@Override
	public WrappedVlan query(Long moId, WrappedVlan wrappedVlan,
			GenericBizData data) throws Exception {
		// 将模型转换为网元消息
		McBtsMessage request = convertModelToRequest(moId, data, null,
				McBtsConstants.OPERATION_QUERY);

		try {
			McBtsMessage response = connector.syncInvoke(request);
			McBtsBizProxyHelper.parseResult(response); // 解析结果

			convertResponseToModel(wrappedVlan, data, response,
					McBtsConstants.OPERATION_QUERY);

			return wrappedVlan;
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		} catch (Exception e) {
			throw new Exception(OmpAppContext.getMessage("mcbts_config_error")
					+ e.getLocalizedMessage());
		}
	}

	private WrappedVlan convertResponseToModel(WrappedVlan wrappedVlan,
			GenericBizData data, McBtsMessage response, String operation)
			throws Exception {
		byte[] buf = response.getContent();
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas()
				.getProtocolMetaBy(data.getBizName(), operation);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, data.getBizName(), operation);
			throw new Exception(msg);
		}
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getResponse()
				.getBody().getItem();

		// 解析消息体
		parseBody(wrappedVlan, data, buf, bodyItems);
		return wrappedVlan;
	}

	private void parseBody(WrappedVlan wrappedVlan, GenericBizData data,
			byte[] buf, McBtsProtocolBodyItemMeta[] bodyItems) throws Exception {

		List<McBtsVlan> vlanList = wrappedVlan.getMcBtsVlanList();
		if (vlanList == null || bodyItems == null)
			return;

		McBtsVlanAttach vlanAttach = new McBtsVlanAttach();

		int offset = 0;

		// eType
		String eType = ByteUtils.toHexString(buf, offset, 2);
		offset += 2;
		vlanAttach.setEtype(eType);

		int totalNum = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
		if (totalNum > 0)
			vlanAttach.setEnableQinq(1);
		else
			vlanAttach.setEnableQinq(0);

		for (int i = 0; i < totalNum; i++) {
			int vlanGroup = -1;
			for (McBtsProtocolBodyItemMeta item : bodyItems) {
				String name = item.getName();
				int length = Integer.parseInt(item.getLength());

				Object value = ByteUtils.toUnsignedNumber(buf, offset, length);
				offset += length;

				if (name.equalsIgnoreCase("vlanGroup")) {
					vlanGroup = Integer.parseInt(String.valueOf(value));
				}

				if (name.equalsIgnoreCase("qinqTag")) {
					for (McBtsVlan vlan : vlanList) {
						if (vlan.getVlanGroup().intValue() == vlanGroup)
							vlan.setVlanID(Integer.valueOf(String
									.valueOf(value)));
					}
				}
			}
		}
	}

	private McBtsMessage convertModelToRequest(Long moId,
			GenericBizData bizData, McBtsVlanAttach attach, String operation)
			throws Exception {
		McBtsMessage message = new McBtsMessage(); // 构造消息
		String bizName = bizData.getBizName();
		// 获取协议配置
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas().getProtocolMetaBy(bizName, operation);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, bizName, operation);
			throw new Exception(msg);
		}
		// 获取元数据
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
				.getHeader().getItem();
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getRequest()
				.getBody().getItem();
		// 填充消息头
		this.fillHeader(message, moId, headerItems);
		// 填充消息体
		List<GenericBizRecord> records = bizData.getRecords();
		if (!records.isEmpty()) {
			GenericBizRecord record = records.get(0);
			this.fillBody(message, record, attach, bodyItems);
		}

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
			// 设置基站ID
			Long btsId = this.getBtsIdByMoId(moId);
			message.setBtsId(btsId);
		}
	}

	private void fillBody(McBtsMessage message, GenericBizRecord record,
			McBtsVlanAttach attach, McBtsProtocolBodyItemMeta[] bodyItems) {
		byte[] buf = new byte[4096];
		int offset = 0;

		GenericBizProperty property = record.getPropertyValue("mcBtsVlanList");
		List<McBtsVlan> mcBtsVlanList = (List<McBtsVlan>) property.getValue();
		// 填充etype字段
		ByteUtils.putNumber(buf, offset,
				Integer.parseInt(attach.getEtype().substring(2), 16) + "", 2);
		offset += 2;
		// 填充消息中的数目字段
		ByteUtils.putNumber(buf, offset, mcBtsVlanList.size() + "", 2);
		// 偏移加2
		offset += 2;
		// for (McBtsProtocolBodyItemMeta item : bodyItems) {
		// String name = item.getName();
		// String type = item.getType();
		// int length = Integer.parseInt(item.getLength());
		// // 填充vlan group和qinq 字段
		// for (McBtsVlan o : mcBtsVlanList) {
		// Object propertyValue = o.getPropertyValueByName(name);
		// if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)
		// || type.equals(McBtsConstants.TYPE_SIGNED_NUMBER)) {
		// // 数值型（包括无符号和有符号数）
		// ByteUtils.putNumber(buf, offset, propertyValue.toString(),
		// length);
		// offset += length;
		// }
		// }
		// }
		// TODO: replace line 127-143 by line 145-160
		for (McBtsVlan o : mcBtsVlanList) {
			for (McBtsProtocolBodyItemMeta item : bodyItems) {
				String name = item.getName();
				String type = item.getType();
				int length = Integer.parseInt(item.getLength());
				// 填充vlan group和qinq 字段
				Object propertyValue = o.getPropertyValueByName(name);
				if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)
						|| type.equals(McBtsConstants.TYPE_SIGNED_NUMBER)) {
					// 数值型（包括无符号和有符号数）
					ByteUtils.putNumber(buf, offset, propertyValue.toString(),
							length);
					offset += length;
				}
			}
		}
		message.setContent(buf, 0, offset);
	}

	// 根据moId获取btsId
	private Long getBtsIdByMoId(Long moId) {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		return mcBts.getBtsId();
	}
}
