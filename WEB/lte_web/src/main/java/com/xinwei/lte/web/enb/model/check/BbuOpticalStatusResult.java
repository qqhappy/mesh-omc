/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-11-5	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.model.check;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.enb.core.model.status.BbuOpticalStatus;

/**
 * 
 * 
 * BBU光口状态检查结果
 * 
 * @author chenlong
 * 
 */

public class BbuOpticalStatusResult extends DetailCheckResult {
	
	List<BbuOpticalStatus> list = new ArrayList<BbuOpticalStatus>();
	
	@Override
	public int check() throws Exception {
		
		return EnbCheckConstants.NORMAL;
	}
	
	@Override
	public String checkDesc() throws Exception {
		return "";
	}
	
	@Override
	public String getCheckName() throws Exception {
		return "BBU检查结果";
	}
	
	@Override
	public List<EnbCheckForm> getCheckForm() throws Exception {
		List<EnbCheckForm> checkForms = new ArrayList<EnbCheckForm>();
		if (null != list && list.size() > 0) {
			for (BbuOpticalStatus bbuOpticalStatus : list) {
				int moduleNo = bbuOpticalStatus.getModuleNo() + 1;
				// 收功率
				addCheckForm(checkForms, "光口" + moduleNo + ":收功率(uW)",
						String.valueOf(bbuOpticalStatus.getReceivePower()), "", EnbCheckConstants.NOT_CHECK);
				// 发功率
				addCheckForm(checkForms, "光口" + moduleNo + ":发功率(uW)",
						String.valueOf(bbuOpticalStatus.getSendPower()), "", EnbCheckConstants.NOT_CHECK);
				// 光模块传输bit速率
				addCheckForm(checkForms, "光口" + moduleNo + ":传输bit速率(Mbit/s)",
						String.valueOf(bbuOpticalStatus.getTransBitRate()), "", EnbCheckConstants.NOT_CHECK);
				// 电压
				addCheckForm(checkForms, "光口" + moduleNo + ":电压(mV)",
						String.valueOf(bbuOpticalStatus.getVoltage()), "", EnbCheckConstants.NOT_CHECK);
				// 电流
				addCheckForm(checkForms, "光口" + moduleNo + ":电流(mA)",
						String.valueOf(bbuOpticalStatus.getCurrent()), "", EnbCheckConstants.NOT_CHECK);
				
				// 温度
				addCheckForm(checkForms, "光口" + moduleNo + ":温度(℃)",
						String.valueOf(bbuOpticalStatus.getTemperature()), "", EnbCheckConstants.NOT_CHECK);
				
			}
		}
		return checkForms;
	}
	
	public List<BbuOpticalStatus> getList() {
		return list;
	}
	
	public void setList(List<BbuOpticalStatus> list) {
		this.list = list;
	}
	
}
