/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-3-21	| jiayi		 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.proxy.layer2.impl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.mcbts.core.model.layer2.PECCHSetting;
import com.xinwei.minas.mcbts.core.model.layer2.SCGChannelConfigItem;
import com.xinwei.minas.mcbts.core.model.layer2.SCGScaleConfigItem;
import com.xinwei.minas.mcbts.core.model.layer2.W0ConfigItem;
import com.xinwei.minas.mcbts.core.model.sysManage.ChannelComparableMode;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer2.AirlinkProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizRecord;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ������·����ҵ�����Proxy
 * 
 * @author jiayi
 * 
 */

public class AirlinkProxyImpl implements AirlinkProxy {

	private Log log = LogFactory.getLog(AirlinkProxyImpl.class);

	// ������·����Mocֵ
	public static final int AIRLINK_CONFIG_MOC = 0x0041;
	// ������·��ѯMocֵ
	public static final int AIRLINK_QUERY_MOC = 0x0043;

	// С��ͨ������Mocֵ
	public static final int SMALLPKGCH_CONFIG_MOC = 0x0069;
	// С��ͨ����ѯMocֵ
	public static final int SMALLPKGCH_QUERY_MOC = 0x006b;

	// ��ǿ����ͨ������Mocֵ
	public static final int PECCH_CONFIG_MOC = 0x0C2E;
	// ��ǿ����ͨ����ѯMocֵ
	public static final int PECCH_QUERY_MOC = 0x0C30;

	public static final int FILL_INDEX = 0xFF;

	public static final int TCHFORBID_BYTES = 10;
	public static final int TCHFORBID_STRLENGTH = 20;

	private McBtsConnector connector;

	public AirlinkProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	/**
	 * ��ѯ��Ԫҵ������
	 * 
	 * @param moId
	 * @param comparableMode
	 * @throws Exception
	 * @return
	 */
	public AirlinkConfig query(Long moId, ChannelComparableMode comparableMode)
			throws Exception {

		AirlinkConfig result = null;

		// ������·������Ϣ
		McBtsMessage airlinkQueryRequest = convertModelToRequest(moId, null,
				getMcBtsProtocolHeaderItemMetas(AIRLINK_QUERY_MOC), null,
				McBtsConstants.OPERATION_QUERY);
		// С��ͨ������������Ϣ
//		McBtsMessage smallPkgChannelQueryRequest = convertModelToRequest(moId,
//				null, getMcBtsProtocolHeaderItemMetas(SMALLPKGCH_QUERY_MOC),
//				null, McBtsConstants.OPERATION_QUERY);

		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage airlinkQueryResponse = connector
					.syncInvoke(airlinkQueryRequest);
			// �������
			McBtsBizProxyHelper.parseResult(airlinkQueryResponse);
			result = convertResponsesToModel(airlinkQueryResponse);

			// TODO ���ڻ�վ��֧��"С�������ŵ���ѯ����",������ʱ��ע������������Ϣ
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
//			McBtsMessage smallPkgChannelQueryResponse = null;
//			try {
//				smallPkgChannelQueryResponse = connector
//						.syncInvoke(smallPkgChannelQueryRequest);
//				// �������
//				McBtsBizProxyHelper.parseResult(smallPkgChannelQueryResponse);
//				result = convertSmallPkgResponsesToModel(result,
//						smallPkgChannelQueryResponse);
//			} catch (UnsupportedOperationException uex) {
//			}

		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		} catch (UnsupportedOperationException e) {
			log.error(OmpAppContext.getMessage("unsupported_biz_operation")
					+ ":mcbts_airlink");
			return null;
		}

		if (comparableMode.getChannelMode() != ChannelComparableMode.PCCH_ONLY) {
			// ��ǿ�����ŵ���ѯ��Ϣ
			McBtsMessage pecchQueryRequest = convertModelToRequest(moId, null,
					getMcBtsProtocolHeaderItemMetas(PECCH_QUERY_MOC), null,
					McBtsConstants.OPERATION_QUERY);

			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ���ǿ�����ŵ�Ӧ��
			McBtsMessage pecchQueryResponse = null;
			try {
				pecchQueryResponse = connector.syncInvoke(pecchQueryRequest);
				// �������
				McBtsBizProxyHelper.parseResult(pecchQueryResponse);
				result = convertPECCHResponsesToModel(result,
						pecchQueryResponse);
			} catch (UnsupportedOperationException uex) {
			}
		}

