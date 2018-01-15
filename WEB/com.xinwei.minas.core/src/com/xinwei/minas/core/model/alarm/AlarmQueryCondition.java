/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-26	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.model.alarm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.xinwei.omp.core.model.biz.PagingCondition;

/**
 * 
 * 告警查询条件
 * 
 * @author chenjunhua
 * 
 */

public class AlarmQueryCondition extends PagingCondition {

	// 网元ID列表
	private List<Long> moIds = new ArrayList();

	// 告警流水号
	private List<Long> alarmIds = new ArrayList();

	// 告警级别列表
	private List<Integer> alarmLevels = new ArrayList();

	// 确认恢复状态列表: 1-未确认未恢复; 2-已确认未恢复; 3-未确认已恢复;
	private List<Integer> confirmRestoreFlags = new ArrayList();

	// 告警内容
	private String content = "";

	// 开始时间(14位长度yyyyMMddhhmmss)
	private long beginTime;

	// 结束时间(14位长度yyyyMMddhhmmss)
	private long endTime;

	// 按列排序,存的是界面上的列名(翻译前的),在AlarmDAOImpl中会转成表中的列名
	private String sortColumn;

	// 正向排序,还是逆向排序,
	private String sortDirection = DESC;

	public static final String ASC = " asc";
	public static final String DESC = " desc";

	public List<Long> getMoIds() {
		return moIds;
	}

	// FIXME: 将setModIds重构为setMoIds
	public void setModIds(List<Long> moIds) {
		this.moIds = moIds;
	}

	/**
	 * 设置网元列表
	 * 
	 * @param moIdArray
	 */
	public void setMoIds(Long[] moIdArray) {
		for (Long moId : moIdArray) {
			moIds.add(moId);
		}
	}

	/**
	 * 获取告警级别的全集合
	 * 
	 * @return
	 */
	public List<Integer> getAllAlarmLevels() {
		List<Integer> list = new LinkedList();
		list.add(AlarmLevel.STATE_CRITICAL);
		list.add(AlarmLevel.STATE_MAJOR);
		list.add(AlarmLevel.STATE_MINOR);
		list.add(AlarmLevel.STATE_INFO);
		return list;
	}

	public List<Integer> getAlarmLevels() {
		return alarmLevels;
	}

	public void setAlarmLevels(List<Integer> alarmLevels) {
		this.alarmLevels = alarmLevels;
	}

	public List<Integer> getConfirmRestoreFlags() {
		return confirmRestoreFlags;
	}

	public void setConfirmRestoreFlags(List<Integer> confirmRestoreFlags) {
		this.confirmRestoreFlags = confirmRestoreFlags;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public List<Long> getAlarmIds() {
		return alarmIds;
	}

	public void setAlarmIds(List<Long> alarmIds) {
		this.alarmIds = alarmIds;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	@Override
	public String toString() {
		return "AlarmQueryCondition [moIds=" + moIds + ", alarmIds=" + alarmIds
				+ ", alarmLevels=" + alarmLevels + ", confirmRestoreFlags="
				+ confirmRestoreFlags + ", content=" + content + ", beginTime="
				+ beginTime + ", endTime=" + endTime + "]";
	}

}
