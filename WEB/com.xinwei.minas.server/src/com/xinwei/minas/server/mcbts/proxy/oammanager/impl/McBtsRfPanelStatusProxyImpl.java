package com.xinwei.minas.server.mcbts.proxy.oammanager.impl;

import java.text.MessageFormat;
import java.util.concurrent.TimeoutException;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.McBtsRfPanelStatus;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.oammanager.McBtsRfPanelStatusProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * McBtsRfPanelStatusProxyImplproxyʵ��
 * 
 * @author fangping
 * 
 */

public class McBtsRfPanelStatusProxyImpl implements McBtsRfPanelStatusProxy {

	public final static String ERROR_QUERY_FAILED = "query failed";

	private McBtsConnector connector;

	@Override
	public McBtsRfPanelStatus queryRfPanelStatus(Long moId) throws Exception,
			UnsupportedOperationException {
		McBtsMessage request = convertModelToRequest1(moId,
				McBtsConstants.OPERATION_QUERY);
		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage response = connector.syncInvoke(request);
			// �жϽṹ�ϲ��Ϸ�
			McBtsBizProxyHelper.parseResult(response);
			// ����Ԫ��Ϣת��Ϊģ��//�˴���first Param must be the same as config file
			// TO DO
			McBtsRfPanelStatus Status = convertResponseToModel(response,
					McBtsConstants.OPERATION_QUERY);

			return Status;
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

	private McBtsRfPanelStatus convertResponseToModel(McBtsMessage response,
			String operation) throws Exception {
		byte[] buf = response.getContent();
		McBtsRfPanelStatus mcbtsStatus = new McBtsRfPanelStatus(
				response.getBtsId(), buf);
		return mcbtsStatus;

	}

	// private McBtsRfPanelStatus convertResponseToModel(String bizName,
	// McBtsMessage response, String operation) throws Exception {
	// McBtsRfPanelStatus mcbtsStatus = new McBtsRfPanelStatus();
	// byte[] buf = response.getContent();
	// McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
	// .getProtocolMetas().getProtocolMetaBy(bizName, operation);
	// if (protocolMeta == null) {
	// String msg = OmpAppContext.getMessage("unknown_biz_operation");
	// msg = MessageFormat.format(msg, bizName, operation);
	// throw new Exception(msg);
	// }
	// McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getResponse()
	// .getBody().getItem();
	// // �����ӻ�վ��ѯ���������ݶ�Ӧ����Ϣ��
	// parseBody(mcbtsStatus, buf, bodyItems);
	// return mcbtsStatus;
	//
	// }

	// // parse ��Ϣ��
	// private void parseBody(McBtsRfPanelStatus mcbtsStatus, byte[] buf,
	// McBtsProtocolBodyItemMeta[] bodyItems) {
	// int offset = 0;
	// if (bodyItems == null) {
	// return;
	// }
	// for (McBtsProtocolBodyItemMeta item : bodyItems) {
	// String name = item.getName();
	// String type = item.getType();
	// int length = Integer.parseInt(item.getLength());
	// Object value = null;
	// if (type.equalsIgnoreCase(McBtsConstants.TYPE_UNSIGNED_NUMBER)) {
	// // �޷�����
	// value = String.valueOf(ByteUtils.toUnsignedNumber(buf, offset,
	// length));
	// } else if (type.equalsIgnoreCase(McBtsConstants.TYPE_SIGNED_NUMBER)) {
	// // �з�����
	// value = String.valueOf(ByteUtils.toSignedNumber(buf, offset,
	// length));
	// }
	// offset += length;
	//
	// // Ƶ�۰�״̬4
	// // Ƶ�۰�Ӳ���汾��
	// if (name.equals("Synhardwareversion")) {
	// mcbtsStatus.setSynhardwareversion(Integer.parseInt(value
	// .toString()));
	// }
	// // Ƶ�۰�����汾��
	//
	// if (name.equals("Synsoftwareversion")) {
	// mcbtsStatus.setSynsoftwareversion(Integer.parseInt(value
	// .toString()));
	// }
	// // ����ƫ��
	// if (name.equals("TCXOoffset")) {
	// mcbtsStatus.setTCXOoffset(Integer.parseInt(value.toString()));
	// }
	// // Ƶ�۰���ճ������
	// if (name.equals("SYNReceivingErrorCounter")) {
	// mcbtsStatus.setSYNReceivingErrorCounter(Integer.parseInt(value
	// .toString()));
	// }
	//
	// // ��Ƶ��״̬8
	// // Ӳ���汾��
	//
	// if (name.equals("hardwareVersion")) {
	// mcbtsStatus.setHardwareVersion(Integer.parseInt(value
	// .toString()));
	// }
	// // ����汾��
	//
	// if (name.equals("softwareVersion")) {
	// mcbtsStatus.setSoftwareVersion(Integer.parseInt(value
	// .toString()));
	// }
	// // ���ѹֵ
	//
	// if (name.equals("BoardVoltageValue")) {
	// mcbtsStatus.setBoardVoltageValue(Integer.parseInt(value
	// .toString()));
	// }
	// // �����ֵ
	//
	// if (name.equals("BoardCurrentValue")) {
	// mcbtsStatus.setBoardCurrentValue(Integer.parseInt(value
	// .toString()));
	// }
	// // ���ŵ�ѹֵ
	//
	// if (name.equals("TTAVoltageValue")) {
	// mcbtsStatus.setTTAVoltageValue(Integer.parseInt(value
	// .toString()));
	// }
	// // ���ŵ���ֵ
	//
	// if (name.equals("TTACurrentValue")) {
	// mcbtsStatus.setTTACurrentValue(Integer.parseInt(value
	// .toString()));
	// }
	// // ���͹���
	//
	// if (name.equals("TransmitPowerValue")) {
	// mcbtsStatus.setTransmitPowerValue(Integer.parseInt(value
	// .toString()));
	// }
	// // ���߽��ճ������
	// if (name.equals("ReceivingErrorCounter")) {
	// mcbtsStatus.setReceivingErrorCounter(Integer.parseInt(value
	// .toString()));
	// }
	// }
	// }

	private McBtsMessage convertModelToRequest1(Long moId, String operation)
			throws Exception {
		McBtsMessage message = new McBtsMessage();
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas()
				.getProtocolMetaBy("mcbts_RfPanelStatus", operation);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, operation);
			throw new Exception(msg);
		}
		// ��ȡԪ����
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
				.getHeader().getItem();
		// �����Ϣͷ
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

}
