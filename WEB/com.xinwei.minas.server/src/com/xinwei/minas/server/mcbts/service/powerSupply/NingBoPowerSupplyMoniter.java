/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.powerSupply;

import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.sysManage.PowerSupply;
import com.xinwei.minas.server.mcbts.helper.McBtsAlarmHelper;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * 宁波涉洋电柜监控类
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class NingBoPowerSupplyMoniter extends PowerSupplyMoniter {

	Log log = LogFactory.getLog(NingBoPowerSupplyMoniter.class);
	
	
	//消息处理器
	private PowerSupplyMessageManager messageManager;
	
	//与电源的连接器
	private PowerSupplyTcpConnector connector;
		
	public NingBoPowerSupplyMoniter(PowerSupply power) {
		super(power);
		messageManager = new NingBoPowerSupplyMessageManager(power);
		connector = PowerSupplyTcpConnector.getInstance();
	}

	@Override
	public byte[] getPollCmd(PowerSupply power) {
		return messageManager.createPollCmdMessage();
	}

	@Override
	public byte[] sendMessage(byte[] message) {
		if (message == null) {
			return null;
		}
		return connector.sendMessage(message, power.getIpAddress(), power.getPort());
	}

	@Override
	public String parseResponse(byte[] response) {
		if (response == null) {
			return null;
		}
		try {
			return messageManager.parseMessage(response);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public void createAlarm(String content) {
		if (content == null || "".equals(content)) {
			return;
		}
		HashSet<Long> moIdSet = power.getMoIdSet();
		if (moIdSet != null && moIdSet.size() > 0) {
			McBtsAlarmHelper mMcBtsAlarmHelper = AppContext.getCtx().getBean(McBtsAlarmHelper.class);
			for (Long moId : moIdSet) {
				McBts bts = McBtsCache.getInstance().queryByMoId(moId);
				if (bts != null) {
					mMcBtsAlarmHelper.firePowerSupplyAlarm(bts, content);
				}
			}
		}
	}

	@Override
	public void dispose() {
		//关闭连接
		if (connector != null) {
			connector.close(power.getIpAddress(), power.getPort());
		}
	}

}
