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
 * ��������ҵ��Э��������ʵ��
 * 
 * @author yinbinqiang
 * 
 */
public class WeakFaultProxyImpl implements WeakFaultProxy {

	// ����Mocֵ
	private final int config_moc = 0x0772;
	// ��ѯMocֵ
	private final int query_moc = 0x0774;

	private McBtsConnector connector;

	public WeakFaultProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	@Override
	public TConfWeakVoiceFault query(Long moId,
			TConfWeakVoiceFault weakVoiceFault) throws Exception {
		// ��ģ��ת��Ϊ��ѯ��Ԫ��Ϣ
		McBtsMessage request = convertModelToRequest(moId, weakVoiceFault,
				McBtsConstants.OPERATION_QUERY);
		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage response = connector.syncInvoke(request);
			// �������
			McBtsBizProxyHelper.parseResult(response);
			// ��������Ϣ����Ϊ��������ʵ����
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
		// ��ģ��ת��Ϊ������Ԫ��Ϣ
		McBtsMessage request = convertModelToRequest(moId, weakVoiceFault,
				McBtsConstants.OPERATION_CONFIG);
		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage response = connector.syncInvoke(request);
			// �������
			McBtsBizProxyHelper.parseResult(response);
		} catch (Exception e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_reply_over_time"));
		}
	}

	/**
	 * ��ģ��ת��Ϊ��Ԫ��Ϣ
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
		// �����Ԫ����������Ϣ
		if (operation.equals(McBtsConstants.OPERATION_CONFIG)) {
			// �����Ϣͷ
			this.fillHeader(message, moId);
			// �����Ϣ��
			this.fillBody(message, weakVoiceFault);
		}
		// �����Ԫ��ѯ������Ϣ
		if (operation.equals(McBtsConstants.OPERATION_QUERY)) {
			message.setMsgArea(1);
			message.setMa(McBtsMessage.MA_CONF);
			message.setMoc(query_moc);
			message.setActionType(McBtsMessage.ACTION_TYPE_QUERY);
			// ���û�վID
			Long btsId = this.getBtsIdByMoId(moId);
			message.setBtsId(btsId);
		}
		return message;
	}

	/**
	 * ����ѯ������Ϣ����Ϊ������������ʵ����
	 * 
	 * @param response
	 * @return
	 */
	private TConfWeakVoiceFault convertResponseToModel(McBtsMessage response) {
		TConfWeakVoiceFault weakVoiceFault = new TConfWeakVoiceFault();
		byte[] buf = response.getContent();
		// ������Ϣ��
		this.parseBody(weakVoiceFault, buf);
		return weakVoiceFault;
	}

	/**
	 * ������ѯ��Ϣ��
	 */
	private void parseBody(TConfWeakVoiceFault weakVoiceFault, byte[] buf) {
		int offset = 0;
		String charsetName = McBtsConstants.CHARSET_US_ASCII;

		// ���ֽ�������Ϊ������������ʵ����
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
	 * �����Ϣͷ
	 * 
	 * @param message
	 * @param moId
	 */
	private void fillHeader(McBtsMessage message, Long moId) {
		message.setMsgArea(1);
		message.setMa(McBtsMessage.MA_CONF);
		message.setMoc(config_moc);
		message.setActionType(McBtsMessage.ACTION_TYPE_CONFIG);
		// ���û�վID
		Long btsId = this.getBtsIdByMoId(moId);
		message.setBtsId(btsId);
	}

	/**
	 * �����Ϣ��
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
		// ��ʵ������Ϣ����Ϊ�ֽ���
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
	 * ����moId��ȡ��վ��Ϣ
	 * 
	 * @param moId
	 * @return
	 */
	private Long getBtsIdByMoId(Long moId) {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		return bts.getBtsId();
	}
}
