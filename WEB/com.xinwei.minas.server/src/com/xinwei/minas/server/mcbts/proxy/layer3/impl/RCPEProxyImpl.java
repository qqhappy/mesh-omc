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
 * rcpeҵ��Э��������ʵ��
 * 
 * @author yinbinqiang
 */
public class RCPEProxyImpl implements RCPEProxy {

	// rcpe����Mocֵ
	private final int rcpe_config_moc = 0x0760;
	// rcpe��ѯMocֵ
	private final int rcpe_query_moc = 0x0762;
	// rcpe+����Mocֵ
	private final int rcpe_ex_config_moc = 0x07C7;
	// rcpe+��ѯMocֵ
	private final int rcpe_ex_query_moc = 0x07C9;

	private McBtsConnector connector;

	public RCPEProxyImpl(McBtsConnector connector) {
		this.connector = connector;
	}

	@Override
	public TConfRCPE query(Long moId) throws Exception {
		// rcpe����������Ϣ
		McBtsMessage rcpeRequest = convertModelToRequest(moId, null,
				rcpe_query_moc, McBtsConstants.OPERATION_QUERY);
		// rcpe+����������Ϣ
		McBtsMessage rcpeEXRequest = convertModelToRequest(moId, null,
				rcpe_ex_query_moc, McBtsConstants.OPERATION_QUERY);
		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage rcpeResponse = connector.syncInvoke(rcpeRequest);
			// �������
			McBtsBizProxyHelper.parseResult(rcpeResponse);
			// ��������Ϣ����Ϊ��������ʵ����
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
		// rcpe����������Ϣ
		McBtsMessage rcpeRequest = convertModelToRequest(moId, rcpe,
				rcpe_config_moc, McBtsConstants.OPERATION_CONFIG);
		// rcpe+����������Ϣ
		McBtsMessage rcpeEXRequest = convertModelToRequest(moId, rcpe,
				rcpe_ex_config_moc, McBtsConstants.OPERATION_CONFIG);
		try {
			// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
			McBtsMessage rcpeResponse = connector.syncInvoke(rcpeRequest);
			// �������
			McBtsBizProxyHelper.parseResult(rcpeResponse);

			try {
				// ���͵ײ�ͨ�Ų㷢����Ϣ, ͬ���ȴ�Ӧ��
				McBtsMessage rcpeEXResponse = connector
						.syncInvoke(rcpeEXRequest);
				// �������
				McBtsBizProxyHelper.parseResult(rcpeEXResponse);
			} catch (UnsupportedOperationException e) {
				// rcpe+�����ÿ�����ĳЩ����汾��֧��,����֧�ֵ�ʱ��,��������
			}
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
	private McBtsMessage convertModelToRequest(Long moId, TConfRCPE rcpe,
			int moc, String operation) throws Exception {
		McBtsMessage message = new McBtsMessage();
		if (rcpe == null && operation.equals(McBtsConstants.OPERATION_CONFIG)) {
			String msg = OmpAppContext.getMessage("unknown_biz_operation");
			throw new Exception(msg);
		}
		// �����Ԫ����������Ϣ
		if (operation.equals(McBtsConstants.OPERATION_CONFIG)) {
			// �����Ϣͷ
			this.fillHeader(message, moId, moc);
			// �����Ϣ��
			this.fillBody(message, rcpe, moc);
		}
		// �����Ԫ��ѯ������Ϣ
		if (operation.equals(McBtsConstants.OPERATION_QUERY)) {
			message.setMsgArea(1);
			message.setMa(McBtsMessage.MA_CONF);
			message.setMoc(moc);
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
	private TConfRCPE convertResponseToModel(Long moId, McBtsMessage response,
			McBtsMessage rcpeEXResponse) {
		byte[] rcpeBuf = response.getContent();
		byte[] rcpePlusBuf = rcpeEXResponse.getContent();
		// ������Ϣ��

		TConfRCPE rcpe = new TConfRCPE();
		rcpe.setMoId(moId);

		parseBody(rcpe, rcpeBuf, rcpePlusBuf);

		return rcpe;

	}

	/**
	 * ������ѯ��Ϣ��
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
	 * �����Ϣͷ
	 * 
	 * @param message
	 * @param moId
	 */
	private void fillHeader(McBtsMessage message, Long moId, int moc) {
		message.setMsgArea(1);
		message.setMa(McBtsMessage.MA_CONF);
		message.setMoc(moc);
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
	private void fillBody(McBtsMessage message, TConfRCPE rcpe, int moc) {
		byte[] buf = new byte[4096];
		int offset = 0;

		// ��ʵ������Ϣ����Ϊ�ֽ���
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
