/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.facade.EnbDynamicInfoFacade;
import com.xinwei.minas.enb.core.model.EnbDynamicInfo;
import com.xinwei.minas.enb.core.model.EnbDynamicInfoCondition;
import com.xinwei.minas.enb.core.model.status.IbtsBoardStatus;
import com.xinwei.minas.enb.core.model.status.IbtsOpticalStatusCollection;
import com.xinwei.minas.enb.core.model.status.IbtsRfChannelStatus;
import com.xinwei.minas.enb.core.model.status.IbtsRfStatus;
import com.xinwei.minas.enb.core.model.status.IbtsRunningStatus;
import com.xinwei.minas.enb.core.model.status.IbtsStatus;
import com.xinwei.minas.enb.core.model.status.IbtsThreshold;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * eNodeB动态信息Action
 * 
 * @author chenjunhua
 * 
 */

public class EnbDynamicInfoAction extends ActionSupport {
	
	private static final Log log = LogFactory.getLog(EnbDynamicInfoAction.class);

	private long moId;

	private String enbHexId;
	
	private int moduleNo;
	
	private String operType = "select";
	
	// 动态查询项标识
	private int queryFlag;
	// 错误提示
	private String error;
	
	// ibts状态模型
	private IbtsStatus enbStatus;
	
	// ibts射频状态模型
	private IbtsRfStatus rfStatus;
	
	// ibts运行状态模型
	private IbtsRunningStatus runningStatus;
	
	//ibts门限模型
	private IbtsThreshold threshold;
	
	//ibts光口状态模型
	private IbtsOpticalStatusCollection opticalStatusCollection = new IbtsOpticalStatusCollection();
	
	//ibts单板状态
	private IbtsBoardStatus boardstatus;	
	private String enbTime;
	private Integer rruChannelNum = 2;
	private List<String> boardList;
	private String statusFlag = "";
	public static final String NO_SPLIT_STRING = "-";
	/**
	 * 单板标识，架-框-槽
	 */
	private String boardFlag;

	private String dlRfSwitch = "";

