/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-12-1	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.model.BoardStatusModel;
import com.xinwei.lte.web.enb.model.CellStatusModel;
import com.xinwei.lte.web.enb.model.check.BbuOpticalStatusResult;
import com.xinwei.lte.web.enb.model.check.EnbCheckConstants;
import com.xinwei.lte.web.enb.model.check.EnbCheckResult;
import com.xinwei.lte.web.enb.model.check.EnbGeneralStateResult;
import com.xinwei.lte.web.enb.model.check.EnbStateInfoResult;
import com.xinwei.lte.web.enb.model.check.RruStatusResult;
import com.xinwei.lte.web.enb.service.ExcelStyleManager;
import com.xinwei.lte.web.enb.util.EnbBizHelper;
import com.xinwei.minas.core.facade.alarm.AlarmFacade;
import com.xinwei.minas.core.facade.conf.XMoBizConfigFacade;
import com.xinwei.minas.core.model.alarm.Alarm;
import com.xinwei.minas.enb.core.facade.EnbStatusFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.status.BbuOpticalStatus;
import com.xinwei.minas.enb.core.model.status.EnbStatus;
import com.xinwei.minas.enb.core.model.status.EnbStatusConstants;
import com.xinwei.minas.enb.core.model.status.EnbStatusQueryCondition;
import com.xinwei.minas.enb.core.model.status.RruOpticalStatus;
import com.xinwei.minas.enb.core.model.status.RruRfStatus;
import com.xinwei.minas.enb.core.model.status.RruRunningStatus;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.omp.core.model.biz.XBizField;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;

/**
 * 
 * 
 * @author Administrator
 * 
 */

public class EnbCheckTask implements Runnable {
	
	public static final String NO_SPLIT_STRING = "-";
	
	private Logger logger = LoggerFactory.getLogger(EnbCheckServiceImpl.class);
	
	private List<Enb> enbList = null;
	
	private String realPath = "";
	
	public EnbCheckTask(List<Enb> enbList, String realPath) {
		this.enbList = enbList;
		this.realPath = realPath;
	}
	
