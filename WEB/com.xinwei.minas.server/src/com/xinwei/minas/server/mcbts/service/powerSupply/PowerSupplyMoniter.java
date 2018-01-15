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
 * 电源监视器
 * 
 * <p>
 * 类详细描述
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
		//获取查询命令
		byte[] pollCmd = getPollCmd(power);
		if (pollCmd != null && pollCmd.length > 0) {
			
			//获取响应信息
			byte[] response = sendMessage(pollCmd);
			
			//解析响应结果
			String content = parseResponse(response);
			
			//生成告警信息
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
	 * 获取查询命令
	 * @param power
	 * @return
	 */
	public abstract byte[] getPollCmd(PowerSupply power);
	
	/**
	 * 向电源设备发送轮询命令
	 * @param message
	 * @return 响应信息
	 */
	public abstract byte[] sendMessage(byte[] message);
	
	/**
	 * 解析查询结果
	 * @param response
	 * @return
	 */
	public abstract String parseResponse(byte[] response);
	
	/**
	 * 生成告警信息
	 * @param conntent
	 */
	public abstract void createAlarm(String content);
	
	/**
	 * 释放资源
	 */
	public abstract void dispose();
}
