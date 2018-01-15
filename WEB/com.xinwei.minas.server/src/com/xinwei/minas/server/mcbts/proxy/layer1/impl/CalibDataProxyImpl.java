/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-3-21	| jiayi		 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.proxy.layer1.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer1.AntennaCalibItem;
import com.xinwei.minas.mcbts.core.model.layer1.AntennaCalibrationConfig;
import com.xinwei.minas.mcbts.core.model.layer1.CalibGenConfigItem;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationDataInfo;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationGeneralConfig;
import com.xinwei.minas.mcbts.core.model.layer1.PSConfigItem;
import com.xinwei.minas.mcbts.core.model.layer1.RFConfig;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer1.CalibDataProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.GenericBizRecord;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 校准参数配置业务服务Proxy
 * 
 * @author jiayi
 * 
 */

public class CalibDataProxyImpl implements CalibDataProxy {

	// RFConfig配置Moc值
	public static final int RFCONFIG_CONFIG_MOC = 0x0086;
	// RFConfig查询Moc值
	public static final int RFCONFIG_QUERY_MOC = 0x0088;

	// CalibrationGeneral配置Moc值
	public static final int CALIBGENERAL_CONFIG_MOC = 0x0090;
	// CalibrationGeneral查询Moc值
	public static final int CALIBGENERAL_QUERY_MOC = 0x0092;

	// AntennaCalibration配置Moc值
	public static final int ANTENNADATACALIB_CONFIG_MOC = 0x0094;
	// AntennaCalibration查询Moc值
	public static final int ANTENNADATACALIB_QUERY_MOC = 0x0096;

	public static final int PRE_H_LENGTH = 23;

	private McBtsConnector connector;

