/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-8	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * 单条记录数据比较详情
 * 
 * @author fanhaoyu
 * 
 */

public class CompareDetail {

	private List<CompareFieldDetail> fieldDetails;

	public void addFieldDetail(CompareFieldDetail fieldDetail) {
		if (fieldDetails == null) {
			fieldDetails = new LinkedList<CompareFieldDetail>();
		}
		fieldDetails.add(fieldDetail);
	}

	public void setFieldDetails(List<CompareFieldDetail> fieldDetails) {
		this.fieldDetails = fieldDetails;
	}

	public List<CompareFieldDetail> getFieldDetails() {
		return fieldDetails;
	}

}
