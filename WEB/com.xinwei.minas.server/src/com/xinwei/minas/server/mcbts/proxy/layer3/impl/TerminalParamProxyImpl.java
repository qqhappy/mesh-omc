/**
 * 
 */
package com.xinwei.minas.server.mcbts.proxy.layer3.impl;

import java.text.MessageFormat;
import java.util.List;

import org.apache.log4j.Logger;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TerminalBizParam;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.layer3.TerminalParamProxy;
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
public class TerminalParamProxyImpl implements TerminalParamProxy {

	private static final Logger logger = Logger
			.getLogger(TerminalParamProxyImpl.class);

	private McBtsConnector connector;

	private McBtsBasicService mcBtsBasicService;

	public TerminalParamProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	public void config(GenericBizData bizData) throws Exception {
		// ��ģ��ת��Ϊ��Ԫ��Ϣ
		McBtsMessage[] requestArr = convertModelToRequest(bizData,
				McBtsConstants.OPERATION_CONFIG);
		// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
		for (int i = 0; i < requestArr.length; i++) {
			try {
				connector.asyncInvoke(requestArr[i]);
			} catch (Exception e) {
				logger.error(OmpAppContext.getMessage("async_send_failed"), e);
			}
		}
	}

	/**
	 * ��ҵ��ģ��ת��Ϊ��Ԫ��Ϣ
	 * 
	 * @param bizData
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage[] convertModelToRequest(GenericBizData bizData,
			String operation) throws Exception {
		// ��ȡȫ����վ
		List<McBts> mcBtsList = mcBtsBasicService.queryAll();
		String bizName = bizData.getBizName();
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
		McBtsMessage[] messageArr = null;
		List<GenericBizRecord> records = bizData.getRecords();
		if (!records.isEmpty()) {
			GenericBizRecord record = records.get(0);
			// ��GenericBizRecord��ȡ��Ϣ��
			GenericBizProperty property = record
					.getPropertyValue("terminalBizParamList");
			List<TerminalBizParam> terminalBizParamList = (List<TerminalBizParam>) property
					.getValue();

			int mcBtsNum = mcBtsList.size();
			int paramNum = terminalBizParamList.size();
			// ��ÿ����վ���� mcBtsNum * paramNum ����Ϣ
			messageArr = new McBtsMessage[mcBtsNum * paramNum];
			for (int i = 0; i < messageArr.length; i++) {
				messageArr[i] = new McBtsMessage();
			}
			int index = 0;
			for (TerminalBizParam entity : terminalBizParamList) {
				for (int i = 0; i < mcBtsNum; i++) {
					// �����Ϣͷ
					this.fillHeader(messageArr[index], mcBtsList.get(i)
							.getMoId(), headerItems);
					// �����Ϣ��
					this.fillBody(messageArr[index], entity, bodyItems);
					index++;
				}
			}
		}
		return messageArr;
	}

	/**
	 * �����Ϣͷ
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
					// 16����
					message.setMoc(Integer.parseInt(itemValue.substring(2), 16));
				} else {
					message.setMoc(Integer.parseInt(itemValue));
				}
			} else if (itemName.equals(McBtsConstants.PROTOCOL_ACTION_TYPE)) {
				message.setActionType(Integer.parseInt(itemValue));
			}
			// ���û�վID
			Long btsId = this.getBtsIdByMoId(moId);
			message.setBtsId(btsId);
		}
	}

	// �����Ϣ��
	private void fillBody(McBtsMessage message, GenericBizRecord record,
			McBtsProtocolBodyItemMeta[] bodyItems) throws Exception {
		byte[] buf = new byte[4096];
		int offset = 0;
		// ��GenericBizRecord��ȡ��Ϣ��
		GenericBizProperty property = record
				.getPropertyValue("terminalBizParamList");
		List<TerminalBizParam> terminalBizParamList = (List<TerminalBizParam>) property
				.getValue();
		// ɸѡ���ʼ�¼
		TerminalBizParam entity = null;
		for (TerminalBizParam o : terminalBizParamList) {
			if (o.getIdx().intValue() == 0) {
				entity = o;
			}
		}
		if (entity == null) {
			throw new Exception(OmpAppContext.getMessage("no_record_with_id=0"));
		}
		for (McBtsProtocolBodyItemMeta item : bodyItems) {
			String name = item.getName();
			String type = item.getType();
			int length = Integer.parseInt(item.getLength());
			// Object propertyValue = entity.getPropertyValueByName(name);
			// if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)
			// || type.equals(McBtsConstants.TYPE_SIGNED_NUMBER)) {
			// // ��ֵ�ͣ������޷��ź��з�������
			// ByteUtils.putNumber(buf, offset, propertyValue.toString(),
			// length);
			// }
			// �����ֶ����0
			if (type.equals(McBtsConstants.TYPE_RESERVE)) {
				ByteUtils.putNumber(buf, offset, "0", length);
			}
			offset += length;
		}
		message.setContent(buf, 0, offset);
	}

	// �����Ϣ��
	private void fillBody(McBtsMessage message, TerminalBizParam entity,
			McBtsProtocolBodyItemMeta[] bodyItems) throws Exception {
		byte[] buf = new byte[4096];
		int offset = 0;
		for (McBtsProtocolBodyItemMeta item : bodyItems) {
			String name = item.getName();
			String type = item.getType();
			int length = Integer.parseInt(item.getLength());
			// Object propertyValue = entity.getPropertyValueByName(name);
			// if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)
			// || type.equals(McBtsConstants.TYPE_SIGNED_NUMBER)) {
			// // ��ֵ�ͣ������޷��ź��з�������
			// ByteUtils.putNumber(buf, offset, propertyValue.toString(),
			// length);
			// }
			// �����ֶ����0
			if (type.equals(McBtsConstants.TYPE_RESERVE)) {
				ByteUtils.putNumber(buf, offset, "0", length);
			}
			offset += length;
		}
		message.setContent(buf, 0, offset);
	}

	// ����moId��ȡbtsId
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
