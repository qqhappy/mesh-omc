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
 * �澯��ѯ����
 * 
 * @author chenjunhua
 * 
 */

public class AlarmQueryCondition extends PagingCondition {

	// ��ԪID�б�
	private List<Long> moIds = new ArrayList();

	// �澯��ˮ��
	private List<Long> alarmIds = new ArrayList();

	// �澯�����б�
	private List<Integer> alarmLevels = new ArrayList();

	// ȷ�ϻָ�״̬�б�: 1-δȷ��δ�ָ�; 2-��ȷ��δ�ָ�; 3-δȷ���ѻָ�;
	private List<Integer> confirmRestoreFlags = new ArrayList();

	// �澯����
	private String content = "";

	// ��ʼʱ��(14λ����yyyyMMddhhmmss)
	private long beginTime;

	// ����ʱ��(14λ����yyyyMMddhhmmss)
	private long endTime;

	// ��������,����ǽ����ϵ�����(����ǰ��),��AlarmDAOImpl�л�ת�ɱ��е�����
	private String sortColumn;

	// ��������,������������,
	private String sortDirection = DESC;

	public static final String ASC = " asc";
	public static final String DESC = " desc";

	public List<Long> getMoIds() {
		return moIds;
	}

	// FIXME: ��setModIds�ع�ΪsetMoIds
	public void setModIds(List<Long> moIds) {
		this.moIds = moIds;
	}

	/**
	 * ������Ԫ�б�
	 * 
	 * @param moIdArray
	 */
	public void setMoIds(Long[] moIdArray) {
		for (Long moId : moIdArray) {
			moIds.add(moId);
		}
	}

	/**
	 * ��ȡ�澯�����ȫ����
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
