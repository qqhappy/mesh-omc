/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.action;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbStatusFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.status.AirFlowTest;
import com.xinwei.minas.enb.core.model.status.BbuOpticalStatus;
import com.xinwei.minas.enb.core.model.status.BoardStatus;
import com.xinwei.minas.enb.core.model.status.EnbStatus;
import com.xinwei.minas.enb.core.model.status.EnbStatusConstants;
import com.xinwei.minas.enb.core.model.status.EnbStatusQueryCondition;
import com.xinwei.minas.enb.core.model.status.EnbxGWAddress;
import com.xinwei.minas.enb.core.model.status.PacketLossRateModel;
import com.xinwei.minas.enb.core.model.status.RruOpticalStatus;
import com.xinwei.minas.enb.core.model.status.RruRfStatus;
import com.xinwei.minas.enb.core.model.status.RruRunningStatus;
import com.xinwei.minas.enb.core.model.status.RruThreshold;
import com.xinwei.minas.enb.core.model.status.TimeDelayModel;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.system.action.web.WebConstants;

/**
 * 
 * eNB状态信息管理Action
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatusManageAction extends ActionSupport {

	public static final String NO_SPLIT_STRING = "-";

	/**
	 * 查询标识
	 */
	private String statusFlag = "";

	private String operType = "select";

	private long moId;

	private String enbHexId;

	private String enbVersion;

	private List<String> boardList;

	/**
	 * 单板标识，架-框-槽
	 */
	private String boardFlag;
	// 光模块:光模块1、光模块2、全部
	private Integer moduleNo;

	private EnbStatus enbStatus;

	private List<BoardStatus> boardStatusList;

	private List<EnbxGWAddress> enbxGWAddressList = new ArrayList<EnbxGWAddress>();

	private List<TimeDelayModel> timeDelayModelList = new ArrayList<TimeDelayModel>();
	
	private List<PacketLossRateModel> packetLossRateModelList = new ArrayList<PacketLossRateModel>();

	private RruRunningStatus rruRunningStatus = new RruRunningStatus();

	private RruThreshold rruThreshold = new RruThreshold();

	private List<RruRfStatus> rruRfStatusList;

	private List<RruOpticalStatus> rruOpticalStatusList = new ArrayList<RruOpticalStatus>();

	private List<BbuOpticalStatus> bbuOpticalStatusList = new ArrayList<BbuOpticalStatus>();

	private AirFlowTest airFlowTest;

	private PacketLossRateModel packetLossRateModel;

	private TimeDelayModel timeDelayModel;

	public static String getNoSplitString() {
		return NO_SPLIT_STRING;
	}

	private Integer rruChannelNum = 4;

	private String dlRfSwitch = "";

	private String ulRfSwitch = "";

	private String enbTime;

	private String error = "";

	public String queryStatus() {
		try {

			String sessionId = getSessionId();
			Enb enb = queryEnb(sessionId, moId);
			// 获取协议版本号
			enbVersion = enb.getProtocolVersion();
			EnbStatusFacade enbStatusFacade = MinasSession.getInstance()
					.getFacade(sessionId, EnbStatusFacade.class);
			// 查询eNB状态信息不需要查询RRU单板列表
			if (!statusFlag.equals(EnbStatusConstants.ENB_STATUS)) {
				// 查询单板列表，如果是查询单板状态，则需要获取所有单板
				boardList = queryBoardList(!statusFlag
						.equals(EnbStatusConstants.BOARD_STATUS));
				// 如果前台没有传boardFlag过来，则默认查询第一个单板
				if (boardFlag == null && boardList!=null) {
					boardFlag = boardList.get(0);
				}
				if (moduleNo == null) {
					if (statusFlag.equals("BBU_OPTICAL_STATUS")) {
						moduleNo = EnbStatusConstants.BBUFIBER_OPTICAL_MODULE_COUNT;
					} else {
						moduleNo = EnbStatusConstants.FIBER_OPTICAL_MODULE_COUNT;
					}
				}
			}
			// 根据状态标识创建查询条件
			EnbStatusQueryCondition condition = generateQueryCondition();

			List<Object> statusList = enbStatusFacade.queryStatus(moId,
					condition);
			// 将查询结果按照查询条件转换为相应的状态数据
			convertResultToStatus(statusList);
		} catch (Exception e) {
			if (e.getMessage() != null) {
				error = e.getLocalizedMessage();
			}

			if (statusFlag.equals(EnbStatusConstants.ENB_STATUS)
					|| statusFlag.equals(EnbStatusConstants.BOARD_STATUS)) {
				return ERROR;
			} else if (statusFlag.equals(EnbStatusConstants.PACKET_LOSS_RATE) || statusFlag.equals(EnbStatusConstants.TIME_DELAY)) {
				
					JSONObject json = new JSONObject();
					error = e.getLocalizedMessage();
					json.put("status", -1);
					json.put("error", e.getLocalizedMessage());
					Util.ajaxSimpleUtil(json.toString());
					return NONE;
				
			}

		}
		if(statusFlag.equals(EnbStatusConstants.PACKET_LOSS_RATE) || statusFlag.equals(EnbStatusConstants.TIME_DELAY)){
			return NONE;
		}
		return statusFlag;
	}

	public String configStatus() {
		JSONObject json = new JSONObject();
		try {
			String sessionId = getSessionId();
			EnbStatusFacade enbStatusFacade = MinasSession.getInstance()
					.getFacade(sessionId, EnbStatusFacade.class);
			if (operType.equals("select")
					&& !statusFlag.equals(EnbStatusConstants.AIR_FLOW_BEGIN)
					&& !statusFlag.equals(EnbStatusConstants.AIR_FLOW_END)
					&& !statusFlag.equals(EnbStatusConstants.S1_PACKAGE_TEST)
					&& !statusFlag.equals(EnbStatusConstants.S1_TIME_TEST)) {
				// 打开功放开关设置页面时需要查询RRU单板
				// 查询单板列表，如果是查询单板状态，则需要获取所有单板
				boardList = queryBoardList(!statusFlag
						.equals(EnbStatusConstants.BOARD_STATUS));
				// 如果前台没有传boardFlag过来，则默认查询第一个单板
				if (boardFlag == null) {
					boardFlag = boardList.get(0);
				}
				// 查询功放开关状态
				queryRfSwitch(enbStatusFacade);
				return statusFlag;
			}

			if (statusFlag.equals(EnbStatusConstants.RF_SWITCH)) {
				// 设置架框槽
				String[] noArray = boardFlag.split(NO_SPLIT_STRING);
				int rackNo = Integer.valueOf(noArray[0]);
				int shelfNo = Integer.valueOf(noArray[1]);
				int slotNo = Integer.valueOf(noArray[2]);

				byte[] switchs = new byte[2];
				// 下行
				switchs[0] = getSwitchValue(dlRfSwitch);
				// 上行
				switchs[1] = getSwitchValue(ulRfSwitch);
				int rfSwitch = ByteUtils.toInt(switchs, 0, 2);

				enbStatusFacade.configRfSwitch(
						OperObject.createEnbOperObject(enbHexId), moId, rackNo,
						shelfNo, slotNo, rfSwitch);
			} else if (statusFlag.equals(EnbStatusConstants.AIR_FLOW_BEGIN)) {

				List<Object> statusList = enbStatusFacade.configAirFlowBegin(
						OperObject.createEnbOperObject(enbHexId), moId,
						airFlowTest.getIpAddress());
				// 将查询结果按照查询条件转换为相应的状态数据
				// 空口流量测试查询
				airFlowTest = (AirFlowTest) statusList.get(0);
				json.put("status", 0);
				json.put("model", JSONObject.fromObject(airFlowTest));
				Util.ajaxSimpleUtil(json.toString());
				return NONE;

			} else if (statusFlag.equals(EnbStatusConstants.AIR_FLOW_END)) {
				List<Object> statusList = enbStatusFacade.configAirFlowEnd(
						OperObject.createEnbOperObject(enbHexId), moId);
				airFlowTest = (AirFlowTest) statusList.get(0);
				json.put("status", 0);
				json.put("model", JSONObject.fromObject(airFlowTest));
				Util.ajaxSimpleUtil(json.toString());
				return NONE;
			} else if (statusFlag.equals(EnbStatusConstants.S1_PACKAGE_TEST)) {
				List<Object> statusList = enbStatusFacade.configS1Package(
						OperObject.createEnbOperObject(enbHexId), moId);
				packetLossRateModel = (PacketLossRateModel) statusList.get(0);
				json.put("status", 0);
				json.put("model", JSONObject.fromObject(packetLossRateModel));
				Util.ajaxSimpleUtil(json.toString());
			} else if (statusFlag.equals(EnbStatusConstants.S1_TIME_TEST)) {
				List<Object> statusList = enbStatusFacade.configS1Time(
						OperObject.createEnbOperObject(enbHexId), moId);
				timeDelayModel = (TimeDelayModel) statusList.get(0);
				json.put("status", 0);
				json.put("model", JSONObject.fromObject(timeDelayModel));
				Util.ajaxSimpleUtil(json.toString());
			} else {
				enbTime = enbTime.replaceAll(":", "");
				enbTime = enbTime.replaceAll("-", "");
				enbTime = enbTime.replaceAll(" ", "");

				long time = convertToTime(enbTime);
				enbStatusFacade.configEnbTime(
						OperObject.createEnbOperObject(enbHexId), moId, time);
				// TODO 暂时定为设置完时间，查询eNB状态信息
				statusFlag = EnbStatusConstants.ENB_STATUS;
				queryEnbStatus(enbStatusFacade);
				return statusFlag;
			}
		} catch (Exception e) {
			if (statusFlag.equals(EnbStatusConstants.AIR_FLOW_BEGIN)
					|| statusFlag.equals(EnbStatusConstants.AIR_FLOW_END)
					|| statusFlag.equals(EnbStatusConstants.S1_PACKAGE_TEST)
					|| statusFlag.equals(EnbStatusConstants.S1_TIME_TEST)) {
				error = e.getLocalizedMessage();
				json.put("status", -1);
				json.put("error", e.getLocalizedMessage());
				Util.ajaxSimpleUtil(json.toString());
				return NONE;
			}

			if (statusFlag.equals(EnbStatusConstants.RF_SWITCH)
					&& operType.equals("select")) {
				if (e.getLocalizedMessage() != null) {
					error = e.getLocalizedMessage();
				}

				return statusFlag;
			}
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 查询天线开关
	 * 
	 * @param enbStatusFacade
	 * @throws Exception
	 */
	private void queryRfSwitch(EnbStatusFacade enbStatusFacade)
			throws Exception {
		EnbStatusQueryCondition condition = generateQueryCondition();
		condition.setFlag(EnbStatusConstants.RRU_RF_STATUS);
		condition.setChannelNo(0);
		List<Object> rruRfStatusList = enbStatusFacade.queryStatus(moId,
				condition);
		rruChannelNum = rruRfStatusList.size();
		int[] dlAntFlags = new int[rruChannelNum];
		int[] ulAntFlags = new int[rruChannelNum];
		for (Object object : rruRfStatusList) {
			RruRfStatus rruRfStatus = (RruRfStatus) object;
			int channelNo = rruRfStatus.getChannelNo();
			dlAntFlags[channelNo] = rruRfStatus.getDlAntStatus();
			ulAntFlags[channelNo] = rruRfStatus.getUlAntStatus();
		}
		dlRfSwitch = "";
		ulRfSwitch = "";
		for (int i = 0; i < rruChannelNum; i++) {
			dlRfSwitch += dlAntFlags[i];
			ulRfSwitch += ulAntFlags[i];
		}
	}

	/**
	 * 查询eNB状态信息
	 * 
	 * @param enbStatusFacade
	 * @throws Exception
	 */
	private void queryEnbStatus(EnbStatusFacade enbStatusFacade)
			throws Exception {
		EnbStatusQueryCondition condition = new EnbStatusQueryCondition();
		condition.setFlag(statusFlag);
		condition.setEnbId(Long.valueOf(enbHexId, 16));
		List<Object> statusList = enbStatusFacade.queryStatus(moId, condition);
		// 将查询结果按照查询条件转换为相应的状态数据
		convertResultToStatus(statusList);
	}

	/**
	 * 将20141114172300类型的字符串转换成指定格式的long
	 * 
	 * @param timeStr
	 * @return
	 */
	private long convertToTime(String timeStr) {
		String year = timeStr.substring(0, 4);
		String month = timeStr.substring(4, 6);
		String day = timeStr.substring(6, 8);
		String hour = timeStr.substring(8, 10);
		String minute = timeStr.substring(10, 12);
		String second = timeStr.substring(12, 14);
		byte[] timeBytes = new byte[8];
		ByteUtils.putNumber(timeBytes, 0, year, 2);
		ByteUtils.putNumber(timeBytes, 2, month, 1);
		ByteUtils.putNumber(timeBytes, 3, day, 1);
		ByteUtils.putNumber(timeBytes, 4, hour, 1);
		ByteUtils.putNumber(timeBytes, 5, minute, 1);
		ByteUtils.putNumber(timeBytes, 6, second, 1);
		return ByteUtils.toLong(timeBytes, 0, 8);
	}

	private byte getSwitchValue(String switchStr) {
		byte value = 0;
		for (int i = 0; i < switchStr.length(); i++) {
			char c = switchStr.charAt(i);
			if (c == '1') {
				value += 1 << i;
			}
		}
		return value;
	}

	/**
	 * 查询单板列表
	 * 
	 * @param onlyRRU
	 *            只查询RRU板
	 * @return
	 * @throws Exception
	 */
	private List<String> queryBoardList(boolean onlyRRU) throws Exception {
		String sessionId = getSessionId();
		XEnbBizConfigFacade facade = MinasSession.getInstance()
				.getFacade(sessionId, XEnbBizConfigFacade.class);
		XBizRecord condition = null;
		// 如果只查询RRU单板，则添加查询条件
		if (onlyRRU) {
			condition = new XBizRecord();
			condition.addField(new XBizField(
					EnbConstantUtils.FIELD_NAME_BDTYPE, String
							.valueOf(EnbConstantUtils.BOARD_TYPE_RRU)));
		}
		XBizTable bizTable = facade.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_BOARD, condition, false);
		List<XBizRecord> recordList = bizTable.getRecords();
		if (recordList == null || recordList.isEmpty()) {
			//throw new Exception();
			return null;
		} else {
			List<String> boardList = new ArrayList<String>();
			for (XBizRecord bizRecord : recordList) {
				int[] array = getRackShelfSlotNo(bizRecord);
				boardList.add(array[0] + NO_SPLIT_STRING + array[1]
						+ NO_SPLIT_STRING + array[2]);
			}
			return boardList;
		}
	}

	/**
	 * 从单板表记录中获取架、框、槽号
	 * 
	 * @param bizRecord
	 * @return
	 */
	private int[] getRackShelfSlotNo(XBizRecord bizRecord) {
		XBizField rackNoField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_RACKNO);
		XBizField shelfNoField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SHELFNO);
		XBizField slotNoField = bizRecord
				.getFieldBy(EnbConstantUtils.FIELD_NAME_SLOTNO);
		int rackNo = Integer.valueOf(rackNoField.getValue());
		int shelfNo = Integer.valueOf(shelfNoField.getValue());
		int slotNo = Integer.valueOf(slotNoField.getValue());
		return new int[] { rackNo, shelfNo, slotNo };
	}

	/**
	 * 将查询结果转换为对应的状态信息
	 * 
	 * @param statusList
	 */
	private void convertResultToStatus(List<Object> statusList) {
		String flag = statusFlag;
		JSONObject json = new JSONObject();
		if (flag.equals(EnbStatusConstants.ENB_STATUS)) {
			// eNB状态信息
			enbStatus = (EnbStatus) statusList.get(0);
		} else if (flag.equals(EnbStatusConstants.BOARD_STATUS)) {
			// 单板状态信息
			boardStatusList = new ArrayList<BoardStatus>();
			for (Object status : statusList) {
				boardStatusList.add((BoardStatus) status);
			}
		} else if (flag.equals(EnbStatusConstants.RRU_RUNNING_STATUS)) {
			// RRU运行状态
			rruRunningStatus = (RruRunningStatus) statusList.get(0);
		} else if (flag.equals(EnbStatusConstants.RRU_THRESHOLD_STATUS)) {
			// RRU门限查询
			rruThreshold = (RruThreshold) statusList.get(0);
			statusList.add(rruThreshold);
		} else if (flag.equals(EnbStatusConstants.RRU_OPTICAL_STATUS)) {
			// RRU光模块状态
			rruOpticalStatusList = new ArrayList<RruOpticalStatus>();
			for (Object status : statusList) {
				rruOpticalStatusList.add((RruOpticalStatus) status);
			}
		} else if (flag.equals(EnbStatusConstants.BBU_OPTICAL_STATUS)) {
			// BBU光模块状态
			bbuOpticalStatusList = new ArrayList<BbuOpticalStatus>();
			for (Object status : statusList) {
				bbuOpticalStatusList.add((BbuOpticalStatus) status);
			}
		} else if (flag.equals(EnbStatusConstants.RRU_RF_STATUS)) {
			// RRU射频状态
			rruRfStatusList = new ArrayList<RruRfStatus>();
			for (Object status : statusList) {
				rruRfStatusList.add((RruRfStatus) status);
			}
			// 设置RRU通道个数
			rruChannelNum = rruRfStatusList.size();
		} else if (flag.equals(EnbStatusConstants.xGW_ADDRESS_STATUS)) {
			// xGW地址对信息查询
			enbxGWAddressList = new ArrayList<EnbxGWAddress>();
			for (Object object : statusList) {
				enbxGWAddressList.add((EnbxGWAddress) object);
			}
		} else if (flag.equals(EnbStatusConstants.TIME_DELAY)) {
			for (Object object : statusList) {
				timeDelayModelList.add((TimeDelayModel) object);
			}
			json.put("status", 0);
			json.put("model", JSONArray.fromObject(timeDelayModelList));
			Util.ajaxSimpleUtil(json.toString());
			
		}

		else if (flag.equals(EnbStatusConstants.AIR_FLOW_STATUS)) {
			airFlowTest = (AirFlowTest) statusList.get(0);
		} else if (flag.equals(EnbStatusConstants.PACKET_LOSS_RATE)) {
			// S1用户面丢包率检测
			for (Object object : statusList) {
				packetLossRateModelList.add((PacketLossRateModel) object);
			}
			json.put("status", 0);
			json.put("model", JSONArray.fromObject(packetLossRateModelList));
			Util.ajaxSimpleUtil(json.toString());
		}

	}

	private EnbStatusQueryCondition generateQueryCondition() {
		EnbStatusQueryCondition condition = null;
		if (statusFlag.equals(EnbStatusConstants.ENB_STATUS)) {
			condition = generateQueryEnbStatusCondition();
		} else if (statusFlag.equals(EnbStatusConstants.BBU_OPTICAL_STATUS)) {
			condition = generateQueryEnbBbuOpticalStatusCondition();
		} else if (statusFlag.equals(EnbStatusConstants.xGW_ADDRESS_STATUS)) {
			// xGW地址对信息查询条件
			condition = generateQueryEnbStatusCondition();
		} else if (statusFlag.equals(EnbStatusConstants.AIR_FLOW_STATUS)) {
			// 空口流量测试查询条件
			condition = generateQueryEnbStatusCondition();
		} else if (statusFlag.equals(EnbStatusConstants.PACKET_LOSS_RATE)) {
			// S1用户面丢包率检测
			condition = generateQueryEnbStatusCondition();
		} else if (statusFlag.equals(EnbStatusConstants.TIME_DELAY)) {
			// S1用户面时延测量结果查询
			condition = generateQueryEnbStatusCondition();
		} else {
			String[] noArray = boardFlag.split(NO_SPLIT_STRING);
			condition = generateQueryBoardStatusCondition(
					Integer.valueOf(noArray[0]), Integer.valueOf(noArray[1]),
					Integer.valueOf(noArray[2]));
			if (statusFlag.equals(EnbStatusConstants.BOARD_STATUS)
					|| statusFlag.equals(EnbStatusConstants.RRU_RUNNING_STATUS)
					|| statusFlag
							.equals(EnbStatusConstants.RRU_THRESHOLD_STATUS)) {
				// 单板状态信息、RRU运行状态、RRU门限查询
			} else if (statusFlag.equals(EnbStatusConstants.RRU_OPTICAL_STATUS)) {
				// RRU和BBU光模块状态
				condition.setModuleNo(moduleNo);
			} else if (statusFlag.equals(EnbStatusConstants.RRU_RF_STATUS)) {
				// RRU射频状态
				condition.setChannelNo(0);
			}
		}
		return condition;
	}

	private EnbStatusQueryCondition generateAirFlowTestCondition() {
		EnbStatusQueryCondition condition = new EnbStatusQueryCondition();
		condition.setFlag(statusFlag);
		condition.setEnbId(Long.valueOf(enbHexId, 16));
		return condition;
	}

	private EnbStatusQueryCondition generateQueryEnbStatusCondition() {
		EnbStatusQueryCondition condition = new EnbStatusQueryCondition();
		condition.setFlag(statusFlag);
		condition.setEnbId(Long.valueOf(enbHexId, 16));
		return condition;
	}

	private EnbStatusQueryCondition generateQueryEnbBbuOpticalStatusCondition() {
		EnbStatusQueryCondition condition = new EnbStatusQueryCondition();
		condition.setFlag(statusFlag);
		condition.setEnbId(Long.valueOf(enbHexId, 16));
		condition.setModuleNo(moduleNo);
		return condition;
	}

	private EnbStatusQueryCondition generateQueryBoardStatusCondition(
			int rackNo, int shelfNo, int slotNo) {
		EnbStatusQueryCondition condition = generateQueryEnbStatusCondition();
		condition.setRackNo(rackNo);
		condition.setShelfNo(shelfNo);
		condition.setSlotNo(slotNo);
		return condition;
	}

	/**
	 * 跳转到S1-u传输质量检测页面
	 */
	public String turnToS1Test() {

		return SUCCESS;
	}

	/**
	 * 查询eNB对象
	 * 
	 * @param sessionId
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	private Enb queryEnb(String sessionId, long moId) throws Exception {
		EnbBasicFacade enbBasicFacade = MinasSession.getInstance().getFacade(
				sessionId, EnbBasicFacade.class);
		return enbBasicFacade.queryByMoId(moId);
	}

	private String getSessionId() {
		return ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
	}

	public String getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getOperType() {
		return operType;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public String getEnbHexId() {
		return enbHexId;
	}

	public void setEnbHexId(String enbHexId) {
		this.enbHexId = enbHexId;
	}

	public void setEnbVersion(String enbVersion) {
		this.enbVersion = enbVersion;
	}

	public String getEnbVersion() {
		return enbVersion;
	}

	public void setBoardList(List<String> boardList) {
		this.boardList = boardList;
	}

	public List<String> getBoardList() {
		return boardList;
	}

	public void setBoardFlag(String boardFlag) {
		this.boardFlag = boardFlag;
	}

	public String getBoardFlag() {
		return boardFlag;
	}

	public Integer getModuleNo() {
		return moduleNo;
	}

	public void setModuleNo(Integer moduleNo) {
		this.moduleNo = moduleNo;
	}

	public EnbStatus getEnbStatus() {
		return enbStatus;
	}

	public void setEnbStatus(EnbStatus enbStatus) {
		this.enbStatus = enbStatus;
	}

	public List<BoardStatus> getBoardStatusList() {
		return boardStatusList;
	}

	public void setBoardStatusList(List<BoardStatus> boardStatusList) {
		this.boardStatusList = boardStatusList;
	}

	public RruRunningStatus getRruRunningStatus() {
		return rruRunningStatus;
	}

	public void setRruRunningStatus(RruRunningStatus rruRunningStatus) {
		this.rruRunningStatus = rruRunningStatus;
	}

	public RruThreshold getRruThreshold() {
		return rruThreshold;
	}

	public void setRruThreshold(RruThreshold rruThreshold) {
		this.rruThreshold = rruThreshold;
	}

	public List<RruRfStatus> getRruRfStatusList() {
		return rruRfStatusList;
	}

	public void setRruRfStatusList(List<RruRfStatus> rruRfStatusList) {
		this.rruRfStatusList = rruRfStatusList;
	}

	public List<RruOpticalStatus> getRruOpticalStatusList() {
		return rruOpticalStatusList;
	}

	public void setRruOpticalStatusList(
			List<RruOpticalStatus> rruOpticalStatusList) {
		this.rruOpticalStatusList = rruOpticalStatusList;
	}

	public void setRruChannelNum(Integer rruChannelNum) {
		this.rruChannelNum = rruChannelNum;
	}

	public Integer getRruChannelNum() {
		return rruChannelNum;
	}

	public void setDlRfSwitch(String dlRfSwitch) {
		this.dlRfSwitch = dlRfSwitch;
	}

	public String getDlRfSwitch() {
		return dlRfSwitch;
	}

	public void setUlRfSwitch(String ulRfSwitch) {
		this.ulRfSwitch = ulRfSwitch;
	}

	public String getUlRfSwitch() {
		return ulRfSwitch;
	}

	public void setEnbTime(String enbTime) {
		this.enbTime = enbTime;
	}

	public String getEnbTime() {
		return enbTime;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<BbuOpticalStatus> getBbuOpticalStatusList() {
		return bbuOpticalStatusList;
	}

	public void setBbuOpticalStatusList(
			List<BbuOpticalStatus> bbuOpticalStatusList) {
		this.bbuOpticalStatusList = bbuOpticalStatusList;
	}

	public List<EnbxGWAddress> getEnbxGWAddressList() {
		return enbxGWAddressList;
	}

	public void setEnbxGWAddressList(List<EnbxGWAddress> enbxGWAddressList) {
		this.enbxGWAddressList = enbxGWAddressList;
	}

	public AirFlowTest getAirFlowTest() {
		return airFlowTest;
	}

	public void setAirFlowTest(AirFlowTest airFlowTest) {
		this.airFlowTest = airFlowTest;
	}

	public PacketLossRateModel getPacketLossRateModel() {
		return packetLossRateModel;
	}

	public void setPacketLossRateModel(PacketLossRateModel packetLossRateModel) {
		this.packetLossRateModel = packetLossRateModel;
	}

	public List<TimeDelayModel> getTimeDelayModelList() {
		return timeDelayModelList;
	}

	public void setTimeDelayModelList(List<TimeDelayModel> timeDelayModelList) {
		this.timeDelayModelList = timeDelayModelList;
	}

	public TimeDelayModel getTimeDelayModel() {
		return timeDelayModel;
	}

	public void setTimeDelayModel(TimeDelayModel timeDelayModel) {
		this.timeDelayModel = timeDelayModel;
	}

	

}
