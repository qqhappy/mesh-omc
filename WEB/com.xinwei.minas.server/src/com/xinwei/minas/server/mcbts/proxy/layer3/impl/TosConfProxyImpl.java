/**
 * 
 */
package com.xinwei.minas.server.mcbts.proxy.layer3.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsTos;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer3.TosConfProxy;
import com.xinwei.minas.server.mcbts.service.McBtsBasicService;
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
public class TosConfProxyImpl implements TosConfProxy {

	private static final Log log = LogFactory.getLog(TosConfProxyImpl.class);

	private McBtsConnector connector;

	private McBtsBasicService mcBtsBasicService;

	public TosConfProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	@Override
	public void config(Long moId, GenericBizData bizData) throws Exception {
		McBtsMessage request = null;
		try {
			// 将模型转换为网元消息
			request = convertModelToRequest(moId, bizData,
					McBtsConstants.OPERATION_CONFIG);
		} catch (Exception e) {
			log.error("配置转换消息的时候出错" + bizData.getBizName(), e);
			throw new Exception(e);
		}
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			// 解析结果
			McBtsBizProxyHelper.parseResult(response);
		} catch (TimeoutException e) {
			log.error("基站配置时发生超时错误,业务:" + bizData.getBizName(), e);
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		} catch (UnsupportedOperationException e) {
			throw new UnsupportedOperationException(
					OmpAppContext.getMessage("unsupported_biz_operation")
							+ bizData.getBizName());
		} catch (Exception e) {
			log.error("基站配置时发生配置错误,业务:" + bizData.getBizName(), e);
			throw new Exception(OmpAppContext.getMessage("mcbts_config_error")
					+ e.getLocalizedMessage());
		}

	}

	/**
	 * 将业务模型转换为网元消息
	 * 
	 * @param moId
	 * @param bizData
	 * @param operation
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequest(Long moId,
			GenericBizData bizData, String operation) throws Exception {
		// 对每个基站构造一条消息
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
	private void fillHeader(McBtsMessage message, long moId,
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

	@SuppressWarnings("unchecked")
	private void fillBody(McBtsMessage message, GenericBizRecord record,
			McBtsProtocolBodyItemMeta[] bodyItems) {
		byte[] buf = new byte[4096];
		int offset = 0;
		for (McBtsProtocolBodyItemMeta item : bodyItems) {
			String name = item.getName();
			String type = item.getType();
			int length = Integer.parseInt(item.getLength());
			GenericBizProperty property = record.getPropertyValue(name);
			// 填充消息中的各个服务等级值
			if (type.equals(McBtsConstants.TYPE_LIST)) {
				List<McBtsTos> mcBtsTosList = (List<McBtsTos>) property
						.getValue();
				for (int i = 0; i < mcBtsTosList.size(); i++) {
					// 数值型（包括无符号和有符号数）
					ByteUtils.putNumber(buf, offset, mcBtsTosList.get(i)
							.getServiceLevel() + "", length);
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

	public McBtsBasicService getMcBtsBasicService() {
		return mcBtsBasicService;
	}

	public void setMcBtsBasicService(McBtsBasicService mcBtsBasicService) {
		this.mcBtsBasicService = mcBtsBasicService;
	}
}
