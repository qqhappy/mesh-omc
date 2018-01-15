/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-2	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model.corenet;

import java.io.Serializable;

/**
 * 
 * 跟踪区码模型
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class TaModel implements Serializable {

	private int id;
	// 跟踪区码
	private String code;
	// 备注
	private String remark;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
