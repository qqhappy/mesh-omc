/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.proxy.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.ut.proxy.UTMemFunctionProxy;
import com.xinwei.minas.ut.core.model.UTLayer3Param;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * MEM���ܴ���ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class UTMemFunctionProxyImpl implements UTMemFunctionProxy{
	
	private static final Log log = LogFactory.getLog(UTMemFunctionProxyImpl.class);

	private McBtsConnector connector;

	public void setConnector(McBtsConnector connector) {
		this.connector = connector;
	}
	
	@Override
	public List<UTLayer3Param> queryMemLayer3Param(Long moId, GenericBizData genericBizData)
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
			List<UTLayer3Param> params = convertResponseToModel(response);
			return params;
		} catch (TimeoutException e) {
			log.error("���վ��ѯ\"" + genericBizData.getBizName() + "\"ʱ������ʱ����", e);
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time")
							+ e.getLocalizedMessage());
		} catch (UnsupportedOperationException e) {
			log.error(OmpAppContext.getMessage("unsupported_biz_operation")
					+ ":" + genericBizData.getBizName());
			throw new UnsupportedOperationException(
					OmpAppContext.getMessage("unsupported_biz_operation"));
		} catch (Exception e) {
			log.error("��ѯ" + genericBizData.getBizName() + "ʱ������ѯ����", e);
			throw new Exception(OmpAppContext.getMessage("mcbts_query_error")
					+ e.getLocalizedMessage());
		}
	}

	
	
	private List<UTLayer3Param> convertResponseToModel(McBtsMessage response) {
		List<UTLayer3Param> params = new ArrayList<UTLayer3Param>();
		byte[] buff = response.getContent();
		int offset = 0;
		
		//pid, 4
		long pid = ByteUtils.toUnsignedNumber(buff, offset, 4);
		offset += 4;
		
		//num, 1
		int num =(int) ByteUtils.toUnsignedNumber(buff, offset, 1);
		offset++;
		
		//Reserve, 1
		offset++;
		
		for (int i = 0; i < num; i++) {
			UTLayer3Param  param = new UTLayer3Param();
			
			//Ip, 4
			String ip = ByteUtils.toIp(buff, offset, 4);
			offset += 4;
			param.setIpAddress(ip);
			
			//mac, 6
			String mac = ByteUtils.toHexString(buff, offset, 6);
			offset += 6;
			param.setMac(mac);
			
			//Ip Mask, 4
			String ipMask = ByteUtils.toIp(buff, offset, 4); 
			offset += 4;
			param.setIpMask(ipMask);
			
			//Ip GateWay, 4
			String ipGateWay = ByteUtils.toIp(buff, offset, 4);
			offset += 4;
			param.setIpGateWay(ipGateWay);
			
			//DHCP flag, 2
			int flag =  ByteUtils.toInt(buff, offset, 2);
			offset += 2;
			param.setDhcpFlag(flag);
			
			params.add(param);
		}
		
		return params;
	}
}
