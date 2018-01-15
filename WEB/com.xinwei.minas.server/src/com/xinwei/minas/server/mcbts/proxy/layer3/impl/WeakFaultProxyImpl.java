package com.xinwei.minas.server.mcbts.proxy.layer3.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfDnInfo;
import com.xinwei.minas.mcbts.core.model.layer3.TConfWeakVoiceFault;
import com.xinwei.minas.server.mcbts.McBtsConstants;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxyHelper;
import com.xinwei.minas.server.mcbts.proxy.layer3.WeakFaultProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.omp.core.utils.ByteUtils;
import com.xinwei.omp.core.utils.ReversalCodeUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 故障弱化业务协议适配器实现
 * 
 * @author yinbinqiang
 * 
 */
public class WeakFaultProxyImpl implements WeakFaultProxy {

	// 配置Moc值
	private final int config_moc = 0x0772;
	// 查询Moc值
	private final int query_moc = 0x0774;

	private McBtsConnector connector;

	public WeakFaultProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	@Override
	public TConfWeakVoiceFault query(Long moId,
			TConfWeakVoiceFault weakVoiceFault) throws Exception {
		// 将模型转换为查询网元消息
		McBtsMessage request = convertModelToRequest(moId, weakVoiceFault,
				McBtsConstants.OPERATION_QUERY);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			// 解析结果
			McBtsBizProxyHelper.parseResult(response);
			// 将返回消息解析为故障弱化实体类
			weakVoiceFault = convertResponseToModel(response);
		} catch (Exception e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
		return weakVoiceFault;
	}

