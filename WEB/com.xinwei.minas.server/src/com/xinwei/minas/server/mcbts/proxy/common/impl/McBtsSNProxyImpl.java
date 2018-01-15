/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.common.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.mcbts.core.model.common.McBtsSN;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.common.McBtsSNProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizRecord;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 基站序列号proxy实现
 * 
 * @author chenshaohua
 * 
 */

public class McBtsSNProxyImpl implements McBtsSNProxy {

	private final static int SN_LENGTH = 20;
	private final static int SN_NUM = 10;
	public final static String ERROR_QUERY_FAILED = "query failed";
	
	private static String NOT_EXIST = "";

	private static String NOT_SUPPORT = "-1";

	private McBtsConnector connector;

	@Override
	public McBtsSN querySN(Long moId) throws UnsupportedOperationException,
			Exception {
		McBtsMessage request1 = convertModelToRequest(moId,
				McBtsConstants.OPERATION_QUERY);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response1 = connector.syncInvoke(request1);
			// 解析结果
			McBtsBizProxyHelper.parseResult(response1);
			byte[] buf = response1.getContent();
			byte[][] panelSN = new byte[SN_NUM][SN_LENGTH];
			int currentindex = 0;
			for (int i = 0; i < SN_NUM; i++) {
				System.arraycopy(buf, currentindex, panelSN[i], 0, SN_LENGTH);
				currentindex += SN_LENGTH;
			}
			McBtsSN mcBtsSN = new McBtsSN();
			String xSN = null;
			for (int i = 0; i < panelSN.length; i++) {
				xSN = getPanelSNWithStatus(panelSN, i);
				if (i == 0) {
					mcBtsSN.setBbPanel(xSN);
				}
				if (i == 1) {
					mcBtsSN.setSynPanel(xSN);
				}
				if (i == 2) {
					mcBtsSN.setRfPanel1(xSN);
				}
				if (i == 3) {
					mcBtsSN.setRfPanel2(xSN);
				}
				if (i == 4) {
					mcBtsSN.setRfPanel3(xSN);
				}
				if (i == 5) {
					mcBtsSN.setRfPanel4(xSN);
				}
				if (i == 6) {
					mcBtsSN.setRfPanel5(xSN);
				}
				if (i == 7) {
					mcBtsSN.setRfPanel6(xSN);
				}
				if (i == 8) {
					mcBtsSN.setRfPanel7(xSN);
				}
				if (i == 9) {
					mcBtsSN.setRfPanel8(xSN);
				}
			}
			return mcBtsSN;
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time")
							+ e.getLocalizedMessage());
		} catch (UnsupportedOperationException e) {
			throw new Exception(
					OmpAppContext.getMessage("unsupported_biz_operation"));
		} catch (Exception e) {
			throw new Exception(OmpAppContext.getMessage("mcbts_query_error")
					+ e.getLocalizedMessage());
		}
	}

	@Override
	public McBtsSN queryRRUSN(Long moId) throws Exception,
			UnsupportedOperationException {
		McBtsMessage request2 = convertModelToRequest1(moId,
				McBtsConstants.OPERATION_QUERY);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response2 = connector.syncInvoke(request2);
			// 解析结果
			McBtsBizProxyHelper.parseResult(response2);
			byte[] buf = response2.getContent();
			byte[][] panelSN = new byte[SN_NUM][SN_LENGTH];
			int currentindex = 2;
			for (int i = 0; i < SN_NUM; i++) {
				System.arraycopy(buf, currentindex, panelSN[i], 0, SN_LENGTH);
				currentindex += 22;
			}
			McBtsSN mcBtsSN = new McBtsSN();
			String xSN = null;
			for (int i = 0; i < panelSN.length; i++) {
				xSN = getPanelSNWithoutStatus(panelSN, i);
				if (i == 0) {
					mcBtsSN.setRfPanel1(xSN);
				}
				if (i == 1) {
					mcBtsSN.setRfPanel2(xSN);
				}
				if (i == 2) {
					mcBtsSN.setRfPanel3(xSN);
				}
				if (i == 3) {
					mcBtsSN.setRfPanel4(xSN);
				}
				if (i == 4) {
					mcBtsSN.setRfPanel5(xSN);
				}
				if (i == 5) {
					mcBtsSN.setRfPanel6(xSN);
				}
				if (i == 6) {
					mcBtsSN.setRfPanel7(xSN);
				}
				if (i == 7) {
					mcBtsSN.setRfPanel8(xSN);
				}
				if (i == 8) {
					mcBtsSN.setSynPanel(xSN);
				}
				if (i == 9) {
					mcBtsSN.setDsbPanel(xSN);
				}
			}
			return mcBtsSN;
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time")
							+ e.getLocalizedMessage());
		} catch (UnsupportedOperationException e) {
			throw new Exception(
					OmpAppContext.getMessage("unsupported_biz_operation"));
		} catch (Exception e) {
			throw new Exception(OmpAppContext.getMessage("mcbts_query_error")
					+ e.getLocalizedMessage());
		}
	}

	private McBtsMessage convertModelToRequest(Long moId, String operation)
			throws Exception {
		McBtsMessage message = new McBtsMessage();
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas().getProtocolMetaBy("mcBtsSN", operation);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, operation);
			throw new Exception(msg);
		}
		// 获取元数据
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
				.getHeader().getItem();
		// 填充消息头
		this.fillHeader(message, moId, headerItems);

		return message;
	}

	private McBtsMessage convertModelToRequest1(Long moId, String operation)
			throws Exception {
		McBtsMessage message = new McBtsMessage();
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas()
				.getProtocolMetaBy("rru_hardware_info", operation);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, operation);
			throw new Exception(msg);
		}
		// 获取元数据
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
				.getHeader().getItem();
		// 填充消息头
		this.fillHeader(message, moId, headerItems);
		return message;
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
		Long btsId = this.getBtsIdByMoId(moId);
		message.setBtsId(btsId);
	}

	private Long getBtsIdByMoId(Long moId) {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		return mcBts.getBtsId();
	}

	/**
	 * 解析多个字节的无符号整型数
	 * 
	 * @param bts
	 *            ，消息包
	 * @param startindex
	 *            ，起始byte地址
	 * @param length
	 *            , byte的个数
	 * @return
	 */
	private int decodeUnsignedInt(byte[] bts, int startindex, int length) {
		byte btResult[] = new byte[length + 1];
		BigInteger bgInt;
		btResult[0] = 0;

		for (int i = 0; i < length; i++) {
			btResult[i + 1] = bts[startindex + i];
		}
		bgInt = new BigInteger(btResult);
		return bgInt.intValue();
	}

	private String getPanelSNWithStatus(byte[][] panelSN, int index)
			throws UnsupportedEncodingException {
		int status = decodeUnsignedInt(panelSN[index], 19, 1);
		switch (status) {
		case 0xff:
			return "request failed";
		case 0xfe:
			return NOT_SUPPORT;
		case 0xfd:
			return NOT_EXIST;
		case 0x00: {
			int strlen = 0;
			for (int i = 0; i < panelSN[index].length - 1; i++) {
				if (panelSN[index][i] != 0) {
					int bytev = (int) panelSN[index][i];
					if (!((bytev >= 48 && bytev <= 57)
							|| (bytev >= 65 && bytev <= 90) || (bytev >= 97 && bytev <= 122))) {
						return NOT_SUPPORT;
					}
					strlen++;
				} else {
					break;
				}
			}
			byte btstr[] = new byte[strlen];
			for (int i = 0; i < btstr.length; i++) {
				btstr[i] = panelSN[index][i];
			}
			String ret = ByteUtils.toString(btstr, 0, btstr.length, "US-ASCII");
			if (ret.trim().equals("")) {
				ret = NOT_SUPPORT;
			}
			return ret;
		}
		default:
			return "unknow status";
		}
	}

	private String getPanelSNWithoutStatus(byte[][] panelSN, int index)
			throws UnsupportedEncodingException {
		String tmp = new String(panelSN[index], 0, ERROR_QUERY_FAILED.length(),
				"US-ASCII");
		if (tmp.equals(ERROR_QUERY_FAILED)) {
			return tmp;
		}

		int strlen = 0;
		for (int i = 0; i < panelSN[index].length; i++) {
			if (panelSN[index][i] != 0) {
				int bytev = (int) panelSN[index][i];
				if (!((bytev >= 48 && bytev <= 57)
						|| (bytev >= 65 && bytev <= 90) || (bytev >= 97 && bytev <= 122))) {
					return NOT_SUPPORT;
				}
				strlen++;
			} else {
				break;
			}
		}
		byte btstr[] = new byte[strlen];
		for (int i = 0; i < btstr.length; i++) {
			btstr[i] = panelSN[index][i];
		}
		String ret = ByteUtils.toString(btstr, 0, btstr.length, "US-ASCII");
		if (ret.trim().equals("")) {
			ret = NOT_SUPPORT;
		}
		return ret;
	}

	public McBtsConnector getConnector() {
		return connector;
	}

	public void setConnector(McBtsConnector connector) {
		this.connector = connector;
	}
}
