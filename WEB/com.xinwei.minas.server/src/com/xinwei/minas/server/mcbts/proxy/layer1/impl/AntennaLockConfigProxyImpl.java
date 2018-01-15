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
 * ���߱�������ҵ�����Proxyʵ����
 * 
 * @author fanhaoyu
 * 
 */

public class AntennaLockConfigProxyImpl implements AntennaLockConfigProxy {

	// ���߱���������������
	public static final int ANTENNALOCK_CONFIG_MOC = 0x07a6;
	// ���߱�����������Ӧ�� 0x07a7

	private McBtsConnector connector;

	public AntennaLockConfigProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	@Override
	public void config(McBtsAntennaLock lockConfig) throws Exception {
		// ��ģ��ת��Ϊ������Ԫ��Ϣ
		McBtsMessage lockConfigRequest = convertModelToRequest(
				lockConfig.getMoId(), lockConfig);
		try {
			// ���ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage lockConfigResponse = connector
					.syncInvoke(lockConfigRequest);
			// �������
			McBtsBizProxyHelper.parseResult(lockConfigResponse);

		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
	}

	/**
	 * ��ģ��ת��Ϊ��Ԫ��Ϣ
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
		// �����Ԫ����������Ϣ
		McBtsMessage message = new McBtsMessage();
		// �����Ϣͷ
		fillHeader(message, moId,
				getMcBtsProtocolHeaderItemMetas(ANTENNALOCK_CONFIG_MOC));
		// �����Ϣ��
		fillBody(message, moId, lockConfig);
		return message;
	}

	/**
	 * �����Ϣͷ
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
					// 16����
					message.setMoc(Integer.parseInt(itemValue.substring(2), 16));
				} else {
					message.setMoc(Integer.parseInt(itemValue));
				}
			} else if (itemName.equals(McBtsConstants.PROTOCOL_ACTION_TYPE)) {
				message.setActionType(Integer.parseInt(itemValue));
			}
		}
		// ���û�վID
		Long btsId = getBtsIdByMoId(moId);
		message.setBtsId(btsId);
	}

	/**
	 * ����MO ID��ȡ��վID
	 * 
	 * @param moId
	 * @return
	 */
	private Long getBtsIdByMoId(Long moId) {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		return mcBts.getBtsId();
	}

	/**
	 * ��ȡ��ϢͷЭ��ö��
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
	 * �����Ϣ��
	 * 
	 * @param message
	 * @param moId
	 * @param lockConfig
	 */
	private void fillBody(McBtsMessage message, Long moId,
			McBtsAntennaLock lockConfig) {
		byte[] buf = new byte[4096];
		int offset = 0;
		// ��ʵ������Ϣ����Ϊ�ֽ���
		ByteUtils.putNumber(buf, offset, String.valueOf(lockConfig.getFlag()),
				2);
		offset += 2;
		message.setContent(buf, 0, offset);
	}

}