	@Override
	public void config(Long moId, TConfWeakVoiceFault weakVoiceFault)
			throws Exception {
		// 将模型转换为配置网元消息
		McBtsMessage request = convertModelToRequest(moId, weakVoiceFault,
				McBtsConstants.OPERATION_CONFIG);
		try {
			// 调低底层通信层发送消息, 同步等待应答
			McBtsMessage response = connector.syncInvoke(request);
			// 解析结果
			McBtsBizProxyHelper.parseResult(response);
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
	private McBtsMessage convertModelToRequest(Long moId,
			TConfWeakVoiceFault weakVoiceFault, String operation)
			throws Exception {
		McBtsMessage message = new McBtsMessage();
		if (weakVoiceFault == null) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			throw new Exception(msg);
		}
		// 获得网元配置请求消息
		if (operation.equals(McBtsConstants.OPERATION_CONFIG)) {
			// 填充消息头
			this.fillHeader(message, moId);
			// 填充消息体
			this.fillBody(message, weakVoiceFault);
		}
		// 获得网元查询请求消息
		if (operation.equals(McBtsConstants.OPERATION_QUERY)) {
			message.setMsgArea(1);
			message.setMa(McBtsMessage.MA_CONF);
			message.setMoc(query_moc);
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
	private TConfWeakVoiceFault convertResponseToModel(McBtsMessage response) {
		TConfWeakVoiceFault weakVoiceFault = new TConfWeakVoiceFault();
		byte[] buf = response.getContent();
		// 解析消息体
		this.parseBody(weakVoiceFault, buf);
		return weakVoiceFault;
	}

	/**
	 * 解析查询消息体
	 */
	private void parseBody(TConfWeakVoiceFault weakVoiceFault, byte[] buf) {
		int offset = 0;
		String charsetName = McBtsConstants.CHARSET_US_ASCII;

		// 将字节流解析为语音故障弱化实体类
		weakVoiceFault
				.setFlag((int) ByteUtils.toUnsignedNumber(buf, offset, 1));
		offset += 1;
		weakVoiceFault.setVoice_user_list_file((int) ByteUtils
				.toUnsignedNumber(buf, offset, 1));
		offset += 1;
		weakVoiceFault.setDivision_code(ByteUtils.toString(buf, offset, 10,
				charsetName).trim());
		offset += 10;
		List<TConfDnInfo> dnInfos = new ArrayList<TConfDnInfo>();
		for (int i = 0; i < (buf.length - 22) / 6; i++) {
			byte[] bt = new byte[5];
			System.arraycopy(buf, offset, bt, 0, 5);
			TConfDnInfo dnInfo = new TConfDnInfo();
			dnInfo.setDn_prefix(ReversalCodeUtils.decode(bt, 5));

			offset += 5;
			dnInfo.setDn_len((int) ByteUtils.toUnsignedNumber(buf, offset, 1));
			offset += 1;
			dnInfos.add(dnInfo);
		}
		weakVoiceFault.TConfDnInfos = dnInfos;

		weakVoiceFault.setMulti_call_idle_time((int) ByteUtils
				.toUnsignedNumber(buf, offset, 2));
		offset += 2;
		weakVoiceFault.setVoice_max_time((int) ByteUtils.toUnsignedNumber(buf,
				offset, 2));
		offset += 2;
		weakVoiceFault.setMulti_call_max_time((int) ByteUtils.toUnsignedNumber(
				buf, offset, 2));
		offset += 2;
		weakVoiceFault.setDelay_interval((int) ByteUtils.toUnsignedNumber(buf,
				offset, 2));
		offset += 2;
		weakVoiceFault.setVoice_user_list_file2((int) ByteUtils
				.toUnsignedNumber(buf, offset, 1));
		offset += 1;
		weakVoiceFault.setTrunk_list_file((int) ByteUtils.toUnsignedNumber(buf,
				offset, 1));
		offset += 1;
	}

	/**
	 * 填充消息头
	 * 
	 * @param message
	 * @param moId
	 */
	private void fillHeader(McBtsMessage message, Long moId) {
		message.setMsgArea(1);
		message.setMa(McBtsMessage.MA_CONF);
		message.setMoc(config_moc);
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
	private void fillBody(McBtsMessage message,
			TConfWeakVoiceFault weakVoiceFault) {
		byte[] buf = new byte[4096];
		int offset = 0;
		String charsetName = McBtsConstants.CHARSET_US_ASCII;
		char fillChar = '\0';
		// 将实体类信息解析为字节流
		ByteUtils
				.putNumber(buf, offset, weakVoiceFault.getFlag().toString(), 1);
		offset += 1;
		ByteUtils.putNumber(buf, offset, weakVoiceFault
				.getVoice_user_list_file().toString(), 1);
		offset += 1;
		ByteUtils.putString(buf, offset, weakVoiceFault.getDivision_code(), 10,
				fillChar, charsetName);
		offset += 10;
		for (TConfDnInfo dnInfo : weakVoiceFault.TConfDnInfos) {
			byte[] bt = ReversalCodeUtils.encode(dnInfo.getDn_prefix(), 5);
			System.arraycopy(bt, 0, buf, offset, 5);
			offset += 5;
			ByteUtils.putNumber(buf, offset, dnInfo.getDn_len().toString(), 1);
			offset += 1;
		}
		ByteUtils.putNumber(buf, offset, weakVoiceFault
				.getMulti_call_idle_time().toString(), 2);
		offset += 2;
		ByteUtils.putNumber(buf, offset, weakVoiceFault.getVoice_max_time()
				.toString(), 2);
		offset += 2;
		ByteUtils.putNumber(buf, offset, weakVoiceFault
				.getMulti_call_max_time().toString(), 2);
		offset += 2;
		ByteUtils.putNumber(buf, offset, weakVoiceFault.getDelay_interval()
				.toString(), 2);
		offset += 2;
		ByteUtils.putNumber(buf, offset, weakVoiceFault
				.getVoice_user_list_file2().toString(), 1);
		offset += 1;
		ByteUtils.putNumber(buf, offset, weakVoiceFault.getTrunk_list_file()
				.toString(), 1);
		offset += 1;

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
