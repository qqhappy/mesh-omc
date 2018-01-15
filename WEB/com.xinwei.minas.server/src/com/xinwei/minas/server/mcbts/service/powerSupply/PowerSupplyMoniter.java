/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.powerSupply;

import com.xinwei.minas.mcbts.core.model.sysManage.PowerSupply;

/**
 * 
 * ��Դ������
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */
public abstract class PowerSupplyMoniter implements Runnable{

	public PowerSupply power;
	
	public PowerSupplyMoniter(PowerSupply power) {
		this.power = power;
	}
	
	@Override
	public void run() {
		doWork();
	}
	
	public void doWork() {
		//��ȡ��ѯ����
		byte[] pollCmd = getPollCmd(power);
		if (pollCmd != null && pollCmd.length > 0) {
			
			//��ȡ��Ӧ��Ϣ
			byte[] response = sendMessage(pollCmd);
			
			//������Ӧ���
			String content = parseResponse(response);
			
			//���ɸ澯��Ϣ
			createAlarm(content);
		}
	}
	
	
	public PowerSupply getPower() {
		return power;
	}

	public void setPower(PowerSupply power) {
		this.power = power;
	}
	
	
	/**
	 * ��ȡ��ѯ����
	 * @param power
	 * @return
	 */
	public abstract byte[] getPollCmd(PowerSupply power);
	
	/**
	 * ���Դ�豸������ѯ����
	 * @param message
	 * @return ��Ӧ��Ϣ
	 */
	public abstract byte[] sendMessage(byte[] message);
	
	/**
	 * ������ѯ���
	 * @param response
	 * @return
	 */
	public abstract String parseResponse(byte[] response);
	
	/**
	 * ���ɸ澯��Ϣ
	 * @param conntent
	 */
	public abstract void createAlarm(String content);
	
	/**
	 * �ͷ���Դ
	 */
	public abstract void dispose();
}
