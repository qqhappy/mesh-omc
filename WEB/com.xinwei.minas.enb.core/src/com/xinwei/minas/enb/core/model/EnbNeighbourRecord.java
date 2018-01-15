/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-12	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model;

import java.io.Serializable;

import com.xinwei.omp.core.model.biz.XBizRecord;

/**
 * 
 * eNB������¼
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbNeighbourRecord implements Serializable {
	// ������ϵ���еļ�¼
	private XBizRecord bizRecord;
	// �Ƿ�Ϊ����
	private int isNeighbour;

	public void setBizRecord(XBizRecord bizRecord) {
		this.bizRecord = bizRecord;
	}

	public XBizRecord getBizRecord() {
		return bizRecord;
	}

	public void setIsNeighbour(int isNeighbour) {
		this.isNeighbour = isNeighbour;
	}

	public int getIsNeighbour() {
		return isNeighbour;
	}

}
