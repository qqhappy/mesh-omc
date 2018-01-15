/**
 * 
 */
package com.xinwei.minas.server.mcbts.proxy.layer2.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer2.TConfFreqSet;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer2.FreqSetProxy;
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
public class FreqSetProxyImpl implements FreqSetProxy {

	private McBtsConnector connector;

	public FreqSetProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	/**
	 * 向基站配置频点集
	 * 
	 */
	public void config(Long moId, GenericBizData genericBizData)
			throws Exception {
		// 将模型转换为网元消息
		McBtsMessage request = convertModelToRequest(moId, genericBizData,
				McBtsConstants.OPERATION_CONFIG);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			// 解析结果
			McBtsBizProxyHelper.parseResult(response);
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
	}

	/**
	 * 向基站查询频点集
	 */
	public TConfFreqSet query(Long moId, TConfFreqSet freqSetEntity)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	private McBtsMessage convertModelToRequest(Long moId,
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
			this.fillBody(message, record, bodyItems);
		}
		return message;
	}

	/**
	 * 填充消息头
	 * 
	 * @param message
	 * @param record
	 * @param headerItems
	 */
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

	/**
	 * 填充消息体
	 * 
	 * @param message
	 * @param record
	 * @param bodyItems
	 */
	private void fillBody(McBtsMessage message, GenericBizRecord record,
			McBtsProtocolBodyItemMeta[] bodyItems) {
		byte[] buf = new byte[4096];
		int offset = 0;
		for (McBtsProtocolBodyItemMeta item : bodyItems) {
			String name = item.getName();
			String type = item.getType();
			int length = Integer.parseInt(item.getLength());
			GenericBizProperty property = record.getPropertyValue(name);
			// 填充消息中的频点个数
			if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)
					|| type.equals(McBtsConstants.TYPE_SIGNED_NUMBER)) {
				// 数值型（包括无符号和有符号数）
				ByteUtils.putNumber(buf, offset,
						property.getValue().toString(), length);
				offset += length;
			}
			// 填充消息中的各个频点
			else if (type.equals(McBtsConstants.TYPE_LIST)) {

				List freqList = (List) property.getValue();
				for (int i = 0; i < freqList.size(); i++) {
					// 数值型（包括无符号和有符号数）
					ByteUtils.putNumber(buf, offset,
							freqList.get(i).toString(), length);
					offset += length;
				}

			}
		}
		message.setContent(buf, 0, offset);
	}

	/**
	 * 根据MO ID获取基站ID
	 * 
	 * @param moId
	 * @return
	 */
	private Long getBtsIdByMoId(Long moId) {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		return mcBts.getBtsId();
	}

}
