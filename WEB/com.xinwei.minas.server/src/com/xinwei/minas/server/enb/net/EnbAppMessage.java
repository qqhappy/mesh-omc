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
 * eNB应用报文层消息
 * 
 * @author chenjunhua
 * 
 */

public class EnbAppMessage {

	public static final int HEADER_LEN = 18;

	// eNB标识，8字节
	private long enbId;

	// 事务编号：4个字节，EMS和eNB使用的空间需分开，用于应用层请求和应答消息的对应
	private long transactionId;

	// 管理类型：2字节（配置管理、故障管理、性能管理、终端管理、文件管理）
	private int ma;

	// 业务编号：2字节，唯一标识一个业务，如GPS业务
	private int moc;

	// 操作类型：1字节（0-查询、1-配置、2-增加、3-修改、4-删除、5-其他）
	private int actionType;

	// 消息类型：1字节（0-请求、1-应答、2-通知）
	private int messageType;

	// TAG -- Value
	private Map<Integer, List<Object>> tvMap = new LinkedHashMap<Integer, List<Object>>();

	private String publicIp;

	private int publicPort;

	/**
	 * 增加tag值，值的形式为单个对象
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
	 * 添加tag值，值的形式为列表
	 * 
	 * @param tag
	 * @param valueList
	 */
	public void addTagListValue(int tag, List<Object> valueList) {
		tvMap.put(tag, valueList);
	}

	/**
	 * 获取整型数值
	 * 
	 * @param tag
	 * @return
	 */
	public int getIntValue(int tag) {
		Object object = getSingleValue(tag);
		return Integer.valueOf(object.toString());
	}

	/**
	 * 获取长整型数值
	 * 
	 * @param tag
	 * @return
	 */
	public long getLongValue(int tag) {
		Object object = getSingleValue(tag);
		return Long.valueOf(object.toString());
	}

	/**
	 * 获取长整型时间数值(yyyyMMddhhmmss)
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
	 * 获取字符串
	 * 
	 * @param tag
	 * @return
	 */
	public String getStringValue(int tag) {
		return (String) getSingleValue(tag);
	}

	/**
	 * 获取字节数组
	 * 
	 * @param tag
	 * @return
	 */
	public byte[] getByteValue(int tag) {
		return (byte[]) getSingleValue(tag);
	}

	/**
	 * 获取列表
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
	 * 获取结果
	 * 
	 * @return 0-成功; 其他-失败
	 */
	public int getResult() {
		int result = this.getIntValue(TagConst.RESULT);
		return result;
	}

	/**
	 * 判断结果是否成功
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
	 * 判断是否心跳消息
	 * 
	 * @return 如果是,返回true. 否则返回false
	 */
	public boolean isHeartbeatMessage() {
		return moc == EnbMessageConstants.MOC_HEARTBEAT;
	}

	/**
	 * 判断是否是请求消息
	 * 
	 * @return 如果是请求消息,返回true; 否则返回false
	 */
	public boolean isRequestMessage() {
		return messageType == EnbMessageConstants.MESSAGE_REQUEST;
	}

	/**
	 * 判断是否是应答消息
	 * 
	 * @return 如果是应答消息,返回true; 否则返回false
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
