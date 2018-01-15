/**
 * 
 */
package com.xinwei.minas.server.mcbts.proxy.layer3.impl;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.expression.spel.ast.LongLiteral;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsACL;
import com.xinwei.minas.mcbts.core.model.layer3.WrappedACL;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.McBtsModule;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolBodyItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolHeaderItemMeta;
import com.xinwei.minas.server.mcbts.model.meta.McBtsProtocolMeta;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer3.ACLProxy;
import com.xinwei.minas.server.mcbts.service.McBtsBasicService;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.GenericBizRecord;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * @author chenshaohua
 * 
 */
public class ACLProxyImpl implements ACLProxy {

	private static Log log = LogFactory.getLog(ACLProxyImpl.class);

	private McBtsConnector connector;

	private McBtsBasicService mcBtsBasicService;

	public ACLProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	public void config(Long moId, GenericBizData bizData) throws Exception {
		// 将模型转换为网元消息
		McBtsMessage request = convertModelToRequest(moId, bizData,
				McBtsConstants.OPERATION_CONFIG);
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
	public WrappedACL query(Long moId, GenericBizData bizData) throws Exception {
		// 将模型转换为网元消息
		McBtsMessage request = convertModelToQueryRequest(moId, bizData);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			// 解析结果
			McBtsBizProxyHelper.parseResult(response);
			// 将网元消息转换为模型
			WrappedACL result = convertQueryResponseToModel(response);
			return result;
		} catch (TimeoutException e) {
			log.error("向基站查询\"" + bizData.getBizName() + "\"时发生超时错误", e);
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time")
							+ e.getLocalizedMessage());
		} catch (UnsupportedOperationException e) {
			throw e;
		} catch (Exception e) {
			log.error("查询" + bizData.getBizName() + "时发生查询出错", e);
			throw new Exception(OmpAppContext.getMessage("mcbts_query_error")
					+ e.getLocalizedMessage());
		}
	}

	private static McBtsMessage convertModelToQueryRequest(Long moId,
			GenericBizData genericBizData) throws Exception {
		McBtsMessage message = new McBtsMessage();
		String bizName = genericBizData.getBizName();
		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas()
				.getProtocolMetaBy(bizName, McBtsConstants.OPERATION_QUERY);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, bizName,
					McBtsConstants.OPERATION_QUERY);
			throw new Exception(msg);
		}
		// 获取元数据
		McBtsProtocolHeaderItemMeta[] headerItems = protocolMeta.getRequest()
				.getHeader().getItem();
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getRequest()
				.getBody().getItem();
		// 填充消息头
		fillHeader(message, moId, headerItems);
		return message;
	}

	private WrappedACL convertQueryResponseToModel(McBtsMessage response)
			throws Exception {
		GenericBizData bizData = new GenericBizData("mcbts_acl");
		byte[] buf = response.getContent();

		McBtsProtocolMeta protocolMeta = McBtsModule.getInstance()
				.getProtocolMetas()
				.getProtocolMetaBy("mcbts_acl", McBtsConstants.OPERATION_QUERY);
		if (protocolMeta == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			msg = MessageFormat.format(msg, "mcbts_acl",
					McBtsConstants.OPERATION_QUERY);
			throw new Exception(msg);
		}
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getResponse()
				.getBody().getItem();
		// 解析消息体
		return parseBody(bizData, buf, bodyItems);

	}

	private static WrappedACL parseBody(GenericBizData bizData, byte[] buf,
			McBtsProtocolBodyItemMeta[] bodyItems) {
		int offset = 0;

		WrappedACL wrappedAcl = new WrappedACL();

		wrappedAcl.setIndex(ByteUtils.toInt(buf, offset, 1));
		offset += 1;

		int total = ByteUtils.toInt(buf, offset, 1);
		offset += 1;
		wrappedAcl.setTotalNum(total);

		List<McBtsACL> aclList = new ArrayList<McBtsACL>();
		wrappedAcl.setMcBtsACLList(aclList);

		for (int i = 0; i < total; i++) {
			McBtsACL acl = new McBtsACL();

			for (McBtsProtocolBodyItemMeta item : bodyItems) {
				String name = item.getName();
				String type = item.getType();
				int length = Integer.parseInt(item.getLength());

				if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)
						|| type.equals(McBtsConstants.TYPE_SIGNED_NUMBER)) {
					long value = ByteUtils.toLong(buf, offset, length);

					Class<?> c = acl.getClass();
					Method method = null;
					try {
						method = c.getDeclaredMethod(
								"set" + WordUtils.capitalize(name), Long.class);
						method.invoke(acl, value);
					} catch (Exception e) {
						try {
							method = c.getDeclaredMethod(
									"set" + WordUtils.capitalize(name),
									Integer.class);
							method.invoke(acl, (int) value);
						} catch (Exception e1) {
							log.error("Error get Method with reflect.", e);
						}
					}

				} else if (type.equals(McBtsConstants.TYPE_STRING)) {

				}
				offset += length;
			}

			aclList.add(acl);
		}

		return wrappedAcl;
	}

	/**
	 * 将业务模型转换为网元消息
	 * 
	 * @param bizData
	 * @return
	 * @throws Exception
	 */
	private McBtsMessage convertModelToRequest(Long moId,
			GenericBizData bizData, String operation) throws Exception {
		McBtsMessage request = new McBtsMessage();
		GenericBizRecord record = bizData.getRecords().get(0);
		GenericBizProperty property = record.getPropertyValue("mcBtsACLList");
		List<McBtsACL> mcBtsACLList = null;
		if (property != null) {
			mcBtsACLList = (List<McBtsACL>) property.getValue();
		}

		String bizName = bizData.getBizName();
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
		McBtsProtocolBodyItemMeta[] bodyItems = protocolMeta.getRequest()
				.getBody().getItem();
		// 填充消息头
		fillHeader(request, moId, headerItems);
		// 填充消息体
		this.fillBody(request, mcBtsACLList, bodyItems);

		return request;
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
			// 设置基站ID
			Long btsId = getBtsIdByMoId(moId);
			message.setBtsId(btsId);
		}
	}

	// 填充消息体
	private void fillBody(McBtsMessage message, List<McBtsACL> mcBtsACLList,
			McBtsProtocolBodyItemMeta[] bodyItems) throws Exception {
		byte[] buf = new byte[4096];
		int offset = 0;

		if (mcBtsACLList == null) {
			ByteUtils.putNumber(buf, offset, "0", 2);
			offset += 2;
			message.setContent(buf, 0, offset);
			return;
		}

		// 填充消息中的索引字段
		if (mcBtsACLList.size() >= 0 && mcBtsACLList.size() <= 50) {
			ByteUtils.putNumber(buf, offset, "0", 1);
		}
		if (mcBtsACLList.size() >= 51 && mcBtsACLList.size() <= 100) {
			ByteUtils.putNumber(buf, offset, "1", 1);
		}
		if (mcBtsACLList.size() >= 101 && mcBtsACLList.size() <= 150) {
			ByteUtils.putNumber(buf, offset, "2", 1);
		}
		offset += 1;

		// 填充消息中的尝试次数字段
		ByteUtils
				.putNumber(buf, offset, String.valueOf(mcBtsACLList.size()), 1);
		offset += 1;

		for (McBtsACL entity : mcBtsACLList) {
			for (McBtsProtocolBodyItemMeta item : bodyItems) {
				String name = item.getName();
				String type = item.getType();
				int length = Integer.parseInt(item.getLength());
				Object propertyValue = entity.getPropertyValueByName(name);
				if (type.equals(McBtsConstants.TYPE_UNSIGNED_NUMBER)
						|| type.equals(McBtsConstants.TYPE_SIGNED_NUMBER)) {
					// 数值型（包括无符号和有符号数）
					ByteUtils.putNumber(buf, offset, propertyValue.toString(),
							length);
				} else if (type.equals(McBtsConstants.TYPE_STRING)) {
					// String
					String charsetName = McBtsConstants.CHARSET_US_ASCII;
					char fillChar = '\0';
					ByteUtils.putString(buf, offset, propertyValue.toString(),
							length, fillChar, charsetName);
				}
				offset += length;
			}
			message.setContent(buf, 0, offset);
		}

	}

	// 根据moId获取btsId
	private static Long getBtsIdByMoId(Long moId) {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		return mcBts.getBtsId();
	}

	public McBtsConnector getConnector() {
		return connector;
	}

	public void setConnector(McBtsConnector connector) {
		this.connector = connector;
	}

	public McBtsBasicService getMcBtsBasicService() {
		return mcBtsBasicService;
	}

	public void setMcBtsBasicService(McBtsBasicService mcBtsBasicService) {
		this.mcBtsBasicService = mcBtsBasicService;
	}

}
