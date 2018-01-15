/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-5	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common.impl;

import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.mcbts.core.facade.common.ValidatorFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.FreqRelatedConfigure;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.utils.NeighborValidatorLogUtill;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 邻接表周期性验证服务
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class NeighborSchedualValidatorServiceImpl {
	
	public void validateTask() {
		ArrayList<String> alarmList = new ArrayList<String>();
		ArrayList<String> attentionList = new ArrayList<String>();
		//获取当前EMS的所有基站
		List<McBts> btsList = McBtsCache.getInstance().queryAll();
		for (McBts bts : btsList) {
			FreqRelatedConfigure freqConfig = new FreqRelatedConfigure(
					bts.getMoId());
			ValidatorFacade vf = AppContext.getCtx().getBean(ValidatorFacade.class);
			try {
				String validateStr = vf.validateFreqConfiguration(freqConfig);
				if (!"".equals(validateStr)) {
					validateStr = OmpAppContext.getMessage(
							"neighorlist_schedual_validator_Result",
							new Object[] { "0x" + bts.getHexBtsId() })
							+ "\n"
							+ OmpAppContext
									.getMessage("neighorlist_schedual_validator_notice")
							+ ":" + validateStr;
					attentionList.add(validateStr);
				}
			} catch (Exception e) {
				String validateStr = OmpAppContext.getMessage(
						"neighorlist_schedual_validator_Result",
						new Object[] { "0x" + bts.getHexBtsId() })
						+ "\n"
						+ OmpAppContext
								.getMessage("neighorlist_schedual_validator_alarm")
						+ ":" + e.getMessage();
				alarmList.add(validateStr);
			}
		}
		writeValidateLog(btsList.size(), alarmList, attentionList);
	}
	
	/**
	 * 记录日志
	 * @param btsId
	 * @param message
	 */
	private void writeValidateLog(int btsNum, ArrayList<String> alarmList, ArrayList<String> attentionList) {
		StringBuffer messageBuffer = new StringBuffer();
		int correctNum = btsNum - alarmList.size() - attentionList.size();
		messageBuffer.append(OmpAppContext.getMessage(
				"neighorlist_schedual_validator_summarize", new Object[] {
						btsNum, correctNum, alarmList.size(), attentionList.size() }));
		for (String st : alarmList) {
			messageBuffer.append(st);
		}
		for (String st : attentionList) {
			messageBuffer.append(st);
		}
		NeighborValidatorLogUtill.getInstance().writeLog(messageBuffer.toString());
	}
}
