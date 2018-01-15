package com.xinwei.lte.web.enb.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.model.AlarmListModel;
import com.xinwei.lte.web.enb.model.AlarmModel;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.minas.core.facade.alarm.AlarmFacade;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.alarm.Alarm;
import com.xinwei.minas.core.model.alarm.AlarmQueryCondition;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.enb.core.facade.EnbAlarmFacade;
import com.xinwei.minas.enb.core.facade.EnbBasicFacade;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.system.action.web.WebConstants;

/**
 * 查询eNB告警
 * 
 * @author zhangqiang
 * 
 */
public class QueryEnbAlarmAction extends ActionSupport {

	private long moId;

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

	/**
	 * 查询到的告警集合
	 */
	private AlarmListModel alarmListModel = new AlarmListModel();

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 告警同步
	 * 
	 * @return
	 */
	public String syncCurrentAlarm() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;

		try {
			out = response.getWriter();
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			EnbAlarmFacade facade = MinasSession.getInstance().getFacade(
					sessionId, EnbAlarmFacade.class);
			EnbBasicFacade facadeTwo = MinasSession.getInstance().getFacade(
					sessionId, EnbBasicFacade.class);
			OperObject object = OperObject.createEnbOperObject(enbIdArray);
			facade.syncAlarm(object,
					facadeTwo.queryByEnbId(Long.parseLong(enbIdArray, 16))
							.getMoId());

		} catch (Exception e) {
			error = e.getLocalizedMessage();
		} finally {
			if (out != null) {
				JSONObject json = new JSONObject();
				json.put("error", error);
				out.println(json.toString());
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	/**
	 * 查询当前告警
	 * 
	 * @return
	 */
	public String queryEnbCurrentAlarm() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;

		try {
			out = response.getWriter();
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			AlarmFacade facade = MinasSession.getInstance().getFacade(
					sessionId, AlarmFacade.class);
			LoginUser loginUser = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT));
			// 确认或恢复
			if (Integer.parseInt(alarmOperType) == 0) {
				List<Long> list = new ArrayList<Long>();
				String[] str = alarmIdArray.split(",");
				for (int i = 0; i < str.length; i++) {
					list.add(Long.parseLong(str[i]));
				}
				// TOGO 从session中获取
				facade.confirmManually(list, loginUser.getUsername());
			}
			if (Integer.parseInt(alarmOperType) == 1) {
				List<Long> list = new ArrayList<Long>();
				String[] str = alarmIdArray.split(",");
				for (int i = 0; i < str.length; i++) {
					list.add(Long.parseLong(str[i]));
				}
				// TOGO 从session中获取
				facade.restoreManually(list, loginUser.getUsername());
			}

			// 构建查询条件
			AlarmQueryCondition queryCondition = new AlarmQueryCondition();
			// 排序条件
			queryCondition.setSortColumn(sortColumn);
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
					alarmListModel.setEnbNullError("该eNB ID不存在");
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
			queryCondition.setContent(alarmContent);
			queryCondition.setCurrentPage(Integer.parseInt(currentPage));
			queryCondition.setNumPerPage(10);
			// 查询告警信息

			PagingData<Alarm> alarmData = facade
					.queryCurrentAlarm(queryCondition);

			if (Integer.parseInt(currentPage) >= alarmData.getTotalPages()) {
				queryCondition.setCurrentPage(alarmData.getTotalPages());
				alarmData = facade.queryCurrentAlarm(queryCondition);
			}
			List<Alarm> listAlarm = alarmData.getResults();
			List<AlarmModel> alarmList = new ArrayList<AlarmModel>();
			if (listAlarm != null && listAlarm.size() > 0) {
				for (Alarm alarm : listAlarm) {
					AlarmModel model = new AlarmModel();
					model.setAlarm(alarm);
					model.setEnbName(alarm.getMoName());
					// model.setEnbName(MinasSession.getInstance().getFacade(EnbBasicFacade.class).queryByMoId(alarm.getMoId()).getName());
					if (alarm.getConfirmTime() != 0) {
						model.setConfirmTimeString(toTimeString(alarm
								.getConfirmTime()));
					}
					if (alarm.getFirstAlarmTime() != 0) {
						model.setFirstAlarmTimeString(toTimeString(alarm
								.getFirstAlarmTime()));
					}
					if (alarm.getRestoredTime() != 0) {
						model.setRestoredTimeString(toTimeString(alarm
								.getRestoredTime()));
					}
					alarmList.add(model);
				}
			}
			alarmListModel.setCurrentPage(alarmData.getCurrentPage());
			alarmListModel.setTotalPage(alarmData.getTotalPages());
			alarmListModel.setAlarmList(alarmList);

		} catch (Exception e) {
			e.printStackTrace();
			alarmListModel.setError(e.getLocalizedMessage());
		} finally {
			if (out != null) {
				JSONObject json = new JSONObject();

				JSONObject object = new JSONObject();
				object = JSONObject.fromObject(alarmListModel);

				json.put("alarmListModel", object);
				json.put("checkStateArray", checkStateArray);
				out.println(json.toString());
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	/**
	 * 查询历史告警
	 * 
	 * @return
	 */
	public String queryEnbHistoryAlarm() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;

		try {
			out = response.getWriter();
			String sessionId = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
					.getSessionId();
			AlarmFacade facade = MinasSession.getInstance().getFacade(
					sessionId, AlarmFacade.class);
			// 构建查询条件
			AlarmQueryCondition queryCondition = new AlarmQueryCondition();
			// 排序条件
			queryCondition.setSortColumn(sortColumn);
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
					alarmListModel.setEnbNullError("该eNB ID不存在");
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
			queryCondition.setContent(alarmContent);
			queryCondition.setCurrentPage(Integer.parseInt(currentPage));
			queryCondition.setNumPerPage(10);
			// 查询告警信息
			PagingData<Alarm> alarmData = facade
					.queryHistoryAlarm(queryCondition);
			if (Integer.parseInt(currentPage) >= alarmData.getTotalPages()) {
				queryCondition.setCurrentPage(alarmData.getTotalPages());
				alarmData = facade.queryHistoryAlarm(queryCondition);
			}
			List<Alarm> listAlarm = alarmData.getResults();
			List<AlarmModel> alarmList = new ArrayList<AlarmModel>();
			if (listAlarm != null && listAlarm.size() > 0) {
				for (Alarm alarm : listAlarm) {
					AlarmModel model = new AlarmModel();
					model.setAlarm(alarm);
					model.setEnbName(alarm.getMoName());
					// model.setEnbName(MinasSession.getInstance().getFacade(EnbBasicFacade.class).queryByMoId(alarm.getMoId()).getName());
					if (alarm.getConfirmTime() != 0) {
						model.setConfirmTimeString(toTimeString(alarm
								.getConfirmTime()));
					}
					if (alarm.getFirstAlarmTime() != 0) {
						model.setFirstAlarmTimeString(toTimeString(alarm
								.getFirstAlarmTime()));
					}
					if (alarm.getRestoredTime() != 0) {
						model.setRestoredTimeString(toTimeString(alarm
								.getRestoredTime()));
					}
					alarmList.add(model);
				}
			}
			alarmListModel.setCurrentPage(alarmData.getCurrentPage());
			alarmListModel.setTotalPage(alarmData.getTotalPages());
			alarmListModel.setAlarmList(alarmList);

			logger.debug("history alarm total page = "
					+ alarmListModel.getTotalPage());

		} catch (Exception e) {
			e.printStackTrace();
			alarmListModel.setError(e.getLocalizedMessage());
		} finally {
			if (out != null) {
				JSONObject json = new JSONObject();
				JSONObject object = new JSONObject();

				object = JSONObject.fromObject(alarmListModel);
				json.put("alarmListModel", object);
				json.put("checkStateArray", checkStateArray);
				out.println(json.toString());
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	public String queryEnbTypeOfMoId(){
		JSONObject json = new JSONObject();
		try {
			EnbBasicFacade facade = Util.getFacadeInstance(EnbBasicFacade.class);
			int enbType = facade.queryByMoId(moId).getEnbType();
			json.put("status", 0);
			json.put("message", enbType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("status", -1);
			json.put("error", e.getLocalizedMessage());
		}
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
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

	/**
	 * 将Long型时间串转为String类型表示
	 * 
	 * @return
	 */
	public String toTimeString(long time) {
		String str = String.valueOf(time);
		String str1 = str.substring(0, 4);
		String str2 = str.substring(4, 6);
		String str3 = str.substring(6, 8);
		String str4 = str.substring(8, 10);
		String str5 = str.substring(10, 12);
		String str6 = str.substring(12, 14);
		return str1 + "-" + str2 + "-" + str3 + " " + str4 + ":" + str5 + ":"
				+ str6;
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

	public String getEnbIdArray() {
		return enbIdArray;
	}

	public void setEnbIdArray(String enbIdArray) {
		this.enbIdArray = enbIdArray;
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

	public String getCheckStateArray() {
		return checkStateArray;
	}

	public void setCheckStateArray(String checkStateArray) {
		this.checkStateArray = checkStateArray;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

}
