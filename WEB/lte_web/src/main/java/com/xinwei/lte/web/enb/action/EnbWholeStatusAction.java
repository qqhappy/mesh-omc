/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-30	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.action;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.model.BoardStatusModel;
import com.xinwei.lte.web.enb.model.CellStatusModel;
import com.xinwei.lte.web.enb.util.EnbBizHelper;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.facade.EnbDynamicInfoFacade;
import com.xinwei.minas.enb.core.facade.EnbStatusFacade;
import com.xinwei.minas.enb.core.facade.XEnbBizConfigFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbDynamicInfo;
import com.xinwei.minas.enb.core.model.EnbDynamicInfoCondition;
import com.xinwei.minas.enb.core.model.EnbTypeDD;
import com.xinwei.minas.enb.core.model.status.EnbStatus;
import com.xinwei.minas.enb.core.model.status.EnbStatusConstants;
import com.xinwei.minas.enb.core.model.status.EnbStatusQueryCondition;
import com.xinwei.minas.enb.core.model.status.IbtsStatus;
import com.xinwei.minas.enb.core.utils.EnbConstantUtils;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.system.action.web.WebConstants;

/**
 * 
 * eNB总体状态Action
 * 
 * @author fanhaoyu
 * 
 */

public class EnbWholeStatusAction extends ActionSupport {

	private long moId;

	private String enbHexId;

	private int registerState = 0;

	private int isActive;

	private int manageStateCode = ManageState.ONLINE_STATE;

	// 单板状态
	private List<BoardStatusModel> boardStatusList;

	// S1链路状态
	private int s1LinkStatus = -1;

	// 小区状态
	private List<CellStatusModel> cellStatusList;

	// 时钟类型
	private int clockType = -1;

	// 时钟状态
	private int clockStatus;

	private String error = "";

	public String queryWholeStatus() {
		int enbType = 0;
		try {
			enbType = getEnbType(moId);
			String sessionId = getSessionId();
			// 看eNB是否已连接
			Enb enb = queryEnb(sessionId);
			registerState = enb.getRegisterState();
			isActive = enb.isActive() ? 1 : 0;
			manageStateCode = enb.getManageStateCode();

			// 单板状态：单板表BBU、RRU的单板状态
			boardStatusList = queryBoardStatus(sessionId);

			// S1状态：SCTP表偶联状态
			s1LinkStatus = queryS1LinkStatus(sessionId);

			// 小区状态：小区参数表的小区状态
			cellStatusList = queryCellStatus(sessionId);

			if ((EnbTypeDD.isXW7400(enb.getEnbType()) && needQueryClockInfo(enb))) {
				if (enb.isConfigurable()) {
					// 如果eNB在线，查询eNB状态信息(时钟类型和时钟状态)
					EnbStatus enbStatus = queryEnbStatus(sessionId);
					clockType = enbStatus.getClockType();
					clockStatus = enbStatus.getClockStatus();
				}
			}
			else if (EnbTypeDD.isXW7102(enb.getEnbType())){
				if (enb.isConfigurable()) {
					// 如果eNB在线，查询eNB状态信息(时钟类型和时钟状态)
					IbtsStatus enbStatus = queryIBtsStatus();
					clockType = enbStatus.getClockType();
					clockStatus = enbStatus.getClockStatus();
				}
			}
		} catch (Exception e) {
			error = e.getLocalizedMessage();
		}
		if (enbType == 0) {
			return "xw7400";
		} else {
			return "xw7102";
		}

	}

	/**
	 * 如果版本号小于2.0.6,则不需要查询时钟状态信息，因为基站不支持
	 * 
	 * @param enb
	 * @return
	 */
	private boolean needQueryClockInfo(Enb enb) {
		String protocolVersion = enb.getProtocolVersion();
		return compareVersion(protocolVersion, "2.0.6") >= 0;
	}

