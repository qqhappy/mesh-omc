/**
 * 
 */
package com.xinwei.minas.server.mcbts.proxy.layer3.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsRepeater;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlan;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer3.VlanProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.GenericBizRecord;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * @author chenshaohua
 * 
 */
public class VlanProxyImpl implements VlanProxy {

	private McBtsConnector connector;

	public VlanProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	public void config(Long moId, GenericBizData data) throws Exception {
		// ��ģ��ת��Ϊ��Ԫ��Ϣ
		McBtsMessage request = convertModelToRequest(moId, data,
				McBtsConstants.OPERATION_CONFIG);
		try {
			McBtsMessage response = connector.syncInvoke(request);
			McBtsBizProxyHelper.parseResult(response); // �������
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		} catch (Exception e) {
			throw new Exception(OmpAppContext.getMessage("mcbts_config_error")
					+ e.getLocalizedMessage());
		}
	}

	@Override
	public List<McBtsVlan> query(Long moId, GenericBizData data)
			throws Exception {
		// ��ģ��ת��Ϊ��Ԫ��Ϣ
		McBtsMessage request = convertModelToRequest(moId, data,
				McBtsConstants.OPERATION_QUERY);

		try {
			McBtsMessage response = connector.syncInvoke(request);
			McBtsBizProxyHelper.parseResult(response); // �������

			List<McBtsVlan> result = convertResponseToModel(data, response,
					McBtsConstants.OPERATION_QUERY);

			return result;
		} catch (TimeoutException e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		} catch (Exception e) {
			throw new Exception(OmpAppContext.getMessage("mcbts_config_error")
					+ e.getLocalizedMessage());
		}
	}

	private McBtsMessage convertModelToRequest(Long moId,
			GenericBizData bizData, String operation) throws Exception {
		McBtsMessage message = new McBtsMessage(); // ������Ϣ
		String bizName = bizData.getBizName();
		// ��ȡЭ������
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
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getRequest()
				.getBody().getItem();
		// �����Ϣͷ
		this.fillHeader(message, moId, headerItems);
		// �����Ϣ��
		List<GenericBizRecord> records = bizData.getRecords();
		if (!records.isEmpty()) {
			GenericBizRecord record = records.get(0);
			this.fillBody(message, record, bodyItems);
		}

		return message;
	}

	private List<McBtsVlan> convertResponseToModel(GenericBizData data,
			McBtsMessage response, String operation) throws Exception {
		byte[] buf = response.getContent();
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas()
				.getProtocolMetaBy(data.getBizName(), operation);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, data.getBizName(), operation);
			throw new Exception(msg);
		}
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getResponse()
				.getBody().getItem();

		List<McBtsVlan> vlanList = new ArrayList<McBtsVlan>();
		// ������Ϣ��
		parseBody(vlanList, data, buf, bodyItems);
		return vlanList;
	}

	private static void parseBody(List<McBtsVlan> vlanList,
			GenericBizData data, byte[] buf,
			McBtsProtocolBodyItemMeta[] bodyItems) throws Exception {
		int offset = 0;
		if (bodyItems == null) {
			return;
		}

		int totalNum = ByteUtils.toInt(buf, offset, 2);
		offset += 2;

		for (int i = 0; i < totalNum; i++) {
			McBtsVlan vlan = new McBtsVlan();

			for (McBtsProtocolBodyItemMeta item : bodyItems) {
				String name = item.getName();
				int length = Integer.parseInt(item.getLength());

				Object value = ByteUtils.toUnsignedNumber(buf, offset, length);
				offset += length;

				if (name.equalsIgnoreCase("vlanGroup")) {
					vlan.setVlanGroup(Integer.valueOf(String.valueOf(value)));
				}
				if (name.equalsIgnoreCase("vlanID")) {
					vlan.setVlanID(Integer.valueOf(String.valueOf(value)));
				}
			}

			vlanList.add(vlan);
		}

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
			// ���û�վID
			Long btsId = this.getBtsIdByMoId(moId);
			message.setBtsId(btsId);
		}
	}

	private void fillBody(McBtsMessage message, GenericBizRecord record,
			McBtsProtocolBodyItemMeta[] bodyItems) {
		byte[] buf = new byte[4096];
		int offset = 0;
		GenericBizProperty property = record.getPropertyValue("mcBtsVlanList");
		List<McBtsVlan> mcBtsVlanList = (List<McBtsVlan>) property.getValue();
		// �����Ϣ�е���Ŀ�ֶ�
		ByteUtils.putNumber(buf, offset, mcBtsVlanList.size() + "", 2);
		// ƫ�Ƽ�2
		offset += 2;
		// for (McBtsProtocolBodyItemMeta item : bodyItems) {
		// String name = item.getName();
		// String type = item.getType();
		// int length = Integer.parseInt(item.getLength());
		// // ��� vlan group ��vlan id
		// for (McBtsVlan o : mcBtsVlanList) {
		// Object propertyValue = o.getPropertyValueByName(name);
		// if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)
		// || type.equals(McBtsConstants.TYPE_SIGNED_NUMBER)) {
		// // ��ֵ�ͣ������޷��ź��з�������
		// ByteUtils.putNumber(buf, offset, propertyValue.toString(),
		// length);
		// offset += length;
		// }
		// }
		// }
		// TODO: replace line 121-136 by line 138-153
		for (McBtsVlan o : mcBtsVlanList) {
			for (McBtsProtocolBodyItemMeta item : bodyItems) {
				String name = item.getName();
				String type = item.getType();
				int length = Integer.parseInt(item.getLength());
				// ��� vlan group ��vlan id
				Object propertyValue = o.getPropertyValueByName(name);
				if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)
						|| type.equals(McBtsConstants.TYPE_SIGNED_NUMBER)) {
					// ��ֵ�ͣ������޷��ź��з�������
					ByteUtils.putNumber(buf, offset, propertyValue.toString(),
							length);
					offset += length;
				}
			}
		}
		message.setContent(buf, 0, offset);
	}

	// ����moId��ȡbtsId
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