	public CalibDataProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	/**
	 * 查询网元业务数据
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public CalibrationDataInfo query(Long moId) throws Exception {
		// RFConfig请求消息
		McBtsMessage rfConfigQueryRequest = convertModelToRequest(moId, null,
				getMcBtsProtocolHeaderItemMetas(RFCONFIG_QUERY_MOC), null,
				McBtsConstants.OPERATION_QUERY);
		// CalibrationGeneral请求消息
		McBtsMessage calibGeneralQueryRequest = convertModelToRequest(moId,
				null, getMcBtsProtocolHeaderItemMetas(CALIBGENERAL_QUERY_MOC),
				null, McBtsConstants.OPERATION_QUERY);
		// // AntennaCalibrationConfig请求消息
		// McBtsMessage antCalibQueryRequest = convertModelToRequest(moId,
		// null, getMcBtsProtocolHeaderItemMetas(ANTENNADATACALIB_QUERY_MOC),
		// null, McBtsConstants.OPERATION_QUERY);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage rfConfigQueryResponse = connector
					.syncInvoke(rfConfigQueryRequest);
			// 解析结果
			McBtsBizProxyHelper.parseResult(rfConfigQueryResponse);

			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage calibGeneralQueryResponse = connector
					.syncInvoke(calibGeneralQueryRequest);
			// 解析结果
			McBtsBizProxyHelper.parseResult(calibGeneralQueryResponse);

			// // 调低底层通信层发送消息, 同步等待应答
			// McBtsMessage antCalibQueryResponse = connector
			// .syncInvoke(antCalibQueryRequest);
			// // 解析结果
			// parseResult(antCalibQueryResponse);

			CalibrationDataInfo result = convertResponsesToModel(
					rfConfigQueryResponse, calibGeneralQueryResponse);
			return result;

		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
	}

	/**
	 * 配置网元业务数据
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public void config(Long moId, CalibrationDataInfo data) throws Exception {
		// 将模型转换为配置网元消息
		McBtsMessage rfConfigConfigRequest = convertModelToRequest(moId,
				data.getRfConfig());
		McBtsMessage calibGeneralConfigRequest = convertModelToRequest(moId,
				data.getGenConfig());
		List<McBtsMessage> antCalibConfigRequests = convertModelToRequest(moId,
				data.getAntConfigList());
		try {
			// 调底层通信层发送消息, 同步等待应答
			McBtsMessage rfConfigConfigResponse = connector
					.syncInvoke(rfConfigConfigRequest);
			// 解析结果
			McBtsBizProxyHelper.parseResult(rfConfigConfigResponse);

			// 无射频增益信息，则不发送此消息
			if (calibGeneralConfigRequest != null) {
				// 调底层通信层发送消息, 同步等待应答
				McBtsMessage calibGeneralConfigResponse = connector
						.syncInvoke(calibGeneralConfigRequest);
				// 解析结果
				McBtsBizProxyHelper.parseResult(calibGeneralConfigResponse);
			}

			for (McBtsMessage antCalibConfigRequest : antCalibConfigRequests) {
				// 调底层通信层发送消息, 同步等待应答
				McBtsMessage antCalibConfigResponse = connector
						.syncInvoke(antCalibConfigRequest);
				// 解析结果
				McBtsBizProxyHelper.parseResult(antCalibConfigResponse);
			}

		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
	}

	/**
	 * 将模型转化为网元消息
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
		// 解析配置消息
		if (operation.equals(McBtsConstants.OPERATION_CONFIG)) {
			// 填充消息头
			fillHeader(message, moId, headerItems);
			// 填充消息体
			List<GenericBizRecord> records = bizData.getRecords();
			if (!records.isEmpty()) {
				GenericBizRecord record = records.get(0);
				fillBody(message, record, bodyItems);
			}
		}
		// 解析查询消息
		else if (operation.equals(McBtsConstants.OPERATION_QUERY)) {
			// 填充消息头
			fillHeader(message, moId, headerItems);
		}
		return message;
	}

	/**
	 * 将模型转化为网元消息
	 * 
	 * @param moId
	 * @param rfConfig
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequest(Long moId, RFConfig rfConfig)
			throws Exception {
		if (rfConfig == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			throw new Exception(msg);
		}
		// 获得网元配置请求消息
		McBtsMessage message = new McBtsMessage();
		// 填充消息头
		fillHeader(message, moId,
				getMcBtsProtocolHeaderItemMetas(RFCONFIG_CONFIG_MOC));
		// 填充消息体
		fillBody(message, moId, rfConfig);
		return message;
	}

	/**
	 * 将模型转化为网元消息
	 * 
	 * @param moId
	 * @param genConfig
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequest(Long moId,
			CalibrationGeneralConfig genConfig) throws Exception {
		if (genConfig == null) {
			return null;
		}
		// 获得网元配置请求消息
		McBtsMessage message = new McBtsMessage();
		// 填充消息头
		fillHeader(message, moId,
				getMcBtsProtocolHeaderItemMetas(CALIBGENERAL_CONFIG_MOC));
		// 填充消息体
		fillBody(message, moId, genConfig);
		return message;
	}

	/**
	 * 将模型转化为网元消息
	 * 
	 * @param moId
	 * @param antConfigs
	 * @return
	 * @throws Exception
	 */
	private List<McBtsMessage> convertModelToRequest(Long moId,
			List<AntennaCalibrationConfig> antConfigs) throws Exception {

		List<McBtsMessage> messages = new ArrayList<McBtsMessage>();
		if (antConfigs == null || antConfigs.isEmpty()) {
			return messages;
		}
		// 获得网元配置请求消息
		for (AntennaCalibrationConfig antConfig : antConfigs) {
			McBtsMessage message = new McBtsMessage();
			// 填充消息头
			fillHeader(
					message,
					moId,
					getMcBtsProtocolHeaderItemMetas(ANTENNADATACALIB_CONFIG_MOC));
			// 填充消息体
			fillBody(message, moId, antConfig);
			messages.add(message);
		}
		return messages;
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
		Long btsId = getBtsIdByMoId(moId);
		message.setBtsId(btsId);
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

	/**
	 * 根据基站ID获取MO ID
	 * 
	 * @param moId
	 * @return
	 */
	private Long getMoIdByBtsId(Long btsId) {
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		return mcBts.getMoId();
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
			if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)
					|| type.equals(McBtsConstants.TYPE_SIGNED_NUMBER)) {
				// 数值型（包括无符号和有符号数）
				ByteUtils.putNumber(buf, offset,
						property.getValue().toString(), length);
			} else if (type.equals(McBtsConstants.TYPE_RESERVE)) {
				// 保留字段
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
	 * 填充消息体
	 * 
	 * @param message
	 * @param weakVoiceFault
	 */
	private void fillBody(McBtsMessage message, Long moId, RFConfig rfConfig) {
		byte[] buf = new byte[4096];
		int offset = 0;
		// 将实体类信息解析为字节流
		ByteUtils
				.putNumber(buf, offset, rfConfig.getFreqOffset().toString(), 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, rfConfig.getAntennaPower().toString(),
				2);
		offset += 2;
		ByteUtils.putNumber(buf, offset,
				rfConfig.getRxSensitivity().toString(), 2);
		offset += 2;
		ByteUtils.putNumber(buf, offset, rfConfig.getCableLoss().toString(), 2);
		offset += 2;
		ByteUtils.putNumber(buf, offset, rfConfig.getPsLoss().toString(), 2);
		offset += 2;
		List<PSConfigItem> psItems = rfConfig.getPsConfigList();
		for (PSConfigItem psItem : psItems) {
			ByteUtils.putNumber(buf, offset, psItem.getPsNormX().toString(), 2);
			offset += 2;
			ByteUtils.putNumber(buf, offset, psItem.getPsNormY().toString(), 2);
			offset += 2;
		}
		message.setContent(buf, 0, offset);
	}

	/**
	 * 填充消息体
	 * 
	 * @param message
	 * @param weakVoiceFault
	 */
	private void fillBody(McBtsMessage message, Long moId,
			CalibrationGeneralConfig genConfig) {
		byte[] buf = new byte[4096];
		int offset = 0;
		// 将实体类信息解析为字节流
		List<CalibGenConfigItem> genItems = genConfig.getGenItemList();
		for (CalibGenConfigItem genItem : genItems) {
			String temp = genItem.getPredH();
			ByteUtils.putString(buf, offset, temp == null ? "" : temp, PRE_H_LENGTH,
					'\0', McBtsConstants.CHARSET_US_ASCII);
			offset += PRE_H_LENGTH;
			ByteUtils.putNumber(buf, offset, genItem.getCalibrationResult()
					.toString(), 1);
			offset += 1;
			ByteUtils.putNumber(buf, offset, genItem.getTxGain().toString(), 2);
			offset += 2;
			ByteUtils.putNumber(buf, offset, genItem.getRxGain().toString(), 2);
			offset += 2;
		}
		ByteUtils
				.putNumber(buf, offset, genConfig.getSynTxGain().toString(), 2);
		offset += 2;
		ByteUtils
				.putNumber(buf, offset, genConfig.getSynRxGain().toString(), 2);
		offset += 2;
		message.setContent(buf, 0, offset);
	}

	/**
	 * 填充消息体
	 * 
	 * @param message
	 * @param weakVoiceFault
	 */
	private void fillBody(McBtsMessage message, Long moId,
			AntennaCalibrationConfig antConfig) {
		byte[] buf = new byte[4096];
		int offset = 0;
		// 将实体类信息解析为字节流
		ByteUtils.putNumber(buf, offset, antConfig.getDataType().toString(), 2);
		offset += 2;
		ByteUtils.putNumber(buf, offset,
				antConfig.getAntennaIndex().toString(), 2);
		offset += 2;
		// subCarrier起始序号为0
		ByteUtils.putNumber(buf, offset, "0", 2);
		offset += 2;
		// 跳过subCarrier个数
		offset += 2;
		List<AntennaCalibItem> antItemList = antConfig.getAntItemList();
		for (AntennaCalibItem antItem : antItemList) {
			ByteUtils.putNumber(buf, offset, antItem.getCarrierData()
					.toString(), 2);
			offset += 2;
		}
		message.setContent(buf, 0, offset);
	}

	/**
	 * 获取消息头协议枚举
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
		case RFCONFIG_QUERY_MOC:
		case CALIBGENERAL_QUERY_MOC:
		case ANTENNADATACALIB_QUERY_MOC:
			items[3].setValue("3");
			break;
		case RFCONFIG_CONFIG_MOC:
		case CALIBGENERAL_CONFIG_MOC:
		case ANTENNADATACALIB_CONFIG_MOC:
			items[3].setValue("1");
			break;
		}
		return items;
	}


	private CalibrationDataInfo convertResponsesToModel(
			McBtsMessage rfConfigQueryResponse,
			McBtsMessage calibGeneralQueryResponse) {

		CalibrationDataInfo result = new CalibrationDataInfo();

		RFConfig rfConfig = new RFConfig();
		// 设置MO ID
		Long moId = getMoIdByBtsId(rfConfigQueryResponse.getBtsId());
		rfConfig.setMoId(moId);

		int offset = 0;
		rfConfig.setFreqOffset((int) ByteUtils.toUnsignedNumber(
				rfConfigQueryResponse.getContent(), offset, 4));
		offset += 4;
		rfConfig.setAntennaPower((int) ByteUtils.toSignedNumber(
				rfConfigQueryResponse.getContent(), offset, 2));
		offset += 2;
		rfConfig.setRxSensitivity((int) ByteUtils.toSignedNumber(
				rfConfigQueryResponse.getContent(), offset, 2));
		offset += 2;
		rfConfig.setCableLoss((int) ByteUtils.toUnsignedNumber(
				rfConfigQueryResponse.getContent(), offset, 2));
		offset += 2;
		rfConfig.setPsLoss((int) ByteUtils.toUnsignedNumber(
				rfConfigQueryResponse.getContent(), offset, 2));
		offset += 2;

		for (int i = 0; i < RFConfig.PS_COUNT; i++) {
			PSConfigItem psConfig = new PSConfigItem();
			psConfig.setMoId(moId);
			psConfig.setAntennaIndex(i);
			psConfig.setPsNormX((int) ByteUtils.toUnsignedNumber(
					rfConfigQueryResponse.getContent(), offset, 2));
			offset += 2;
			psConfig.setPsNormY((int) ByteUtils.toUnsignedNumber(
					rfConfigQueryResponse.getContent(), offset, 2));
			offset += 2;

			rfConfig.getPsConfigList().add(psConfig);
		}
		result.setRfConfig(rfConfig);

		CalibrationGeneralConfig genConfig = new CalibrationGeneralConfig();
		// 设置MO ID
		genConfig.setMoId(moId);

		offset = 0;
		for (int i = 0; i < CalibrationGeneralConfig.GEN_COUNT; i++) {
			CalibGenConfigItem genConfigItem = new CalibGenConfigItem();
			genConfigItem.setMoId(moId);
			genConfigItem.setAntennaIndex(i);
			genConfigItem.setPredH(ByteUtils.toString(
					calibGeneralQueryResponse.getContent(), offset,
					PRE_H_LENGTH, McBtsConstants.CHARSET_US_ASCII));
			offset += PRE_H_LENGTH;
			genConfigItem.setCalibrationResult((int) ByteUtils
					.toUnsignedNumber(calibGeneralQueryResponse.getContent(),
							offset, 1));
			offset += 1;
			genConfigItem.setTxGain((int) ByteUtils.toUnsignedNumber(
					calibGeneralQueryResponse.getContent(), offset, 2));
			offset += 2;
			genConfigItem.setRxGain((int) ByteUtils.toUnsignedNumber(
					calibGeneralQueryResponse.getContent(), offset, 2));
			offset += 2;

			genConfig.getGenItemList().add(genConfigItem);
		}
		genConfig.setSynTxGain((int) ByteUtils.toUnsignedNumber(
				calibGeneralQueryResponse.getContent(), offset, 2));
		offset += 2;
		genConfig.setSynRxGain((int) ByteUtils.toUnsignedNumber(
				calibGeneralQueryResponse.getContent(), offset, 2));
		offset += 2;
		result.setGenConfig(genConfig);

		return result;
	}
}
