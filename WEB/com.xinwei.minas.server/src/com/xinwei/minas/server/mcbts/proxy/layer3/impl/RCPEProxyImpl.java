package com.xinwei.minas.server.mcbts.proxy.layer3.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPE;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItem;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItemPK;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer3.RCPEProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * rcpe业务协议适配器实现
 * 
 * @author yinbinqiang
 */
public class RCPEProxyImpl implements RCPEProxy {

	// rcpe配置Moc值
	private final int rcpe_config_moc = 0x0760;
	// rcpe查询Moc值
	private final int rcpe_query_moc = 0x0762;
	// rcpe+配置Moc值
	private final int rcpe_ex_config_moc = 0x07C7;
	// rcpe+查询Moc值
	private final int rcpe_ex_query_moc = 0x07C9;

	private McBtsConnector connector;

	public RCPEProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	@Override
	public TConfRCPE query(Long moId) throws Exception {
		// rcpe配置请求消息
		McBtsMessage rcpeRequest = convertModelToRequest(moId, null,
				rcpe_query_moc, McBtsConstants.OPERATION_QUERY);
		// rcpe+配置请求消息
		McBtsMessage rcpeEXRequest = convertModelToRequest(moId, null,
				rcpe_ex_query_moc, McBtsConstants.OPERATION_QUERY);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage rcpeResponse = connector.syncInvoke(rcpeRequest);
			// 解析结果
			McBtsBizProxyHelper.parseResult(rcpeResponse);
			// 将返回消息解析为故障弱化实体类
			McBtsMessage rcpeEXResponse = connector.syncInvoke(rcpeEXRequest);
			McBtsBizProxyHelper.parseResult(rcpeEXResponse);

			return convertResponseToModel(moId, rcpeResponse, rcpeEXResponse);
		} catch (Exception e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
	}

	@Override
	public void config(Long moId, TConfRCPE rcpe) throws Exception {
		// rcpe配置请求消息
		McBtsMessage rcpeRequest = convertModelToRequest(moId, rcpe,
				rcpe_config_moc, McBtsConstants.OPERATION_CONFIG);
		// rcpe+配置请求消息
		McBtsMessage rcpeEXRequest = convertModelToRequest(moId, rcpe,
				rcpe_ex_config_moc, McBtsConstants.OPERATION_CONFIG);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage rcpeResponse = connector.syncInvoke(rcpeRequest);
			// 解析结果
			McBtsBizProxyHelper.parseResult(rcpeResponse);

			try {
				// 调低底层通信层发送消息, 同步等待应答
				McBtsMessage rcpeEXResponse = connector
						.syncInvoke(rcpeEXRequest);
				// 解析结果
				McBtsBizProxyHelper.parseResult(rcpeEXResponse);
			} catch (UnsupportedOperationException e) {
				// rcpe+的配置可能在某些软件版本不支持,当不支持的时候,不做处理
			}
		} catch (Exception e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
	}

	/**
	 * 将模型转化为网元消息
	 * 
	 * @param moId
	 * @param weakVoiceFault
	 * @param operation
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequest(Long moId, TConfRCPE rcpe,
			int moc, String operation) throws Exception {
		McBtsMessage message = new McBtsMessage();
		if (rcpe == null && operation.equals(McBtsConstants.OPERATION_CONFIG)) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			throw new Exception(msg);
		}
		// 获得网元配置请求消息
		if (operation.equals(McBtsConstants.OPERATION_CONFIG)) {
			// 填充消息头
			this.fillHeader(message, moId, moc);
			// 填充消息体
			this.fillBody(message, rcpe, moc);
		}
		// 获得网元查询请求消息
		if (operation.equals(McBtsConstants.OPERATION_QUERY)) {
			message.setMsgArea(1);
			message.setMa(McBtsMessage.MA_CONF);
			message.setMoc(moc);
			message.setActionType(McBtsMessage.ACTION_TYPE_QUERY);
			// 设置基站ID
			Long btsId = this.getBtsIdByMoId(moId);
			message.setBtsId(btsId);
		}
		return message;
	}


	/**
	 * 将查询返回消息解析为语音故障弱化实体类
	 * 
	 * @param response
	 * @return
	 */
	private TConfRCPE convertResponseToModel(Long moId, McBtsMessage response,
			McBtsMessage rcpeEXResponse) {
		byte[] rcpeBuf = response.getContent();
		byte[] rcpePlusBuf = rcpeEXResponse.getContent();
		// 解析消息体

		TConfRCPE rcpe = new TConfRCPE();
		rcpe.setMoId(moId);

		parseBody(rcpe, rcpeBuf, rcpePlusBuf);

		return rcpe;

	}

