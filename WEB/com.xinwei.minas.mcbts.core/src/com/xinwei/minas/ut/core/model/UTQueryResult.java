/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-31	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * ��ѯ�ն˵Ľ����ģ��
 * 
 * @author tiance
 * 
 */

public class UTQueryResult implements Serializable {
	// ��ѯ�ɹ�
	public static final int SUCCESS = 0x00;
	// EMS�����Ȩ��
	public static final int NO_AUTHORITY = 0x01;
	// SHLR�ڲ�����
	public static final int SHLR_INNER_ERROR = 0x02;

	// ��HLR�����������з��Ϲ���Ľ��������
	private int totalNumInHlr;
	// ���β�ѯ�����صĽ��������
	private int resultNum;
	// ҳ��
	private int pageNum;
	// ��ǰҳ
	private int curPage;

	// ��ѯ�����ն˼���
	private List<UserTerminal> utList = new ArrayList<UserTerminal>();

	public int getTotalNumInHlr() {
		return totalNumInHlr;
	}

	public void setTotalNumInHlr(int totalNumInHlr) {
		this.totalNumInHlr = totalNumInHlr;
	}

	public int getResultNum() {
		return resultNum;
	}

	public void setResultNum(int resultNum) {
		this.resultNum = resultNum;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public List<UserTerminal> getUtList() {
		return utList;
	}

	public void setUtList(List<UserTerminal> utList) {
		this.utList = utList;
	}

	public void addUt(UserTerminal ut) {
		this.utList.add(ut);
	}

	public void addUtList(List<UserTerminal> utList) {
		this.utList.addAll(utList);
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
