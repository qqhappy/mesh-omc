package com.xinwei.lte.web.enb.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 告警集合模型，用于页面传参
 * 
 * @author zhangqiang
 * 
 */
public class AlarmListModel {
	
	/**
	 * 异常
	 */
	private String error = "";

	/**
	 * 查询基站为空的异常
	 */
	private String enbNullError = "";
	
	/**
	 * 当前页号
	 */
	private int currentPage;

	/**
	 * 总页数
	 */
	private int totalPage;

	/**
	 * 告警集合
	 */
	private List<AlarmModel> alarmList = new ArrayList<AlarmModel>();

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<AlarmModel> getAlarmList() {
		return alarmList;
	}

	public void setAlarmList(List<AlarmModel> alarmList) {
		this.alarmList = alarmList;
	}

	public String getEnbNullError() {
		return enbNullError;
	}

	public void setEnbNullError(String enbNullError) {
		this.enbNullError = enbNullError;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	
}