	/**
	 * 解析查询消息体
	 */
	private void parseBody(TConfRCPE rcpe, byte[] rcpeBuf, byte[] rcpePlusBuf) {
		int offset = 0;
		int ex_offset = 0;

		List<TConfRCPEItem> itemList = new ArrayList<TConfRCPEItem>();
		rcpe.setItems(itemList);

		// RCPE
		rcpe.setWorkMode(Integer.parseInt(String.valueOf(ByteUtils
				.toUnsignedNumber(rcpeBuf, offset, 2))));
		offset += 2;

		int num = Integer.parseInt(String.valueOf(ByteUtils.toUnsignedNumber(
				rcpeBuf, offset, 1)));
		offset += 1;

		for (int i = 0; i < num; i++) {
			TConfRCPEItem item = new TConfRCPEItem();
			item.setId(new TConfRCPEItemPK(rcpe.getMoId(), i));
			item.setUIDType(TConfRCPE.CFG_FLAG_RCPE);

			item.setrCPEUid(Long.parseLong(String.valueOf(ByteUtils
					.toUnsignedNumber(rcpeBuf, offset, 4))));
			offset += 4;

			itemList.add(item);
		}

		// RCPE+
		int rcpeEXNum = Integer.parseInt(String.valueOf(ByteUtils
				.toUnsignedNumber(rcpePlusBuf, offset, 2)));
		ex_offset += 2;

		for (int i = 0; i < rcpeEXNum; i++) {
			TConfRCPEItem item = new TConfRCPEItem();
			item.setId(new TConfRCPEItemPK(rcpe.getMoId(), num + i));
			item.setUIDType(TConfRCPE.CFG_FLAG_RCPEEX);

			item.setrCPEUid(Long.parseLong(String.valueOf(ByteUtils
					.toUnsignedNumber(rcpePlusBuf, offset, 4))));
			offset += 4;

			itemList.add(item);
		}
	}

	/**
	 * 填充消息头
	 * 
	 * @param message
	 * @param moId
	 */
	private void fillHeader(McBtsMessage message, Long moId, int moc) {
		message.setMsgArea(1);
		message.setMa(McBtsMessage.MA_CONF);
		message.setMoc(moc);
		message.setActionType(McBtsMessage.ACTION_TYPE_CONFIG);
		// 设置基站ID
		Long btsId = this.getBtsIdByMoId(moId);
		message.setBtsId(btsId);
	}

	/**
	 * 填充消息体
	 * 
	 * @param message
	 * @param weakVoiceFault
	 */
	private void fillBody(McBtsMessage message, TConfRCPE rcpe, int moc) {
		byte[] buf = new byte[4096];
		int offset = 0;

		// 将实体类信息解析为字节流
		if (moc == rcpe_config_moc) {
			ByteUtils.putNumber(buf, offset, rcpe.getWorkMode().toString(), 2);
			offset += 2;

			List<TConfRCPEItem> rcpeList = new ArrayList<TConfRCPEItem>();

			List<TConfRCPEItem> list = rcpe.getItems();
			for (TConfRCPEItem item : list) {
				if (item.getUIDType().intValue() == TConfRCPE.CFG_FLAG_RCPE) {
					rcpeList.add(item);
				}
			}

			ByteUtils
					.putNumber(buf, offset, String.valueOf(rcpeList.size()), 1);
			offset += 1;

			for (TConfRCPEItem item : rcpeList) {
				ByteUtils.putNumber(buf, offset, item.getrCPEUid().toString(),
						4);
				offset += 4;
			}

		} else if (moc == rcpe_ex_config_moc) {
			List<TConfRCPEItem> rcpePlusList = new ArrayList<TConfRCPEItem>();

			List<TConfRCPEItem> list = rcpe.getItems();
			for (TConfRCPEItem item : list) {
				if (item.getUIDType().intValue() == TConfRCPE.CFG_FLAG_RCPEEX) {
					rcpePlusList.add(item);
				}
			}

			ByteUtils.putNumber(buf, offset,
					String.valueOf(rcpePlusList.size()), 2);
			offset += 2;

			for (TConfRCPEItem item : rcpePlusList) {
				ByteUtils.putNumber(buf, offset, item.getrCPEUid().toString(),
						4);
				offset += 4;
			}
		}
		message.setContent(buf, 0, offset);
	}

	/**
	 * 根据moId获取基站信息
	 * 
	 * @param moId
	 * @return
	 */
	private Long getBtsIdByMoId(Long moId) {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		return bts.getBtsId();
	}
}
