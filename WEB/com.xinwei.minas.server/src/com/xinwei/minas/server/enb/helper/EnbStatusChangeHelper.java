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
 * ��վ״̬�ı�����
 * 
 * @author chenjunhua
 * 
 */

public class EnbStatusChangeHelper {

	/**
	 * �޸�Ϊ����״̬
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
	 * �޸�Ϊ����״̬
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
	 * �޸�Ϊע����״̬
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
