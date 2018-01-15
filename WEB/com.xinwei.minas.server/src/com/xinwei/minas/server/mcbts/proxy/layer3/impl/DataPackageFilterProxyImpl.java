package com.xinwei.minas.server.mcbts.proxy.layer3.impl;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.DataPackageFilter;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer3.DataPackageFilterProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * DataPackageFilterProxyImpl实现
 * 
 * @author fangping
 * 
 */
public class DataPackageFilterProxyImpl implements DataPackageFilterProxy {

	private Log log = LogFactory.getLog(DataPackageFilterProxyImpl.class);
	private McBtsConnector connector;

	// 数据包过滤查询请求moc值
	private static int dataPackageFilter_query_moc = 0x0c14;

	public DataPackageFilterProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	@Override
	public Object[] queryFromNE(Long moId) throws RemoteException, Exception {

		// 将模型转换为查询网元消息
		McBtsMessage request = convertModelToQueryRequest(moId);

		try {
			// 向底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			// 解析结果
			McBtsBizProxyHelper.parseResult(response);
			// 将网元消息转换为模型
			Object[] objs = convertResponseToModel(response);
			return objs;
		} catch (TimeoutException e) {
			log.error(e);
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		} catch (UnsupportedOperationException e) {
			log.error(e);
			throw new Exception(
					OmpAppContext.getMessage("unsupported_biz_operation"));
		} catch (Exception e) {
			log.error(e);
			throw new Exception(OmpAppContext.getMessage("mcbts_query_error")
					+ e.getLocalizedMessage());
		}
	}

	@Override
	public void config(Long moId, int filterType,
			List<DataPackageFilter> filterList) throws RemoteException,
			Exception {
		// 将模型转换为配置网元消息
		McBtsMessage request = null;
		try {
			request = convertModelToRequest(moId, filterType, filterList,
					"dataPackageFilter", McBtsConstants.OPERATION_CONFIG);
		} catch (Exception e) {
			log.error(
					"Error parsing model to request for data package filter.",
					e);
			throw new Exception(e);
		}
		try {
			// 向底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			// 解析结果
			McBtsBizProxyHelper.parseResult(response);
		} catch (TimeoutException e) {
			log.error("Timeout while configuring data package filter to bts.",
					e);
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		} catch (UnsupportedOperationException e) {
			throw new UnsupportedOperationException(
					OmpAppContext.getMessage("unsupported_biz_operation"));
		} catch (Exception e) {
			log.error("Error while configuring data package filter to bts.", e);
			throw new Exception(OmpAppContext.getMessage("mcbts_config_error")
					+ e.getLocalizedMessage());
		}
	}

	/**
	 * 将查询业务模型转换为网元消息
	 * 
	 * @param bizData
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequest(Long moId, int filterType,
			List<DataPackageFilter> filterList, String bizName, String operation)
			throws Exception {
		McBtsMessage message = new McBtsMessage();

		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas().getProtocolMetaBy(bizName, operation);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, bizName, operation);
			throw new Exception(msg);
		}
		// 获取元数据
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
				.getHeader().getItem();
		// 填充消息头
		fillHeader(message, moId, headerItems);
		// 配置业务才需要fillBody
		if (operation.equals(McBtsConstants.OPERATION_CONFIG))
			fillBody(message, filterType, filterList);
		return message;
	}

	/**
	 * 填充消息头
	 * 
	 * @param message
	 * @param record
	 * @param headerItems
	 */
	private static void fillHeader(McBtsMessage message, Long moId,
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
		Long btsId = getBtsIdByMoId(moId);
		message.setBtsId(btsId);
	}

	/**
	 * 根据MO ID获取基站ID
	 * 
	 * @param moId
	 * @return
	 */
	private static Long getBtsIdByMoId(Long moId) {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		return mcBts.getBtsId();
	}