	private String ulRfSwitch = "";


	
	/**
	 * 查询动态信息
	 * 
	 * @return
	 */
	public String queryEnbDynamicInfo() {
		EnbDynamicInfoCondition condition = this.generateEnbDynamicInfoCondition();
		try {
			// 查询条件			
			EnbDynamicInfoFacade facade = MinasSession.getInstance().getFacade(					
					EnbDynamicInfoFacade.class);
			EnbDynamicInfo dynamicInfo = facade.queryEnbDynamicInfo(condition);
			if (condition.getQueryFlag() == condition.XW7102_STATUS) {
				// XW7102 状态信息查询
				enbStatus = (IbtsStatus)dynamicInfo;
			}
			else if (condition.getQueryFlag() == condition.XW7102_RF_STATUS) {
				// XW7102 射频状态查询
				rfStatus = (IbtsRfStatus)dynamicInfo;
			}
			else if (condition.getQueryFlag() == condition.XW7102_RUNNING_STATUS) {
				// XW7102 运行状态查询
				runningStatus = (IbtsRunningStatus)dynamicInfo;
			}
			else if(condition.getQueryFlag() == condition.XW7102_THRESHOLD){
				//XW7102门限查询
				threshold = (IbtsThreshold)dynamicInfo;
			}
			else if(condition.getQueryFlag() == condition.XW7102_FIBER_STATUS){
				//XW7102光口状态查询
				opticalStatusCollection = (IbtsOpticalStatusCollection)dynamicInfo;
			}
			else if(condition.getQueryFlag() == condition.XW7102_BOARD_STATUS){
				//XW7102单板状态查询
				boardstatus = (IbtsBoardStatus)dynamicInfo;
			}
			return String.valueOf(queryFlag);
		} catch (Exception e) {
			log.error("failed to queryEnbDynamicInfo: " + condition, e);
			error = e.getLocalizedMessage();
		}
		return ERROR;
	}
	/**
	 * 查询射频通道action
	 * @return
	 */
	public String queryfStatus(){
		EnbDynamicInfoCondition condition = this.generateEnbDynamicInfoCondition();
		EnbDynamicInfoFacade facade =null;
	
			try {
				facade = MinasSession.getInstance().getFacade(					
						EnbDynamicInfoFacade.class);
				queryrfStatus(facade);
			} catch (Exception e) {
				error = e.getLocalizedMessage();
				return ERROR;
			}
		
		return SUCCESS;
	}
	/**
	 * 查询射频通道信息
	 * @return
	 */
	public void queryrfStatus(EnbDynamicInfoFacade facade){
		EnbDynamicInfoCondition condition = this.generateEnbDynamicInfoCondition();
			IbtsRfStatus rfStatus = null;
			try {
				condition.setQueryFlag(condition.XW7102_RF_STATUS);
				rfStatus = (IbtsRfStatus) facade.queryEnbDynamicInfo(condition);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			List<IbtsRfChannelStatus> rfChannelStatusList = rfStatus.getRfChannelStatusList();
			rruChannelNum =  rfChannelStatusList.size();
			int[] dlAntFlags = new int[rruChannelNum];
			int[] ulAntFlags = new int[rruChannelNum];
			for (IbtsRfChannelStatus ibtsRfChannelStatus : rfChannelStatusList) {
				int channelNo = ibtsRfChannelStatus.getChannelNo();
				dlAntFlags[channelNo] = ibtsRfChannelStatus.getDlAntStatus();
				ulAntFlags[channelNo] = ibtsRfChannelStatus.getUlAntStatus();
			}
			dlRfSwitch = "";
			ulRfSwitch = "";
			for (int i = 0; i < rruChannelNum; i++) {
				dlRfSwitch += dlAntFlags[i];
				ulRfSwitch += ulAntFlags[i];
			}
	}
	public String configrfStatus() {
		EnbDynamicInfoFacade facade = null;
		try {
			facade = MinasSession.getInstance().getFacade(					
					EnbDynamicInfoFacade.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int rackNo = 1;
		int shelfNo = 1;
		int slotNo = 1;

		byte[] switchs = new byte[2];
		// 下行
		switchs[0] = getSwitchValue(dlRfSwitch);
		// 上行
		switchs[1] = getSwitchValue(ulRfSwitch);
		int rfSwitch = ByteUtils.toInt(switchs, 0, 2);

		try {
			facade.configRfSwitch(
					OperObject.createEnbOperObject(enbHexId), moId, rackNo,
					shelfNo, slotNo, rfSwitch);
		} catch (Exception e) {
			
			error = e.getLocalizedMessage();
			operType = "config";
			return ERROR;
		}
		queryrfStatus(facade);
		return SUCCESS;
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
	 * 小基站修改基站时间
	 * @return
	 */
	public String changeEnbTime(){
		
			EnbDynamicInfoFacade facade = null;
			try {
				facade = MinasSession.getInstance().getFacade(					
						EnbDynamicInfoFacade.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
				enbTime = enbTime.replaceAll(":", "");
				enbTime = enbTime.replaceAll("-", "");
				enbTime = enbTime.replaceAll(" ", "");
				long time = convertToTime(enbTime);
				try {
					facade.changeEnbTime(OperObject.createEnbOperObject(enbHexId),moId , time);
					EnbDynamicInfoCondition condition = this.generateEnbDynamicInfoCondition();
					condition.setQueryFlag(condition.XW7102_STATUS);
					enbStatus = (IbtsStatus) facade.queryEnbDynamicInfo(condition);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return SUCCESS;
		
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
	/**
	 * 生成查询条件
	 * @return
	 */
	private EnbDynamicInfoCondition generateEnbDynamicInfoCondition() {
		EnbDynamicInfoCondition condition = new EnbDynamicInfoCondition();
		condition.setMoId(moId);
		condition.setQueryFlag(queryFlag);		
		return condition;
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


	public IbtsStatus getEnbStatus() {
		return enbStatus;
	}

	public void setEnbStatus(IbtsStatus enbStatus) {
		this.enbStatus = enbStatus;
	}

	public IbtsRfStatus getRfStatus() {
		return rfStatus;
	}

	public void setRfStatus(IbtsRfStatus rfStatus) {
		this.rfStatus = rfStatus;
	}

	public int getQueryFlag() {
		return queryFlag;
	}

	public void setQueryFlag(int queryFlag) {
		this.queryFlag = queryFlag;
	}


	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public IbtsRunningStatus getRunningStatus() {
		return runningStatus;
	}

	public void setRunningStatus(IbtsRunningStatus runningStatus) {
		this.runningStatus = runningStatus;
	}

	public IbtsThreshold getThreshold() {
		return threshold;
	}

	public void setThreshold(IbtsThreshold threshold) {
		this.threshold = threshold;
	}

	public IbtsOpticalStatusCollection getOpticalStatusCollection() {
		return opticalStatusCollection;
	}

	public void setOpticalStatusCollection(
			IbtsOpticalStatusCollection opticalStatusCollection) {
		this.opticalStatusCollection = opticalStatusCollection;
	}

	public IbtsBoardStatus getBoardstatus() {
		return boardstatus;
	}

	public void setBoardstatus(IbtsBoardStatus boardstatus) {
		this.boardstatus = boardstatus;
	}

	public int getModuleNo() {
		return moduleNo;
	}

	public void setModuleNo(int moduleNo) {
		this.moduleNo = moduleNo;
	}	

	public String getEnbTime() {
		return enbTime;
	}

	public void setEnbTime(String enbTime) {
		this.enbTime = enbTime;
	}
	public List<String> getBoardList() {
		return boardList;
	}



	public void setBoardList(List<String> boardList) {
		this.boardList = boardList;
	}
	public String getBoardFlag() {
		return boardFlag;
	}



	public void setBoardFlag(String boardFlag) {
		this.boardFlag = boardFlag;
	}



	public String getStatusFlag() {
		return statusFlag;
	}



	public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}
	public Integer getRruChannelNum() {
		return rruChannelNum;
	}
	public void setRruChannelNum(Integer rruChannelNum) {
		this.rruChannelNum = rruChannelNum;
	}
	public String getDlRfSwitch() {
		return dlRfSwitch;
	}
	public void setDlRfSwitch(String dlRfSwitch) {
		this.dlRfSwitch = dlRfSwitch;
	}
	public String getUlRfSwitch() {
		return ulRfSwitch;
	}
	public void setUlRfSwitch(String ulRfSwitch) {
		this.ulRfSwitch = ulRfSwitch;
	}
}
