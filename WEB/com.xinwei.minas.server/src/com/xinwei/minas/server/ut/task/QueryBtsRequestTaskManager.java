/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-30	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.server.hlr.net.oss.HlrOssConnector;
import com.xinwei.minas.server.hlr.net.oss.model.HlrOssBizMessage;
import com.xinwei.minas.server.hlr.net.udp.HlrUdpConnector;
import com.xinwei.minas.server.hlr.net.udp.HlrUdpMessage;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.ut.model.message.HlrQueryBtsResponse;

/**
 * 
 * ��ѯ��վ��������������
 * 
 * @author tiance
 * 
 */

public class QueryBtsRequestTaskManager {
	private Log log = LogFactory.getLog(QueryBtsRequestTaskManager.class);

	private static final int MAX_BTS_PER_BODY = 15;

	private HlrOssConnector hlrOssConnector;

	private HlrUdpConnector hlrUdpConnector;

	private static final QueryBtsRequestTaskManager instance = new QueryBtsRequestTaskManager();

	private QueryBtsRequestTaskManager() {
		hlrOssConnector = AppContext.getCtx().getBean(HlrOssConnector.class);
		hlrUdpConnector = AppContext.getCtx().getBean(HlrUdpConnector.class);
	}

	public static QueryBtsRequestTaskManager getInstance() {
		return instance;
	}

	public void handleRequest(HlrUdpMessage message) {
		// ���ȷ��ز�ѯ��վӦ��Tag:6,�����ͨ��OSS���ͻ�վ�б�.
		HlrUdpMessage hlrResponse = new HlrUdpMessage();
		hlrResponse.setHlrIp(message.getHlrIp());
		hlrResponse.setHlrPort(message.getHlrPort());
		hlrResponse.setMessageType(6);
		hlrResponse.setLastPacketFlag(1);
		hlrResponse.setVersion(3);
		hlrResponse.setTransactionId(message.getTransactionId());
		hlrResponse.setTotalPacket(1);
		// Tag6��Ӧ�����Ϊ�ջ�վ�б�,��Ҫ���ǿ�OSS͸�����б�.
		hlrResponse.setContent(new byte[] { 0, 0, 0 });

		try {
			hlrUdpConnector.asyncInvoke(hlrResponse);
			log.debug("send hlr query bts response.");
		} catch (Exception e1) {
			log.error("Sending hlr query bts response.", e1);
		}

		List<McBts> list = McBtsCache.getInstance().queryAll();

		// �ְ�,listInList�е�ÿ��Ԫ��Ϊһ����
		List<List<McBts>> listInList = split(list);

		int packNum = listInList.size();
		for (int pageIndex = 0; pageIndex < packNum; pageIndex++) {

			HlrQueryBtsResponse response = new HlrQueryBtsResponse(
					pageIndex == (packNum - 1), list.size(), pageIndex,
					listInList.get(pageIndex));

			HlrOssBizMessage hlrOssResponse = response.toHlrOssBizMessage();

			try {
				log.debug("Send hlr bts response.");
				hlrOssConnector.asyncInvoke(hlrOssResponse);

			} catch (TimeoutException e) {
				log.error("Sending hlr bts res timeout!", e);
			} catch (Exception e) {
				log.error("Error sending hlr bts res", e);
			}
		}
	}

	/**
	 * ��������վ�б���ÿ��������ĸ������б�ļ���
	 * 
	 * @param list
	 * @return
	 */
	private List<List<McBts>> split(List<McBts> list) {
		List<List<McBts>> listInList = new ArrayList<List<McBts>>();

		if (list.size() < MAX_BTS_PER_BODY) {
			listInList.add(list);
		} else {
			int full_list = list.size() / MAX_BTS_PER_BODY;
			for (int i = 0; i < full_list; i++) {
				List<McBts> sub = list.subList(i * MAX_BTS_PER_BODY, (i + 1)
						* MAX_BTS_PER_BODY);
				listInList.add(Arrays.asList(sub.toArray(new McBts[0])));
			}
			List<McBts> sub = list.subList(full_list * MAX_BTS_PER_BODY,
					list.size());
			listInList.add(Arrays.asList(sub.toArray(new McBts[0])));
		}

		return listInList;
	}
}
