package com.xinwei.minas.server.mcbts.proxy.oammanager.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.ActiveUserInfo;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.message.McBtsOnlineTerminalList;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.net.TransactionIdGenerator;
import com.xinwei.minas.server.mcbts.proxy.oammanager.McBtsOnlineTerminalListProxy;
import com.xinwei.minas.server.mcbts.proxy.oammanager.McBtsOnlineTerminalListRegistry;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * BTS在线终端列表proxy实现
 * 
 * @author fangping
 * 
 */

public class McBtsOnlineTerminalListProxyImpl implements
		McBtsOnlineTerminalListProxy {
	private  Log log = LogFactory.getLog(McBtsOnlineTerminalListProxyImpl.class);

	public final static String ERROR_QUERY_FAILED = "query failed";

	private McBtsConnector connector;

	private McBtsMessage convertModelToRequest(Long moId, String operation)
			throws Exception {

		McBtsMessage message = new McBtsMessage();
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas()
				.getProtocolMetaBy("mcBtsOnlineTerminalList", operation);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, operation);
			throw new Exception(msg);
		}
		// 获取元数据
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
				.getHeader().getItem();
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getRequest()
				.getBody().getItem();
		// 填充消息头
		this.fillHeader(message, moId, headerItems);
		// 无消息体
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

	public McBtsConnector getConnector() {
		return connector;
	}

	public void setConnector(McBtsConnector connector) {
		this.connector = connector;
	}

	@Override
	public List<ActiveUserInfo> queryOnlineTerminalList(Long moId)
			throws Exception, UnsupportedOperationException {
		McBtsMessage request = convertModelToRequest(moId,
				McBtsConstants.OPERATION_QUERY);
		// 生成并设置事务ID
		int transactionId = TransactionIdGenerator.next();
		request.setTransactionId(transactionId);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			FutureResult futureResult = new FutureResult();
			McBtsOnlineTerminalListRegistry.getInstance().register(
					transactionId, futureResult);
			// 发送消息
			connector.asyncInvoke(request);
			// 等待结果
			McBtsOnlineTerminalList list = (McBtsOnlineTerminalList) futureResult
					.timedGet(5000);
			// 将网元消息转换为模型
			List<ActiveUserInfo> NEToModle = list.getActiveUsers();
			return NEToModle;
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time")
							+ e.getLocalizedMessage());
		} catch (UnsupportedOperationException e) {
			throw new Exception(
					OmpAppContext.getMessage("unsupported_biz_operation"));
		} catch (Exception e) {
			log.error(e);
			throw new Exception(OmpAppContext.getMessage("mcbts_query_error")
					+ e.getLocalizedMessage());
		} finally {
			McBtsOnlineTerminalListRegistry.getInstance().unregister(
					transactionId);
		}
	}

	private List<ActiveUserInfo> convertResponseToModel(McBtsMessage response,
			String operation) throws Exception {
		byte[] buf = response.getContent();
		McBtsOnlineTerminalList mcBtsOnlineTerminalList = new McBtsOnlineTerminalList(
				response.getBtsId(), buf);
		return mcBtsOnlineTerminalList.getActiveUsers();
	}
}
