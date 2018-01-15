/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.helper;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 基站状态改变助手
 * 
 * @author chenjunhua
 * 
 */

public class EnbStatusChangeHelper {

	/**
	 * 修改为断连状态
	 * 
	 * @param bts
	 */
	public static void setDisconnected(Enb enb) {
		try {
			enb.setDisconnected();
			EnbAlarmHelper enbAlarmHelper = OmpAppContext.getCtx().getBean(
					EnbAlarmHelper.class);
			enbAlarmHelper.fireBtsDisconnectedAlarm(enb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改为连接状态
	 * 
	 * @param enb
	 */
	public static void setConnected(Enb enb) {
		try {
			enb.setConnected();
			EnbAlarmHelper enbAlarmHelper = OmpAppContext.getCtx().getBean(
					EnbAlarmHelper.class);
			enbAlarmHelper.fireBtsDisconnectedAlarmRestored(enb);
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	/**
	 * 修改为注册中状态
	 * 
	 * @param enb
	 */
	public static void setRegistering(Enb enb) {
		try {
			enb.setRegistering();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
