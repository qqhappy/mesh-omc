/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.net;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.enb.core.model.EnbDynamicInfo;
import com.xinwei.minas.enb.core.model.EnbDynamicInfoCondition;
import com.xinwei.minas.enb.core.model.status.AirFlowTest;
import com.xinwei.minas.enb.core.model.status.BbuOpticalStatus;
import com.xinwei.minas.enb.core.model.status.BoardStatus;
import com.xinwei.minas.enb.core.model.status.EnbStatus;
import com.xinwei.minas.enb.core.model.status.EnbStatusConstants;
import com.xinwei.minas.enb.core.model.status.EnbxGWAddress;
import com.xinwei.minas.enb.core.model.status.EnbxGWAddressCollection;
import com.xinwei.minas.enb.core.model.status.IbtsBoardStatus;
import com.xinwei.minas.enb.core.model.status.IbtsOpticalStatus;
import com.xinwei.minas.enb.core.model.status.IbtsOpticalStatusCollection;
import com.xinwei.minas.enb.core.model.status.IbtsRfChannelStatus;
import com.xinwei.minas.enb.core.model.status.IbtsRfStatus;
import com.xinwei.minas.enb.core.model.status.IbtsRunningStatus;
import com.xinwei.minas.enb.core.model.status.IbtsStatus;
import com.xinwei.minas.enb.core.model.status.IbtsThreshold;
import com.xinwei.minas.enb.core.model.status.PackageTestCollection;
import com.xinwei.minas.enb.core.model.status.PacketLossRateModel;
import com.xinwei.minas.enb.core.model.status.RruOpticalStatus;
import com.xinwei.minas.enb.core.model.status.RruRfStatus;
import com.xinwei.minas.enb.core.model.status.RruRunningStatus;
import com.xinwei.minas.enb.core.model.status.RruThreshold;
import com.xinwei.minas.enb.core.model.status.TimeDelayCollection;
import com.xinwei.minas.enb.core.model.status.TimeDelayModel;
import com.xinwei.minas.server.core.conf.net.model.CompositeValue;
import com.xinwei.minas.server.core.conf.net.model.TlvFieldUtil;
import com.xinwei.minas.server.enb.EnbModule;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.core.utils.StringUtils;

