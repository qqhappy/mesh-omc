/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.helper;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ��վ״̬�ı�����
 * 
 * @author chenjunhua
 * 
 */

public class McBtsStatusChangeHelper {

	/**
	 * �޸�Ϊ����״̬
	 * 
	 * @param bts
	 */
	public static void setDisconnected(McBts bts) {
		try {
			bts.setDisconnected();
			McBtsAlarmHelper mcBtsAlarmHelper = OmpAppContext.getCtx().getBean(
					McBtsAlarmHelper.class);
			mcBtsAlarmHelper.fireBtsDisconnectedAlarm(bts);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �޸�Ϊ����״̬
	 * 
	 * @param bts
	 */
	public static void setConnected(McBts bts) {
		try {
			bts.setConnected();
			McBtsAlarmHelper mcBtsAlarmHelper = OmpAppContext.getCtx().getBean(
					McBtsAlarmHelper.class);
			mcBtsAlarmHelper.fireBtsDisconnectedAlarmRestored(bts);
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

	/**
	 * �޸�Ϊע����״̬
	 * 
	 * @param bts
	 */
	public static void setRegistering(McBts bts) {
		try {
			bts.setRegistering();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
