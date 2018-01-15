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
		// ��ģ��ת��Ϊ��Ԫ��Ϣ
		McBtsMessage request = convertModelToRequest(moId, data,
				McBtsConstants.OPERATION_CONFIG);

		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage response = connector.syncInvoke(request);
			McBtsBizProxyHelper.parseResult(response); // �������
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
	}

	@Override
	public WrappedRepeater query(Long moId, GenericBizData bizData)
			throws Exception {
		// ��ģ��ת��Ϊ��Ԫ��Ϣ
		McBtsMessage request = convertModelToQueryRequest(moId, bizData);
		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage response = connector.syncInvoke(request);
			// �������
			McBtsBizProxyHelper.parseResult(response);
			// ����Ԫ��Ϣת��Ϊģ��
			WrappedRepeater result = convertQueryResponseToModel(response, moId);
			return result;
		} catch (TimeoutException e) {
			log.error("���վ��ѯ\"" + bizData.getBizName() + "\"ʱ������ʱ����", e);
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time")
							+ e.getLocalizedMessage());
		} catch (UnsupportedOperationException e) {
			throw e;
		} catch (Exception e) {
			log.error("��ѯ" + bizData.getBizName() + "ʱ������ѯ����", e);
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
		// ��ȡԪ����
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
				.getHeader().getItem();
		// �����Ϣͷ
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
		// ������Ϣ��
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
		McBtsMessage message = new McBtsMessage(); // ������Ϣ
		String bizName = bizData.getBizName();
		// ��ȡЭ������
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
					// 16����
					message.setMoc(Integer.parseInt(itemValue.substring(2), 16));
				} else {
					message.setMoc(Integer.parseInt(itemValue));
				}
			} else if (itemName.equals(McBtsConstants.PROTOCOL_ACTION_TYPE)) {
				message.setActionType(Integer.parseInt(itemValue));
			}
			// ���û�վID
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
		// �����Ϣ�е�Ƶ����Ŀ�ֶ�
		ByteUtils.putNumber(buf, offset,
				String.valueOf(mcBtsRepeaterList.size()), 2);
		offset += 2;
		for (McBtsProtocolBodyItemMeta item : bodyItems) {
			String name = item.getName();
			String type = item.getType();
			int length = Integer.parseInt(item.getLength());
			// �����Ϣ�е�Ƶ��
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

	// ����moId��ȡbtsId
	private static Long getBtsIdByMoId(Long moId) {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		return mcBts.getBtsId();
	}
}
