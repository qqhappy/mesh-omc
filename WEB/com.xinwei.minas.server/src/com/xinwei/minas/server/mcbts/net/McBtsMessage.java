/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2012-11-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.net;

import com.xinwei.common.utils.ByteArrayUtil;

/**
 * 
 * McBts��Ϣ
 * 
 * @author chenjunhua
 * 
 */

public class McBtsMessage {

	// 0 ���ù���
	public static final int MA_CONF = 0;

	// 1 ���Ϲ���
	public static final int MA_ALARM = 1;

	// 2 ���ܹ���
	public static final int MA_PERF = 2;

	// 3 �ն˹���
	public static final int MA_UT = 3;

	// 4 �ļ�����
	public static final int MA_FILE = 4;

	// 5 ��Ϲ���
	public static final int MA_DIAGNOSIS = 5;

	// 6 ��վ��ȫ����
	public static final int MA_BTS_SECU = 6;

	// 7 GPS����
	public static final int MA_GPS = 7;

	// 10 MEM����
	public static final int MA_MEM = 10;

	public static final int ACTION_TYPE_RESP = 0;

	public static final int ACTION_TYPE_CONFIG = 1;

	public static final int ACTION_TYPE_DELETE = 2;

	public static final int ACTION_TYPE_QUERY = 3;

	public static final int ACTION_TYPE_NOTIFY = 0xff00;

	// 1-EMS
	protected int msgArea = 1; // 2 bytes

	// �豸���
	protected long btsId; // 4 bytes

	// ��������
	protected int ma; // 2 bytes

	// �����¼���
	// 0~65535
	// ��ģ�����ID��ʶ��Χ���壺
	// 0X0000~0X00FF���ù���
	// 0X0100~0X01FF���Ϲ���
	// 0X0200~0X02FF���ܹ���
	// 0X0300~0X03FF�ն˹���
	// 0X0400~0X04FF�ļ�����
	// 0X0500~0X05FF��Ϲ���
	// 0x0600~0x06FF��վ�������
	// 0x0700~0x07FF GPS����
	protected int moc; // 2 bytes

	// ��������
	// 0 �� Ӧ��
	// 1 �� ����/�޸�
	// 2 �� ɾ��
	// 3 �� ��ѯ
	// 0xff00 �C ֪ͨ
	protected int actionType = -1; // 2 bytes

	// ������߱�������к�
	// 0 ~ 65535�����λ��Ϊ�����߱�־��0-EMS��1-BTS��
	protected int transactionId; // 2 bytes

	// �������,�����ڲ�������ΪӦ��ʱ��Ч
	// 0 �C success
	// �����빦�ܷ������֣�
	// ���ֽ�MOC
	// ���ֽڣ�1~255����ʶ��ģ�鶨��Ĵ���ԭ��
	protected int actionResult; // 2 bytes

	// ��Ϣ��
	protected byte[] content = new byte[0];

	// ****************************
	// ��վ����IP
	protected String publicIp;

	// ��վ�����˿�
	protected int publicPort;

	public McBtsMessage() {
	}

	public McBtsMessage(byte[] buf) {
		this(buf, 0);
	}

	/**
	 * ���캯��
	 * 
	 * @param buf
	 *            ��Ϣ�ֽ�����
	 * @param offset
	 *            ƫ����
	 */
	public McBtsMessage(byte[] buf, int offset) {
		int index = offset;
		// Msg Area
		this.msgArea = ByteArrayUtil.toInt(buf, index, 2);
		index += 2;
		// BTS ID
		this.btsId = ByteArrayUtil.toLong(buf, index, 4);
		index += 4;
		// MA
		this.ma = ByteArrayUtil.toInt(buf, index, 2);
		index += 2;
		// MOC
		this.moc = ByteArrayUtil.toInt(buf, index, 2);
		index += 2;
		// Action Type
		this.actionType = ByteArrayUtil.toInt(buf, index, 2);
		index += 2;
		// transactionId
		this.transactionId = ByteArrayUtil.toInt(buf, index, 2);
		index += 2;

		// �����Ӧ����Ϣ�����в�������ֶ�
		if (isResponseMessage()) {
			// Action Result
			this.actionResult = ByteArrayUtil.toInt(buf, index, 2);
			index += 2;
		}
		int contentLength = buf.length - index;
		setContent(buf, index, contentLength);
	}

	/**
	 * �ж��Ƿ��ǲ�֧�ֵ�ҵ�����
	 * 
	 * @return
	 */
	public boolean isUnsupportedOperation() {
		return moc == 0x0800;
	}

	/**
	 * ��ȡ��Ϣ����
	 * 
	 * @return ��Ϣ����
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * ������Ϣ���ݡ�
	 * 
	 * @param buf
	 *            ������Ϣ���ݵ����顣
	 * @param offset
	 *            ��Ϣ�����������е���ʼλ�á�
	 * @param length
	 *            ��Ϣ���ݵĳ��ȡ�
	 */
	public void setContent(byte[] buf, int offset, int length) {
		if (buf == null) {
			return;
		}
		this.content = new byte[length];
		System.arraycopy(buf, offset, this.content, 0, length);
	}

