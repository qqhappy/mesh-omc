/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-27	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.layer3.impl;

import java.text.MessageFormat;
import java.util.concurrent.TimeoutException;

import com.xinwei.minas.core.exception.BizException;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.Airlink_BCH;
import com.xinwei.minas.mcbts.core.model.layer3.Airlink_RARCH;
import com.xinwei.minas.mcbts.core.model.layer3.Airlink_RRCH;
import com.xinwei.minas.mcbts.core.model.layer3.AppendNeighborMessage;
import com.xinwei.minas.mcbts.core.model.layer3.AppendNeighborPECCHMessage;
import com.xinwei.minas.mcbts.core.model.layer3.BtsNeighborPECCHITtem;
import com.xinwei.minas.mcbts.core.model.layer3.BtsNeighbourItem;
import com.xinwei.minas.mcbts.core.model.layer3.ChannelItem;
import com.xinwei.minas.mcbts.core.model.layer3.NbSameFreqMessage;
import com.xinwei.minas.mcbts.core.model.layer3.NeighborChannelItem;
import com.xinwei.minas.mcbts.core.model.layer3.NeighborMessage;
import com.xinwei.minas.mcbts.core.model.layer3.NeighborPECCHMessage;
import com.xinwei.minas.mcbts.core.model.layer3.NeighborSmallPack;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer3.NeighbourProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 
 * @author chenshaohua
 * 
 */

public class NeighborProxyImpl implements NeighbourProxy {

	// 邻接表配置Moc值
	private final int neighbour_config_moc = 0x00B8;

	//PECCH邻站表请求配置Moc值
	private final int pecch_neighbor_config_moc = 0x0C32;
	
	// 切换附加邻接表配置Moc值
	private final int append_neighbor_query_moc = 0x0C26;
	
	//PECCH切换附加邻站表配置请求
	private final int pecch_append_neighbor_query_moc = 0x0C36;
	
	// 邻接同频配置Moc值
	private final int nb_same_freq_config_moc = 0x0712;
	
	private final int nb_small_pacakge_moc = 0x006d;

	private McBtsConnector connector;

	@Override
	public void configNeighbour(Long moId, NeighborMessage data) throws Exception {
		// 将模型转换为网元消息
		McBtsMessage request = convertModelToRequest(moId, data,
				neighbour_config_moc, McBtsConstants.OPERATION_CONFIG);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			McBtsBizProxyHelper.parseResult(response); // 解析结果
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
	}
	
	@Override
	public void configPECCHNeighbor(Long moId, NeighborPECCHMessage data)
			throws Exception {
		// 将模型转换为网元消息
		McBtsMessage request = convertModelToRequestOfPECCHNb(moId, data,
				pecch_neighbor_config_moc, McBtsConstants.OPERATION_CONFIG);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			McBtsBizProxyHelper.parseResult(response); // 解析结果
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
		
	}

