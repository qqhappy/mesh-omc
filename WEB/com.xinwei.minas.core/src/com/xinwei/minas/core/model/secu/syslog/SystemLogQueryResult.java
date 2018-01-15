/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.core.model.secu.syslog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ϵͳ��־��ѯ���
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class SystemLogQueryResult implements Serializable {

	private int totalNum;
	// ���β�ѯ�����صĽ��������
	private int resultNum;
	// ҳ��
	private int totalPage;
	// ��ǰҳ
	private int currentPage;

	// ��ѯ���ļ���
	private List<SystemLog> logList = new ArrayList<SystemLog>();

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getResultNum() {
		return resultNum;
	}

	public void setResultNum(int resultNum) {
		this.resultNum = resultNum;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<SystemLog> getLogList() {
		return logList;
	}

	public void setLogList(List<SystemLog> logList) {
		this.logList = logList;
	}

	/**
	 * �����Ƿ��н��
	 * 
	 * @return
	 */
	public boolean hasResult() {
		return resultNum != 0;
	}
}
