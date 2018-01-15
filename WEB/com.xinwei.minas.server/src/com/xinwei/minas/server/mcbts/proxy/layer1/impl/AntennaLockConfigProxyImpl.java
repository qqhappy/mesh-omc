/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-8-7	| fanhaoyu		 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.layer1.impl;

import java.text.MessageFormat;
import java.util.concurrent.TimeoutException;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer1.McBtsAntennaLock;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer1.AntennaLockConfigProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 天线闭锁配置业务服务Proxy实现类
 * 
 * @author fanhaoyu
 * 
 */

public class AntennaLockConfigProxyImpl implements AntennaLockConfigProxy {

	// 天线闭锁功能配置请求
	public static final int ANTENNALOCK_CONFIG_MOC = 0x07a6;
	// 天线闭锁功能配置应答 0x07a7

	private McBtsConnector connector;

	public AntennaLockConfigProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	@Override
	public void config(McBtsAntennaLock lockConfig) throws Exception {
		// 将模型转换为配置网元消息
		McBtsMessage lockConfigRequest = convertModelToRequest(
				lockConfig.getMoId(), lockConfig);
		try {
			// 调底层通信层发送消息, 同步等待应答
			McBtsMessage lockConfigResponse = connector
					.syncInvoke(lockConfigRequest);
			// 解析结果
			McBtsBizProxyHelper.parseResult(lockConfigResponse);

		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
	}

	/**
	 * 将模型转化为网元消息
	 * 
	 * @param moId
	 * @param lockConfig
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequest(Long moId,
			McBtsAntennaLock lockConfig) throws Exception {
		if (lockConfig == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			throw new Exception(msg);
		}
		// 获得网元配置请求消息
		McBtsMessage message = new McBtsMessage();
		// 填充消息头
		fillHeader(message, moId,
				getMcBtsProtocolHeaderItemMetas(ANTENNALOCK_CONFIG_MOC));
		// 填充消息体
		fillBody(message, moId, lockConfig);
		return message;
	}

	/**
	 * 填充消息头
	 * 
	 * @param message
	 * @param moId
	 * @param headerItems
	 */
	private void fillHeader(McBtsMessage message, Long moId,
			McBtsProtocolHeaderItemMeta[] headerItems) {
		for (McBtsProtocolHeaderItemMeta item : headerItems) {
			String itemName = item.getName();
			String itemValue = item.getValue();
			if (itemName.equals(McBtsConstants.PROTOCOL_MSG_AREA)) {
				message.setMsgArea(Integer.parseInt(itemValue));
			} else if (itemName.equals(McBtsConstants.PROTOCOL_MA)) {
				message.setMa(Integer.parseInt(itemValue));
			} else if (itemName.equals(McBtsConstants.PROTOCOL_MOC)) {
				if (itemValue.toLowerCase().startsWith("0x")) {
					// 16进制
					message.setMoc(Integer.parseInt(itemValue.substring(2), 16));
				} else {
					message.setMoc(Integer.parseInt(itemValue));
				}
			} else if (itemName.equals(McBtsConstants.PROTOCOL_ACTION_TYPE)) {
				message.setActionType(Integer.parseInt(itemValue));
			}
		}
		// 设置基站ID
		Long btsId = getBtsIdByMoId(moId);
		message.setBtsId(btsId);
	}

	/**
	 * 根据MO ID获取基站ID
	 * 
	 * @param moId
	 * @return
	 */
	private Long getBtsIdByMoId(Long moId) {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		return mcBts.getBtsId();
	}

	/**
	 * 获取消息头协议枚举
	 * 
	 * @param moc
	 * @return
	 */
	private McBtsProtocolHeaderItemMeta[] getMcBtsProtocolHeaderItemMetas(
			int moc) {
		McBtsProtocolHeaderItemMeta[] items = new McBtsProtocolHeaderItemMeta[4];
		items[0] = new McBtsProtocolHeaderItemMeta();
		items[1] = new McBtsProtocolHeaderItemMeta();
		items[2] = new McBtsProtocolHeaderItemMeta();
		items[3] = new McBtsProtocolHeaderItemMeta();
		items[0].setName("MsgArea");
		items[0].setValue("1");
		items[1].setName("MA");
		items[1].setValue("0");
		items[2].setName("MOC");
		items[2].setValue("" + moc);
		items[3].setName("ActionType");
		items[3].setValue("1");
		return items;
	}

	/**
	 * 填充消息体
	 * 
	 * @param message
	 * @param moId
	 * @param lockConfig
	 */
	private void fillBody(McBtsMessage message, Long moId,
			McBtsAntennaLock lockConfig) {
		byte[] buf = new byte[4096];
		int offset = 0;
		// 将实体类信息解析为字节流
		ByteUtils.putNumber(buf, offset, String.valueOf(lockConfig.getFlag()),
				2);
		offset += 2;
		message.setContent(buf, 0, offset);
	}

}