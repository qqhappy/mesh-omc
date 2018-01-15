package com.xinwei.minas.server.mcbts.proxy.layer3.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqModule;
import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqToBts;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer3.SysFreqProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.GenericBizRecord;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * ϵͳƵ���·�ʵ��
 * 
 * @author liuzhongyan
 * 
 */
public class SysFreqProxyImpl implements SysFreqProxy {

	private Log log = LogFactory.getLog(SysFreqProxyImpl.class);

	// ����Mocֵ
	private static final int CONFIG_MOC = 0x0780;

	// ��ѯMocֵ
	private static final int QUERY_MOC = 0x0782;

	private McBtsConnector connector;

	public SysFreqProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	public Object[] queryData(Long moId) throws Exception {
		GenericBizData genericBizData = new GenericBizData("t_conf_system_freq");
		// ��ģ��ת��Ϊ��ѯ��Ԫ��Ϣ
		McBtsMessage request = convertModelToRequest(moId, genericBizData,
				McBtsConstants.OPERATION_QUERY);

		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage response = connector.syncInvoke(request);
			McBtsBizProxyHelper.parseResult(response);
			GenericBizData data = convertResponseToModel("t_conf_system_freq",
					response, McBtsConstants.OPERATION_QUERY);
			List<GenericBizRecord> records = data.getRecords();
			if (!records.isEmpty()) {
				Object[] objs = new Object[2];

				GenericBizRecord record = records.get(0);
				GenericBizProperty property1 = record
						.getPropertyValue("freqSwitch");
				int freqSwitch = Integer.parseInt(String.valueOf(property1
						.getValue()));
				objs[0] = freqSwitch;

				GenericBizProperty property2 = record
						.getPropertyValue("sysMoList");
				objs[1] = (List<TSysFreqModule>) property2.getValue();

				return objs;

			}
		} catch (TimeoutException e) {
			log.error(e);
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time")
							+ e.getLocalizedMessage());
		} catch (UnsupportedOperationException e) {
			log.error(e);
			throw new Exception(
					OmpAppContext.getMessage("unsupported_biz_operation"));
		} catch (Exception e) {
			log.error(e);
			throw new Exception(OmpAppContext.getMessage("mcbts_query_error")
					+ e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * ���û�վƵ��
	 * 
	 */
	@Override
	public void configData(Long moId, GenericBizData bizData, int freqSwitch)
			throws Exception {
		// ��ģ��ת��Ϊ������Ԫ��Ϣ
		McBtsMessage request = null;
		try {
			request = convertModelToRequest(moId, bizData, freqSwitch,
					CONFIG_MOC, McBtsConstants.OPERATION_CONFIG);
		} catch (Exception e) {
			throw e;
		}
		// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
		McBtsMessage response;
		try {
			response = connector.syncInvoke(request);
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
		// �������
		McBtsBizProxyHelper.parseResult(response);
	}

	/**
	 * ����ѯҵ��ģ��ת��Ϊ��Ԫ��Ϣ
	 * 
	 * @param bizData
	 * @return
	 * @throws Exception
	 */
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
		// ��ȡԪ����
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
				.getHeader().getItem();
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getRequest()
				.getBody().getItem();
		// �����Ϣͷ
		this.fillHeader(message, moId, headerItems);
		return message;
	}

	/**
	 * �����Ϣͷ
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
					// 16����
					message.setMoc(Integer.parseInt(itemValue.substring(2), 16));
				} else {
					message.setMoc(Integer.parseInt(itemValue));
				}
			} else if (itemName.equals(McBtsConstants.PROTOCOL_ACTION_TYPE)) {
				message.setActionType(Integer.parseInt(itemValue));
			}
		}
		// ���û�վID
		Long btsId = this.getBtsIdByMoId(moId);
		message.setBtsId(btsId);
	}

	/**
	 * ������ģ��ת��Ϊ��Ԫ��Ϣ
	 * 
	 * @param moId
	 * @param weakVoiceFault
	 * @param operation
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequest(Long moId,
			GenericBizData bizData, int freqSwitch, int moc, String operation)
			throws Exception {
		McBtsMessage message = new McBtsMessage();
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
		// �����Ϣͷ
		this.fillHeader(message, moId, moc);
		// �����Ϣ��
		List<GenericBizRecord> records = bizData.getRecords();
		if (!records.isEmpty()) {
			GenericBizRecord record = records.get(0);
			this.fillBody(message, freqSwitch, record, bodyItems);
		}

		return message;
	}

	/**
	 * �����ظ���ϢΪģ��
	 * 
	 * @param bizName
	 * @param response
	 * @param operation
	 * @return
	 * @throws Exception
	 */
	private GenericBizData convertResponseToModel(String bizName,
			McBtsMessage response, String operation) throws Exception {

		GenericBizData bizData = new GenericBizData(bizName);
		byte[] buf = response.getContent();

		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas().getProtocolMetaBy(bizName, operation);

		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, bizName, operation);
			throw new Exception(msg);
		}

		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getResponse()
				.getHeader().getItem();
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getResponse()
				.getBody().getItem();
		// ������Ϣ��
		this.parseBody(bizData, buf, bodyItems);
		return bizData;
	}

	/**
	 * ������Ϣ��
	 * 
	 * @param record
	 * @param buf
	 * @param bodyItems
	 */
	private void parseBody(GenericBizData bizData, byte[] buf,
			McBtsProtocolBodyItemMeta[] bodyItems) {
		int offset = 0;
		if (bodyItems == null) {
			return;
		}
		List<TSysFreqModule> mcBtsTosList = new ArrayList<TSysFreqModule>();

		for (McBtsProtocolBodyItemMeta item : bodyItems) {
			String type = item.getType();
			int length = Integer.parseInt(item.getLength());

			if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)) {
				TSysFreqToBts entity = new TSysFreqToBts();

				// ȫ�ڵ�,����Ч�ڵ�
				int feqVolInd = ByteUtils.toInt(buf, offset, 1);
				entity.setFreqSwitch(feqVolInd);
				offset += 1;
				// freq length
				int freqLength = ByteUtils.toInt(buf, offset, 1);
				offset += 1;

				for (int i = 0; i < freqLength; i++) {
					long freq = ByteUtils.toSignedNumber(buf, offset, length);
					TSysFreqModule module = new TSysFreqModule();
					module.setFreq((int) freq);
					mcBtsTosList.add(module);
					offset += 2;
				}

				entity.setSysMoList(mcBtsTosList);
				bizData.addEntity(entity);
			}
		}
	}

	/**
	 * �����Ϣͷ
	 * 
	 * @param message
	 * @param moId
	 */
	private void fillHeader(McBtsMessage message, Long moId, int moc) {
		message.setMsgArea(1);
		message.setMa(McBtsMessage.MA_CONF);
		message.setMoc(moc);
		message.setActionType(McBtsMessage.ACTION_TYPE_CONFIG);
		// ���û�վID
		Long btsId = this.getBtsIdByMoId(moId);
		message.setBtsId(btsId);
	}

	/**
	 * �����Ϣ��
	 * 
	 * @param message
	 * @param weakVoiceFault
	 */
	private void fillBody(McBtsMessage message, int freqSwitch,
			GenericBizRecord record, McBtsProtocolBodyItemMeta[] bodyItems) {
		byte[] buf = new byte[4096];
		int offset = 0;

		GenericBizProperty property = record.getPropertyValue("sysMoList");
		List<TSysFreqModule> mcBtsTosList = (List<TSysFreqModule>) property
				.getValue();

		for (McBtsProtocolBodyItemMeta item : bodyItems) {
			String type = item.getType();
			int length = Integer.parseInt(item.getLength());

			if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)) {
				// FeqVolInd
				ByteUtils.putNumber(buf, offset, String.valueOf(freqSwitch), 1);
				offset += 1;
				// number
				ByteUtils.putNumber(buf, offset, mcBtsTosList.size() + "", 1);
				offset += 1;
				// freqList
				for (int i = 0; i < mcBtsTosList.size(); i++) {
					// ��ֵ�ͣ������޷��ź��з�������
					ByteUtils.putNumber(buf, offset, mcBtsTosList.get(i)
							.getFreq() + "", length);
					offset += length;
				}
			}
		}
		message.setContent(buf, 0, offset);

	}

	/**
	 * ����moId��ȡ��վ��Ϣ
	 * 
	 * @param moId
	 * @return
	 */
	private Long getBtsIdByMoId(Long moId) {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		return bts.getBtsId();
	}

}
