package com.xinwei.minas.server.mcbts.proxy.layer3.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfBackupSag;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer3.SAGParamProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.GenericBizRecord;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * ����SAGҵ��Э��������ʵ��
 * 
 * @author yinbinqiang
 * 
 */
public class SAGParamProxyImpl implements SAGParamProxy {
	private  Log log = LogFactory.getLog(SAGParamProxyImpl.class);
	// ����Mocֵ
	private final int backup_sag_config_moc = 0x0733;
	// ��ѯMocֵ
	private final int backup_sag_query_moc = 0x0735;
	// ����Mocֵ
	private final int Jitterbuffer_config_moc = 0x0737;
	// ��ѯMocֵ
	private final int Jitterbuffer_query_moc = 0x0739;
	// ����Mocֵ
	private final int tos_config_moc = 0x073b;
	// ��ѯMocֵ
	private final int tos_query_moc = 0x073d;

	private McBtsConnector connector;

	public SAGParamProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	@Override
	public TConfBackupSag query(Long moId, TConfBackupSag backupSag)
			throws Exception {
		// ����SAG������Ϣ
		McBtsMessage backupSagRequest = convertModelToRequest(moId, null,
				getMcBtsProtocolHeaderItemMetas(backup_sag_query_moc), null,
				McBtsConstants.OPERATION_QUERY);
		// Jitterbuffer������Ϣ
		McBtsMessage jitterbufferRequest = convertModelToRequest(moId, null,
				getMcBtsProtocolHeaderItemMetas(Jitterbuffer_query_moc), null,
				McBtsConstants.OPERATION_QUERY);
		// SAG����Tos����������Ϣ
		McBtsMessage tosRequest = convertModelToRequest(moId, null,
				getMcBtsProtocolHeaderItemMetas(tos_query_moc), null,
				McBtsConstants.OPERATION_QUERY);
		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage backupResponse = connector
					.syncInvoke(backupSagRequest);
			// �������
			McBtsBizProxyHelper.parseResult(backupResponse);

			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage jitterbufferResponse = connector
					.syncInvoke(jitterbufferRequest);
			// �������
			McBtsBizProxyHelper.parseResult(jitterbufferResponse);

			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage tosResponse = connector.syncInvoke(tosRequest);
			// �������
			McBtsBizProxyHelper.parseResult(tosResponse);

			backupSag = convertResponsesToModel(backupResponse,
					jitterbufferResponse, tosResponse);
		} catch (Exception e) {
			log.error(e);
		}

