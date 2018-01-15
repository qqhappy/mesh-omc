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
 * McBts消息
 * 
 * @author chenjunhua
 * 
 */

public class McBtsMessage {

	// 0 配置管理
	public static final int MA_CONF = 0;

	// 1 故障管理
	public static final int MA_ALARM = 1;

	// 2 性能管理
	public static final int MA_PERF = 2;

	// 3 终端管理
	public static final int MA_UT = 3;

	// 4 文件管理
	public static final int MA_FILE = 4;

	// 5 诊断管理
	public static final int MA_DIAGNOSIS = 5;

	// 6 基站安全管理
	public static final int MA_BTS_SECU = 6;

	// 7 GPS管理
	public static final int MA_GPS = 7;

	// 10 MEM管理
	public static final int MA_MEM = 10;

	public static final int ACTION_TYPE_RESP = 0;

	public static final int ACTION_TYPE_CONFIG = 1;

	public static final int ACTION_TYPE_DELETE = 2;

	public static final int ACTION_TYPE_QUERY = 3;

	public static final int ACTION_TYPE_NOTIFY = 0xff00;

	// 1-EMS
	protected int msgArea = 1; // 2 bytes

	// 设备编号
	protected long btsId; // 4 bytes

	// 管理类型
	protected int ma; // 2 bytes

	// 管理事件号
	// 0~65535
	// 各模块对象ID标识范围定义：
	// 0X0000~0X00FF配置管理
	// 0X0100~0X01FF故障管理
	// 0X0200~0X02FF性能管理
	// 0X0300~0X03FF终端管理
	// 0X0400~0X04FF文件管理
	// 0X0500~0X05FF诊断管理
	// 0x0600~0x06FF基站导入管理
	// 0x0700~0x07FF GPS管理
	protected int moc; // 2 bytes

	// 操作类型
	// 0 － 应答
	// 1 － 设置/修改
	// 2 － 删除
	// 3 － 查询
	// 0xff00 – 通知
	protected int actionType = -1; // 2 bytes

	// 命令或者报告的序列号
	// 0 ~ 65535（最高位作为发起者标志，0-EMS，1-BTS）
	protected int transactionId; // 2 bytes

	// 操作结果,仅仅在操作类型为应答时有效
	// 0 – success
	// 错误码功能分两部分：
	// 高字节MOC
	// 低字节（1~255）标识本模块定义的错误原因
	protected int actionResult; // 2 bytes

	// 消息体
	protected byte[] content = new byte[0];

	// ****************************
	// 基站公网IP
	protected String publicIp;

	// 基站公网端口
	protected int publicPort;

	public McBtsMessage() {
	}

	public McBtsMessage(byte[] buf) {
		this(buf, 0);
	}

	/**
	 * 构造函数
	 * 
	 * @param buf
	 *            消息字节数组
	 * @param offset
	 *            偏移量
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

		// 如果是应答消息，则有操作结果字段
		if (isResponseMessage()) {
			// Action Result
			this.actionResult = ByteArrayUtil.toInt(buf, index, 2);
			index += 2;
		}
		int contentLength = buf.length - index;
		setContent(buf, index, contentLength);
	}

	/**
	 * 判断是否是不支持的业务操作
	 * 
	 * @return
	 */
	public boolean isUnsupportedOperation() {
		return moc == 0x0800;
	}

	/**
	 * 获取消息内容
	 * 
	 * @return 消息内容
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * 设置消息内容。
	 * 
	 * @param buf
	 *            包含消息内容的数组。
	 * @param offset
	 *            消息内容在数组中的起始位置。
	 * @param length
	 *            消息内容的长度。
	 */
	public void setContent(byte[] buf, int offset, int length) {
		if (buf == null) {
			return;
		}
		this.content = new byte[length];
		System.arraycopy(buf, offset, this.content, 0, length);
	}

	/**
	 * 设置消息内容。
	 * 
	 * @param buf
	 *            byte[] 包含消息内容的数组。
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
	 * 按照协议，将消息转换为字节数组
	 * 
	 * @return 字节数组
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
	 * 判断是否是请求消息
	 * 
	 * @return 如果是请求消息,返回true; 否则返回false
	 */
	public boolean isRequestMessage() {
		return actionType == ACTION_TYPE_CONFIG
				|| actionType == ACTION_TYPE_DELETE
				|| actionType == ACTION_TYPE_QUERY;
	}

	/**
	 * 判断是否是应答消息
	 * 
	 * @return 如果是应答消息,返回true; 否则返回false
	 */
	public boolean isResponseMessage() {
		return actionType == ACTION_TYPE_RESP;
	}

	/**
	 * 判断是否是定位消息
	 * 
	 * @return
	 */
	public boolean isLocationMessage() {
		return (transactionId >= 30001 && transactionId <= 32767);
	}

	/**
	 * 判断消息是否是异步发送的基站实时性能消息回复
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
	 * 判断是否是基站注册通知消息
	 * 
	 * @return
	 */
	public boolean isRegisterNotifyMessage() {
		return moc == McBtsMessageConstants.MOC_REGISTER_NOTIFY;
	}

	/**
	 * 判断是否是异步消息（包括通知消息和需要异步处理的消息）
	 * 
	 * @return 如果是通知消息,返回true. 否则返回false
	 */
	public boolean isAsyncMessage() {
		return actionType == ACTION_TYPE_NOTIFY // 通知消息
				|| moc == McBtsMessageConstants.MOC_HEARTBEAT_RESPONSE // 基站心跳应答
				|| (ma == MA_PERF && moc == McBtsMessageConstants.MOC_ONLINE_USERLIST_RESPONSE) // 在线终端列表应答
				|| moc == McBtsMessageConstants.MOC_LOCATION_RESPONSE; // 定位消息
	}

	/**
	 * 判断是否心跳消息
	 * 
	 * @return 如果是,返回true. 否则返回false
	 */
	public boolean isHeartbeatMessage() {
		return (moc == McBtsMessageConstants.MOC_HEARTBEAT_REQUEST // 基站心跳请求
		|| moc == McBtsMessageConstants.MOC_HEARTBEAT_RESPONSE // 基站心跳应答
		);
	}

	/**
	 * 判断结果是否成功
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
