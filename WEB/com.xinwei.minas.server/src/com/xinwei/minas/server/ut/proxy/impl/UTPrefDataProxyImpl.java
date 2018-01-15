/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-19	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.proxy.impl;

import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.ut.proxy.UTPrefDataProxy;
import com.xinwei.minas.ut.core.model.SNRCI;
import com.xinwei.minas.ut.core.model.UTChannelPpc;
import com.xinwei.minas.ut.core.model.UTPerfData;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 *  �鿴�ն��������ݴ���
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class UTPrefDataProxyImpl implements UTPrefDataProxy {

	private static final Log log = LogFactory.getLog(UTPrefDataProxyImpl.class);

	private McBtsConnector connector;

	public void setConnector(McBtsConnector connector) {
		this.connector = connector;
	}
	
	@Override
	public UTPerfData query(Long moId, GenericBizData genericBizData) throws Exception {
		// ��ģ��ת��Ϊ��Ԫ��Ϣ
		McBtsMessage request = McBtsBizProxyHelper.convertModelToRequest(moId,
				genericBizData, McBtsConstants.OPERATION_QUERY);
		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage response = connector.syncInvoke(request);
			// �������
			McBtsBizProxyHelper.parseResult(response);
			// ����Ԫ��Ϣת��Ϊģ��
			UTPerfData data = convertResponseToModel(response);
			return data;
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

	private UTPerfData convertResponseToModel(McBtsMessage response) {
		UTPerfData data = new UTPerfData();
		byte[] buf = response.getContent();
		int offset = 0;
		
		//pid, 4
		data.setPid(ByteUtils.toUnsignedNumber(buf, offset, 4));
		offset += 4;
		
		//press
		data.setPress((int)ByteUtils.toUnsignedNumber(buf, offset, 1));
		
		//status, 2
		int status = (int)ByteUtils.toUnsignedNumber(buf, offset, 2);
		offset += 2;
		//data status, bit0
		data.setDataStatus(status & 0x01);
		//voice status, bit1
		data.setVoiceStatus((status & 0x02) >> 1);
		
		//UplinkBW, 4
		data.setUplinkBW(ByteUtils.toUnsignedNumber(buf, offset, 4));
		offset += 4;
		
		//DownlinkBW, 4
		data.setDownlinkBW(ByteUtils.toUnsignedNumber(buf, offset, 4));
		offset += 4;
		
		UTChannelPpc[] ppcs = new UTChannelPpc[UTPerfData.TIME_SLOT];
		
		for(int i = 0; i < UTPerfData.TIME_SLOT; i++) {
			ppcs[i] = new UTChannelPpc();
			//���ŵ���,1
			ppcs[i].setSubChannelNum((int)ByteUtils.toUnsignedNumber(buf, offset, 1));
			offset += 1;
			//ppc, 1, -127~127
			ppcs[i].setPpc(ByteUtils.toInt(buf, offset, 1));
			offset += 1;
		}
		data.setuTChannelPpcs(ppcs);
		
		SNRCI[] sNRCIs = new SNRCI[UTPerfData.TIME_SLOT];
		for (int i = 0; i < UTPerfData.TIME_SLOT; i++) {
			sNRCIs[i] = new SNRCI();
			//SNR, 1
			sNRCIs[i].setSnr(ByteUtils.toInt(buf, offset, 1));
			offset += 1;
			//CI, 2
			sNRCIs[i].setCi(ByteUtils.toInt(buf, offset, 2));
			offset += 2;
		}
		data.setsNRCIs(sNRCIs);
		
		return data;
	}
}