		return backupSag;
	}

	@Override
	public void config(Long moId, GenericBizData bizData) throws Exception {
		// ����SAG������Ϣ
		McBtsMessage backupSagRequest = convertModelToRequest(moId, bizData,
				getMcBtsProtocolHeaderItemMetas(backup_sag_config_moc),
				getMcBtsProtocolBodyItemMetas("backupSag"),
				McBtsConstants.OPERATION_CONFIG);
		// Jitterbuffer������Ϣ
		McBtsMessage jitterbufferRequest = convertModelToRequest(moId, bizData,
				getMcBtsProtocolHeaderItemMetas(Jitterbuffer_config_moc),
				getMcBtsProtocolBodyItemMetas("jitterbuffer"),
				McBtsConstants.OPERATION_CONFIG);
		// SAG����Tos����������Ϣ
		McBtsMessage tosRequest = convertModelToRequest(moId, bizData,
				getMcBtsProtocolHeaderItemMetas(tos_config_moc),
				getMcBtsProtocolBodyItemMetas("tos"),
				McBtsConstants.OPERATION_CONFIG);
		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage backupResponse = connector
					.syncInvoke(backupSagRequest);
			// �������
			McBtsBizProxyHelper.parseResult(backupResponse);

			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage jitterbufferResponse = connector
					.syncInvoke(jitterbufferRequest);
			// �������
			McBtsBizProxyHelper.parseResult(jitterbufferResponse);

			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage tosResponse = connector.syncInvoke(tosRequest);
			// �������
			McBtsBizProxyHelper.parseResult(tosResponse);
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}

	}

	/**
	 * ��ģ��ת��Ϊ��Ԫ��Ϣ
	 * 
	 * @param moId
	 * @param weakVoiceFault
	 * @param operation
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequest(Long moId,
			GenericBizData bizData, McBtsProtocolHeaderItemMeta[] headerItems,
			McBtsProtocolBodyItemMeta[] bodyItems, String operation)
			throws Exception {
		McBtsMessage message = new McBtsMessage();
		// ����������Ϣ
		if (operation.equals(McBtsConstants.OPERATION_CONFIG)) {
			// �����Ϣͷ
			this.fillHeader(message, moId, headerItems);
			// �����Ϣ��
			List<GenericBizRecord> records = bizData.getRecords();
			if (!records.isEmpty()) {
				GenericBizRecord record = records.get(0);
				this.fillBody(message, record, bodyItems);
			}
		}
		// ������ѯ��Ϣ
		else if (operation.equals(McBtsConstants.OPERATION_QUERY)) {
			// �����Ϣͷ
			this.fillHeader(message, moId, headerItems);
		}
		return message;
	}

	private TConfBackupSag convertResponsesToModel(McBtsMessage backupResponse,
			McBtsMessage jitterbufferResponse, McBtsMessage tosResponse) {
		byte[] buf = new byte[4096];

		TConfBackupSag backupSag = new TConfBackupSag();
		int offset = 0;
		backupSag.setSAGID(ByteUtils.toUnsignedNumber(
				backupResponse.getContent(), offset, 4));
		offset += 4;
		backupSag.setsAGIPforVoice(ByteUtils.toUnsignedNumber(
				backupResponse.getContent(), offset, 4));
		offset += 4;
		backupSag.setsAGIPforsignal(ByteUtils.toUnsignedNumber(
				backupResponse.getContent(), offset, 4));
		offset += 4;
		backupSag.setSAGRxPortForVoice((int) ByteUtils.toUnsignedNumber(
				backupResponse.getContent(), offset, 2));
		offset += 2;
		backupSag.setSAGTxPortForVoice((int) ByteUtils.toUnsignedNumber(
				backupResponse.getContent(), offset, 2));
		offset += 2;
		backupSag.setSAGRxPortForSignal((int) ByteUtils.toUnsignedNumber(
				backupResponse.getContent(), offset, 2));
		offset += 2;
		backupSag.setSAGTxPortForSignal((int) ByteUtils.toUnsignedNumber(
				backupResponse.getContent(), offset, 2));
		offset += 2;
		backupSag.setLocationAreaID(ByteUtils.toSignedNumber(
				backupResponse.getContent(), offset, 4));
		offset += 4;
		backupSag.setSAGSignalPointCode((int) ByteUtils.toUnsignedNumber(
				backupResponse.getContent(), offset, 2));
		offset += 2;
		backupSag.setBTSSignalPointCode((int) ByteUtils.toUnsignedNumber(
				backupResponse.getContent(), offset, 2));
		offset += 2;
		offset += 8;
		backupSag.setNatAPKey((int) ByteUtils.toUnsignedNumber(
				backupResponse.getContent(), offset, 1));
		offset = 0;
		backupSag.setbSForceUseJitterbuffer((int) ByteUtils.toUnsignedNumber(
				jitterbufferResponse.getContent(), offset, 2));
		offset += 2;
		backupSag.setzModelUseJitterbuffer((int) ByteUtils.toUnsignedNumber(
				jitterbufferResponse.getContent(), offset, 2));
		offset += 2;
		backupSag.setJitterbufferSize((int) ByteUtils.toUnsignedNumber(
				jitterbufferResponse.getContent(), offset, 2));
		offset += 2;
		backupSag
				.setJitterbufferPackageThreshold((int) ByteUtils
						.toUnsignedNumber(jitterbufferResponse.getContent(),
								offset, 2));
		offset = 0;
		backupSag.setsAGVoiceTOS((int) ByteUtils.toUnsignedNumber(
				tosResponse.getContent(), offset, 1));
		return backupSag;
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
	 * �����Ϣ��
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
			if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)
					|| type.equals(McBtsConstants.TYPE_SIGNED_NUMBER)) {
				// ��ֵ�ͣ������޷��ź��з�������
				ByteUtils.putNumber(buf, offset,
						property.getValue().toString(), length);
			} else if (type.equals(McBtsConstants.TYPE_RESERVE)) {
				// �����ֶ�
				// do nothing
			} else if (type.equals(McBtsConstants.TYPE_IPv4)) {
				// IPv4
				ByteUtils.putIp(buf, offset, property.getValue().toString());
			} else if (type.equals(McBtsConstants.TYPE_STRING)) {
				// String
				String charsetName = McBtsConstants.CHARSET_US_ASCII;
				char fillChar = '\0';
				ByteUtils.putString(buf, offset,
						property.getValue().toString(), length, fillChar,
						charsetName);
			}
			offset += length;
		}
		message.setContent(buf, 0, offset);
	}

	/**
	 * ����MO ID��ȡ��վID
	 * 
	 * @param moId
	 * @return
	 */
	private Long getBtsIdByMoId(Long moId) {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		return mcBts.getBtsId();
	}

	/**
	 * ��ȡ��ϢͷЭ��ö��
	 * 
	 * @param moc
	 * @return
	 */
	private McBtsProtocolHeaderItemMeta[] getMcBtsProtocolHeaderItemMetas(
			int moc) {
		McBtsProtocolHeaderItemMeta[] items = new McBtsProtocolHeaderItemMeta[4];
		items[0] = new McBtsProtocolHeaderItemMeta();
		items[1] = new McBtsProtocolHeaderItemMeta();
		items[2] = new McBtsProtocolHeaderItemMeta();
		items[3] = new McBtsProtocolHeaderItemMeta();
		items[0].setName("MsgArea");
		items[1].setName("MA");
		items[2].setName("MOC");
		items[3].setName("ActionType");
		if (moc == backup_sag_config_moc) {
			items[0].setValue("1");
			items[1].setValue("0");
			items[2].setValue("" + backup_sag_config_moc);
			items[3].setValue("1");
		} else if (moc == Jitterbuffer_config_moc) {
			items[0].setValue("1");
			items[1].setValue("0");
			items[2].setValue("" + Jitterbuffer_config_moc);
			items[3].setValue("1");
		} else if (moc == tos_config_moc) {
			items[0].setValue("1");
			items[1].setValue("0");
			items[2].setValue("" + tos_config_moc);
			items[3].setValue("1");
		} else if (moc == backup_sag_query_moc) {
			items[0].setValue("1");
			items[1].setValue("0");
			items[2].setValue("" + backup_sag_query_moc);
			items[3].setValue("3");
		} else if (moc == Jitterbuffer_query_moc) {
			items[0].setValue("1");
			items[1].setValue("0");
			items[2].setValue("" + Jitterbuffer_query_moc);
			items[3].setValue("3");
		} else if (moc == tos_query_moc) {
			items[0].setValue("1");
			items[1].setValue("0");
			items[2].setValue("" + tos_query_moc);
			items[3].setValue("3");
		}
		return items;
	}

	/**
	 * ��ȡ��Ϣ��Э��ö��
	 * 
	 * @param type
	 * @return
	 */
	private McBtsProtocolBodyItemMeta[] getMcBtsProtocolBodyItemMetas(
			String type) {
		McBtsProtocolBodyItemMeta[] items = null;
		if (type.equals("backupSag")) {
			items = new McBtsProtocolBodyItemMeta[12];
			items[0] = new McBtsProtocolBodyItemMeta("sAGID", "UnsignedNumber",
					"4");
			items[1] = new McBtsProtocolBodyItemMeta("sAGIPforVoice",
					"UnsignedNumber", "4");
			items[2] = new McBtsProtocolBodyItemMeta("sAGIPforsignal",
					"UnsignedNumber", "4");
			items[3] = new McBtsProtocolBodyItemMeta("sAGRxPortForVoice",
					"UnsignedNumber", "2");
			items[4] = new McBtsProtocolBodyItemMeta("sAGTxPortForVoice",
					"UnsignedNumber", "2");
			items[5] = new McBtsProtocolBodyItemMeta("sAGRxPortForSignal",
					"UnsignedNumber", "2");
			items[6] = new McBtsProtocolBodyItemMeta("sAGTxPortForSignal",
					"UnsignedNumber", "2");
			items[7] = new McBtsProtocolBodyItemMeta("locationAreaID",
					"UnsignedNumber", "4");
			items[8] = new McBtsProtocolBodyItemMeta("sAGSignalPointCode",
					"UnsignedNumber", "2");
			items[9] = new McBtsProtocolBodyItemMeta("bTSSignalPointCode",
					"UnsignedNumber", "2");
			items[10] = new McBtsProtocolBodyItemMeta("", "Reserve", "8");
			items[11] = new McBtsProtocolBodyItemMeta("natAPKey",
					"UnsignedNumber", "1");
		} else if (type.equals("jitterbuffer")) {
			items = new McBtsProtocolBodyItemMeta[4];
			items[0] = new McBtsProtocolBodyItemMeta("bSForceUseJitterbuffer",
					"UnsignedNumber", "2");
			items[1] = new McBtsProtocolBodyItemMeta("zModelUseJitterbuffer",
					"UnsignedNumber", "2");
			items[2] = new McBtsProtocolBodyItemMeta("jitterbufferSize",
					"UnsignedNumber", "2");
			items[3] = new McBtsProtocolBodyItemMeta(
					"jitterbufferPackageThreshold", "UnsignedNumber", "2");
		} else if (type.equals("tos")) {
			items = new McBtsProtocolBodyItemMeta[1];
			items[0] = new McBtsProtocolBodyItemMeta("sAGVoiceTOS",
					"UnsignedNumber", "2");
		}
		return items;
	}

}
