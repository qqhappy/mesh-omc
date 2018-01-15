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
 * eNB邻区记录
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbNeighbourRecord implements Serializable {
	// 邻区关系表中的记录
	private XBizRecord bizRecord;
	// 是否互为邻区
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
