/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.net;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xinwei.omp.core.utils.StringUtils;

/**
 * 
 * eNBӦ�ñ��Ĳ���Ϣ
 * 
 * @author chenjunhua
 * 
 */

public class EnbAppMessage {

	public static final int HEADER_LEN = 18;

	// eNB��ʶ��8�ֽ�
	private long enbId;

	// �����ţ�4���ֽڣ�EMS��eNBʹ�õĿռ���ֿ�������Ӧ�ò������Ӧ����Ϣ�Ķ�Ӧ
	private long transactionId;

	// �������ͣ�2�ֽڣ����ù������Ϲ������ܹ����ն˹����ļ�����
	private int ma;

	// ҵ���ţ�2�ֽڣ�Ψһ��ʶһ��ҵ����GPSҵ��
	private int moc;

	// �������ͣ�1�ֽڣ�0-��ѯ��1-���á�2-���ӡ�3-�޸ġ�4-ɾ����5-������
	private int actionType;

	// ��Ϣ���ͣ�1�ֽڣ�0-����1-Ӧ��2-֪ͨ��
	private int messageType;

	// TAG -- Value
	private Map<Integer, List<Object>> tvMap = new LinkedHashMap<Integer, List<Object>>();

	private String publicIp;

	private int publicPort;

	/**
	 * ����tagֵ��ֵ����ʽΪ��������
	 * 
	 * @param field
	 */
	public void addTagValue(int tag, Object value) {
		List<Object> valueList = tvMap.get(tag);
		if (valueList == null) {
			valueList = new LinkedList<Object>();
			tvMap.put(tag, valueList);
		}
		valueList.add(value);
	}

	/**
	 * ���tagֵ��ֵ����ʽΪ�б�
	 * 
	 * @param tag
	 * @param valueList
	 */
	public void addTagListValue(int tag, List<Object> valueList) {
		tvMap.put(tag, valueList);
	}

	/**
	 * ��ȡ������ֵ
	 * 
	 * @param tag
	 * @return
	 */
	public int getIntValue(int tag) {
		Object object = getSingleValue(tag);
		return Integer.valueOf(object.toString());
	}

	/**
	 * ��ȡ��������ֵ
	 * 
	 * @param tag
	 * @return
	 */
	public long getLongValue(int tag) {
		Object object = getSingleValue(tag);
		return Long.valueOf(object.toString());
	}

	/**
	 * ��ȡ������ʱ����ֵ(yyyyMMddhhmmss)
	 * 
	 * @param tag
	 * @return
	 */
	public long getDateTimeValue(int tag) {
		Object object = getSingleValue(tag);
		Long oTime = Long.valueOf(object.toString());
		String timeStr = Long.toHexString(oTime);
		for (int i = timeStr.length(); i < 16; i++) {
			timeStr = "0" + timeStr;
		}
		long year = Long.valueOf(timeStr.substring(0, 4), 16);
		long month = Long.valueOf(timeStr.substring(4, 6), 16);
		long day = Long.valueOf(timeStr.substring(6, 8), 16);
		long hour = Long.valueOf(timeStr.substring(8, 10), 16);
		long minute = Long.valueOf(timeStr.substring(10, 12), 16);
		long second = Long.valueOf(timeStr.substring(12, 14), 16);
		String str = StringUtils.appendPrefix(String.valueOf(year), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(month), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(day), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(hour), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(minute), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(second), "0", 2);
		return Long.valueOf(str);
	}

	/**
	 * ��ȡ�ַ���
	 * 
	 * @param tag
	 * @return
	 */
	public String getStringValue(int tag) {
		return (String) getSingleValue(tag);
	}

	/**
	 * ��ȡ�ֽ�����
	 * 
	 * @param tag
	 * @return
	 */
	public byte[] getByteValue(int tag) {
		return (byte[]) getSingleValue(tag);
	}

	/**
	 * ��ȡ�б�
	 * 
	 * @param tag
	 * @return
	 */
	public List<Object> getListValue(int tag) {
		return tvMap.get(tag);
	}

	private Object getSingleValue(int tag) {
		List<Object> valueList = tvMap.get(tag);
		if (valueList == null)
			return null;
		return valueList.get(0);
	}

	/**
	 * ��ȡ���
	 * 
	 * @return 0-�ɹ�; ����-ʧ��
	 */
	public int getResult() {
		int result = this.getIntValue(TagConst.RESULT);
		return result;
	}

	/**
	 * �жϽ���Ƿ�ɹ�
	 * 
	 * @return
	 */
	public boolean isSuccessful() {
		return getResult() == 0;
	}

	public boolean isAsyncMessage() {
		if (messageType == EnbMessageConstants.MESSAGE_NOTIFY
				|| moc == EnbMessageConstants.MOC_REGISTER_NOTIFY
				|| moc == EnbMessageConstants.MOC_HEARTBEAT
				|| moc == EnbMessageConstants.MOC_ALARM_NTFY
				|| moc == EnbMessageConstants.MOC_ASSET_INFO_NOTIFY)
			return true;
		return false;
	}

	/**
	 * �ж��Ƿ�������Ϣ
	 * 
	 * @return �����,����true. ���򷵻�false
	 */
	public boolean isHeartbeatMessage() {
		return moc == EnbMessageConstants.MOC_HEARTBEAT;
	}

	/**
	 * �ж��Ƿ���������Ϣ
	 * 
	 * @return �����������Ϣ,����true; ���򷵻�false
	 */
	public boolean isRequestMessage() {
		return messageType == EnbMessageConstants.MESSAGE_REQUEST;
	}

	/**
	 * �ж��Ƿ���Ӧ����Ϣ
	 * 
	 * @return �����Ӧ����Ϣ,����true; ���򷵻�false
	 */
	public boolean isResponseMessage() {
		return messageType == EnbMessageConstants.MESSAGE_RESPONSE;
	}

	public long getEnbId() {
		return enbId;
	}

	public void setEnbId(long enbId) {
		this.enbId = enbId;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
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

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public Map<Integer, List<Object>> getFieldMap() {
		return tvMap;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicPort(int publicPort) {
		this.publicPort = publicPort;
	}

	public int getPublicPort() {
		return publicPort;
	}

	public static void main(String[] args) {
		Long oTime = Long.valueOf("07de090c0d1a0400", 16);
		String timeStr = Long.toHexString(oTime);
		for (int i = timeStr.length(); i < 16; i++) {
			timeStr = "0" + timeStr;
		}
		long year = Long.valueOf(timeStr.substring(0, 4), 16);
		long month = Long.valueOf(timeStr.substring(4, 6), 16);
		long day = Long.valueOf(timeStr.substring(6, 8), 16);
		long hour = Long.valueOf(timeStr.substring(8, 10), 16);
		long minute = Long.valueOf(timeStr.substring(10, 12), 16);
		long second = Long.valueOf(timeStr.substring(12, 14), 16);
		String str = StringUtils.appendPrefix(String.valueOf(year), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(month), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(day), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(hour), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(minute), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(second), "0", 2);
		System.out.println(str);
	}
}
