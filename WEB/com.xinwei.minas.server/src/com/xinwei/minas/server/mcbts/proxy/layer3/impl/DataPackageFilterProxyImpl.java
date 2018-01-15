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
 * DataPackageFilterProxyImplʵ��
 * 
 * @author fangping
 * 
 */
public class DataPackageFilterProxyImpl implements DataPackageFilterProxy {

	private Log log = LogFactory.getLog(DataPackageFilterProxyImpl.class);
	private McBtsConnector connector;

	// ���ݰ����˲�ѯ����mocֵ
	private static int dataPackageFilter_query_moc = 0x0c14;

	public DataPackageFilterProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	@Override
	public Object[] queryFromNE(Long moId) throws RemoteException, Exception {

		// ��ģ��ת��Ϊ��ѯ��Ԫ��Ϣ
		McBtsMessage request = convertModelToQueryRequest(moId);

		try {
			// ��ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage response = connector.syncInvoke(request);
			// �������
			McBtsBizProxyHelper.parseResult(response);
			// ����Ԫ��Ϣת��Ϊģ��
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
		// ��ģ��ת��Ϊ������Ԫ��Ϣ
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
			// ��ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage response = connector.syncInvoke(request);
			// �������
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
	 * ����ѯҵ��ģ��ת��Ϊ��Ԫ��Ϣ
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
		// ��ȡԪ����
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
				.getHeader().getItem();
		// �����Ϣͷ
		fillHeader(message, moId, headerItems);
		// ����ҵ�����ҪfillBody
		if (operation.equals(McBtsConstants.OPERATION_CONFIG))
			fillBody(message, filterType, filterList);
		return message;
	}

	/**
	 * �����Ϣͷ
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
					// 16����
					message.setMoc(Integer.parseInt(itemValue.substring(2), 16));
				} else {
					message.setMoc(Integer.parseInt(itemValue));
				}
			} else if (itemName.equals(McBtsConstants.PROTOCOL_ACTION_TYPE)) {
				message.setActionType(Integer.parseInt(itemValue));
			}
		}
		// ���û�վID
		Long btsId = getBtsIdByMoId(moId);
		message.setBtsId(btsId);
	}

	/**
	 * ����MO ID��ȡ��վID
	 * 
	 * @param moId
	 * @return
	 */
	private static Long getBtsIdByMoId(Long moId) {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		return mcBts.getBtsId();
	}

	/**
	 * �����Ϣ��
	 * 
	 * @param message
	 * @param filterList
	 */
	private void fillBody(McBtsMessage message, int filterType,
			List<DataPackageFilter> filterList) {
		byte[] buf = new byte[1024];
		int offset = 0;
		// ��������
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
			// ֻ����ԴPort����
			case DataPackageFilter.TYPE_PORT_FILTER: {
				ByteUtils.putNumber(buf, offset,
						String.valueOf(filter.getPort()), 2);
				offset += 2;
				break;
			}
				// ֻ����ԴIP����
			case DataPackageFilter.TYPE_IP_FILTER: {
				ByteUtils.putIp(buf, offset, filter.getIp());
				offset += 4;
				break;
			}
				// ����ԴPort+IP����
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
	 * ������ģ��ת��Ϊ���ݰ����˹�������
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
	 * ������ݰ����˹����ѯ����ͷ��
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
		// ���û�վID
		Long btsId = getBtsIdByMoId(moId);
		message.setBtsId(btsId);
	}

	/**
	 * �������ݰ����˹����ѯ��Ӧ
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

		// ���ݹ���������Ŀ������������Ϊ5����
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
