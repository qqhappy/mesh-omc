/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-3	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.rruManage.impl;

import java.text.MessageFormat;
import java.util.concurrent.TimeoutException;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.rruManage.RRUResetProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.server.OmpAppContext;

/**
 * @author chenshaohua
 * 
 */

public class RRUResetProxyImpl implements RRUResetProxy {

	private McBtsConnector connector;

	@Override
	public void config(Long moId) throws Exception,
			UnsupportedOperationException {
		// ��ģ��ת��Ϊ��Ԫ��Ϣ
		McBtsMessage request = convertModelToRequest(moId,
				McBtsConstants.OPERATION_CONFIG);
		// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
		connector.asyncInvoke(request);
	}

	private McBtsMessage convertModelToRequest(Long moId, String operation)
			throws Exception {
		McBtsMessage request = new McBtsMessage();
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas().getProtocolMetaBy("rruReset", operation);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, operation);
			throw new Exception(msg);
		}
		// ��ȡԪ����
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
				.getHeader().getItem();
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getRequest()
				.getBody().getItem();
		// �����Ϣͷ
		this.fillHeader(request, moId, headerItems);
		return request;
	}

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
			// ���û�վID
			Long btsId = this.getBtsIdByMoId(moId);
			message.setBtsId(btsId);
		}
	}

	// ����moId��ȡbtsId
	private Long getBtsIdByMoId(Long moId) {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		return mcBts.getBtsId();
	}

	public McBtsConnector getConnector() {
		return connector;
	}

	public void setConnector(McBtsConnector connector) {
		this.connector = connector;
	}

}
