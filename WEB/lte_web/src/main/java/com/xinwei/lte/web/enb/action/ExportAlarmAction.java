package com.xinwei.lte.web.enb.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.util.poi.AlarmDataExcelUtil;
import com.xinwei.minas.core.facade.alarm.AlarmFacade;
import com.xinwei.minas.core.model.alarm.Alarm;
import com.xinwei.minas.core.model.alarm.AlarmQueryCondition;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.system.action.web.WebConstants;

public class ExportAlarmAction extends ActionSupport{
	
	private long moId;
	
	/**
	 * 当前告警or历史告警标识
	 */
	private int timeFlag;
	public static int CURRENT_ALARM = 0;
	public static int HISTORY_ALARM = 1;
	
	/**
	 * 选中的网元
	 */
	private String enbIdArray;

	/**
	 * 选中的告警级别
	 */
	private String levelArray;

	/**
	 * 选中的告警状态
	 */
	private String stateArray;

	/**
	 * 告警开始时间
	 */
	private String beginTime;

	/**
	 * 告警结束时间
	 */
	private String endTime;

	/**
	 * 告警描述
	 */
	private String alarmContent;

	/**
	 * 当前页号
	 */
	private String currentPage;

	/**
	 * 复选框状态
	 */
	private String checkStateArray;

	/**
	 * 0:确认 1:恢复
	 */
	private String alarmOperType;

	/**
	 * 告警ID
	 */
	private String alarmIdArray;

	/**
	 * 按列名排序
	 */
	private String sortColumn;

	/**
	 * 1:asc -1:desc;
	 */
	private int sortDirection = 0;

	/**
	 * 异常
	 */
	private String error = "";
	
	private AlarmDataExcelUtil util = new AlarmDataExcelUtil(); 

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String exportAlarmData(){
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String now = sf.format(new Date());
		
		String fileName = "AlarmExport" + now + ".xls"; 
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("Application/msexcel;charset=utf-8");		
		response.setHeader("Content-disposition", "attachment;filename="
				+ fileName);
		
		PrintWriter out = null;
		List<Alarm> alarmList = queryAlarmData();
		if(alarmList != null){
			try {
				ServletOutputStream outPutStream = response.getOutputStream();
				util.exportAlarm(alarmList, outPutStream);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("ALARM_EXPORT: Export error!");
			}
		}
		return NONE;
	}

	private List<Alarm> queryAlarmData(){
		List<Alarm> alarmList = new ArrayList<Alarm>();
		try{
			// 构建查询条件
 			AlarmQueryCondition queryCondition = new AlarmQueryCondition();
			// 排序条件
			queryCondition.setSortColumn(sortColumn);
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			AlarmFacade facade = MinasSession.getInstance().getFacade(
					sessionId, AlarmFacade.class);
			
			if (sortDirection == 1) {
				queryCondition.setSortDirection(AlarmQueryCondition.ASC);
			} else {
				queryCondition.setSortDirection(AlarmQueryCondition.DESC);
			}
			// moId
			if (enbIdArray != null && !enbIdArray.equals("")) {
				List<Long> moIds = new ArrayList<Long>();
				EnbBasicFacade enbBasicFacade = MinasSession.getInstance()
						.getFacade(sessionId, EnbBasicFacade.class);
				Enb enb = enbBasicFacade.queryByEnbId(Long.parseLong(
						enbIdArray, 16));
				if (enb != null) {
					long moId = enb.getMoId();
					moIds.add(moId);
					queryCondition.setModIds(moIds);
				} else {
					//alarmListModel.setEnbNullError("该eNB ID不存在");
				}
			}
			// 级别
			if (levelArray != null && !levelArray.equals("")) {
				queryCondition.setAlarmLevels(turnToList(levelArray));
			}
			// 状态
			if (stateArray != null && !stateArray.equals("")) {
				queryCondition.setConfirmRestoreFlags(turnToList(stateArray));
			}
			// 开始时间
			if (beginTime != null && !beginTime.equals("")) {
				queryCondition.setBeginTime(turnToLong(beginTime));
			}
			// 结束时间
			if (endTime != null && !endTime.equals("")) {
				queryCondition.setEndTime(turnToLong(endTime));
			}
			queryCondition.setContent(new String(alarmContent.getBytes("iso8859-1"), "utf-8"));
			
			// 查询告警信息
			if (timeFlag == CURRENT_ALARM) {
				alarmList = facade.queryAllCurrentAlarm(queryCondition);
			}else if(timeFlag == HISTORY_ALARM)
			{
				alarmList = facade.queryAllHistoryAlarm(queryCondition);
			}
						
		}catch(Exception e ){
			e.printStackTrace();
			//alarmListModel.setError(e.getLocalizedMessage());
			logger.error("ALARM_EXPORT: Query error!");
		}
		
		return alarmList;
		
	}
			
	/**
	 * 获取字符串中的数字转换为list
	 * 
	 * @param str
	 * @return
	 */
	public List<Integer> turnToList(String str) {
		List<Integer> list = new ArrayList<Integer>();
		String[] strArray = str.split("");
		if (strArray.length > 0) {
			for (int i = 0; i < strArray.length; i++) {
				if (strArray[i].matches("\\d")) {
					list.add(Integer.parseInt(strArray[i]));
				}
			}
		}
		return list;
	}
	
	/**
	 * 将时间字符串转为long型
	 * 
	 * @param str
	 * @return
	 */
	public long turnToLong(String str) {

		String[] strArray = str.split("");
		String strLong = "";
		if (strArray.length > 0) {
			for (int i = 0; i < strArray.length; i++) {
				if (strArray[i].matches("\\d")) {
					strLong += strArray[i];
				}
			}
		}
		return Long.parseLong(strLong);
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public int getTimeFlag() {
		return timeFlag;
	}

	public void setTimeFlag(int timeFlag) {
		this.timeFlag = timeFlag;
	}

	public String getEnbIdArray() {
		return enbIdArray;
	}

	public void setEnbIdArray(String enbIdArray) {
		this.enbIdArray = enbIdArray;
	}

	public String getLevelArray() {
		return levelArray;
	}

	public void setLevelArray(String levelArray) {
		this.levelArray = levelArray;
	}

	public String getStateArray() {
		return stateArray;
	}

	public void setStateArray(String stateArray) {
		this.stateArray = stateArray;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAlarmContent() {
		return alarmContent;
	}

	public void setAlarmContent(String alarmContent) {
		this.alarmContent = alarmContent;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getCheckStateArray() {
		return checkStateArray;
	}

	public void setCheckStateArray(String checkStateArray) {
		this.checkStateArray = checkStateArray;
	}

	public String getAlarmOperType() {
		return alarmOperType;
	}

	public void setAlarmOperType(String alarmOperType) {
		this.alarmOperType = alarmOperType;
	}

	public String getAlarmIdArray() {
		return alarmIdArray;
	}

	public void setAlarmIdArray(String alarmIdArray) {
		this.alarmIdArray = alarmIdArray;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public int getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(int sortDirection) {
		this.sortDirection = sortDirection;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	
}
