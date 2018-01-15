/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-8	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.sysManage.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.sysManage.TerminalDetectiveProxy;
import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.minas.ut.core.model.UserTerminalNetwork;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * �ն�̽�������
 * 
 * 
 * @author tiance
 * 
 */

public class TerminalDetectiveProxyImpl implements TerminalDetectiveProxy {
	private static final Log log = LogFactory
			.getLog(TerminalDetectiveProxyImpl.class);

	private McBtsConnector connector;

	public TerminalDetectiveProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	/**
	 * ��ѯ��Ԫҵ������
	 * 
	 * @param genericBizData
	 *            ��Ԫҵ������
	 * @return ��¼��
	 * @throws Exception
	 */
	@Override
	public UserTerminal query(Long moId, GenericBizData genericBizData)
			throws Exception {
		// ��ģ��ת��Ϊ��Ԫ��Ϣ
		McBtsMessage request = McBtsBizProxyHelper.convertModelToRequest(moId,
				genericBizData, McBtsConstants.OPERATION_QUERY);
		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage response = connector.syncInvoke(request);
			// �������
			McBtsBizProxyHelper.parseResult(response);
			// ����Ԫ��Ϣת��Ϊģ��
			UserTerminal result = convertResponseToModel(response);
			return result;
		} catch (TimeoutException e) {
			log.error("���վ��ѯ\"" + genericBizData.getBizName() + "\"ʱ������ʱ����", e);
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time")
							+ e.getLocalizedMessage());
		} catch (UnsupportedOperationException e) {
			throw e;
		} catch (Exception e) {
			log.error("��ѯ" + genericBizData.getBizName() + "ʱ������ѯ����", e);
			throw new Exception(OmpAppContext.getMessage("mcbts_query_error")
					+ e.getLocalizedMessage());
		}
	}

	private UserTerminal convertResponseToModel(McBtsMessage message) {
		byte[] buf = message.getContent();
		int offset = 0;

		UserTerminal ut = new UserTerminal();

		ut.setPid(ByteUtils.toHexString(buf, offset, 4));
		offset += 4;

		int size = ByteUtils.toInt(buf, offset, 1);
		offset += 1;
		List<UserTerminalNetwork> list = new ArrayList<UserTerminalNetwork>(
				size);

		for (int i = 0; i < size; i++) {
			UserTerminalNetwork network = new UserTerminalNetwork();
			network.setIp(ByteUtils.toIp(buf, offset, 4));
			offset += 4;
			network.setMac(ByteUtils.toHexString(buf, offset, 6));
			offset += 6;
			network.setDhcp(ByteUtils.toInt(buf, offset, 1));
			offset += 1;
			list.add(network);
		}

		ut.setUtNetwork(list);

		return ut;
	}
}
