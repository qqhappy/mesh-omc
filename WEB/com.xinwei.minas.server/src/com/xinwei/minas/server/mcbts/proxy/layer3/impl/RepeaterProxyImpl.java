/**
 * 
 */
package com.xinwei.minas.server.mcbts.proxy.layer3.impl;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsACL;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsRepeater;
import com.xinwei.minas.mcbts.core.model.layer3.WrappedACL;
import com.xinwei.minas.mcbts.core.model.layer3.WrappedRepeater;
import com.xinwei.minas.mcbts.core.utils.FreqConvertUtil;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer3.RepeaterProxy;
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
public class RepeaterProxyImpl implements RepeaterProxy {

	private static Log log = LogFactory.getLog(RepeaterProxyImpl.class);

	private McBtsConnector connector;

	public RepeaterProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	public void config(Long moId, GenericBizData data) throws Exception {
		// 将模型转换为网元消息
		McBtsMessage request = convertModelToRequest(moId, data,
				McBtsConstants.OPERATION_CONFIG);

		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			McBtsBizProxyHelper.parseResult(response); // 解析结果
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
	}

	@Override
	public WrappedRepeater query(Long moId, GenericBizData bizData)
			throws Exception {
		// 将模型转换为网元消息
		McBtsMessage request = convertModelToQueryRequest(moId, bizData);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			// 解析结果
			McBtsBizProxyHelper.parseResult(response);
			// 将网元消息转换为模型
			WrappedRepeater result = convertQueryResponseToModel(response, moId);
			return result;
		} catch (TimeoutException e) {
			log.error("向基站查询\"" + bizData.getBizName() + "\"时发生超时错误", e);
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time")
							+ e.getLocalizedMessage());
		} catch (UnsupportedOperationException e) {
			throw e;
		} catch (Exception e) {
			log.error("查询" + bizData.getBizName() + "时发生查询出错", e);
			throw new Exception(OmpAppContext.getMessage("mcbts_query_error")
					+ e.getLocalizedMessage());
		}
	}

	private static McBtsMessage convertModelToQueryRequest(Long moId,
			GenericBizData genericBizData) throws Exception {
		McBtsMessage message = new McBtsMessage();
		String bizName = genericBizData.getBizName();
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas()
				.getProtocolMetaBy(bizName, McBtsConstants.OPERATION_QUERY);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, bizName,
					McBtsConstants.OPERATION_QUERY);
			throw new Exception(msg);
		}
		// 获取元数据
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
				.getHeader().getItem();
		// 填充消息头
		fillHeader(message, moId, headerItems);
		return message;
	}

	private WrappedRepeater convertQueryResponseToModel(McBtsMessage response,
			Long moId) throws Exception {
		GenericBizData bizData = new GenericBizData("mcbts_repeater");
		byte[] buf = response.getContent();

		McBtsProtocolMeta protocolMeta = McBtsModule
				.getInstance()
				.getProtocolMetas()
				.getProtocolMetaBy("mcbts_repeater",
						McBtsConstants.OPERATION_QUERY);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, "mcbts_repeater",
					McBtsConstants.OPERATION_QUERY);
			throw new Exception(msg);
		}
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getResponse()
				.getBody().getItem();
		// 解析消息体
		return parseBody(bizData, buf, bodyItems, moId);

	}

	private static WrappedRepeater parseBody(GenericBizData bizData,
			byte[] buf, McBtsProtocolBodyItemMeta[] bodyItems, Long moId) {

		FreqConvertUtil freqConvertUtil = new FreqConvertUtil();
		freqConvertUtil.setFreqType(McBtsCache.getInstance().queryByMoId(moId)
				.getBtsFreqType());

		int offset = 0;

		WrappedRepeater wrappedRepeater = new WrappedRepeater();

		int total = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
		wrappedRepeater.setTotalNum(total);

		List<McBtsRepeater> repeaterList = new ArrayList<McBtsRepeater>();
		wrappedRepeater.setMcBtsRepeaterList(repeaterList);

		for (int i = 0; i < total; i++) {
			McBtsRepeater repeater = new McBtsRepeater();

			for (McBtsProtocolBodyItemMeta item : bodyItems) {
				int length = Integer.parseInt(item.getLength());

				int value = ByteUtils.toInt(buf, offset, length);

				repeater.setOffset(value);
				
				repeater.setMiddleFreq(freqConvertUtil.getMidFreqValue(value));

				offset += length;
			}

			repeaterList.add(repeater);
		}

		return wrappedRepeater;
	}

	private McBtsMessage convertModelToRequest(Long moId,
			GenericBizData bizData, String operation) throws Exception {
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
		fillHeader(message, moId, headerItems);
		// 填充消息体
		List<GenericBizRecord> records = bizData.getRecords();
		if (!records.isEmpty()) {
			GenericBizRecord record = records.get(0);
			this.fillBody(message, record, bodyItems);
		}

		return message;
	}

	private static void fillHeader(McBtsMessage message, Long moId,
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
			Long btsId = getBtsIdByMoId(moId);
			message.setBtsId(btsId);
		}
	}

	private void fillBody(McBtsMessage message, GenericBizRecord record,
			McBtsProtocolBodyItemMeta[] bodyItems) {
		byte[] buf = new byte[4096];
		int offset = 0;
		GenericBizProperty property = record
				.getPropertyValue("mcBtsRepeaterList");
		List<McBtsRepeater> mcBtsRepeaterList = (List<McBtsRepeater>) property
				.getValue();

		if (mcBtsRepeaterList == null)
			mcBtsRepeaterList = new ArrayList<McBtsRepeater>();
		// 填充消息中的频点数目字段
		ByteUtils.putNumber(buf, offset,
				String.valueOf(mcBtsRepeaterList.size()), 2);
		offset += 2;
		for (McBtsProtocolBodyItemMeta item : bodyItems) {
			String name = item.getName();
			String type = item.getType();
			int length = Integer.parseInt(item.getLength());
			// 填充消息中的频点
			for (McBtsRepeater o : mcBtsRepeaterList) {
				String freqOffset = String.valueOf(o.getOffset());
				if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)) {
					ByteUtils.putNumber(buf, offset, freqOffset, length);
					offset += length;
				}
			}
		}
		message.setContent(buf, 0, offset);
	}

	// 根据moId获取btsId
	private static Long getBtsIdByMoId(Long moId) {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		return mcBts.getBtsId();
	}
}