	@Override
	public void configAppendNeighbor(Long moId, AppendNeighborMessage data)
			throws Exception {
		McBtsMessage request = convertModelToResquestOfAppendNb(moId, data, 
				append_neighbor_query_moc, McBtsConstants.OPERATION_CONFIG);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			McBtsBizProxyHelper.parseResult(response); // 解析结果
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		} 
	}
	
	@Override
	public void configPECCHAppendNeighbor(Long moId,
			AppendNeighborPECCHMessage data) throws Exception {
		McBtsMessage request = convertModelToRequestOfPECCHAppend(moId, data, 
				pecch_append_neighbor_query_moc, McBtsConstants.OPERATION_CONFIG);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			McBtsBizProxyHelper.parseResult(response); // 解析结果
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		} 
		
	}
	
	@Override
	public void configNbSameFreq(Long moId, NbSameFreqMessage data) throws Exception {
		// 将模型转换为网元消息
		McBtsMessage request = convertModelToRequestOfSameFreq(moId, data,
				nb_same_freq_config_moc, McBtsConstants.OPERATION_CONFIG);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			McBtsBizProxyHelper.parseResult(response); // 解析结果
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
	}

	@Override
	public void configSmallPKGReq(Long moId, NeighborSmallPack data) throws Exception {
		McBtsMessage request = converModelToSmallPKG(moId, data, nb_small_pacakge_moc, McBtsConstants.OPERATION_CONFIG);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			McBtsBizProxyHelper.parseResult(response); // 解析结果
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
	}
	
	/**
	 * 邻接表转换
	 * @param moId
	 * @param data
	 * @param moc
	 * @param operation
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequest(Long moId,
			NeighborMessage data, int moc, String operation) throws Exception {
		McBtsMessage message = new McBtsMessage(); // 构造消息
		if (data == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			throw new Exception(msg);
		}
		// 获得网元配置请求消息
		if (operation.equals(McBtsConstants.OPERATION_CONFIG)) {
			// 填充消息头
			this.fillHeader(message, moc, moId);
			// 填充消息体
			this.fillBody(message, data);
		}
		return message;
	}
	
	/**
	 * PECCH邻接表转换
	 * @param moId
	 * @param data
	 * @param moc
	 * @param operation
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequestOfPECCHNb(Long moId,
			NeighborPECCHMessage data, int moc, String operation) throws Exception {
		McBtsMessage message = new McBtsMessage(); // 构造消息
		if (data == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			throw new Exception(msg);
		}
		// 获得网元配置请求消息
		if (operation.equals(McBtsConstants.OPERATION_CONFIG)) {
			// 填充消息头
			this.fillHeader(message, moc, moId);
			// 填充消息体
			this.fillPECCHNbBody(message, data);
		}
		return message;
	}
	
	/**
	 * 附加邻接表转换
	 * @param moId
	 * @param data
	 * @param moc
	 * @param operation
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToResquestOfAppendNb(Long moId, 
			AppendNeighborMessage data, int moc, String operation) throws Exception{
		McBtsMessage message = new McBtsMessage(); // 构造消息
		if (data == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			throw new Exception(msg);
		}
		// 获得网元配置请求消息
		if (operation.equals(McBtsConstants.OPERATION_CONFIG)) {
			// 填充消息头
			this.fillHeader(message, moc, moId);
			// 填充消息体
			this.fillAppendNbBody(message, data);
		}
		return message;
	}
	
	/**
	 * PECCH附加邻接表转换
	 * @param moId
	 * @param data
	 * @param moc
	 * @param operation
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequestOfPECCHAppend(Long moId,
			AppendNeighborPECCHMessage data, int moc, String operation) throws Exception {
		McBtsMessage message = new McBtsMessage(); // 构造消息
		if (data == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			throw new Exception(msg);
		}
		// 获得网元配置请求消息
		if (operation.equals(McBtsConstants.OPERATION_CONFIG)) {
			// 填充消息头
			this.fillHeader(message, moc, moId);
			// 填充消息体
			this.fillPECCHAppendNbBody(message, data);
		}
		return message;
	}
	
	/**
	 * 邻站同频模型转换
	 * @param moId
	 * @param data
	 * @param moc
	 * @param operation
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequestOfSameFreq(Long moId,
			NbSameFreqMessage data, int moc, String operation) throws Exception {
		McBtsMessage message = new McBtsMessage(); // 构造消息
		if (data == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			throw new Exception(msg);
		}
		// 获得网元配置请求消息
		if (operation.equals(McBtsConstants.OPERATION_CONFIG)) {
			// 填充消息头
			this.fillHeader(message, moc, moId);
			// 填充消息体
			this.fillBodyOfSameFreq(message, data);
		}
		return message;
	}

	
	private McBtsMessage converModelToSmallPKG(Long moId,
			NeighborSmallPack data, int moc, String operation) throws Exception{
		McBtsMessage message = new McBtsMessage(); // 构造消息
		if (data == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			throw new Exception(msg);
		}
		// 获得网元配置请求消息
		if (operation.equals(McBtsConstants.OPERATION_CONFIG)) {
			// 填充消息头
			this.fillHeader(message, moc, moId);
			// 填充消息体
			this.fillSmallPKGBody(message, data);
		}
		return message;
	}
	
	/**
	 * 对邻站同频消息的消息体进行编码
	 * @param message
	 * @param data
	 */
	private void fillBodyOfSameFreq(McBtsMessage message, NbSameFreqMessage data) {
		byte[] buf = new byte[4096];
		int offset = 0;
		
		//neighbor size, 2
		ByteUtils.putNumber(buf, offset, data.getmNeighborBTSnumber() + "", 2);
		offset += 2;
		
		BtsNeighbourItem[] items = data.getItem();
		
		for (int i = 0; i < items.length; i++) {
			
			//BTS IP, 4
			ByteUtils.putIp(buf, offset, items[i].getBtsIp());
			offset += 4;
			
			//BTS ID, 4
			ByteUtils.putNumber(buf, offset, items[i].getmBTSID() + "", 4);
			offset += 4;
			
			//Frequency index, 2
			ByteUtils.putNumber(buf, offset,
					items[i].getmFrequencyindex() + "", 2);
			offset += 2;
			
			//Sequence ID, 1
			ByteUtils.putNumber(buf, offset, items[i].getmSequenceID() + "", 1);
			offset += 1;
			
			//Sub-carrier group Mask, 1
			ByteUtils.putNumber(buf, offset, items[i].getmSubcarriergroupMask()
					+ "", 1);
			offset += 1;

			//BCH
			Airlink_BCH[] bchs = items[i].getmAirlink_BCH();
			for (int j = 0; j < bchs.length; j++) {
				//BCH SCG, 1
				ByteUtils.putNumber(buf, offset,
						bchs[j].getmBCHSCGindex() + "", 1);
				offset += 1;
				
				//BCH TS, 1
				ByteUtils.putNumber(buf, offset, bchs[j].getmBCHTSindex() + "",
						1);
				offset += 1;
			}
			
			//RRCH 
			Airlink_RRCH[] rrchs = items[i].getmAirlink_RRCH();
			for (int k = 0; k < rrchs.length; k++) {
				//RRCH SCG, 1
				ByteUtils.putNumber(buf, offset, rrchs[k].getmRRCHSCGindex()
						+ "", 1);
				offset += 1;
				
				//RRCH TS, 1
				ByteUtils.putNumber(buf, offset, rrchs[k].getmRRCHTSindex()
						+ "", 1);
				offset += 1;
			}
			
			//RARCH 
			Airlink_RARCH[] rarchs = items[i].getmAirlink_RARCH();
			for (int m = 0; m < rarchs.length; m++) {
				//RARCH SCG, 1
				ByteUtils.putNumber(buf, offset, rarchs[m].getmRARCHSCGindex()
						+ "", 1);
				offset += 1;
				
				//RARCH TS, 1
				ByteUtils.putNumber(buf, offset, rarchs[m].getmRARCHTSindex()
						+ "", 1);
				offset += 1;
			}

			//N_ANT, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_ANT() + "", 1);
			offset += 1;
			
			//TRANSMIT_PWR, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTRANSMIT_PWR() + "",
					1);
			offset += 1;
			
			//N_TS, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_TS() + "", 1);
			offset += 1;
			
			//N_DN_TS, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_DN_TS() + "", 1);
			offset += 1;
			
			//RECEIVE_SENSITIVITY, 1
			ByteUtils.putNumber(buf, offset, items[i].getmRECEIVE_SENSITIVITY()
					+ "", 1);
			offset += 1;
			
			//MAX_SCALE, 1
			ByteUtils.putNumber(buf, offset, items[i].getmMAX_SCALE() + "", 1);
			offset += 1;
			
			//PREAMBLE_SCALE, 1
			ByteUtils.putNumber(buf, offset, items[i].getmPREAMBLE_SCALE() + "", 1);
			offset += 1;
			
			//TCH
			//TCH_SCALE0, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE0() + "", 1);
			offset += 1;
			
			//TCH_SCALE1, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE1() + "", 1);
			offset += 1;
			
			//TCH_SCALE2, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE2() + "", 1);
			offset += 1;
			
			//TCH_SCALE3, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE3() + "", 1);
			offset += 1;
			
			//TCH_SCALE4, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE4() + "", 1);
			offset += 1;
			
			//TCH_SCALE5, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE5() + "", 1);
			offset += 1;
			
			//TCH_SCALE6, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE6() + "", 1);
			offset += 1;

			//Repeater number, 2
			ByteUtils.putNumber(buf, offset,
					items[i].getmRepeaternumber() + "", 2);
			offset += 2;
		}

		message.setContent(buf, 0, offset);		
	}
	
	
	/**
	 * 对邻站表配置请求消息的数据部分进行编码
	 * @param message
	 * @param data
	 */
	private void fillBody(McBtsMessage message, NeighborMessage data) {
		byte[] buf = new byte[4096];
		int offset = 0;
		//neighbor size, 2
		ByteUtils.putNumber(buf, offset, data.getmNeighborBTSnumber() + "", 2);
		offset += 2;
		
		BtsNeighbourItem[] items = data.getItem();
		
		for (int i = 0; i < items.length; i++) {
			//BTS IP, 4
			ByteUtils.putIp(buf, offset, items[i].getBtsIp());
			offset += 4;
			
			//BTS ID, 4
			ByteUtils.putNumber(buf, offset, items[i].getmBTSID() + "", 4);
			offset += 4;
			
			//Frequency index, 2
			ByteUtils.putNumber(buf, offset,
					items[i].getmFrequencyindex() + "", 2);
			offset += 2;
			
			//Sequence ID, 1
			ByteUtils.putNumber(buf, offset, items[i].getmSequenceID() + "", 1);
			offset += 1;
		
			//Sub-carrier group Mask, 1
			ByteUtils.putNumber(buf, offset, items[i].getmSubcarriergroupMask()
					+ "", 1);
			offset += 1;
			
			//BCH
			Airlink_BCH[] bchs = items[i].getmAirlink_BCH();
			for (int j = 0; j < bchs.length; j++) {
				//BCH SCG, 1
				ByteUtils.putNumber(buf, offset,
						bchs[j].getmBCHSCGindex() + "", 1);
				offset += 1;
				
				//BCH TS, 1
				ByteUtils.putNumber(buf, offset, bchs[j].getmBCHTSindex() + "",
						1);
				offset += 1;
			}
			
			//RRCH
			Airlink_RRCH[] rrchs = items[i].getmAirlink_RRCH();
			for (int k = 0; k < rrchs.length; k++) {
				//RRCH SCG, 1
				ByteUtils.putNumber(buf, offset, rrchs[k].getmRRCHSCGindex()
						+ "", 1);
				offset += 1;
				
				//RRCH TS, 1
				ByteUtils.putNumber(buf, offset, rrchs[k].getmRRCHTSindex()
						+ "", 1);
				offset += 1;
			}
			
			//RARCH
			Airlink_RARCH[] rarchs = items[i].getmAirlink_RARCH();
			for (int m = 0; m < rarchs.length; m++) {
				//RARCH SCG, 1
				ByteUtils.putNumber(buf, offset, rarchs[m].getmRARCHSCGindex()
						+ "", 1);
				offset += 1;
				
				//RARCH TS, 1
				ByteUtils.putNumber(buf, offset, rarchs[m].getmRARCHTSindex()
						+ "", 1);
				offset += 1;
			}
			
			//N_ANT, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_ANT() + "", 1);
			offset += 1;
			
			//TRANSMIT_PWR, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTRANSMIT_PWR() + "",
					1);
			offset += 1;
			
			//N_TS, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_TS() + "", 1);
			offset += 1;
			
			//N_DN_TS, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_DN_TS() + "", 1);
			offset += 1;
			
			//RECEIVE_SENSITIVITY, 1
			ByteUtils.putNumber(buf, offset, items[i].getmRECEIVE_SENSITIVITY()
					+ "", 1);
			offset += 1;
			
			//MAX_SCALE, 1
			ByteUtils.putNumber(buf, offset, items[i].getmMAX_SCALE() + "", 1);
			offset += 1;
			
			//PREAMBLE_SCALE, 1
			ByteUtils.putNumber(buf, offset, items[i].getmPREAMBLE_SCALE() + "", 1);
			offset += 1;
			
			//TCH
			//TCH_SCALE0, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE0() + "", 1);
			offset += 1;
			
			//TCH_SCALE1, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE1() + "", 1);
			offset += 1;
			
			//TCH_SCALE2, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE2() + "", 1);
			offset += 1;
			
			//TCH_SCALE3, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE3() + "", 1);
			offset += 1;
			
			//TCH_SCALE4, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE4() + "", 1);
			offset += 1;
			
			//TCH_SCALE5, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE5() + "", 1);
			offset += 1;
			
			//TCH_SCALE6, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE6() + "", 1);
			offset += 1;
			
			//Repeater number, 2 (设置的是0)
			ByteUtils.putNumber(buf, offset,
					items[i].getmRepeaternumber() + "", 2);
			offset += 2;
		}
		
		//LongDistModFlag, 2
		ByteUtils.putNumber(buf, offset, data.getLongDistModFlag() + "", 2);
		offset += 2;
		
		//BTSMASK, 3, 
		
		//先放高8位
		short highValue = (short)(data.getBTSMASK() >> 16);
		ByteUtils.putNumber(buf, offset, highValue + "", 1);
		offset += 1;
		
		//放低16位
		ByteUtils.putNumber(buf, offset, data.getBTSMASK() + "", 2);
		offset += 2;
		
		message.setContent(buf, 0, offset);
	}

	/**
	 *  对PECCH邻站表配置请求消息的数据部分进行编码
	 * @param message
	 * @param data
	 */
	private void fillPECCHNbBody(McBtsMessage message, NeighborPECCHMessage data) {
		byte[] buf = new byte[4096];
		int offset = 0;
		//neighbor size, 2
		ByteUtils.putNumber(buf, offset, data.getmNeighborBTSnumber() + "", 2);
		offset += 2;
		
		BtsNeighborPECCHITtem[] items = data.getItems();
		
		for (int i = 0; i < items.length; i++) {
			//BTS IP, 4
			ByteUtils.putIp(buf, offset, items[i].getBtsIp());
			offset += 4;
			
			//BTS ID, 4
			ByteUtils.putNumber(buf, offset, items[i].getmBTSID() + "", 4);
			offset += 4;
			
			//Frequency index, 2
			ByteUtils.putNumber(buf, offset,
					items[i].getmFrequencyindex() + "", 2);
			offset += 2;
			
			//Sequence ID, 1
			ByteUtils.putNumber(buf, offset, items[i].getmSequenceID() + "", 1);
			offset += 1;
		
			//Sub-carrier group Mask, 1
			ByteUtils.putNumber(buf, offset, items[i].getmSubcarriergroupMask()
					+ "", 1);
			offset += 1;
			
			//PECCH_Config, 2
			ByteUtils.putNumber(buf, offset, items[i].getmPECCHConfig().encode() + "", 2);
			offset += 2;
			
			//N_ANT, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_ANT() + "", 1);
			offset += 1;
			
			//TRANSMIT_PWR, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTRANSMIT_PWR() + "",
					1);
			offset += 1;
			
			//N_TS, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_TS() + "", 1);
			offset += 1;
			
			//N_DN_TS, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_DN_TS() + "", 1);
			offset += 1;
			
			//RECEIVE_SENSITIVITY, 1
			ByteUtils.putNumber(buf, offset, items[i].getmRECEIVE_SENSITIVITY()
					+ "", 1);
			offset += 1;
			
			//MAX_SCALE, 1
			ByteUtils.putNumber(buf, offset, items[i].getmMAX_SCALE() + "", 1);
			offset += 1;
			
			//PREAMBLE_SCALE, 1
			ByteUtils.putNumber(buf, offset, items[i].getmPREAMBLE_SCALE() + "", 1);
			offset += 1;
			
			//TCH
			//TCH_SCALE0, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE0() + "", 1);
			offset += 1;
			
			//TCH_SCALE1, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE1() + "", 1);
			offset += 1;
			
			//TCH_SCALE2, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE2() + "", 1);
			offset += 1;
			
			//TCH_SCALE3, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE3() + "", 1);
			offset += 1;
			
			//TCH_SCALE4, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE4() + "", 1);
			offset += 1;
			
			//TCH_SCALE5, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE5() + "", 1);
			offset += 1;
			
			//TCH_SCALE6, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE6() + "", 1);
			offset += 1;
			
			//Repeater number, 2 (设置的是0)
			ByteUtils.putNumber(buf, offset,
					items[i].getmRepeaternumber() + "", 2);
			offset += 2;
			
		}
		
		//LongDistModFlag, 2
		ByteUtils.putNumber(buf, offset, data.getLongDistModFlag() + "", 2);
		offset += 2;
		
		//BTSMASK, 3, 
		
		//先放高8位
		short highValue = (short)(data.getBTSMASK() >> 16);
		ByteUtils.putNumber(buf, offset, highValue + "", 1);
		offset += 1;
		
		//放低16位
		ByteUtils.putNumber(buf, offset, data.getBTSMASK() + "", 2);
		offset += 2;
		
		message.setContent(buf, 0, offset);
	}
	
	/**
	 * 对切换附加邻接表的请求消息进行编码
	 * @param message
	 * @param data
	 */
	private void fillAppendNbBody(McBtsMessage message, AppendNeighborMessage data) {
		byte[] buf = new byte[4096];
		int offset = 0;
		
		//ConfigFlag size, 2
		ByteUtils.putNumber(buf, offset, data.getConfigFlag() + "", 2);
		offset += 2;
		
		//neighbor size, 2
		ByteUtils.putNumber(buf, offset, data.getmAppendNeighborBTSnumber() + "", 2);
		offset += 2;
		
		BtsNeighbourItem[] items = data.getItem();
		
		for (int i = 0; i < items.length; i++) {
			//BTS IP, 4
			ByteUtils.putIp(buf, offset, items[i].getBtsIp());
			offset += 4;
			
			//BTS ID, 4
			ByteUtils.putNumber(buf, offset, items[i].getmBTSID() + "", 4);
			offset += 4;
			
			//Frequency index, 2
			ByteUtils.putNumber(buf, offset,
					items[i].getmFrequencyindex() + "", 2);
			offset += 2;
			
			//Sequence ID, 1
			ByteUtils.putNumber(buf, offset, items[i].getmSequenceID() + "", 1);
			offset += 1;
		
			//Sub-carrier group Mask, 1
			ByteUtils.putNumber(buf, offset, items[i].getmSubcarriergroupMask()
					+ "", 1);
			offset += 1;
			
			//BCH
			Airlink_BCH[] bchs = items[i].getmAirlink_BCH();
			for (int j = 0; j < bchs.length; j++) {
				//BCH SCG, 1
				ByteUtils.putNumber(buf, offset,
						bchs[j].getmBCHSCGindex() + "", 1);
				offset += 1;
				
				//BCH TS, 1
				ByteUtils.putNumber(buf, offset, bchs[j].getmBCHTSindex() + "",
						1);
				offset += 1;
			}
			
			//RRCH
			Airlink_RRCH[] rrchs = items[i].getmAirlink_RRCH();
			for (int k = 0; k < rrchs.length; k++) {
				//RRCH SCG, 1
				ByteUtils.putNumber(buf, offset, rrchs[k].getmRRCHSCGindex()
						+ "", 1);
				offset += 1;
				
				//RRCH TS, 1
				ByteUtils.putNumber(buf, offset, rrchs[k].getmRRCHTSindex()
						+ "", 1);
				offset += 1;
			}
			
			//RARCH
			Airlink_RARCH[] rarchs = items[i].getmAirlink_RARCH();
			for (int m = 0; m < rarchs.length; m++) {
				//RARCH SCG, 1
				ByteUtils.putNumber(buf, offset, rarchs[m].getmRARCHSCGindex()
						+ "", 1);
				offset += 1;
				
				//RARCH TS, 1
				ByteUtils.putNumber(buf, offset, rarchs[m].getmRARCHTSindex()
						+ "", 1);
				offset += 1;
			}
			
			//N_ANT, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_ANT() + "", 1);
			offset += 1;
			
			//TRANSMIT_PWR, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTRANSMIT_PWR() + "",
					1);
			offset += 1;
			
			//N_TS, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_TS() + "", 1);
			offset += 1;
			
			//N_DN_TS, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_DN_TS() + "", 1);
			offset += 1;
			
			//RECEIVE_SENSITIVITY, 1
			ByteUtils.putNumber(buf, offset, items[i].getmRECEIVE_SENSITIVITY()
					+ "", 1);
			offset += 1;
			
			//MAX_SCALE, 1
			ByteUtils.putNumber(buf, offset, items[i].getmMAX_SCALE() + "", 1);
			offset += 1;
			
			//PREAMBLE_SCALE, 1
			ByteUtils.putNumber(buf, offset, items[i].getmPREAMBLE_SCALE() + "", 1);
			offset += 1;
			
			//TCH
			//TCH_SCALE0, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE0() + "", 1);
			offset += 1;
			
			//TCH_SCALE1, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE1() + "", 1);
			offset += 1;
			
			//TCH_SCALE2, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE2() + "", 1);
			offset += 1;
			
			//TCH_SCALE3, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE3() + "", 1);
			offset += 1;
			
			//TCH_SCALE4, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE4() + "", 1);
			offset += 1;
			
			//TCH_SCALE5, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE5() + "", 1);
			offset += 1;
			
			//TCH_SCALE6, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE6() + "", 1);
			offset += 1;
			
			//Repeater number, 2 (设置的是0)
			ByteUtils.putNumber(buf, offset,
					items[i].getmRepeaternumber() + "", 2);
			offset += 2;
		}
		
		//BTSMASK1, 4
		ByteUtils.putNumber(buf, offset, data.getBTSMASK1() + "", 4);
		offset += 4;
		
		//BTSMASK2, 2
		ByteUtils.putNumber(buf, offset, data.getBTSMASK2() + "", 2);
		offset += 2;
		
		message.setContent(buf, 0, offset);
	}
	
	/**
	 * 对PECCH切换附加邻接表的请求消息进行编码
	 * @param message
	 * @param data
	 */
	public void fillPECCHAppendNbBody(McBtsMessage message, AppendNeighborPECCHMessage data) {
		byte[] buf = new byte[4096];
		int offset = 0;
		
		//ConfigFlag size, 2
		ByteUtils.putNumber(buf, offset, data.getConfigFlag() + "", 2);
		offset += 2;
		
		//neighbor size, 2
		ByteUtils.putNumber(buf, offset, data.getmAppendNeighborBTSnumber() + "", 2);
		offset += 2;
		
		BtsNeighborPECCHITtem[] items = data.getItems();
		
		for(int i = 0; i < items.length; i++) {
			//BTS IP, 4
			ByteUtils.putIp(buf, offset, items[i].getBtsIp());
			offset += 4;
			
			//BTS ID, 4
			ByteUtils.putNumber(buf, offset, items[i].getmBTSID() + "", 4);
			offset += 4;
			
			//Frequency index, 2
			ByteUtils.putNumber(buf, offset,
					items[i].getmFrequencyindex() + "", 2);
			offset += 2;
			
			//Sequence ID, 1
			ByteUtils.putNumber(buf, offset, items[i].getmSequenceID() + "", 1);
			offset += 1;
		
			//Sub-carrier group Mask, 1
			ByteUtils.putNumber(buf, offset, items[i].getmSubcarriergroupMask()
					+ "", 1);
			offset += 1;
			
			//PECCH_Config, 2
			ByteUtils.putNumber(buf, offset, items[i].getmPECCHConfig().encode() + "", 2);
			offset += 2;
			
			//N_ANT, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_ANT() + "", 1);
			offset += 1;
			
			//TRANSMIT_PWR, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTRANSMIT_PWR() + "",
					1);
			offset += 1;
			
			//N_TS, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_TS() + "", 1);
			offset += 1;
			
			//N_DN_TS, 1
			ByteUtils.putNumber(buf, offset, items[i].getmN_DN_TS() + "", 1);
			offset += 1;
			
			//RECEIVE_SENSITIVITY, 1
			ByteUtils.putNumber(buf, offset, items[i].getmRECEIVE_SENSITIVITY()
					+ "", 1);
			offset += 1;
			
			//MAX_SCALE, 1
			ByteUtils.putNumber(buf, offset, items[i].getmMAX_SCALE() + "", 1);
			offset += 1;
			
			//PREAMBLE_SCALE, 1
			ByteUtils.putNumber(buf, offset, items[i].getmPREAMBLE_SCALE() + "", 1);
			offset += 1;
			
			//TCH
			//TCH_SCALE0, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE0() + "", 1);
			offset += 1;
			
			//TCH_SCALE1, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE1() + "", 1);
			offset += 1;
			
			//TCH_SCALE2, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE2() + "", 1);
			offset += 1;
			
			//TCH_SCALE3, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE3() + "", 1);
			offset += 1;
			
			//TCH_SCALE4, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE4() + "", 1);
			offset += 1;
			
			//TCH_SCALE5, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE5() + "", 1);
			offset += 1;
			
			//TCH_SCALE6, 1
			ByteUtils.putNumber(buf, offset, items[i].getmTCH_SCALE6() + "", 1);
			offset += 1;
			
			//Repeater number, 2 (设置的是0)
			ByteUtils.putNumber(buf, offset,
					items[i].getmRepeaternumber() + "", 2);
			offset += 2;
		}
		
		//BTSMASK1, 4
		ByteUtils.putNumber(buf, offset, data.getBTSMASK1() + "", 4);
		offset += 4;
		
		//BTSMASK2, 2
		ByteUtils.putNumber(buf, offset, data.getBTSMASK2() + "", 2);
		offset += 2;
		
		message.setContent(buf, 0, offset);
	}
	
	/**
	 * 对邻站小包信道配置请求消息的数据部分进行填充
	 * @param message
	 * @param data
	 */
	private void fillSmallPKGBody(McBtsMessage message, NeighborSmallPack data) {
		byte[] buf = new byte[4096];
		int offset = 0;
		
		//neighbor size, 2
		ByteUtils.putNumber(buf, offset, String.valueOf(data.getmNeighborBTSnumber()), 2);
		offset += 2;
		
		NeighborChannelItem[] item = data.getItems();
		for (int i = 0; i < item.length; i++) {
			//BTS ID, 4
			ByteUtils.putNumber(buf, offset, String.valueOf(item[i].getmBtsId()), 4);
			offset += 4;
			
			//fach
			ChannelItem[] fachs = item[i].getFachs();
			for (int j = 0; j < fachs.length; j++) {
				ByteUtils.putNumber(buf, offset, String.valueOf(fachs[j].getSCGindex()), 1);
				offset += 1;
				ByteUtils.putNumber(buf, offset, String.valueOf(fachs[j].getTSindex()), 1);
				offset += 1;
			}
			
			//rpch
			ChannelItem[] rpchs = item[i].getRpchs();
			for (int j = 0; j < rpchs.length; j++) {
				ByteUtils.putNumber(buf, offset, String.valueOf(rpchs[j].getSCGindex()), 1);
				offset += 1;
				ByteUtils.putNumber(buf, offset, String.valueOf(rpchs[j].getTSindex()), 1);
				offset += 1;
			}
		}
		
		message.setContent(buf, 0 , offset);
	}
	
	private void fillHeader(McBtsMessage message, int moc, Long moId) {
		message.setMsgArea(1);
		message.setMa(McBtsMessage.MA_CONF);
		message.setMoc(moc);
		message.setActionType(McBtsMessage.ACTION_TYPE_CONFIG);
		// 设置基站ID
		Long btsId = this.getBtsIdByMoId(moId);
		message.setBtsId(btsId);
	}

	// 根据moId获取btsId
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