	/**
	 * ������Ϣ���ݡ�
	 * 
	 * @param buf
	 *            byte[] ������Ϣ���ݵ����顣
	 */
	public void setContent(byte[] buf) {
		if (buf == null) {
			return;
		}
		int len = buf.length;
		this.content = new byte[len];
		System.arraycopy(buf, 0, this.content, 0, len);
	}

	public int getActionResult() {
		return actionResult;
	}

	public void setActionResult(int actionResult) {
		this.actionResult = actionResult;
	}

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public int getMa() {
		return ma;
	}

	public void setMa(int ma) {
		this.ma = ma;
	}

	public int getMoc() {
		return moc;
	}

	public void setMoc(int moc) {
		this.moc = moc;
	}

	public int getMsgArea() {
		return msgArea;
	}

	public void setMsgArea(int msgArea) {
		this.msgArea = msgArea;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * ����Э�飬����Ϣת��Ϊ�ֽ�����
	 * 
	 * @return �ֽ�����
	 */
	public byte[] toBytes() {
		int totalLength = content.length + 14;
		if (isResponseMessage()) {
			totalLength += 2;
		}
		byte[] msg = new byte[totalLength];
		int index = 0;

		msg[index++] = (byte) (msgArea >>> 8);
		msg[index++] = (byte) (msgArea);

		msg[index++] = (byte) (btsId >>> 24);
		msg[index++] = (byte) (btsId >>> 16);
		msg[index++] = (byte) (btsId >>> 8);
		msg[index++] = (byte) (btsId);

		msg[index++] = (byte) (ma >>> 8);
		msg[index++] = (byte) (ma);

		msg[index++] = (byte) (moc >>> 8);
		msg[index++] = (byte) (moc);

		msg[index++] = (byte) (actionType >>> 8);
		msg[index++] = (byte) (actionType);

		msg[index++] = (byte) (transactionId >>> 8);
		msg[index++] = (byte) (transactionId);

		if (isResponseMessage()) {
			msg[index++] = (byte) (actionResult >>> 8);
			msg[index++] = (byte) (actionResult);
		}

		System.arraycopy(content, 0, msg, index, content.length);
		index += content.length;
		return msg;
	}

	public long getBtsId() {
		return btsId;
	}

	public void setBtsId(long btsId) {
		this.btsId = btsId;
	}

	/**
	 * �ж��Ƿ���������Ϣ
	 * 
	 * @return �����������Ϣ,����true; ���򷵻�false
	 */
	public boolean isRequestMessage() {
		return actionType == ACTION_TYPE_CONFIG
				|| actionType == ACTION_TYPE_DELETE
				|| actionType == ACTION_TYPE_QUERY;
	}

	/**
	 * �ж��Ƿ���Ӧ����Ϣ
	 * 
	 * @return �����Ӧ����Ϣ,����true; ���򷵻�false
	 */
	public boolean isResponseMessage() {
		return actionType == ACTION_TYPE_RESP;
	}

	/**
	 * �ж��Ƿ��Ƕ�λ��Ϣ
	 * 
	 * @return
	 */
	public boolean isLocationMessage() {
		return (transactionId >= 30001 && transactionId <= 32767);
	}

	/**
	 * �ж���Ϣ�Ƿ����첽���͵Ļ�վʵʱ������Ϣ�ظ�
	 * 
	 * @return
	 */
	public boolean isAsyncPerfMessage() {
		if (transactionId >= 10
				&& moc == McBtsMessageConstants.MOC_REAL_TIME_PERF_RESPONSE) {
			return true;
		}
		return false;
	}

	/**
	 * �ж��Ƿ��ǻ�վע��֪ͨ��Ϣ
	 * 
	 * @return
	 */
	public boolean isRegisterNotifyMessage() {
		return moc == McBtsMessageConstants.MOC_REGISTER_NOTIFY;
	}

	/**
	 * �ж��Ƿ����첽��Ϣ������֪ͨ��Ϣ����Ҫ�첽�������Ϣ��
	 * 
	 * @return �����֪ͨ��Ϣ,����true. ���򷵻�false
	 */
	public boolean isAsyncMessage() {
		return actionType == ACTION_TYPE_NOTIFY // ֪ͨ��Ϣ
				|| moc == McBtsMessageConstants.MOC_HEARTBEAT_RESPONSE // ��վ����Ӧ��
				|| (ma == MA_PERF && moc == McBtsMessageConstants.MOC_ONLINE_USERLIST_RESPONSE) // �����ն��б�Ӧ��
				|| moc == McBtsMessageConstants.MOC_LOCATION_RESPONSE; // ��λ��Ϣ
	}

	/**
	 * �ж��Ƿ�������Ϣ
	 * 
	 * @return �����,����true. ���򷵻�false
	 */
	public boolean isHeartbeatMessage() {
		return (moc == McBtsMessageConstants.MOC_HEARTBEAT_REQUEST // ��վ��������
		|| moc == McBtsMessageConstants.MOC_HEARTBEAT_RESPONSE // ��վ����Ӧ��
		);
	}

	/**
	 * �жϽ���Ƿ�ɹ�
	 * 
	 * @return
	 */
	public boolean isSuccessful() {
		return actionResult == 0;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public int getPublicPort() {
		return publicPort;
	}

	public void setPublicPort(int publicPort) {
		this.publicPort = publicPort;
	}

}
