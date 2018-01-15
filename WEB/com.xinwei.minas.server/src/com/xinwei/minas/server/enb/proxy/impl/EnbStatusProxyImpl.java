/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.proxy.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbDynamicInfo;
import com.xinwei.minas.enb.core.model.EnbDynamicInfoCondition;
import com.xinwei.minas.enb.core.model.status.AirFlowTest;
import com.xinwei.minas.enb.core.model.status.BbuOpticalStatus;
import com.xinwei.minas.enb.core.model.status.BoardStatus;
import com.xinwei.minas.enb.core.model.status.EnbStatus;
import com.xinwei.minas.enb.core.model.status.EnbStatusConfigCondition;
import com.xinwei.minas.enb.core.model.status.EnbStatusConstants;
import com.xinwei.minas.enb.core.model.status.EnbStatusQueryCondition;
import com.xinwei.minas.enb.core.model.status.EnbxGWAddress;
import com.xinwei.minas.enb.core.model.status.PacketLossRateModel;
import com.xinwei.minas.enb.core.model.status.RruOpticalStatus;
import com.xinwei.minas.enb.core.model.status.RruRfStatus;
import com.xinwei.minas.enb.core.model.status.RruRunningStatus;
import com.xinwei.minas.enb.core.model.status.RruThreshold;
import com.xinwei.minas.enb.core.model.status.TimeDelayModel;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbConnector;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.net.EnbMessageHelper;
import com.xinwei.minas.server.enb.net.EnbStatusMessageHelper;
import com.xinwei.minas.server.enb.net.TagConst;
import com.xinwei.minas.server.enb.proxy.EnbStatusProxy;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.omp.core.utils.DateUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB扩展业务代理接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatusProxyImpl implements EnbStatusProxy {

	private static Log log = LogFactory.getLog(EnbStatusProxyImpl.class);

	private EnbConnector enbConnector;

	public void setEnbConnector(EnbConnector enbConnector) {
		this.enbConnector = enbConnector;
	}

	@Override
	public List<Object> queryStatus(long moId, EnbStatusQueryCondition condition)
			throws Exception {
		boolean isTest = false;
		if (isTest) {
			return createTestData(condition);
		}

		String flag = condition.getFlag();
		EnbAppMessage appMessage = null;
		// eNB状态信息
		if (flag.equals(EnbStatusConstants.ENB_STATUS)) {
			appMessage = createEnbStatusMessage(condition);
		} else if (flag.equals(EnbStatusConstants.BOARD_STATUS)) {
			// 单板状态信息
			appMessage = createAllBoardStatusMessage(condition);
		} else if (flag.equals(EnbStatusConstants.RRU_RF_STATUS)) {
			// 查询RRU射频状态特殊处理
			return queryRruRfStatus(moId, condition);
		} else if (flag.equals(EnbStatusConstants.RRU_THRESHOLD_STATUS)) {
			// RRU门限查询
			appMessage = createBoardStatusMessage(condition, 4);
		} else if (flag.equals(EnbStatusConstants.RRU_RUNNING_STATUS)) {
			// RRU运行状态，特殊处理
			return queryRruRunningStatus(moId, condition);
		} else if (flag.equals(EnbStatusConstants.RRU_OPTICAL_STATUS)) {
			// RRU光模块状态
			appMessage = createBoardStatusMessage(condition, 6);
			appMessage.addTagValue(TagConst.FIBER_OPTIC_NO,
					condition.getModuleNo());
		} else if(flag.equals(EnbStatusConstants.BBU_OPTICAL_STATUS)){
			//BBU光模块状态
			appMessage = createQueryMessage(condition, 8);
			appMessage.addTagValue(TagConst.FIBER_OPTIC_NO,
					condition.getModuleNo());
		}
		else if (flag.equals(EnbStatusConstants.RRU_CHANNEL_NUM)) {
			// RRU通道个数
			appMessage = createBoardStatusMessage(condition, 7);
		}else if(flag.equals(EnbStatusConstants.xGW_ADDRESS_STATUS)){
			//xGW地址对信息查询
			appMessage = createQueryMessage(condition,9);
		}else if(flag.equals(EnbStatusConstants.TIME_DELAY)){
			//S1用户面时延测量结果查询
			appMessage = createQueryMessage(condition,16);
		}
		else if(flag.equals(EnbStatusConstants.PACKET_LOSS_RATE)){
			//S1用户面丢包率检测
			appMessage = createQueryMessage(condition,14);
		}
		else if(flag.equals(EnbStatusConstants.AIR_FLOW_STATUS)){
			//空口流量测试查询
			appMessage = createQueryMessage(condition,10);
		}else if(flag.equals(EnbStatusConstants.AIR_FLOW_BEGIN)){
			//空口流量测试开始灌包
			appMessage = config(condition,11);
			appMessage.addTagValue(TagConst.xGWAddressLocalIP, condition.getIpAddress());
		}else if(flag.equals(EnbStatusConstants.AIR_FLOW_END)){
			//空口流量测试结束灌包
			appMessage = config(condition,12);
		}else if(flag.equals(EnbStatusConstants.S1_PACKAGE_TEST)){
			//S1丢包率检测开始
			appMessage = config(condition,13);
			try {
				// 调低底层通信层发送消息, 同步等待应答
				if (enbConnector != null) {
					EnbAppMessage resp = enbConnector.syncInvoke(appMessage,3000);
					EnbMessageHelper.parseResponse(resp);
					return parseStatus(moId, resp, condition);
				}
			} catch (TimeoutException e) {
				throw new Exception(OmpAppContext.getMessage("enb_reply_over_time"));
			} catch (Exception e) {
				e.printStackTrace();
				log.error("query enb status error.", e);
				throw new Exception(e.getLocalizedMessage());
			}
		}else if(flag.equals(EnbStatusConstants.S1_TIME_TEST)){
			//S1时延检测开始
			appMessage = config(condition,15);
		}
		try {
			// 调低底层通信层发送消息, 同步等待应答
			if (enbConnector != null) {
				EnbAppMessage resp = enbConnector.syncInvoke(appMessage);
				EnbMessageHelper.parseResponse(resp);
				return parseStatus(moId, resp, condition);
			}
		} catch (TimeoutException e) {
			throw new Exception(OmpAppContext.getMessage("enb_reply_over_time"));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("query enb status error.", e);
			throw new Exception(e.getLocalizedMessage());
		}
		return null;
	}



	/**
	 * 配置公共方法
	 * @param condition
	 * @param configFlag
	 * @return
	 */
	private EnbAppMessage config(EnbStatusQueryCondition condition,int configFlag){
		EnbAppMessage appMessage = new EnbAppMessage();
		appMessage.setEnbId(condition.getEnbId());
		appMessage.setMa(EnbMessageConstants.MA_CONF);
		appMessage.setMoc(EnbMessageConstants.MOC_STATUS_CONFIG_REQ_AND_RES);
		appMessage.setActionType(EnbMessageConstants.ACTION_CONFIG);
		appMessage.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		appMessage.addTagValue(TagConst.STATUS_CONFIG_FLAG, configFlag);
		return appMessage;
	}
	

	

	private EnbAppMessage createQueryMessage(
			EnbStatusQueryCondition condition, int queryFlag) {
		EnbAppMessage appMessage = createMessage(condition);
		appMessage.addTagValue(TagConst.STATUS_QUERY_FLAG, queryFlag);
		return appMessage;
	}
	

	/**
	 * 查询RRU射频状态
	 * 
	 * @param moId
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	private List<Object> queryRruRfStatus(long moId,
			EnbStatusQueryCondition condition) throws Exception {

		int rruChannelNum = queryRRUChannelNum(moId, condition);
		// RRU射频状态，默认查询全部通道
		EnbAppMessage appMessage = createBoardStatusMessage(condition, 3);
		appMessage.addTagValue(TagConst.CHANNEL_NO, rruChannelNum);

		try {
			// 调低底层通信层发送消息, 同步等待应答
			EnbAppMessage resp = enbConnector.syncInvoke(appMessage, 5000);
			EnbMessageHelper.parseResponse(resp);
			List<RruRfStatus> rfStatus = EnbStatusMessageHelper
					.parseRruRfStatus(resp, rruChannelNum);
			List<Object> list = new LinkedList<Object>();
			list.addAll(rfStatus);
			return list;
		} catch (TimeoutException e) {
			throw new Exception(OmpAppContext.getMessage("enb_reply_over_time"));
		} catch (Exception e) {
			log.error("query rru rf status error.", e);
			throw new Exception(e.getLocalizedMessage());
		}
	}

	/**
	 * 查询RRU运行状态
	 * 
	 * @param moId
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	private List<Object> queryRruRunningStatus(long moId,
			EnbStatusQueryCondition condition) throws Exception {

		int rruChannelNum = queryRRUChannelNum(moId, condition);
		EnbAppMessage appMessage = createBoardStatusMessage(condition, 5);
		appMessage.addTagValue(TagConst.CHANNEL_NO, rruChannelNum);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			EnbAppMessage resp = enbConnector.syncInvoke(appMessage);
			EnbMessageHelper.parseResponse(resp);
			RruRunningStatus rruRunningStatus = EnbStatusMessageHelper
					.parseRruRunningStatus(resp);
			// 设置通道数
			rruRunningStatus.setChannelNum(rruChannelNum);
			List<Object> list = new LinkedList<Object>();
			list.add(rruRunningStatus);
			return list;
		} catch (TimeoutException e) {
			throw new Exception(OmpAppContext.getMessage("enb_reply_over_time"));
		} catch (Exception e) {
			log.error("query rru running status error.", e);
			throw new Exception(e.getLocalizedMessage());
		}
	}

	/**
	 * 查询RRU通道个数
	 * 
	 * @param moId
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	private int queryRRUChannelNum(long moId, EnbStatusQueryCondition condition)
			throws Exception {
		EnbAppMessage appMessage = createBoardStatusMessage(condition, 7);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			EnbAppMessage resp = enbConnector.syncInvoke(appMessage);
			EnbMessageHelper.parseResponse(resp);
			// RRU通道个数
			return resp.getIntValue(TagConst.RRU_CHANNEL_NUM);
		} catch (TimeoutException e) {
			throw new Exception(OmpAppContext.getMessage("enb_reply_over_time"));
		} catch (Exception e) {
			log.error("query rru channel num error.", e);
			throw new Exception(e.getLocalizedMessage());
		}
	}

	@Override
	public void configStatus(long moId, EnbStatusConfigCondition condition)
			throws Exception {
		String flag = condition.getFlag();
		EnbAppMessage appMessage = createConfigStatusMessage(condition);
		if (flag.equals(EnbStatusConstants.ENB_TIME)) {
			// 设置基站时间
			appMessage.addTagValue(TagConst.STATUS_CONFIG_FLAG, 1);
			appMessage.addTagValue(TagConst.ENB_TIME, condition.getEnbTime());
		} else if (flag.equals(EnbStatusConstants.RF_SWITCH)) {
			// 设置功放开关
			appMessage.addTagValue(TagConst.RACK_NO, condition.getRackNo());
			appMessage.addTagValue(TagConst.SHELF_NO, condition.getShelfNo());
			appMessage.addTagValue(TagConst.SLOT_NO, condition.getSlotNo());
			appMessage.addTagValue(TagConst.STATUS_CONFIG_FLAG, 2);
			appMessage.addTagValue(TagConst.RF_SWITCH,
					condition.getPowerSwitch());
		}
		try {
			// 调低底层通信层发送消息, 同步等待应答
			if (enbConnector != null) {
				EnbAppMessage resp = enbConnector.syncInvoke(appMessage);
				EnbMessageHelper.parseResponse(resp);
			}
		} catch (TimeoutException e) {
			throw new Exception(OmpAppContext.getMessage("enb_reply_over_time"));
		} catch (Exception e) {
			log.error("config enb status error.", e);
			throw new Exception(e.getLocalizedMessage());
		}

	}

	/**
	 * 创建状态配置消息
	 * 
	 * @param condition
	 * @return
	 */
	private EnbAppMessage createConfigStatusMessage(
			EnbStatusConfigCondition condition) {

		EnbAppMessage appMessage = new EnbAppMessage();
		appMessage.setEnbId(condition.getEnbId());
		appMessage.setMa(EnbMessageConstants.MA_CONF);
		appMessage.setMoc(EnbMessageConstants.MOC_STATUS_CONFIG_REQ_AND_RES);
		appMessage.setActionType(EnbMessageConstants.ACTION_CONFIG);
		appMessage.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);

		return appMessage;

	}

	private List<Object> parseStatus(long moId, EnbAppMessage appMessage,
			EnbStatusQueryCondition condition) throws Exception {
		String flag = condition.getFlag();
		List<Object> statusList = new ArrayList<Object>();
		if (flag.equals(EnbStatusConstants.ENB_STATUS)) {
			// eNB状态信息
			EnbStatus enbStatus = EnbStatusMessageHelper
					.parseEnbStatus(appMessage);
			statusList.add(enbStatus);
		} else if (flag.equals(EnbStatusConstants.BOARD_STATUS)) {
			// 单板状态信息
			List<BoardStatus> boardStatus = EnbStatusMessageHelper
					.parseBoardStatus(appMessage);
			statusList.addAll(boardStatus);
		} else if (flag.equals(EnbStatusConstants.RRU_RUNNING_STATUS)) {
			// RRU运行状态
			RruRunningStatus rruRunningStatus = EnbStatusMessageHelper
					.parseRruRunningStatus(appMessage);
			statusList.add(rruRunningStatus);
		} else if (flag.equals(EnbStatusConstants.RRU_THRESHOLD_STATUS)) {
			// RRU门限查询
			RruThreshold rruThreshold = EnbStatusMessageHelper
					.parseRruThreshold(appMessage);
			statusList.add(rruThreshold);
		} else if (flag.equals(EnbStatusConstants.RRU_OPTICAL_STATUS)) {
			// RRU光模块状态
			List<RruOpticalStatus> opticalStatus = EnbStatusMessageHelper
					.parseRruOpticalStatus(appMessage);
			statusList.addAll(opticalStatus);
		} 
		else if (flag.equals(EnbStatusConstants.BBU_OPTICAL_STATUS)) {
			// BBU光模块状态
			List<BbuOpticalStatus> opticalStatus = EnbStatusMessageHelper
					.parseBbuOpticalStatus(appMessage);
			statusList.addAll(opticalStatus);
		}
		else if (flag.equals(EnbStatusConstants.RRU_CHANNEL_NUM)) {
			// RRU通道个数
			int channelNum = appMessage.getIntValue(TagConst.RRU_CHANNEL_NUM);
			statusList.add(channelNum);
		}
		else if (flag.equals(EnbStatusConstants.xGW_ADDRESS_STATUS)) {
			// xGW地址对信息查询解析
			List<EnbxGWAddress> xgwAddress = EnbStatusMessageHelper
					.parsexGWAddress(appMessage).getEnbxGWAddresslist();
			statusList.addAll(xgwAddress);
		}else if(flag.equals(EnbStatusConstants.TIME_DELAY)){
			//S1时延检测查询
			List<TimeDelayModel> timeDelayModel = EnbStatusMessageHelper.parseTimeDelayModel(appMessage).getTimeDelayModellist();
			statusList.addAll(timeDelayModel);
		}
		else if(flag.equals(EnbStatusConstants.AIR_FLOW_STATUS)){
			//空口流量测试查询解析
			AirFlowTest airFlowTest = EnbStatusMessageHelper.parseAirFlowTest(appMessage);
			statusList.add(airFlowTest);
		}else if(flag.equals(EnbStatusConstants.PACKET_LOSS_RATE)){
			//S1用户面丢包率检测结果解析
			List<PacketLossRateModel> packetLossRateModelList = EnbStatusMessageHelper.paraPacketLossRate(appMessage).getPackageTestCollection();
			statusList.addAll(packetLossRateModelList);
		}else if(flag.equals(EnbStatusConstants.AIR_FLOW_BEGIN) || flag.equals(EnbStatusConstants.AIR_FLOW_END) ){
			//空口流量测试开始或者停止灌包
			AirFlowTest airFlowTest = EnbStatusMessageHelper.parseAriFlowBeginEnd(appMessage);
			statusList.add(airFlowTest);
		}else if(flag.equals(EnbStatusConstants.S1_PACKAGE_TEST)){
			//S1丢包率检测
			PacketLossRateModel packetLossRateModel = EnbStatusMessageHelper.configParaPacketLossRate(appMessage);
			statusList.add(packetLossRateModel);
		}else if(flag.equals(EnbStatusConstants.S1_TIME_TEST)){
			//S1时延检测
			TimeDelayModel timeDelayModel = EnbStatusMessageHelper.configParseTimeDelayModel(appMessage);
			statusList.add(timeDelayModel);
		}

		return statusList;
	}

	private EnbAppMessage createMessage(EnbStatusQueryCondition condition) {
		EnbAppMessage appMessage = new EnbAppMessage();
		appMessage.setEnbId(condition.getEnbId());
		appMessage.setMa(EnbMessageConstants.MA_CONF);
		appMessage.setMoc(EnbMessageConstants.MOC_STATUS_QUERY_REQ_AND_RES);
		appMessage.setActionType(EnbMessageConstants.ACTION_OTHERS);
		appMessage.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		return appMessage;
	}

	private EnbAppMessage createEnbStatusMessage(
			EnbStatusQueryCondition condition) {
		EnbAppMessage appMessage = createMessage(condition);
		appMessage.addTagValue(TagConst.STATUS_QUERY_FLAG, 1);
		return appMessage;
	}

	private EnbAppMessage createAllBoardStatusMessage(
			EnbStatusQueryCondition condition) {
		EnbAppMessage appMessage = createMessage(condition);
		appMessage.addTagValue(TagConst.STATUS_QUERY_FLAG, 2);
		return appMessage;
	}

	private EnbAppMessage createBoardStatusMessage(
			EnbStatusQueryCondition condition, int queryFlag) {
		EnbAppMessage appMessage = createMessage(condition);
		appMessage.addTagValue(TagConst.STATUS_QUERY_FLAG, queryFlag);
		appMessage.addTagValue(TagConst.RACK_NO, condition.getRackNo());
		appMessage.addTagValue(TagConst.SHELF_NO, condition.getShelfNo());
		appMessage.addTagValue(TagConst.SLOT_NO, condition.getSlotNo());
		return appMessage;
	}
	

	private List<Object> createTestData(EnbStatusQueryCondition condition) {

		List<Object> statusList = new ArrayList<Object>();

		String flag = condition.getFlag();

		// eNB状态信息
		if (flag.equals(EnbStatusConstants.ENB_STATUS)) {
			EnbStatus enbStatus = new EnbStatus();
			enbStatus.setClockStatus(1);
			enbStatus.setClockType(1);
			enbStatus.setEnbStatus(1);
			enbStatus
					.setEnbTime(String.valueOf(DateUtils
							.getBriefTimeFromMillisecondTime(System
									.currentTimeMillis())));
			enbStatus.setFanSpeeds(new int[] { 11, 22, 33, 44 });
			enbStatus
					.setRunningTime(String.valueOf(System.currentTimeMillis() % 1000));
			enbStatus.setTemperature(34);
			enbStatus.setTrackSatelliteNum(33);
			enbStatus.setVisibleSatelliteNum(2);
			statusList.add(enbStatus);
		} else if (flag.equals(EnbStatusConstants.BOARD_STATUS)) {
			// 单板状态信息
			BoardStatus boardStatus = new BoardStatus();
			boardStatus.setHardwareSerialNo("test-" + condition.getRackNo()
					+ condition.getShelfNo() + condition.getSlotNo());
			boardStatus.setHardwareVersion("test-" + condition.getRackNo()
					+ condition.getShelfNo() + condition.getSlotNo());
			boardStatus.setStatus(1);
			statusList.add(boardStatus);
		} else if (flag.equals(EnbStatusConstants.RRU_RF_STATUS)) {
			// RRU射频状态
			for (int i = 0; i < 8; i++) {
				RruRfStatus rfStatus = new RruRfStatus();
				rfStatus.setChannelTemperature((int) (System
						.currentTimeMillis() % 100));
				rfStatus.setDlAntStatus(1);
				rfStatus.setDlPowerReadResult(1);
				rfStatus.setReceiveGain(45);
				rfStatus.setReceivePower(67);
				rfStatus.setSendGain(76);
				rfStatus.setSendPower(45);
				rfStatus.setUlAntStatus(1);
				rfStatus.setUlPowerReadResult(1);
				rfStatus.setVswr(5634);
				rfStatus.setVswrCalResult(1);
				rfStatus.setChannelNo(i);
				statusList.add(rfStatus);
			}
		} else if (flag.equals(EnbStatusConstants.RRU_THRESHOLD_STATUS)) {
			// RRU门限查询
			RruThreshold threshold = new RruThreshold();
			threshold.setBoardTempThres(34);
			threshold.setRfChannelTempThres(78);
			threshold.setVswrThres(3435);
			statusList.add(threshold);
		} else if (flag.equals(EnbStatusConstants.RRU_RUNNING_STATUS)) {
			// RRU运行状态
			RruRunningStatus rruRunningStatus = new RruRunningStatus();
			rruRunningStatus.setClockStatus(1);
			rruRunningStatus.setDpdTrainResult(1);
			rruRunningStatus.setIrInfWorkMode(1);
			rruRunningStatus.setMainBoardTemp(34);
			rruRunningStatus.setRfLocalFreq(4556);
			rruRunningStatus.setRfLocalStatus(45);
			rruRunningStatus.setRunningStatus(1);
			rruRunningStatus.setSlaveBoardTemp(33);
			rruRunningStatus.setChannelNum(4);
			statusList.add(rruRunningStatus);
		} else if (flag.equals(EnbStatusConstants.RRU_OPTICAL_STATUS)) {
			// RRU光模块状态
			RruOpticalStatus opticalStatus = new RruOpticalStatus();
			opticalStatus.setCurrent(45);
			opticalStatus.setInPlaceFlag(1);
			opticalStatus.setManufacture("xinwei");
			opticalStatus.setReceivePower(34);
			opticalStatus.setSendPower(45);
			opticalStatus.setTemperature(45);
			opticalStatus.setTransBitRate(56);
			opticalStatus.setVoltage(45);

			if (condition.getModuleNo() == EnbStatusConstants.FIBER_OPTICAL_MODULE_COUNT) {
				for (int i = 0; i < EnbStatusConstants.FIBER_OPTICAL_MODULE_COUNT; i++) {
					opticalStatus.setModuleNo(i + 1);
					statusList.add(opticalStatus);
				}
			} else {
				opticalStatus.setModuleNo(condition.getChannelNo() + 1);
				statusList.add(opticalStatus);
			}
		} else if (flag.equals(EnbStatusConstants.RRU_CHANNEL_NUM)) {
			statusList.add(8);
		}

		return statusList;

	}

	@Override
	public EnbDynamicInfo queryEnbDynamicInfo(EnbDynamicInfoCondition condition)
			throws Exception {
		// 创建请求消息
		EnbAppMessage appMessage = createEnbDynamicInfoMessage(condition);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			EnbAppMessage resp = enbConnector.syncInvoke(appMessage);			
			// 解析返回结果
			EnbMessageHelper.parseResponse(resp);
			return EnbStatusMessageHelper.parseDynamicInfo(resp);
		} catch (TimeoutException e) {
			throw new Exception(OmpAppContext.getMessage("enb_reply_over_time"));
		} catch (Exception e) {
			log.error("query rru running status error.", e);
			throw new Exception(e.getLocalizedMessage());
		}
	}

	
	/**
	 * 创建动态查询项的请求消息
	 * @param condition
	 * @return
	 */
	private EnbAppMessage createEnbDynamicInfoMessage(
			EnbDynamicInfoCondition condition) {
		long moId = condition.getMoId();
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		EnbAppMessage appMessage = new EnbAppMessage();
		appMessage.setEnbId(enb.getEnbId());
		appMessage.setMa(EnbMessageConstants.MA_CONF);
		appMessage.setMoc(EnbMessageConstants.MOC_STATUS_QUERY_REQ_AND_RES);
		appMessage.setActionType(EnbMessageConstants.ACTION_OTHERS);
		appMessage.setMessageType(EnbMessageConstants.MESSAGE_REQUEST);
		appMessage.addTagValue(TagConst.STATUS_QUERY_FLAG, condition.getQueryFlag());
		if (condition.getQueryFlag() == condition.XW7102_RF_STATUS) {
			// 102 射频状态查询  需要增加默认的通道号
			appMessage.addTagValue(TagConst.CHANNEL_NO, 2);
		}
		else if (condition.getQueryFlag() == condition.XW7102_FIBER_STATUS) {
			// 105 光口状态查询  需要增加默认的光口号
			appMessage.addTagValue(TagConst.FIBER_OPTIC_NO, 2);
		}
		return appMessage;
	}
}
