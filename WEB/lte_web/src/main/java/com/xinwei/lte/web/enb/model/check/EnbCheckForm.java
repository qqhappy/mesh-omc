/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-11-3	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model.check;

/**
 * 
 * 类简要描述
 * 
 * @author Administrator
 * 
 */

public class EnbCheckForm {
	
	// 检查项目名称
	private String checkName;
	
	// 检查结果描述
	private String checkResultDesc;
	
	// 检查结果期望值描述
	private String checkHopeDesc;
	
	// 检查结果
	private int checkResult;
	
	public EnbCheckForm() {
		super();
	}
	
	public EnbCheckForm(String checkName, String checkHopeDesc) {
		super();
		this.checkName = checkName;
		this.checkHopeDesc = checkHopeDesc;
	}
	
	public EnbCheckForm(String checkName, String checkResultDesc,
			String checkHopeDesc) {
		super();
		this.checkName = checkName;
		this.checkResultDesc = checkResultDesc;
		this.checkHopeDesc = checkHopeDesc;
	}
	
	public String getCheckName() {
		return checkName;
	}
	
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	
	public String getCheckResultDesc() {
		return checkResultDesc;
	}
	
	public void setCheckResultDesc(String checkResultDesc) {
		this.checkResultDesc = checkResultDesc;
	}
	
	public String getCheckHopeDesc() {
		return checkHopeDesc;
	}
	
	public void setCheckHopeDesc(String checkHopeDesc) {
		this.checkHopeDesc = checkHopeDesc;
	}
	
	public int getCheckResult() {
		return checkResult;
	}
	
	public void setCheckResult(int checkResult) {
		this.checkResult = checkResult;
	}
	
}