	@Override
	public void run() {
		// 标识正在查询
		EnbCheckTaskManager.getInstance().setQuerying();
		String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		logger.debug("Check enb health start:" + startTime);
		if (null == enbList || enbList.size() < 1) {
			return;
		}
		// 如果没有check目录 则创建一个
		String checkPath = realPath + "check";
		File checkDir = new File(checkPath);
		if (!checkDir.exists() && !checkDir.isDirectory()) {
			checkDir.mkdir();
		}
		
		List<EnbCheckResult> enbCheckResults = new ArrayList<EnbCheckResult>();
		for (Enb enb : enbList) {
			// 创建检查结果
			EnbCheckResult enbCheckResult = new EnbCheckResult(enb.getEnbId(),
					enb.getName(), enb.getProtocolVersion());
			if (!enb.isConnected()) {
				enbCheckResult
						.setConnectionStatus(EnbCheckConstants.NOT_NORMAL);
				enbCheckResults.add(enbCheckResult);
				continue;
			}
			enbCheckResult.setSoftEnbVersion(enb.getSoftwareVersion());
			// 当前告警检查
			currentAlarmDoCheck(enbCheckResult, enb);
			// 基站总体状态查询
			enbGenaralStateDoCheck(enbCheckResult, enb);
			// enb状态信息检查
			enbStateInfoDoCheck(enbCheckResult, enb);
			// BBU光口状态
			bbuOpticalStatusDoCheck(enbCheckResult, enb);
			// RRU状态查询
			rruStatusDoCheck(enbCheckResult, enb);
			enbCheckResults.add(enbCheckResult);
		}
		// 导出报表
		try {
			ExcelStyleManager.getInstance().exportEnbCheckExcel(
					enbCheckResults, realPath + "check");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		logger.debug("Check enb health end:" + endTime);
		// 标识查询完成
		EnbCheckTaskManager.getInstance().setFree();
	}
	
	/**
	 * RRU状态检查
	 * 
	 * @param enbCheckResult
	 * @param enb
	 */
	private void rruStatusDoCheck(EnbCheckResult enbCheckResult, Enb enb) {
		EnbStatusFacade enbStatusFacade = getEnbStatusFacade();
		List<String> boardList = queryBoardList(enb);
		if(null != boardList) {
			for (String boardFlag : boardList) {
				RruStatusResult rruStatusResult = new RruStatusResult();
				String[] noArray = boardFlag.split(NO_SPLIT_STRING);
				rruStatusResult.setRackNo(Integer.valueOf(noArray[0]));
				// RRU运行状态列表
				List<RruRunningStatus> rruRunningStatusList = new ArrayList<RruRunningStatus>();
				EnbStatusQueryCondition rruRunningStatusCondition = generateQueryBoardStatusCondition(
						EnbStatusConstants.RRU_RUNNING_STATUS, enb.getEnbId(),
						Integer.valueOf(noArray[0]), Integer.valueOf(noArray[1]),
						Integer.valueOf(noArray[2]));
				List rruRunningList = null;
				try {
					rruRunningList = enbStatusFacade.queryStatus(enb.getMoId(),
							rruRunningStatusCondition);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				if (null != rruRunningList && rruRunningList.size() > 0) {
					for (Object status : rruRunningList) {
						RruRunningStatus rruRunningStatus = (RruRunningStatus) status;
						rruRunningStatusList.add((RruRunningStatus) status);
					}
					
				}
				
				// RRU射频状态列表
				List<RruRfStatus> rruRfStatusList = new ArrayList<RruRfStatus>();
				
				EnbStatusQueryCondition rruRfStatusCondition = generateQueryBoardStatusCondition(
						EnbStatusConstants.RRU_RF_STATUS, enb.getEnbId(),
						Integer.valueOf(noArray[0]), Integer.valueOf(noArray[1]),
						Integer.valueOf(noArray[2]));
				
				List rruRfList = null;
				try {
					rruRfList = enbStatusFacade.queryStatus(enb.getMoId(),
							rruRfStatusCondition);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				if (null != rruRfList && rruRfList.size() > 0) {
					for (Object status : rruRfList) {
						RruRfStatus rruRfStatus = (RruRfStatus) status;
						rruRfStatusList.add((RruRfStatus) status);
					}
					
					// 根据机架号查询拓扑号,根据拓扑号查询小区数据,获取小区天线使能索引
				}
				// RRU光模块状态列表
				List<RruOpticalStatus> rruOpticalStatusList = new ArrayList<RruOpticalStatus>();
				EnbStatusQueryCondition rruOpticalCondition = generateQueryBoardStatusCondition(
						EnbStatusConstants.RRU_OPTICAL_STATUS, enb.getEnbId(),
						Integer.valueOf(noArray[0]), Integer.valueOf(noArray[1]),
						Integer.valueOf(noArray[2]));
				List rruOpticalList = null;
				try {
					rruOpticalList = enbStatusFacade.queryStatus(enb.getMoId(),
							rruOpticalCondition);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				if (null != rruOpticalList && rruOpticalList.size() > 0) {
					for (Object status : rruOpticalList) {
						RruOpticalStatus rruOpticalStatus = (RruOpticalStatus) status;
						if (rruOpticalStatus.getInPlaceFlag() == 1) {
							rruOpticalStatusList.add((RruOpticalStatus) status);
						}
					}
					
				}
				if (rruRunningStatusList.size() > 0) {
					rruStatusResult.setRruRunningStatusList(rruRunningStatusList);
				}
				if (rruRfStatusList.size() > 0) {
					rruStatusResult.setRruRfStatusList(rruRfStatusList);
				}
				
				if (rruOpticalStatusList.size() > 0) {
					rruStatusResult.setRruOpticalStatusList(rruOpticalStatusList);
				}
				enbCheckResult.addDetailCheckResult(rruStatusResult);
			}
		}
	}
	private EnbStatusFacade getEnbStatusFacade(){
		EnbStatusFacade enbStatusFacade = null;
		try {
			 enbStatusFacade = MinasSession.getInstance()
			.getFacade(MinasSession.DEFAULT_SESSION_ID,
					EnbStatusFacade.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return enbStatusFacade;
	}

	private EnbStatusQueryCondition generateQueryBoardStatusCondition(
			String statusFlag, Long enbId, int rackNo, int shelfNo, int slotNo) {
		EnbStatusQueryCondition condition = new EnbStatusQueryCondition();
		condition.setFlag(statusFlag);
		condition.setEnbId(enbId);
		condition.setRackNo(rackNo);
		condition.setShelfNo(shelfNo);
		condition.setSlotNo(slotNo);
		if (EnbStatusConstants.RRU_OPTICAL_STATUS.equals(statusFlag)) {
			condition
					.setModuleNo(EnbStatusConstants.FIBER_OPTICAL_MODULE_COUNT);
		} else if (EnbStatusConstants.RRU_OPTICAL_STATUS.equals(statusFlag)) {
			condition.setChannelNo(4);
		}

		return condition;
	}



	/**
	 * BBU光模块状态检查
	 * 
	 * @param enbCheckResult
	 * @param enb
	 */
	private void bbuOpticalStatusDoCheck(EnbCheckResult enbCheckResult, Enb enb) {
		
		EnbStatusFacade enbStatusFacade = getEnbStatusFacade();
		EnbStatusQueryCondition enbStateInfoCondition = generateQueryEnbBbuOpticalStatusCondition(
				EnbStatusConstants.BBU_OPTICAL_STATUS, enb.getEnbId());
		List statusList=null;
		try {
			statusList = enbStatusFacade.queryStatus(enb.getMoId(),
					enbStateInfoCondition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != statusList && statusList.size() > 0) {
			List bbuOpticalStatusList = new ArrayList<BbuOpticalStatus>();
			for (Object status : statusList) {
				BbuOpticalStatus bbuOpticalStatus = (BbuOpticalStatus) status;
				if (bbuOpticalStatus.getInPlaceFlag() == 1) {
					bbuOpticalStatusList.add((BbuOpticalStatus) status);
				}
			}
			BbuOpticalStatusResult bbuOpticalStatusResult = new BbuOpticalStatusResult();
			bbuOpticalStatusResult.setList(bbuOpticalStatusList);
			enbCheckResult.addDetailCheckResult(bbuOpticalStatusResult);
		}

	
}

	/**
	 * 当前告警检查
	 * 
	 * @param enbCheckResult
	 * @param enb
	 */
	private void currentAlarmDoCheck(EnbCheckResult enbCheckResult, Enb enb) {
		try {
			AlarmFacade facade = MinasSession.getInstance().getFacade(
					MinasSession.DEFAULT_SESSION_ID, AlarmFacade.class);
			List<Alarm> alarmList = facade.queryActiveAlarm(enb.getMoId());
			enbCheckResult.addAlarmList(alarmList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 基站总体状态检查
	 * 
	 * @param enbCheckResult
	 * @param enb
	 */
	private void enbGenaralStateDoCheck(EnbCheckResult enbCheckResult, Enb enb) {
		try {
			EnbGeneralStateResult enbGeneralStateResult = new EnbGeneralStateResult();
			enbGeneralStateResult.setConnectionStatus(enbCheckResult
					.getConnectionStatus());
			// 单板状态：单板表BBU、RRU的单板状态
			List<BoardStatusModel> boardStatusList = queryBoardStatus(enb
					.getMoId());
			enbGeneralStateResult.setBoradsStatus(boardStatusList);
			// S1状态：SCTP表偶联状态
			int s1LinkStatus = queryS1LinkStatus(enb.getMoId());
			enbGeneralStateResult.setSctpStatus(s1LinkStatus);
			// 小区状态：小区参数表的小区状态
			List<CellStatusModel> cellStatusList = queryCellStatus(enb
					.getMoId());
			enbGeneralStateResult.setCellsStatus(cellStatusList);
			enbCheckResult.addDetailCheckResult(enbGeneralStateResult);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * enb状态信息检查
	 * 
	 * @param enbCheckResult
	 * @param enb
	 */
	private void enbStateInfoDoCheck(EnbCheckResult enbCheckResult, Enb enb) {
		try {
			EnbStatusFacade enbStatusFacade = MinasSession.getInstance()
					.getFacade(MinasSession.DEFAULT_SESSION_ID,
							EnbStatusFacade.class);
			EnbStatusQueryCondition enbStateInfoCondition = generateQueryEnbStatusCondition(
					EnbStatusConstants.ENB_STATUS, enb.getEnbId());
			List<Object> statusList = enbStatusFacade.queryStatus(
					enb.getMoId(), enbStateInfoCondition);
			if (null != statusList && statusList.size() > 0) {
				EnbStateInfoResult enbStateInfoResult = new EnbStateInfoResult();
				EnbStatus enbStatus = (EnbStatus) statusList.get(0);
				if (null != enbStatus) {
					enbStateInfoResult.setBbuTemperature(enbStatus
							.getTemperature());
					enbStateInfoResult.setClockStatus(enbStatus
							.getClockStatus());
					enbStateInfoResult.setClockType(enbStatus.getClockType());
					enbStateInfoResult.setEnbTime(ExcelStyleManager.getInstance().getTimeFromLong(enbStatus.getEnbTime()));
					enbStateInfoResult.setFanSpeeds(enbStatus.getFanSpeeds());
					enbStateInfoResult.setPower(enbStatus.getPower());
					enbStateInfoResult.setRunningTime(enbStatus
							.getRunningTime());
					enbStateInfoResult.setTrackSatelliteNum(enbStatus
							.getTrackSatelliteNum());
					enbStateInfoResult.setVisibleSatelliteNum(enbStatus
							.getVisibleSatelliteNum());
				}
				enbCheckResult.addDetailCheckResult(enbStateInfoResult);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 查询单板状态
	 * 
	 * @param sessionId
	 * @throws Exception
	 */
	private List<BoardStatusModel> queryBoardStatus(long moId) throws Exception {
		XMoBizConfigFacade configFacade = MinasSession.getInstance().getFacade(
				MinasSession.DEFAULT_SESSION_ID, XMoBizConfigFacade.class);
		XBizTable bizTable = configFacade.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_BOARD, null);
		boolean hasBoard = EnbBizHelper.hasRecord(bizTable);
		List<BoardStatusModel> boardStatusList = null;
		if (hasBoard) {
			boardStatusList = new ArrayList<BoardStatusModel>();
			List<XBizRecord> bizRecords = bizTable.getRecords();
			for (XBizRecord bizRecord : bizRecords) {
				BoardStatusModel boardStatus = new BoardStatusModel();

				String rackNo = bizRecord.getFieldBy(
						EnbConstantUtils.FIELD_NAME_RACKNO).getValue();
				int boardType = getIntFieldValue(bizRecord,
						EnbConstantUtils.FIELD_NAME_BDTYPE);
				String typeString = (boardType == EnbConstantUtils.BOARD_TYPE_BBU) ? "BBU"
						: "RRU";
				// RRU编号为机架号减1
				String boardName = typeString + (Integer.valueOf(rackNo) - 1);
				if (boardType == EnbConstantUtils.BOARD_TYPE_BBU)
					boardName = "BBU";

				int status = getIntFieldValue(bizRecord,
						EnbConstantUtils.FIELD_NAME_STATUS);
				boardStatus.setBoardName(boardName);
				boardStatus.setStatus(status);

				boardStatusList.add(boardStatus);
			}
		}
		return boardStatusList;
	}

	/**
	 * 查询小区状态
	 * 
	 * @param sessionId
	 * @throws Exception
	 */
	private List<CellStatusModel> queryCellStatus(long moId) throws Exception {
		XMoBizConfigFacade configFacade = MinasSession.getInstance().getFacade(
				MinasSession.DEFAULT_SESSION_ID, XMoBizConfigFacade.class);
		XBizTable bizTable = configFacade.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_CELL_PARA, null);
		boolean hasCell = EnbBizHelper.hasRecord(bizTable);
		List<CellStatusModel> cellStatusList = null;
		if (hasCell) {
			cellStatusList = new ArrayList<CellStatusModel>();
			List<XBizRecord> bizRecords = bizTable.getRecords();
			for (XBizRecord bizRecord : bizRecords) {
				CellStatusModel cellStatus = new CellStatusModel();
				int cellId = getIntFieldValue(bizRecord,
						EnbConstantUtils.FIELD_NAME_CELL_ID);
				String cellName = bizRecord.getFieldBy(
						EnbConstantUtils.FIELD_NAME_CELL_NAME).getValue();
				int status = getIntFieldValue(bizRecord,
						EnbConstantUtils.FIELD_NAME_STATUS);

				cellStatus.setCellId(cellId);
				cellStatus.setCellName(cellName);
				cellStatus.setStatus(status);

				cellStatusList.add(cellStatus);
			}
		}
		return cellStatusList;
	}

	/**
	 * 查询S1链路状态
	 * 
	 * @param sessionId
	 * @throws Exception
	 */
	private int queryS1LinkStatus(long moId) throws Exception {
		XMoBizConfigFacade configFacade = MinasSession.getInstance().getFacade(
				MinasSession.DEFAULT_SESSION_ID, XMoBizConfigFacade.class);
		XBizTable bizTable = configFacade.queryFromEms(moId,
				EnbConstantUtils.TABLE_NAME_T_SCTP, null);
		boolean hasLink = EnbBizHelper.hasRecord(bizTable);
		if (hasLink) {
			// 只取第一条记录
			XBizRecord bizRecord = bizTable.getRecords().get(0);
			return getIntFieldValue(bizRecord,
					EnbConstantUtils.FIELD_NAME_STATUS);
		}
		return EnbConstantUtils.STATUS_ABNORMAL;
	}

	private int getIntFieldValue(XBizRecord bizRecord, String fieldName) {
		return Integer.valueOf(bizRecord.getFieldBy(fieldName).getValue());
	}

	private EnbStatusQueryCondition generateQueryEnbStatusCondition(
			String statusFlag, long enbId) {
		EnbStatusQueryCondition condition = new EnbStatusQueryCondition();
		condition.setFlag(statusFlag);
		condition.setEnbId(enbId);
		return condition;
	}

	private EnbStatusQueryCondition generateQueryEnbBbuOpticalStatusCondition(
			String statusFlag, long enbId) {
		EnbStatusQueryCondition condition = new EnbStatusQueryCondition();
		condition.setFlag(statusFlag);
		condition.setEnbId(enbId);
		condition.setModuleNo(EnbStatusConstants.BBUFIBER_OPTICAL_MODULE_COUNT);
		return condition;
	}

	/**
	 * 查询单板列表
	 * 
	 * @param onlyRRU
	 *            只查询RRU板
	 * @return
	 * @throws Exception
	 */
	private List<String> queryBoardList(Enb enb) {

		XMoBizConfigFacade xMoBizConfigFacade=null;
		try {
			xMoBizConfigFacade = MinasSession.getInstance()
					.getFacade(MinasSession.DEFAULT_SESSION_ID,
							XMoBizConfigFacade.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XBizRecord condition = null;
		// 如果只查询RRU单板，则添加查询条件
		condition = new XBizRecord();
		condition.addField(new XBizField(EnbConstantUtils.FIELD_NAME_BDTYPE,
				String.valueOf(EnbConstantUtils.BOARD_TYPE_RRU)));

		XBizTable bizTable=null;
		try {
			bizTable = xMoBizConfigFacade.queryFromEms(enb.getMoId(),
					EnbConstantUtils.TABLE_NAME_T_BOARD, condition, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<XBizRecord> recordList = bizTable.getRecords();
		if (recordList == null || recordList.isEmpty()) {
			// throw new Exception();
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
	
}
