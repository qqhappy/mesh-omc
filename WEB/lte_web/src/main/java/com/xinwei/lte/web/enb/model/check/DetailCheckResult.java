/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-11-2	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model.check;

import java.util.List;

import com.xinwei.lte.web.enb.model.check.EnbCheckForm;


/**
 * 
 * 健康检查结果父类
 * 
 * @author chenlong
 * 
 */

public abstract class DetailCheckResult {
	
	/**
	 * 健康检查
	 * @return 0通过 1不通过
	 * @throws Exception
	 */
	public abstract int check() throws Exception;
	
	/**
	 * 健康检查概述
	 * @return
	 * @throws Exception
	 */
	public abstract String checkDesc() throws Exception;
	
	/**
	 * 获取检查类名
	 * @return
	 * @throws Exception
	 */
	public abstract String getCheckName() throws Exception;
	
	/**
	 * 将结果分析成表格形式
	 * @return
	 * @throws Exception
	 */
	public abstract List<EnbCheckForm> getCheckForm() throws Exception;
	
	/**
	 * 增加一个表格项
	 * @param checkForms
	 * @param checkName
	 * @param checkResultDesc
	 * @param checkHopeDesc
	 * @param checkResult
	 */
	public void addCheckForm(List<EnbCheckForm> checkForms, String checkName,
			String checkResultDesc, String checkHopeDesc, int checkResult) {
		EnbCheckForm enbCheckForm = new EnbCheckForm(checkName,
				checkResultDesc, checkHopeDesc);
		enbCheckForm.setCheckResult(checkResult);
		checkForms.add(enbCheckForm);
	}
	
	/**
	 * 根据机架号获取RRU编号
	 * @param rackNo
	 * @return
	 */
	public String getRru(int rackNo) {
		if(2 == rackNo) {
			return "RRU1";
		} else if(3 == rackNo) {
			return "RRU2";
		} else if(4 == rackNo) {
			return "RRU3"; 
		} else {
			return "RRU";
		}
	}
	
}
