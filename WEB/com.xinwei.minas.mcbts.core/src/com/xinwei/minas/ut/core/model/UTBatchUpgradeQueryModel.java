/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * ���Ҫ����
 * <p>
 * ����ϸ����:�ն�������ѯ�ķ�ҳģ��
 * </p>
 * 
 * @author qiwei
 * 
 */

public class UTBatchUpgradeQueryModel implements Serializable {
	// Ĭ�ϵ�ǰҳ
	private Integer DEFAULT_CUR_PAGE = 1;
	// Ĭ��ҳ���С
	private Integer DEFAULT_PAGE_SIZE = 15;
	// ��ǰҳ
	private Integer curPage;
	// ҳ���С
	private Integer pageSize;
	// ҳ��������
	private Integer pageNo;
	// �ܼ�¼��
	private Integer totalCount;
	// ��ѯ��������ı�־ 1----��ѯ���������ɹ���¼  -1----��ѯ��������ʧ�ܼ�¼  0----��ѯ�������ȣ�
	private Integer successFlag;
	// ��ѯ�����ݼ���
	private List<UTBacthUpgradeResult> utResults;
	// hibernate�з�ҳÿҳ��һ����¼����ʼ������
	private Integer queryFirstResults;

	public UTBatchUpgradeQueryModel() {
		this.curPage = DEFAULT_CUR_PAGE;
		this.pageSize = DEFAULT_PAGE_SIZE;

	}

	/**
	 * ��ʼ����ҳ���ķ�����
	 * 
	 * @param totalCount
	 */
	public void InitPageNo(Integer totalCount) {
		this.totalCount = totalCount;
		this.pageNo = (totalCount - 1) / pageSize + 1;
	}

	/**
	 * ��ʼ����ҳ��ѯ��һ����¼�ķ�����
	 * 
	 * @return
	 */
	public void initQueryFirstResult() {
		if (pageNo != null && pageNo != 0) {
			this.queryFirstResults = (curPage - 1) * pageSize;
		} else {
			this.queryFirstResults = 0;
		}
	}

	public Integer getQueryFirstResults() {
		return queryFirstResults;
	}

	public void setQueryFirstResults(Integer queryFirstResults) {
		this.queryFirstResults = queryFirstResults;
	}

	public Integer getCurPage() {
		return curPage;
	}

	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public List<UTBacthUpgradeResult> getUtResults() {
		return utResults;
	}

	public void setUtResults(List<UTBacthUpgradeResult> utResults) {
		this.utResults = utResults;
	}

	public Integer getSuccessFlag() {
		return successFlag;
	}

	public void setSuccessFlag(Integer successFlag) {
		this.successFlag = successFlag;
	}
	
}