	private int compareVersion(String ver1, String ver2) {
		String[] v1 = ver1.split("\\.");
		String[] v2 = ver2.split("\\.");

		for (int i = 0; i < v1.length; i++) {
			String num1 = v1[i];
			String num2 = v2[i];
			int result = num1.compareTo(num2);
			if (result < 0) {
				return -1;
			}
			if (result > 0) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 查询eNB基本信息
	 * 
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	private Enb queryEnb(String sessionId) throws Exception {
		EnbBasicFacade facade = MinasSession.getInstance().getFacade(sessionId,
				EnbBasicFacade.class);
		return facade.queryByMoId(moId);
	}

	/**
	 * 查询单板状态
	 * 
	 * @param sessionId
	 * @throws Exception
	 */
	private List<BoardStatusModel> queryBoardStatus(String sessionId)
			throws Exception {
		XEnbBizConfigFacade configFacade = MinasSession.getInstance().getFacade(
				sessionId, XEnbBizConfigFacade.class);
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
	 * 查询S1链路状态
	 * 
	 * @param sessionId
	 * @throws Exception
	 */
	private int queryS1LinkStatus(String sessionId) throws Exception {
		XEnbBizConfigFacade configFacade = MinasSession.getInstance().getFacade(
				sessionId, XEnbBizConfigFacade.class);
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

	/**
	 * 查询小区状态
	 * 
	 * @param sessionId
	 * @throws Exception
	 */
	private List<CellStatusModel> queryCellStatus(String sessionId)
			throws Exception {
		XEnbBizConfigFacade configFacade = MinasSession.getInstance().getFacade(
				sessionId, XEnbBizConfigFacade.class);
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
	 * 查询eNB状态
	 * 
	 * @param sessionId
	 * @throws Exception
	 */
	private EnbStatus queryEnbStatus(String sessionId) throws Exception {

		EnbStatusFacade enbStatusFacade = MinasSession.getInstance().getFacade(
				sessionId, EnbStatusFacade.class);

		EnbStatusQueryCondition condition = new EnbStatusQueryCondition();
		condition.setFlag(EnbStatusConstants.ENB_STATUS);
		condition.setEnbId(Long.valueOf(enbHexId, 16));

		List<Object> statusList = enbStatusFacade.queryStatus(moId, condition);

		return (EnbStatus) statusList.get(0);
	}
	
	/**
	 * 查询小基站状态
	 * 
	 * @param sessionId
	 * @throws Exception
	 */
	private IbtsStatus queryIBtsStatus() throws Exception {

		EnbDynamicInfoCondition condition = new EnbDynamicInfoCondition();
		condition.setMoId(moId);
		condition.setQueryFlag(EnbDynamicInfoCondition.XW7102_STATUS);

		EnbDynamicInfoFacade facade = MinasSession.getInstance().getFacade(					
				EnbDynamicInfoFacade.class);
		EnbDynamicInfo dynamicInfo = facade.queryEnbDynamicInfo(condition);

		return (IbtsStatus) dynamicInfo;
	}

	private int getIntFieldValue(XBizRecord bizRecord, String fieldName) {
		return Integer.valueOf(bizRecord.getFieldBy(fieldName).getValue());
	}

	private String getSessionId() {
		return ((LoginUser) ActionContext.getContext().getSession()
				.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
	}

	private int getEnbType(long moId) throws Exception {
		EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
		return facade.queryByMoId(moId).getEnbType();
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

	public void setRegisterState(int registerState) {
		this.registerState = registerState;
	}

	public int getRegisterState() {
		return registerState;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setManageStateCode(int manageStateCode) {
		this.manageStateCode = manageStateCode;
	}

	public int getManageStateCode() {
		return manageStateCode;
	}

	public List<BoardStatusModel> getBoardStatusList() {
		return boardStatusList;
	}

	public void setBoardStatusList(List<BoardStatusModel> boardStatusList) {
		this.boardStatusList = boardStatusList;
	}

	public int getS1LinkStatus() {
		return s1LinkStatus;
	}

	public void setS1LinkStatus(int s1LinkStatus) {
		this.s1LinkStatus = s1LinkStatus;
	}

	public List<CellStatusModel> getCellStatusList() {
		return cellStatusList;
	}

	public void setCellStatusList(List<CellStatusModel> cellStatusList) {
		this.cellStatusList = cellStatusList;
	}

	public int getClockType() {
		return clockType;
	}

	public void setClockType(int clockType) {
		this.clockType = clockType;
	}

	public int getClockStatus() {
		return clockStatus;
	}

	public void setClockStatus(int clockStatus) {
		this.clockStatus = clockStatus;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