	/**
	 * 填充消息体
	 * 
	 * @param message
	 * @param filterList
	 */
	private void fillBody(McBtsMessage message, int filterType,
			List<DataPackageFilter> filterList) {
		byte[] buf = new byte[1024];
		int offset = 0;
		// 过滤类型
		ByteUtils.putNumber(buf, offset, String.valueOf(filterType), 1);
		offset += 1;

		if (filterList == null || filterList.isEmpty()) {
			// num
			ByteUtils.putNumber(buf, offset, "0", 1);
			message.setContent(buf, 0, offset);
			return;
		} else {
			ByteUtils.putNumber(buf, offset, String.valueOf(filterList.size()),
					1);
			message.setContent(buf, 0, offset);
			offset+=1;
		}

		for (DataPackageFilter filter : filterList) {
			switch (filterType) {
			// 只根据源Port过滤
			case DataPackageFilter.TYPE_PORT_FILTER: {
				ByteUtils.putNumber(buf, offset,
						String.valueOf(filter.getPort()), 2);
				offset += 2;
				break;
			}
				// 只根据源IP过滤
			case DataPackageFilter.TYPE_IP_FILTER: {
				ByteUtils.putIp(buf, offset, filter.getIp());
				offset += 4;
				break;
			}
				// 根据源Port+IP过滤
			case DataPackageFilter.TYPE_NO_FILTER:
			case DataPackageFilter.TYPE_BOTH_FILTER: {
				ByteUtils.putNumber(buf, offset,
						String.valueOf(filter.getPort()), 2);
				offset += 2;
				ByteUtils.putIp(buf, offset, filter.getIp());
				offset += 4;
				break;
			}
			}
		}

		message.setContent(buf, 0, offset);
	}

	/**
	 * 将数据模型转换为数据包过滤规则请求
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToQueryRequest(Long moId) throws Exception {
		McBtsMessage message = new McBtsMessage();
		fillQueryHeader(message, dataPackageFilter_query_moc, moId);
		return message;
	}

	/**
	 * 填充数据包过滤规则查询请求头部
	 * 
	 * @param message
	 * @param moc
	 * @param moId
	 */
	private void fillQueryHeader(McBtsMessage message, int moc, Long moId) {
		message.setMsgArea(1);
		message.setMa(McBtsMessage.MA_CONF);
		message.setMoc(moc);
		message.setActionType(McBtsMessage.ACTION_TYPE_QUERY);
		// 设置基站ID
		Long btsId = getBtsIdByMoId(moId);
		message.setBtsId(btsId);
	}

	/**
	 * 解析数据包过滤规则查询响应
	 * 
	 * @param response
	 * @return
	 */
	private Object[] convertResponseToModel(McBtsMessage response) {
		Object[] objs = new Object[2];
		List<DataPackageFilter> filterRules = new ArrayList<DataPackageFilter>();

		byte[] buf = response.getContent();
		int offset = 0;

		int filterType = ByteUtils.toInt(buf, offset, 1);
		offset += 1;

		// 数据过滤配置项目个数（最大个数为5个）
		int filterNum = ByteUtils.toInt(buf, offset, 1);
		offset += 1;

		for (int i = 0; i < filterNum; i++) {
			DataPackageFilter filter = new DataPackageFilter();
			filter.setType(filterType);
			switch (filterType) {
			case DataPackageFilter.TYPE_PORT_FILTER:
				filter.setPort(ByteUtils.toInt(buf, offset, 2));
				offset += 2;
				break;
			case DataPackageFilter.TYPE_IP_FILTER:
				filter.setIp(ByteUtils.toIp(buf, offset, 4));
				offset += 4;
				break;
			case DataPackageFilter.TYPE_NO_FILTER:
			case DataPackageFilter.TYPE_BOTH_FILTER:
				filter.setPort(ByteUtils.toInt(buf, offset, 2));
				offset += 2;
				filter.setIp(ByteUtils.toIp(buf, offset, 4));
				offset += 4;
				break;
			}
			filterRules.add(filter);
		}

		objs[0] = filterType;
		objs[1] = filterRules;

		return objs;
	}
}