/**
 * 
 * eNB状态消息解析助手
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatusMessageHelper {

	/**
	 * 解析eNB状态信息
	 * 
	 * @param buf
	 * @return
	 * @throws BizException
	 */
	public static EnbStatus parseEnbStatus(EnbAppMessage appMessage)
			throws BizException {

		EnbStatus enbStatus = new EnbStatus();

		enbStatus.setClockStatus(appMessage
				.getIntValue(TagConst.ENB_CLOCK_STATUS));
		enbStatus.setClockType(appMessage.getIntValue(TagConst.ENB_CLOCK_TYPE));
		enbStatus.setEnbStatus(appMessage.getIntValue(TagConst.STATE));
		// eNB时间需要转换格式
		long enbTime = getEnbTime(appMessage.getLongValue(TagConst.ENB_TIME));
		enbStatus.setEnbTime(String.valueOf(enbTime));
		enbStatus.setRunningTime(String.valueOf(appMessage
				.getIntValue(TagConst.ENB_RUNNING_TIME)));
		enbStatus.setTemperature(appMessage
				.getIntValue(TagConst.ENB_TEMPERATURE));
		enbStatus.setPower(appMessage
				.getIntValue(TagConst.ENB_POWER_CONSUMPTION));
		enbStatus.setTrackSatelliteNum(appMessage
				.getIntValue(TagConst.ENB_TRACK_SATELLITES));
		enbStatus.setVisibleSatelliteNum(appMessage
				.getIntValue(TagConst.ENB_VISIBLE_SATELLITES));

		byte[] fanSpeedBytes = appMessage.getByteValue(TagConst.ENB_FAN_SPEED);

		int[] fanSpeeds = new int[4];
		int offset = 9;
		for (int i = 0; i < 4; i++) {
			fanSpeeds[i] = ByteUtils.toInt(fanSpeedBytes, offset, 4);
			offset += 13;
		}
		enbStatus.setFanSpeeds(fanSpeeds);
		// 端口工作模式相关代码是后面版本加的，需要做兼容性处理
		try {
			int portWorkMode = appMessage.getIntValue(TagConst.PORT_WORK_MODE);
			enbStatus.setPortWorkMode(portWorkMode);
			int portRate = appMessage.getIntValue(TagConst.PORT_RATE);
			enbStatus.setPortRate(portRate);
			int portDuplexMode = appMessage
					.getIntValue(TagConst.PORT_DUPLEX_MODE);
			enbStatus.setPortDuplexMode(portDuplexMode);
		} catch (Exception e) {
		}

		return enbStatus;
	}
	

	/**
	 * 解析单板状态信息
	 * 
	 * @param buf
	 * @param isBBU
	 *            单板是否是BBU单板
	 * @return
	 * @throws BizException
	 */
	public static List<BoardStatus> parseBoardStatus(EnbAppMessage appMessage)
			throws BizException {

		List<BoardStatus> statusList = new ArrayList<BoardStatus>();
		BoardStatus boardStatus = new BoardStatus();
		boardStatus.setRackNo(appMessage.getIntValue(TagConst.RACK_NO));
		boardStatus.setShelfNo(appMessage.getIntValue(TagConst.SHELF_NO));
		boardStatus.setSlotNo(appMessage.getIntValue(TagConst.SLOT_NO));
		boardStatus.setStatus(appMessage.getIntValue(TagConst.STATE));
		boardStatus.setHardwareSerialNo(appMessage
				.getStringValue(TagConst.BBU_PRODUCTION_SN));
		boardStatus.setHardwareVersion(appMessage
				.getStringValue(TagConst.BBU_HARDWARE_VERSION));

		statusList.add(boardStatus);

		int rruBoardNum = appMessage.getIntValue(TagConst.RRU_BOARD_NUM);
		if (rruBoardNum != 0) {
			byte[] rruBoardList = appMessage
					.getByteValue(TagConst.BOARD_STATUS);
			int offset = 0;
			int unitLength = rruBoardList.length / rruBoardNum;
			for (int i = 0; i < rruBoardNum; i++) {
				byte[] rruBoardBytes = new byte[unitLength];
				System.arraycopy(rruBoardList, offset, rruBoardBytes, 0,
						unitLength);
				BoardStatus rruStatus = getRRUBoardStatus(rruBoardBytes);
				statusList.add(rruStatus);
				offset += unitLength;
			}
		}
		return statusList;
	}
	
	
	private static BoardStatus getRRUBoardStatus(byte[] buf)
			throws BizException {
		BoardStatus boardStatus = new BoardStatus();
		ByteBuffer buffer = ByteBuffer.wrap(buf);
		Map<Integer, List<Object>> fieldMap = getFieldMap(buffer);
		boardStatus.setRackNo(getIntegerValue(fieldMap, TagConst.RACK_NO));
		boardStatus.setShelfNo(getIntegerValue(fieldMap, TagConst.SHELF_NO));
		boardStatus.setSlotNo(getIntegerValue(fieldMap, TagConst.SLOT_NO));
		boardStatus.setHardwareSerialNo(getStringValue(fieldMap,
				TagConst.RRU_PRODUCTION_SN));
		boardStatus.setHardwareVersion(getStringValue(fieldMap,
				TagConst.RRU_HARDWARE_VERSION));
		boardStatus.setStatus(getIntegerValue(fieldMap, TagConst.STATE));
		return boardStatus;
	}

	/**
	 * 解析RRU射频状态
	 * 
	 * @param appMessage
	 * @param rruChannelNum
	 *            RRU单板通道数
	 * @return
	 * @throws BizException
	 */
	public static List<RruRfStatus> parseRruRfStatus(EnbAppMessage appMessage,
			int rruChannelNum) throws BizException {

		List<RruRfStatus> statusList = new ArrayList<RruRfStatus>();

		int channelNo = appMessage.getIntValue(TagConst.CHANNEL_NO);
		byte[] rfStatusList = appMessage.getByteValue(TagConst.RRU_RF_STATUS);

		if (channelNo == rruChannelNum) {
			int unitLength = rfStatusList.length / rruChannelNum;
			int offset = 0;
			for (int i = 0; i < rruChannelNum; i++) {
				byte[] rfBytes = new byte[unitLength];
				System.arraycopy(rfStatusList, offset, rfBytes, 0, unitLength);
				ByteBuffer buffer = ByteBuffer.wrap(rfBytes);
				RruRfStatus status = createRruRfStatus(i, buffer);
				statusList.add(status);
				offset += unitLength;
			}
		} else {
			ByteBuffer buffer = ByteBuffer.wrap(rfStatusList);
			RruRfStatus status = createRruRfStatus(channelNo, buffer);
			statusList.add(status);
		}
		return statusList;
	}


	/**
	 * 解析RRU光口状态
	 * 
	 * @param buf
	 * @param moduleNo
	 * @return
	 * @throws BizException
	 */
	public static List<RruOpticalStatus> parseRruOpticalStatus(
			EnbAppMessage appMessage) throws BizException {
		List<RruOpticalStatus> statusList = new ArrayList<RruOpticalStatus>();

		int moduleNo = appMessage.getIntValue(TagConst.FIBER_OPTIC_NO);
		byte[] opStatusList = appMessage
				.getByteValue(TagConst.RRU_FIBER_OPTICAL_STATUS);
		if (moduleNo == EnbStatusConstants.FIBER_OPTICAL_MODULE_COUNT) {
			int unitLength = opStatusList.length / 2;
			int offset = 0;
			for (int i = 0; i < EnbStatusConstants.FIBER_OPTICAL_MODULE_COUNT; i++) {
				byte[] opStatusBytes = new byte[unitLength];
				System.arraycopy(opStatusList, offset, opStatusBytes, 0,
						unitLength);
				ByteBuffer buffer = ByteBuffer.wrap(opStatusBytes);
				RruOpticalStatus status = createRruOpticalStatus(i, buffer);
				statusList.add(status);
				offset += unitLength;
			}
		} else {
			ByteBuffer buffer = ByteBuffer.wrap(opStatusList);
			RruOpticalStatus status = createRruOpticalStatus(moduleNo, buffer);
			statusList.add(status);
		}
		return statusList;
	}
	/**
	 * 解析BBU光口状态
	 * 
	 * @param buf
	 * @param moduleNo
	 * @return
	 * @throws BizException
	 */
	public static List<BbuOpticalStatus> parseBbuOpticalStatus(
			EnbAppMessage appMessage) throws BizException {
		List<BbuOpticalStatus> statusList = new ArrayList<BbuOpticalStatus>();

		int moduleNo = appMessage.getIntValue(TagConst.FIBER_OPTIC_NO);
		byte[] opStatusList = appMessage
				.getByteValue(TagConst.BBU_FIBER_OPTICAL_STATUS);
		if (moduleNo == EnbStatusConstants.BBUFIBER_OPTICAL_MODULE_COUNT) {
			int unitLength = opStatusList.length / moduleNo;
			int offset = 0;
			for (int i = 0; i < EnbStatusConstants.BBUFIBER_OPTICAL_MODULE_COUNT; i++) {
				byte[] opStatusBytes = new byte[unitLength];
				System.arraycopy(opStatusList, offset, opStatusBytes, 0,
						unitLength);
				ByteBuffer buffer = ByteBuffer.wrap(opStatusBytes);
				BbuOpticalStatus status = createBbuOpticalStatus(i, buffer);
				statusList.add(status);
				offset += unitLength;
			}
		} else {
			ByteBuffer buffer = ByteBuffer.wrap(opStatusList);
			BbuOpticalStatus status = createBbuOpticalStatus(moduleNo, buffer);
			statusList.add(status);
		}
		return statusList;
	}
	

	/**
	 * 解析xGWAddress查询
	 * @param appMessage 
	 * @return
	 */
	public static EnbxGWAddressCollection parsexGWAddress(EnbAppMessage appMessage) {
		EnbxGWAddressCollection enbxGWAddressCollection = new EnbxGWAddressCollection();
		List<Object> list = appMessage.getListValue(TagConst.XCG_ADDRESS_COUNT);
		if(list != null){
		for (Object object : list) {
			EnbxGWAddress enbxGWAddress = new EnbxGWAddress();
			CompositeValue compositeValue = (CompositeValue) object;
			enbxGWAddress.setLocalIp(compositeValue.getStringValue(TagConst.xGWAddressLocalIP));
			enbxGWAddress.setDesIp(compositeValue.getStringValue(TagConst.xGWAddressDstIP));
			enbxGWAddress.setVlanIndex(compositeValue.getIntValue(TagConst.xGWAddressVlanIndex));
			enbxGWAddressCollection.addEnbxGWAddress(enbxGWAddress);
		}}

		return enbxGWAddressCollection;
	}
	
	public static TimeDelayCollection parseTimeDelayModel(EnbAppMessage appMessage) {
		TimeDelayCollection timeDelayCollection = new TimeDelayCollection();
		List<Object> list = appMessage.getListValue(TagConst.TIME_DELAY_COUNT);
		if(list != null){
		for (Object object : list) {
			TimeDelayModel timeDelayModel = new TimeDelayModel();
			CompositeValue compositeValue = (CompositeValue) object;
			timeDelayModel.setTimeDelay(compositeValue.getIntValue(TagConst.TIME_DELAY));
			timeDelayCollection.addTimeDelayModel(timeDelayModel);
		}}

		return timeDelayCollection;
	}
	public static  PackageTestCollection paraPacketLossRate(
			EnbAppMessage appMessage) {
		PackageTestCollection packageTestCollection = new PackageTestCollection();
		List<Object> list = appMessage.getListValue(TagConst.PACKAGE_TEST_COUNT);
		if(list != null){
			for (Object object : list) {
				PacketLossRateModel packetLossRateModel = new PacketLossRateModel();
				CompositeValue compositeValue = (CompositeValue) object;
				packetLossRateModel.setLocalpacketLossRate(compositeValue.getStringValue(TagConst.LOCAL_PACKETLOSSRATE));
				packetLossRateModel.setDstpacketLossRate(compositeValue.getStringValue(TagConst.DST_PACKETLOSSRATE));
				packageTestCollection.addPacketLossRateModel(packetLossRateModel);
			}}
		return packageTestCollection;
	}
	public static TimeDelayModel configParseTimeDelayModel(EnbAppMessage appMessage){
		TimeDelayModel timeDelayModel = new TimeDelayModel();
		timeDelayModel.setResult(appMessage.getIntValue(TagConst.RESULT));
		timeDelayModel.setErrorMsg(appMessage.getStringValue(TagConst.ERR_MSG));
		return timeDelayModel;
	}
	
	
	/**
	 * 解析RRU运行状态
	 * 
	 * @param buf
	 * @return
	 * @throws BizException
	 */
	public static RruRunningStatus parseRruRunningStatus(
			EnbAppMessage appMessage) throws BizException {

		RruRunningStatus status = new RruRunningStatus();

		status.setClockStatus(appMessage.getIntValue(TagConst.CLOCK_STATE));
		status.setDpdTrainResult(appMessage
				.getIntValue(TagConst.DPD_TRAINING_RESULT));
		status.setIrInfWorkMode(appMessage.getIntValue(TagConst.IR_WORK_MODE));
		status.setMainBoardTemp(appMessage
				.getIntValue(TagConst.RF_MB_TEMPERATURE));
		status.setRfLocalFreq(appMessage.getIntValue(TagConst.RF_LOCAL_FREQ));
		status.setRfLocalStatus(appMessage
				.getIntValue(TagConst.RF_LOCAL_FREQ_STATE));
		status.setRunningStatus(appMessage
				.getIntValue(TagConst.RRU_RUNNING_STATE));
		status.setSlaveBoardTemp(appMessage
				.getIntValue(TagConst.RF_AB_TEMPERATURE));

		return status;
	}

	/**
	 * 解析RRU门限信息
	 * 
	 * @param buf
	 * @return
	 * @throws BizException
	 */
	public static RruThreshold parseRruThreshold(EnbAppMessage appMessage)
			throws BizException {

		RruThreshold threshold = new RruThreshold();

		threshold.setBoardTempThres(appMessage
				.getIntValue(TagConst.BOARD_TEMP_THRES));
		threshold.setRfChannelTempThres(appMessage
				.getIntValue(TagConst.CHANNEL_TEMP_THRES));
		threshold.setVswrThres(appMessage.getIntValue(TagConst.VSWR_THRESHOLD));

		return threshold;
	}

	private static RruOpticalStatus createRruOpticalStatus(int moduleNo,
			ByteBuffer buffer) throws BizException {

		Map<Integer, List<Object>> fieldMap = getFieldMap(buffer);

		RruOpticalStatus status = new RruOpticalStatus();
		status.setCurrent(getIntegerValue(fieldMap, TagConst.CURRENT));
		status.setInPlaceFlag(getIntegerValue(fieldMap, TagConst.INSERT_STATE));
		status.setManufacture(getStringValue(fieldMap,
				TagConst.MODULE_MANUFACTURE));
		status.setModuleNo(moduleNo);
		status.setReceivePower(getIntegerValue(fieldMap, TagConst.RX_POWER));
		status.setSendPower(getIntegerValue(fieldMap, TagConst.TX_POWER));
		status.setTemperature(getIntegerValue(fieldMap,
				TagConst.MODULE_TEMPERATURE));
		status.setTransBitRate(getIntegerValue(fieldMap,
				TagConst.MODULE_TRANS_RATE));
		status.setVoltage(getIntegerValue(fieldMap, TagConst.VOLTAGE));

		return status;
	}
	
	private static BbuOpticalStatus createBbuOpticalStatus(int moduleNo,
			ByteBuffer buffer) throws BizException {

		Map<Integer, List<Object>> fieldMap = getFieldMap(buffer);

		BbuOpticalStatus status = new BbuOpticalStatus();
		status.setCurrent(getIntegerValue(fieldMap, TagConst.CURRENT));
		status.setInPlaceFlag(getIntegerValue(fieldMap, TagConst.INSERT_STATE));
		status.setManufacture(getStringValue(fieldMap,
				TagConst.MODULE_MANUFACTURE1));
		status.setModuleNo(moduleNo);
		status.setReceivePower(getIntegerValue(fieldMap, TagConst.RX_POWER));
		status.setSendPower(getIntegerValue(fieldMap, TagConst.TX_POWER));
		status.setTemperature(getIntegerValue(fieldMap,
				TagConst.MODULE_TEMPERATURE));
		status.setTransBitRate(getIntegerValue(fieldMap,
				TagConst.MODULE_TRANS_RATE));
		status.setVoltage(getIntegerValue(fieldMap, TagConst.VOLTAGE));

		return status;
	}

	private static RruRfStatus createRruRfStatus(int channelNo,
			ByteBuffer buffer) throws BizException {

		Map<Integer, List<Object>> fieldMap = getFieldMap(buffer);

		RruRfStatus status = new RruRfStatus();

		status.setChannelNo(channelNo);
		status.setChannelTemperature(getIntegerValue(fieldMap,
				TagConst.CHANNEL_TEMPERATURE));
		status.setDlAntStatus(getIntegerValue(fieldMap, TagConst.DL_ANT_STATE));
		status.setDlPowerReadResult(getIntegerValue(fieldMap,
				TagConst.DL_POWER_RESULT));
		status.setReceiveGain(getIntegerValue(fieldMap,
				TagConst.CHANNEL_RX_GAIN));
		status.setReceivePower(getIntegerValue(fieldMap,
				TagConst.CHANNEL_RX_POWER));
		status.setSendGain(getIntegerValue(fieldMap, TagConst.CHANNEL_TX_GAIN));
		status.setSendPower(getIntegerValue(fieldMap, TagConst.CHANNEL_TX_POWER));
		status.setUlAntStatus(getIntegerValue(fieldMap, TagConst.UL_ANT_STATE));
		status.setUlPowerReadResult(getIntegerValue(fieldMap,
				TagConst.UL_POWER_RESULT));
		status.setVswr(getIntegerValue(fieldMap, TagConst.CHANNEL_VSWR));
		status.setVswrCalResult(getIntegerValue(fieldMap, TagConst.VSWR_RESULT));

		return status;
	}

	private static Map<Integer, List<Object>> getFieldMap(ByteBuffer buffer)
			throws BizException {
		TlvFieldUtil tlvFieldUtil = new TlvFieldUtil(EnbModule.getInstance()
				.getTagCollection());
		return tlvFieldUtil.parse(buffer);
	}

	private static int getIntegerValue(Map<Integer, List<Object>> fieldMap,
			int tag) {
		return Integer.valueOf(fieldMap.get(tag).get(0).toString());
	}

	private static String getStringValue(Map<Integer, List<Object>> fieldMap,
			int tag) {
		return fieldMap.get(tag).get(0).toString();
	}

	/**
	 * 获取长整型时间数值(yyyyMMddhhmmss)
	 * 
	 * @param tag
	 * @return
	 */
	private static long getEnbTime(long time) {
		String timeStr = Long.toHexString(time);
		for (int i = timeStr.length(); i < 16; i++) {
			timeStr = "0" + timeStr;
		}
		long year = Long.valueOf(timeStr.substring(0, 4), 16);
		long month = Long.valueOf(timeStr.substring(4, 6), 16);
		long day = Long.valueOf(timeStr.substring(6, 8), 16);
		long hour = Long.valueOf(timeStr.substring(8, 10), 16);
		long minute = Long.valueOf(timeStr.substring(10, 12), 16);
		long second = Long.valueOf(timeStr.substring(12, 14), 16);
		String str = StringUtils.appendPrefix(String.valueOf(year), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(month), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(day), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(hour), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(minute), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(second), "0", 2);
		return Long.valueOf(str);
	}

	/**
	 * 解析动态查询项目的结果
	 * 
	 * @param appMessage
	 * @return
	 * @throws BizException
	 */
	public static EnbDynamicInfo parseDynamicInfo(EnbAppMessage appMessage)
			throws BizException {
		// 获取动态项查询标识
		int queryFlag = appMessage.getIntValue(TagConst.STATUS_QUERY_FLAG);
		if (queryFlag == EnbDynamicInfoCondition.XW7102_STATUS) {
			// 101 XW7102 状态信息查询
			return parseIbtsStatus(appMessage);
		} else if (queryFlag == EnbDynamicInfoCondition.XW7102_RF_STATUS) {
			// 102 XW7102 射频状态信息查询
			return parseIbtsRfStatus(appMessage);
		} else if (queryFlag == EnbDynamicInfoCondition.XW7102_THRESHOLD) {
			// 103 XW7102 门限信息查询
			return parseIbtsThreshold(appMessage);
		} else if (queryFlag == EnbDynamicInfoCondition.XW7102_RUNNING_STATUS) {
			// 104 XW7102 运行状态查询
			return parseIbtsRunningStatus(appMessage);
		} else if (queryFlag == EnbDynamicInfoCondition.XW7102_FIBER_STATUS) {
			// 105 XW7102 光口状态查询
			return parseIbtsOpticalStatus(appMessage);
		} else if (queryFlag == EnbDynamicInfoCondition.XW7102_BOARD_STATUS) {
			// 106 XW7102 单板状态信息查询
			return parseIbtsBoardStatus(appMessage);
		}

		return null;
	}

	/**
	 * 解析ibts状态信息
	 * 
	 * @param buf
	 * @return
	 * @throws BizException
	 */
	private static IbtsStatus parseIbtsStatus(EnbAppMessage appMessage)
			throws BizException {

		IbtsStatus enbStatus = new IbtsStatus();

		// eNB时间需要转换格式
		long enbTime = getEnbTime(appMessage.getLongValue(TagConst.ENB_TIME));
		enbStatus.setEnbTime(String.valueOf(enbTime));

		enbStatus.setClockType(appMessage.getIntValue(TagConst.ENB_CLOCK_TYPE));
		enbStatus.setClockStatus(appMessage
				.getIntValue(TagConst.ENB_CLOCK_STATUS));
		enbStatus.setTemperature(appMessage
				.getIntValue(TagConst.ENB_TEMPERATURE));
		enbStatus.setVisibleSatelliteNum(appMessage
				.getIntValue(TagConst.ENB_VISIBLE_SATELLITES));
		enbStatus.setTrackSatelliteNum(appMessage
				.getIntValue(TagConst.ENB_TRACK_SATELLITES));
		enbStatus.setRunningTime(String.valueOf(appMessage
				.getIntValue(TagConst.ENB_RUNNING_TIME)));
		enbStatus.setState(appMessage.getIntValue(TagConst.STATE));
        
		enbStatus.setRflofreqState(appMessage.getIntValue(TagConst.RFLOFREGSTATE));
		return enbStatus;
	}

	/**
	 * 解析ibts射频状态信息
	 * 
	 * @param appMessage
	 * @return
	 * @throws BizException
	 */
	private static IbtsRfStatus parseIbtsRfStatus(EnbAppMessage appMessage)
			throws BizException {
		IbtsRfStatus rfStatus = new IbtsRfStatus();
		List<Object> list = appMessage.getListValue(TagConst.IBTS_RF_STATUS);
		if(list != null){
		for (Object object : list) {
			IbtsRfChannelStatus rfChannelStatus = new IbtsRfChannelStatus();
			CompositeValue compositeValue = (CompositeValue) object;
			rfChannelStatus.setChannelNo(compositeValue
					.getIntValue(TagConst.CHANNEL_NO));
			rfChannelStatus.setChannelTemperature(compositeValue
					.getIntValue(TagConst.CHANNEL_TEMPERATURE));
			rfChannelStatus.setDlAntStatus(compositeValue
					.getIntValue(TagConst.DL_ANT_STATE));
			rfChannelStatus.setDpdTrainingResult(compositeValue
					.getIntValue(TagConst.DPDTRAININGRESULT));
			rfChannelStatus.setReceiveGain(compositeValue
					.getIntValue(TagConst.CHANNEL_RX_GAIN));
			rfChannelStatus.setReceivePower(compositeValue
					.getIntValue(TagConst.CHANNEL_RX_POWER));
			rfChannelStatus.setSendGain(compositeValue
					.getIntValue(TagConst.CHANNEL_TX_GAIN));
			rfChannelStatus.setSendPower(compositeValue
					.getIntValue(TagConst.CHANNEL_TX_POWER));
			rfChannelStatus.setUlAntStatus(compositeValue
					.getIntValue(TagConst.UL_ANT_STATE));
			rfChannelStatus.setPowerResult(compositeValue
					.getIntValue(TagConst.POWERRESULT));
			rfChannelStatus.setVswr(compositeValue
					.getIntValue(TagConst.CHANNEL_VSWR));
			rfChannelStatus.setVswrCalResult(compositeValue
					.getIntValue(TagConst.VSWR_RESULT));

			rfStatus.addIbtsRfChannelStatus(rfChannelStatus);
		}}

		return rfStatus;
	}

	/**
	 * 解析ibts运行状态信息
	 * 
	 * @param buf
	 * @return
	 * @throws BizException
	 */
	private static IbtsRunningStatus parseIbtsRunningStatus(
			EnbAppMessage appMessage) throws BizException {

		IbtsRunningStatus status = new IbtsRunningStatus();

		status.setDpdTrainResult(appMessage
				.getIntValue(TagConst.DPD_TRAINING_RESULT));
		status.setMainBoardTemp(appMessage
				.getIntValue(TagConst.RF_MB_TEMPERATURE));
		status.setRfLocalFreq(appMessage.getIntValue(TagConst.RF_LOCAL_FREQ));
		status.setRfLocalStatus(appMessage
				.getIntValue(TagConst.RF_LOCAL_FREQ_STATE));

		return status;
	}

	/**
	 * 解析ibts门限信息
	 * 
	 * @param buf
	 * @return
	 * @throws BizException
	 */
	private static IbtsThreshold parseIbtsThreshold(EnbAppMessage appMessage)
			throws BizException {

		IbtsThreshold threshold = new IbtsThreshold();

		threshold.setBoardTempThres(appMessage
				.getIntValue(TagConst.BOARD_TEMP_THRES));
		threshold.setRfChannelTempThres(appMessage
				.getIntValue(TagConst.CHANNEL_TEMP_THRES));
		threshold.setVswrThres(appMessage.getIntValue(TagConst.VSWR_THRESHOLD));

		return threshold;
	}

	/**
	 * 解析ibts光口状态信息
	 * 
	 * @param buf
	 * @return
	 * @throws BizException
	 */
	private static IbtsOpticalStatusCollection parseIbtsOpticalStatus(
			EnbAppMessage appMessage) throws BizException {
		// status.setCurrent(getIntegerValue(fieldMap, TagConst.CURRENT));
		// status.setInPlaceFlag(getIntegerValue(fieldMap,
		// TagConst.INSERT_STATE));
		// status.setManufacture(getStringValue(fieldMap,
		// TagConst.MODULE_MANUFACTURE));
		// status.setModuleNo(moduleNo);
		// status.setReceivePower(getIntegerValue(fieldMap, TagConst.RX_POWER));
		// status.setSendPower(getIntegerValue(fieldMap, TagConst.TX_POWER));
		// status.setTemperature(getIntegerValue(fieldMap,
		// TagConst.MODULE_TEMPERATURE));
		// status.setTransBitRate(getIntegerValue(fieldMap,
		// TagConst.MODULE_TRANS_RATE));
		// status.setVoltage(getIntegerValue(fieldMap, TagConst.VOLTAGE));

		IbtsOpticalStatusCollection opticalStatusCollection = new IbtsOpticalStatusCollection();
		List<Object> list = appMessage
				.getListValue(TagConst.IBTS_OPTICAL_STATUS);
		for (Object object : list) {
			IbtsOpticalStatus status = new IbtsOpticalStatus();
			CompositeValue compositeValue = (CompositeValue) object;
			status.setCurrent(compositeValue.getIntValue(TagConst.CURRENT));
			status.setInPlaceFlag(compositeValue
					.getIntValue(TagConst.INSERT_STATE));
			status.setManufacture(compositeValue
					.getStringValue(TagConst.MODULE_MANUFACTURE));
			status.setModuleNo(compositeValue.getIntValue(TagConst.FIBER_OPTIC_NO));
			status.setReceivePower(compositeValue
					.getIntValue(TagConst.RX_POWER));
			status.setSendPower(compositeValue.getIntValue(TagConst.TX_POWER));
			status.setTemperature(compositeValue
					.getIntValue(TagConst.MODULE_TEMPERATURE));
			status.setTransBitRate(compositeValue
					.getIntValue(TagConst.MODULE_TRANS_RATE));
			status.setVoltage(compositeValue.getIntValue(TagConst.VOLTAGE));
			opticalStatusCollection.addIbtsOpticalStatus(status);
		}

		return opticalStatusCollection;
	}

	/**
	 * 解析ibts单板状态信息
	 * 
	 * @param buf
	 * @return
	 * @throws BizException
	 */
	private static IbtsBoardStatus parseIbtsBoardStatus(EnbAppMessage appMessage)
			throws BizException {

		IbtsBoardStatus boardStatus = new IbtsBoardStatus();
		boardStatus.setStatus(appMessage.getIntValue(TagConst.STATE));
		boardStatus.setMbdHardWareVersion(appMessage.getStringValue(TagConst.MBD_HARDWARE_VERSION));
		boardStatus.setMbdProductionSn(appMessage.getStringValue(TagConst.MBD_PRODUCTION_SN));
		boardStatus.setPauHardWareVersion(appMessage.getStringValue(TagConst.PAU_HARDWARE_VERSION));
		boardStatus.setPauProductionSn(appMessage.getStringValue(TagConst.PAU_PRODUCTION_SN));

		return boardStatus;
	}
	
	/**
	 * 空口流量测试查询
	 * @param appMessage
	 * @return
	 */
	public static AirFlowTest parseAirFlowTest(EnbAppMessage appMessage) {
		AirFlowTest airFlowTest = new AirFlowTest();
		airFlowTest.setIpAddress(appMessage
				.getStringValue(TagConst.xGWAddressLocalIP));
		airFlowTest.setPacketLength(appMessage
				.getIntValue(TagConst.PACKETLENGTH));
		airFlowTest.setFlowRate(appMessage.getIntValue(TagConst.FLOWRATE));
		return airFlowTest;
	}
	public static AirFlowTest parseAriFlowBeginEnd(EnbAppMessage appMessage) {
		AirFlowTest airFlowTest = new AirFlowTest();
		airFlowTest.setBeginResult(appMessage.getIntValue(TagConst.RESULT));
		airFlowTest.setErrorStr(appMessage.getStringValue(TagConst.ERR_MSG));
		return airFlowTest;
	}
	public static PacketLossRateModel parseS1PackageTest(EnbAppMessage appMessage) {
		PacketLossRateModel packetLossRateModel = new PacketLossRateModel();
		packetLossRateModel.setResult(appMessage.getIntValue(TagConst.RESULT));
		packetLossRateModel.setErrorMsg(appMessage.getStringValue(TagConst.ERR_MSG));
		return packetLossRateModel;
	}
	

	
	public static  PacketLossRateModel configParaPacketLossRate(
			EnbAppMessage appMessage) {
		PacketLossRateModel packetLossRateModel = new PacketLossRateModel();
		packetLossRateModel.setResult(appMessage.getIntValue(TagConst.RESULT));
		packetLossRateModel.setErrorMsg(appMessage.getStringValue(TagConst.ERR_MSG));
		return packetLossRateModel;
	}


	



}