		return result;
	}

	/**
	 * ������Ԫҵ������
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public void config(Long moId, AirlinkConfig data,
			ChannelComparableMode comparableMode) throws Exception {

		// ������·������Ϣ
		McBtsMessage airlinkConfigRequest = convertModelToRequest(moId, data);

		// С��ͨ��������Ϣ
//		McBtsMessage smallPkgChannelConfigRequest = convertModelToRequest(moId,
//				data.getScgChannelConfigList());

		try {
			// ���ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage rfConfigConfigResponse = connector
					.syncInvoke(airlinkConfigRequest);
			// �������
			McBtsBizProxyHelper.parseResult(rfConfigConfigResponse);

			// ���ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			// TODO: ��ȷ��
//			McBtsMessage calibGeneralConfigResponse = connector
//					.syncInvoke(smallPkgChannelConfigRequest);
//			// �������
//			McBtsBizProxyHelper.parseResult(calibGeneralConfigResponse);

		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		} catch (UnsupportedOperationException e) {
			log.error(OmpAppContext.getMessage("unsupported_biz_operation")
					+ ":" + data.getBizName());
		} catch (Exception e) {
			log.error("��վ����ʱ�������ô���,ҵ��:������·", e);
			throw e;
		}

		if (comparableMode.getChannelMode() != ChannelComparableMode.PCCH_ONLY) {
			// ��ǿ�����ŵ�������Ϣ
			McBtsMessage pecchConfigRequest = convertPECCHModelToRequest(moId,
					data.getPecchSetting());

			try {
				// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ���ǿ�����ŵ�Ӧ��
				McBtsMessage pecchConfigResponse = connector
						.syncInvoke(pecchConfigRequest);
				// �������
				McBtsBizProxyHelper.parseResult(pecchConfigResponse);
			} catch (TimeoutException e) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_reply_over_time"));
			} catch (UnsupportedOperationException uex) {
			} catch (Exception e) {
				log.error("��վ����ʱ�������ô���,ҵ��:��ǿ�����ŵ�����", e);
				throw e;
			}
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
			McBtsBizProxyHelper.fillHeader(message, moId, headerItems);
			// �����Ϣ��
			List<GenericBizRecord> records = bizData.getRecords();
			if (!records.isEmpty()) {
				GenericBizRecord record = records.get(0);
				McBtsBizProxyHelper.fillBody(message, record, bodyItems);
			}
		}
		// ������ѯ��Ϣ
		else if (operation.equals(McBtsConstants.OPERATION_QUERY)) {
			// �����Ϣͷ
			McBtsBizProxyHelper.fillHeader(message, moId, headerItems);
		}
		return message;
	}

	/**
	 * ��ģ��ת��Ϊ��Ԫ��Ϣ
	 * 
	 * @param moId
	 * @param airlinkConfig
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequest(Long moId,
			AirlinkConfig airlinkConfig) throws Exception {
		if (airlinkConfig == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			throw new Exception(msg);
		}
		// �����Ԫ����������Ϣ
		McBtsMessage message = new McBtsMessage();
		// �����Ϣͷ
		McBtsBizProxyHelper.fillHeader(message, moId,
				getMcBtsProtocolHeaderItemMetas(AIRLINK_CONFIG_MOC));
		// �����Ϣ��
		fillBody(message, moId, airlinkConfig);
		return message;
	}

	/**
	 * ��ģ��ת��Ϊ��Ԫ��Ϣ
	 * 
	 * @param moId
	 * @param antConfigs
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequest(Long moId,
			List<SCGChannelConfigItem> scgChannelConfigList) throws Exception {

		if (scgChannelConfigList == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			throw new Exception(msg);
		}

		// �����Ԫ����������Ϣ
		McBtsMessage message = new McBtsMessage();
		// �����Ϣͷ
		McBtsBizProxyHelper.fillHeader(message, moId,
				getMcBtsProtocolHeaderItemMetas(SMALLPKGCH_CONFIG_MOC));
		// �����Ϣ��
		fillBody(message, moId, scgChannelConfigList);

		return message;
	}

	/**
	 * ��ģ��ת��Ϊ��Ԫ��Ϣ
	 * 
	 * @param moId
	 * @param airlinkConfig
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertPECCHModelToRequest(Long moId,
			PECCHSetting pecchSetting) throws Exception {
		if (pecchSetting == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			throw new Exception(msg);
		}
		// �����Ԫ����������Ϣ
		McBtsMessage message = new McBtsMessage();
		// �����Ϣͷ
		McBtsBizProxyHelper.fillHeader(message, moId,
				getMcBtsProtocolHeaderItemMetas(PECCH_CONFIG_MOC));
		// �����Ϣ��
		fillBody(message, moId, pecchSetting);
		return message;
	}

	/**
	 * ���ݻ�վID��ȡMO ID
	 * 
	 * @param moId
	 * @return
	 */
	private Long getMoIdByBtsId(Long btsId) {
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		return mcBts.getMoId();
	}

	/**
	 * �����Ϣ��
	 * 
	 * @param message
	 * @param weakVoiceFault
	 */
	private void fillBody(McBtsMessage message, Long moId,
			AirlinkConfig airlinkConfig) {
		// ��ʵ������Ϣ����Ϊ�ֽ���
		byte[] buf = new byte[4096];
		int offset = 0;
		// ǰ�����к�
		ByteUtils.putNumber(buf, offset, airlinkConfig.getSequenceId()
				.toString(), 1);
		offset += 1;
		// �ز�������
		ByteUtils.putNumber(buf, offset, airlinkConfig.getScgMask().toString(),
				1);
		offset += 1;
		// ��ʱ϶��
		ByteUtils.putNumber(buf, offset, airlinkConfig.getTotalTS().toString(),
				1);
		offset += 1;
		// ����ʱ϶��
		ByteUtils.putNumber(buf, offset, airlinkConfig.getDownlinkTS()
				.toString(), 1);
		offset += 1;
		// BCHͨ����Ϣ
		putSCGChannelInfo(AirlinkConfig.BCH_COUNT, SCGChannelConfigItem.BCH,
				offset, buf, airlinkConfig.getScgChannelConfigList());
		offset += 2 * AirlinkConfig.BCH_COUNT;
		// RRCHͨ����Ϣ
		putSCGChannelInfo(AirlinkConfig.RRCH_COUNT, SCGChannelConfigItem.RRCH,
				offset, buf, airlinkConfig.getScgChannelConfigList());
		offset += 2 * AirlinkConfig.RRCH_COUNT;
		// RARCHͨ����Ϣ
		putSCGChannelInfo(AirlinkConfig.RARCH_COUNT,
				SCGChannelConfigItem.RARCH, offset, buf,
				airlinkConfig.getScgChannelConfigList());
		offset += 2 * AirlinkConfig.RARCH_COUNT;
		// ���ϵ��
		ByteUtils.putNumber(buf, offset,
				"" + convertScaleToSend(airlinkConfig.getMaxScale()), 2);
		offset += 2;
		// ǰ��ϵ��
		ByteUtils.putNumber(buf, offset,
				"" + convertScaleToSend(airlinkConfig.getPreambleScale()), 2);
		offset += 2;
		// Scaleֵ��Ϣ
		List<SCGScaleConfigItem> scgScaleConfigList = airlinkConfig
				.getScgScaleConfigList();
		for (SCGScaleConfigItem scgScaleItem : scgScaleConfigList) {
			ByteUtils.putNumber(buf, offset, ""
					+ convertScaleToSend(scgScaleItem.getBchScale()), 2);
			offset += 2;
			ByteUtils.putNumber(buf, offset, ""
					+ convertScaleToSend(scgScaleItem.getTchScale()), 2);
			offset += 2;
		}
		// W0ֵ��Ϣ
		List<W0ConfigItem> w0ConfigList = airlinkConfig.getW0ConfigList();
		for (W0ConfigItem w0Item : w0ConfigList) {
			ByteUtils.putNumber(buf, offset, ""
					+ w02Int(w0Item.getW0I().doubleValue()), 2);
			offset += 2;
			ByteUtils.putNumber(buf, offset, ""
					+ w02Int(w0Item.getW0Q().doubleValue()), 2);
			offset += 2;
		}
		// TCH������Ϣ
		byte[] temp = tchForbidStr2Bytes(airlinkConfig.getTchForbidden());
		System.arraycopy(temp, 0, buf, offset, TCHFORBID_BYTES);
		offset += TCHFORBID_BYTES;

		message.setContent(buf, 0, offset);
	}

	private void putSCGChannelInfo(int itemCount, int channelType, int offset,
			byte[] buf, List<SCGChannelConfigItem> scgChannelConfigList) {

		int dataSize = 0;
		for (SCGChannelConfigItem scgChannelItem : scgChannelConfigList) {
			if (scgChannelItem.getChannelType() != channelType) {
				continue;
			}
			ByteUtils.putNumber(buf, offset, scgChannelItem.getScgIndex()
					.toString(), 1);
			offset += 1;
			ByteUtils.putNumber(buf, offset, scgChannelItem.getTsIndex()
					.toString(), 1);
			offset += 1;

			dataSize++;
			if (dataSize == itemCount) {
				break;
			}
		}
		// ����ͨ����
		for (int i = dataSize; i < itemCount; i++) {
			ByteUtils.putNumber(buf, offset,
					new Integer(FILL_INDEX).toString(), 1);
			offset += 1;
			ByteUtils.putNumber(buf, offset,
					new Integer(FILL_INDEX).toString(), 1);
			offset += 1;
		}
	}

	/**
	 * �����Ϣ��
	 * 
	 * @param message
	 * @param weakVoiceFault
	 */
	private void fillBody(McBtsMessage message, Long moId,
			List<SCGChannelConfigItem> scgChannelConfigList) {
		// ��ʵ������Ϣ����Ϊ�ֽ���
		byte[] buf = new byte[4096];
		int offset = 0;
		// FACHͨ����Ϣ
		putSCGChannelInfo(AirlinkConfig.FACH_COUNT, SCGChannelConfigItem.FACH,
				offset, buf, scgChannelConfigList);
		offset += 2 * AirlinkConfig.FACH_COUNT;
		// RPCHͨ����Ϣ
		putSCGChannelInfo(AirlinkConfig.RPCH_COUNT, SCGChannelConfigItem.RPCH,
				offset, buf, scgChannelConfigList);
		offset += 2 * AirlinkConfig.RPCH_COUNT;
		message.setContent(buf, 0, offset);
	}

	/**
	 * �����Ϣ��
	 * 
	 * @param message
	 * @param weakVoiceFault
	 */
	private void fillBody(McBtsMessage message, Long moId,
			PECCHSetting pecchSetting) {

		// ��ʵ������Ϣ����Ϊ�ֽ���
		byte[] buf = new byte[4096];
		
		StringBuffer buffer = new StringBuffer();
		// ��ǿ�ŵ��ز������
		String temp = Integer.toBinaryString(pecchSetting.getScgIndex());
		while (temp.length() < 2) {
			temp = "0" + temp;
		}
		buffer.append(temp);
		
		// PCH����
		temp = Integer.toBinaryString(pecchSetting.getPchCount() - 1);
		while (temp.length() < 3) {
			temp = "0" + temp;
		}
		buffer.append(temp);

		// PCH�����
		temp = Integer.toBinaryString(pecchSetting.getPchIndex() - 1);
		while (temp.length() < 3) {
			temp = "0" + temp;
		}
		buffer.append(temp);

		// RARCH����
		temp = Integer.toBinaryString(pecchSetting.getRarchCount() - 1);
		while (temp.length() < 2) {
			temp = "0" + temp;
		}
		buffer.append(temp);

		// RRCH����
		temp = Integer.toBinaryString(pecchSetting.getRrchCount() - 1);
		while (temp.length() < 1) {
			temp = "0" + temp;
		}
		buffer.append(temp);

		// ������
		buffer.append("00000");

		int offset = 0;
		Integer value = Integer.parseInt(buffer.toString(), 2);
		ByteUtils.putNumber(buf, offset, value.toString(), 2);
		offset += 2;
		
		message.setContent(buf, 0, offset);
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
		items[0].setValue("1");
		items[1].setName("MA");
		items[1].setValue("0");
		items[2].setName("MOC");
		items[2].setValue("" + moc);
		items[3].setName("ActionType");
		switch (moc) {
		case AIRLINK_QUERY_MOC:
		case SMALLPKGCH_QUERY_MOC:
		case PECCH_QUERY_MOC:
			items[3].setValue("3");
			break;
		case AIRLINK_CONFIG_MOC:
		case SMALLPKGCH_CONFIG_MOC:
		case PECCH_CONFIG_MOC:
			items[3].setValue("1");
			break;
		}
		return items;
	}

	private AirlinkConfig convertResponsesToModel(
			McBtsMessage airlinkQueryResponse) {

		AirlinkConfig airlinkConfig = new AirlinkConfig();
		// ����MO ID
		Long moId = getMoIdByBtsId(airlinkQueryResponse.getBtsId());
		airlinkConfig.setMoId(moId);

		int offset = 0;
		// ǰ�����к�
		airlinkConfig.setSequenceId((int) ByteUtils.toUnsignedNumber(
				airlinkQueryResponse.getContent(), offset, 1));
		offset += 1;
		// �ز�������
		airlinkConfig.setScgMask((int) ByteUtils.toUnsignedNumber(
				airlinkQueryResponse.getContent(), offset, 1));
		offset += 1;
		// ��ʱ϶��
		airlinkConfig.setTotalTS((int) ByteUtils.toUnsignedNumber(
				airlinkQueryResponse.getContent(), offset, 1));
		offset += 1;
		// ����ʱ϶��
		airlinkConfig.setDownlinkTS((int) ByteUtils.toUnsignedNumber(
				airlinkQueryResponse.getContent(), offset, 1));
		offset += 1;
		// BCHͨ����Ϣ
		getSCGChannelInfo(moId, AirlinkConfig.BCH_COUNT,
				SCGChannelConfigItem.BCH, offset, airlinkQueryResponse,
				airlinkConfig);
		offset += 2 * AirlinkConfig.BCH_COUNT;
		// RRCHͨ����Ϣ
		getSCGChannelInfo(moId, AirlinkConfig.RRCH_COUNT,
				SCGChannelConfigItem.RRCH, offset, airlinkQueryResponse,
				airlinkConfig);
		offset += 2 * AirlinkConfig.RRCH_COUNT;
		// RARCHͨ����Ϣ
		getSCGChannelInfo(moId, AirlinkConfig.RARCH_COUNT,
				SCGChannelConfigItem.RARCH, offset, airlinkQueryResponse,
				airlinkConfig);
		offset += 2 * AirlinkConfig.RARCH_COUNT;
		// ���ϵ��
		airlinkConfig
				.setMaxScale(convertScaleToSave((int) ByteUtils
						.toUnsignedNumber(airlinkQueryResponse.getContent(),
								offset, 2)));
		offset += 2;
		// ǰ��ϵ��
		airlinkConfig
				.setPreambleScale(convertScaleToSave((int) ByteUtils
						.toUnsignedNumber(airlinkQueryResponse.getContent(),
								offset, 2)));
		offset += 2;
		// Scaleֵ��Ϣ
		for (int i = 0; i < AirlinkConfig.TS_COUNT; i++) {
			SCGScaleConfigItem scgScaleItem = new SCGScaleConfigItem();
			scgScaleItem.setMoId(moId);
			scgScaleItem.setTsIndex(i);
			scgScaleItem.setBchScale(convertScaleToSave((int) ByteUtils
					.toUnsignedNumber(airlinkQueryResponse.getContent(),
							offset, 2)));
			offset += 2;
			scgScaleItem.setTchScale(convertScaleToSave((int) ByteUtils
					.toUnsignedNumber(airlinkQueryResponse.getContent(),
							offset, 2)));
			offset += 2;
			airlinkConfig.getScgScaleConfigList().add(scgScaleItem);
		}
		// W0ֵ��Ϣ
		for (int i = 0; i < AirlinkConfig.W0_COUNT; i++) {
			W0ConfigItem w0Item = new W0ConfigItem();
			w0Item.setMoId(moId);
			w0Item.setAntennaIndex(i);
			int temp = (int) ByteUtils.toSignedNumber(
					airlinkQueryResponse.getContent(), offset, 2);
			w0Item.setW0I(w02Double(temp));
			offset += 2;
			temp = (int) ByteUtils.toSignedNumber(
					airlinkQueryResponse.getContent(), offset, 2);
			w0Item.setW0Q(w02Double(temp));
			offset += 2;
			airlinkConfig.getW0ConfigList().add(w0Item);
		}
		// TCH������Ϣ
		byte[] temp = Arrays.copyOfRange(airlinkQueryResponse.getContent(),
				offset, offset + TCHFORBID_BYTES);
		airlinkConfig.setTchForbidden(tchForbidBytes2String(temp));
		offset += TCHFORBID_BYTES;
		
		return airlinkConfig;
	}

	private AirlinkConfig convertSmallPkgResponsesToModel(
			AirlinkConfig airlinkConfig,
			McBtsMessage smallPkgChannelQueryResponse) {

		if (smallPkgChannelQueryResponse == null) {
			airlinkConfig.setSmallPkgSupport(false);
			return airlinkConfig;
		}

		int offset = 0;
		Long moId = airlinkConfig.getMoId();

		// FACHͨ����Ϣ
		getSCGChannelInfo(moId, AirlinkConfig.FACH_COUNT,
				SCGChannelConfigItem.FACH, offset,
				smallPkgChannelQueryResponse, airlinkConfig);
		offset += 2 * AirlinkConfig.FACH_COUNT;
		// RPCHͨ����Ϣ
		getSCGChannelInfo(moId, AirlinkConfig.RPCH_COUNT,
				SCGChannelConfigItem.RPCH, offset,
				smallPkgChannelQueryResponse, airlinkConfig);
		offset += 2 * AirlinkConfig.RPCH_COUNT;

		return airlinkConfig;
	}

	private AirlinkConfig convertPECCHResponsesToModel(
			AirlinkConfig airlinkConfig, McBtsMessage pecchQueryResponse) {

		if (pecchQueryResponse == null) {
			airlinkConfig.setPecchSetting(null);
			return airlinkConfig;
		}

		PECCHSetting pecchSetting = new PECCHSetting();
		Long moId = airlinkConfig.getMoId();
		pecchSetting.setMoId(moId);

		int offset = 0;
		String binaryStr = ByteUtils.toBinaryString(
				pecchQueryResponse.getContent(), offset, 2);

		// ��ǿ�ŵ��ز������
		offset = 0;
		int scgIndex = Integer.parseInt(binaryStr.substring(offset, offset + 2), 2);
		pecchSetting.setScgIndex(scgIndex);
		offset += 2;

		// PCH����
		int pchCount = Integer.parseInt(binaryStr.substring(offset, offset + 3), 2);
		pecchSetting.setPchCount(pchCount + 1);
		offset += 3;

		// PCH�����
		int pchIndex = Integer.parseInt(binaryStr.substring(offset, offset + 3), 2);
		pecchSetting.setPchIndex(pchIndex + 1);
		offset += 3;

		// RARCH����
		int rarchCount = Integer.parseInt(binaryStr.substring(offset, offset + 2), 2);
		pecchSetting.setRarchCount(rarchCount + 1);
		offset += 2;

		// RRCH����
		int rrchCount = Integer.parseInt(binaryStr.substring(offset, offset + 1), 2);
		pecchSetting.setRrchCount(rrchCount + 1);
		offset += 1;

		// ������
		int reserved = Integer.parseInt(binaryStr.substring(offset, offset + 5), 2);
		offset += 5;

		airlinkConfig.setPecchSetting(pecchSetting);
		
		return airlinkConfig;
	}

	private void getSCGChannelInfo(long moId, int itemCount, int channelType,
			int offset, McBtsMessage response, AirlinkConfig airlinkConfig) {
		for (int i = 0; i < itemCount; i++) {
			SCGChannelConfigItem scgChannelItem = new SCGChannelConfigItem();
			scgChannelItem.setMoId(moId);
			scgChannelItem.setChannelType(channelType);
			int scgIndex = (int) ByteUtils.toUnsignedNumber(
					response.getContent(), offset, 1);
			if (scgIndex == FILL_INDEX) {
				// ��ʵ�����ݣ�����
				continue;
			}
			scgChannelItem.setScgIndex((int) ByteUtils.toUnsignedNumber(
					response.getContent(), offset, 1));
			offset += 1;
			scgChannelItem.setTsIndex((int) ByteUtils.toUnsignedNumber(
					response.getContent(), offset, 1));
			offset += 1;
			airlinkConfig.getScgChannelConfigList().add(scgChannelItem);
		}
	}

	/**
	 * w0_I, w0_Q��Ϣת����Double����
	 * 
	 * @param value
	 * @return
	 */
	public static double w02Double(int value) {
		double ret = ((double) value) / 32767.0;
		return ret;
	}

	/**
	 * w0_I, w0_Q��Ϣת����Int����
	 * 
	 * @param value
	 * @return
	 */
	public static int w02Int(double value) {
		return (int) (value * 32767);
	}

	/**
	 * scale��Ϣת�����·�����
	 * 
	 * @param value
	 * @return
	 */
	public static int convertScaleToSend(int value) {
		return (int) (((double) (value)) / 10000.0 * 32767.0);
	}

	/**
	 * scale��Ϣת���ɱ�������
	 * 
	 * @param value
	 * @return
	 */
	public static int convertScaleToSave(int value) {
		return (int) (((double) (value)) / 32767.0 * 10000.0);
	}

	/**
	 * TCH������Ϣת����byte��������
	 * 
	 * @param tchForbid
	 * @return
	 */
	public static byte[] tchForbidStr2Bytes(String tchForbid) {

		byte btforbidtch[] = new byte[TCHFORBID_BYTES];

		String strforbidtch = tchForbid;
		if (strforbidtch.length() == TCHFORBID_STRLENGTH) {
			BigInteger bi = new BigInteger(strforbidtch, 16);
			byte bt[] = bi.toByteArray();
			if (bt.length == TCHFORBID_BYTES) {
				for (int i = 0; i < bt.length; i++) {
					btforbidtch[i] = bt[i];
				}
			} else if (bt.length == TCHFORBID_BYTES + 1 && bt[0] == 0) {
				for (int i = 1; i < bt.length; i++) {
					btforbidtch[i - 1] = bt[i];
				}
			} else if (bt.length < TCHFORBID_BYTES) {
				for (int i = 0; i < TCHFORBID_BYTES; i++) {
					btforbidtch[i] = 0;
				}
				for (int i = 0; i < bt.length; i++) {
					btforbidtch[TCHFORBID_BYTES - i - 1] = bt[bt.length - i - 1];
				}
			}
		}

		return btforbidtch;
	}

	/**
	 * TCH������Ϣת�����ַ�������
	 * 
	 * @param bt
	 * @return
	 */
	public static String tchForbidBytes2String(byte[] bt) {

		byte btn[] = new byte[bt.length + 1];
		btn[0] = 0;
		for (int i = 0; i < bt.length; i++) {
			btn[i + 1] = bt[i];
		}
		BigInteger bi = new BigInteger(btn);
		String sz = bi.toString(16);
		int len = sz.length();
		for (int i = 0; i < TCHFORBID_STRLENGTH - len; i++) {
			sz = "0" + sz;
		}
		return sz;
	}
}
