/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-10-30	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model.check;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.core.model.alarm.Alarm;

/**
 * 
 * 基站健康检查概要
 * 
 * @author Administrator
 * 
 */

public class EnbCheckResult {
	
	private long enbId;
	
	private String enbName;
	
	// 基站与网管链接状态
	private int connectionStatus;
	
	// 协议版本
	private String enbVersion;
	
	// 软件版本
	private String softEnbVersion;
	
	// 0正常 1故障
	private int checkResult;
	
	// 检查结果概要
	private String desc;
	
	private List<DetailCheckResult> detailCheckResultList = new ArrayList<DetailCheckResult>();
	
	private List<Alarm> alarmList = new ArrayList<Alarm>();
	
	public EnbCheckResult() {
		
	}
	
	public EnbCheckResult(long enbId, String enbName, String enbVersion) {
		this.enbId = enbId;
		this.enbName = enbName;
		this.enbVersion = enbVersion;
	}
	
	/**
	 * 添加告警列表
	 * 
	 * @param alarms
	 */
	public void addAlarmList(List<Alarm> alarms) {
		if (null != alarms && alarms.size() > 0) {
			alarmList = alarms;
		}
	}
	
	/**
	 * 获取告警列表
	 * 
	 * @return
	 */
	public List<Alarm> getAlarmList() {
		return alarmList;
	}
	
	/**
	 * 获取告警表格
	 * 
	 * @return
	 */
	public List<AlarmCheckForm> getAlarmCheckForm() {
		List<AlarmCheckForm> alarmCheckForms = new ArrayList<AlarmCheckForm>();
		for (Alarm alarm : alarmList) {
			AlarmCheckForm alarmCheckForm = new AlarmCheckForm();
			alarmCheckForm.setEnbName(enbName);
			if (1 == alarm.getAlarmLevel()) {
				alarmCheckForm.setLevel("紧急");
			}
			else if (2 == alarm.getAlarmLevel()) {
				alarmCheckForm.setLevel("重要");
			}
			else if (3 == alarm.getAlarmLevel()) {
				alarmCheckForm.setLevel("次要");
			}
			else if (4 == alarm.getAlarmLevel()) {
				alarmCheckForm.setLevel("提示");
			}
			else {
				alarmCheckForm.setLevel("未知");
			}
			alarmCheckForm.setLocation(alarm.getLocation());
			alarmCheckForm.setContent(alarm.getAlarmContent());
			// 告警状态: 0-恢复; 1-告警 ; 
			// 确认状态: 0-未确认; 1-已确认
			if (0 == alarm.getAlarmState() && 0 == alarm.getConfirmState()) {
				alarmCheckForm.setStatus("未确认已恢复");
			}
			else if (0 == alarm.getAlarmState() && 1 == alarm.getConfirmState()) {
				alarmCheckForm.setStatus("已确认已恢复");
			}
			else if (1 == alarm.getAlarmState() && 0 == alarm.getConfirmState()) {
				alarmCheckForm.setStatus("未确认未恢复");
			}
			else if(1 == alarm.getAlarmState() && 1 == alarm.getConfirmState()) {
				alarmCheckForm.setStatus("已确认未恢复");
			} else {
				alarmCheckForm.setStatus("未知");
			}
			alarmCheckForm.setHappenTime(longToStringTime(alarm
					.getFirstAlarmTime()));
			alarmCheckForms.add(alarmCheckForm);
		}
		return alarmCheckForms;
	}
	
	/**
	 * 14位长度yyyyMMddhhmmss 转换成yyyy-MM-dd hh:mm:ss格式的字符串型
	 * 
	 * @param longTime
	 * @return
	 */
	public String longToStringTime(long longTime) {
		String time = String.valueOf(longTime);
		if (14 != time.length()) {
			return time;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(time.substring(0, 4) + "-");
		sb.append(time.substring(4, 6) + "-");
		sb.append(time.substring(6, 8) + " ");
		sb.append(time.substring(8, 10) + ":");
		sb.append(time.substring(10, 12) + ":");
		sb.append(time.substring(12, 14));
		return sb.toString();
	}
	
	/**
	 * 增加一个检查类
	 * 
	 * @param checkName
	 * @param detailCheckResult
	 */
	public void addDetailCheckResult(DetailCheckResult detailCheckResult) {
		detailCheckResultList.add(detailCheckResult);
	}
	
	/**
	 * 获得检查类
	 * 
	 * @param checkName
	 * @return
	 */
	public List<DetailCheckResult> getDetailCheckResultList() {
		if (null != detailCheckResultList) {
			return detailCheckResultList;
		}
		return null;
	}
	
	/**
	 * 获取检查结果
	 * 
	 * @return
	 */
	public int getCheckResult() {
		checkResult = EnbCheckConstants.NORMAL;
		if (alarmList.size() > 0) {
			checkResult = EnbCheckConstants.NOT_NORMAL;
			return checkResult;
		}
		if (detailCheckResultList.size() > 0) {
			checkResult = check();
		}
		return checkResult;
	}
	
	/**
	 * 检查类中是否有不通过的项
	 * 
	 * @return
	 */
	public int check() {
		int result = EnbCheckConstants.NORMAL;
		try {
			for (DetailCheckResult detailCheckResult : detailCheckResultList) {
				int check = detailCheckResult.check();
				if (EnbCheckConstants.NOT_NORMAL == check) {
					return EnbCheckConstants.NOT_NORMAL;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 基站与网管是否链接
	 * 
	 * @return
	 */
	public boolean isConnection() {
		if (EnbCheckConstants.NORMAL == connectionStatus) {
			return true;
		}
		return false;
	}
	
	public String getCheckDesc() {
		StringBuilder sb = new StringBuilder();
		if (alarmList.size() > 0) {
			sb.append("基站有告警.\n");
		}
		for (DetailCheckResult checkResult : detailCheckResultList) {
			try {
				sb.append(checkResult.checkDesc());
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public long getEnbId() {
		return enbId;
	}
	
	public void setEnbId(long enbId) {
		this.enbId = enbId;
	}
	
	public String getEnbName() {
		return enbName;
	}
	
	public void setEnbName(String enbName) {
		this.enbName = enbName;
	}
	
	public String getEnbVersion() {
		return enbVersion;
	}
	
	public void setEnbVersion(String enbVersion) {
		this.enbVersion = enbVersion;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public int getConnectionStatus() {
		return connectionStatus;
	}
	
	public void setConnectionStatus(int connectionStatus) {
		this.connectionStatus = connectionStatus;
	}
	
	public String getSoftEnbVersion() {
		return softEnbVersion;
	}
	
	public void setSoftEnbVersion(String softEnbVersion) {
		this.softEnbVersion = softEnbVersion;
	}
	
}
