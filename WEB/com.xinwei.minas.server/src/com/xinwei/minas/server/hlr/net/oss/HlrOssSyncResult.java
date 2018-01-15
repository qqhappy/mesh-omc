/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-07-31	| chenjunhua 	| 	create the file                    
 */
package com.xinwei.minas.server.hlr.net.oss;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.xinwei.minas.server.hlr.net.oss.model.HlrOssBizMessage;

/**
 * HLR消息同步等待结果
 * 
 * @author chenjunhua
 * 
 */
public class HlrOssSyncResult {
	
	// 是否准备完毕
	private boolean ready;

	// 异常
	private InvocationTargetException exception;

	// 应答结果消息字节数组列表
	private List<HlrOssBizMessage> messageList = new LinkedList();

	// 完整的应用层报文
	private HlrOssBizMessage completeMessage;

	// 事务编号
	private int sessionId;

	public HlrOssSyncResult(int sessionId) {
		this.sessionId = sessionId;
		this.ready = false;
		this.exception = null;
	}

	/**
	 * 增加Oss消息
	 * 
	 * @param hlrMessage
	 *            Oss消息
	 * @return 如果应用层报文已经收集完整，则返回完整的应用层报文
	 */
	public synchronized HlrOssBizMessage append(HlrOssBizMessage hlrMessage) {
		Collections.sort(messageList, new Comparator<HlrOssBizMessage>() {
			public int compare(HlrOssBizMessage hlrBizMessage1,
					HlrOssBizMessage hlrBizMessage2) { // 比较包序号

				int pkgNo1 = hlrBizMessage1.getPrivateHeader().getPackageNo();
				int pkgNo2 = hlrBizMessage2.getPrivateHeader().getPackageNo();
				return (pkgNo1 - pkgNo2);
			}
		});
		// 如果消息已经收集完毕，调用notifyAll
		if (isMessageComplete()) {
			// 合并应用层报文
			completeMessage = mergeMessage();
			try {
				ready = true;
				this.notifyAll();
			} catch (Throwable e) {
			}
		}
		return completeMessage;
	}

	public synchronized boolean isReady() {
		return ready;
	}

	/**
	 * 等待结果
	 * 
	 * @param timeout
	 *            超时时间, 单位: 毫秒
	 * @return 结果报文
	 */
	public synchronized HlrOssBizMessage timeWait(int timeout)
			throws InvocationTargetException {
		if (isReady()) {
			return doGet();
		}
		try {
			this.wait(timeout);
		} catch (InterruptedException e) {
		}
		return doGet();

	}

	/**
	 * 设置异常
	 * 
	 * @param ex
	 */
	public synchronized void setException(Throwable ex) {
		exception = new InvocationTargetException(ex);
		ready = true;
		notifyAll();
	}

	/**
	 * 将多条业务层消息合并为一条
	 * 
	 * @return 合并后的业务层消息字节流
	 */
	private HlrOssBizMessage mergeMessage() {
		// 如果只有一条消息，说明不需要合并，直接返回
		if (messageList.size() == 1) {
			return messageList.get(0);
		}
		// 信息体偏移量
		int PUBLIC_LEN = 4; // 公有报头长度
		int PRIVATE_LEN = 24; // 私有报头长度
		int BIZ_OFFSET = PUBLIC_LEN + PRIVATE_LEN; // 消息体偏移量

		// 业务消息的消息体总长度
		int totalBizLength = 0;
		for (HlrOssBizMessage message : messageList) {
			// 单条消息业务消息体长度
			int bufbizLength = message.getBodyLength();
			totalBizLength += bufbizLength;
		}
		int totalLength = PUBLIC_LEN + PRIVATE_LEN + totalBizLength;
		byte[] buf = new byte[totalLength];
		int offset = 0;
		// 复制公有包头、私有包头
		{
			HlrOssBizMessage lastMessage = messageList
					.get(messageList.size() - 1);
			byte[] lastBuf = lastMessage.toBytes();
			for (int i = 0; i < (PUBLIC_LEN + PRIVATE_LEN - 2); i++) {
				buf[offset] = lastBuf[offset];
				offset++;
			}
			// 赋值私有包头的消息体长度
			buf[offset++] = (byte) (totalBizLength >>> 8);
			buf[offset++] = (byte) (totalBizLength);
		}
		// 复制信息体
		for (HlrOssBizMessage message : messageList) {
			// 单条消息业务消息体长度
			int bodyLength = message.getBodyLength();
			System.arraycopy(message, BIZ_OFFSET, buf, offset, bodyLength);
			offset += bodyLength;
		}
		HlrOssBizMessage mergedMessage = new HlrOssBizMessage(buf, 0);
		return mergedMessage;
	}

	/**
	 * 判断消息是否已经收集完整
	 * 
	 * @return 如果收集完整，返回true；否则返回false
	 */
	private boolean isMessageComplete() {
		int totalNum = messageList.size();
		HlrOssBizMessage hlrBizMessage = messageList.get(totalNum - 1);
		// 如果是最后一包，且最后一包的包号等于包的数目-1，表明包已完整
		if (hlrBizMessage.getPrivateHeader().isLastPackage()
				&& hlrBizMessage.getPrivateHeader().getPackageNo() == (totalNum - 1)) {
			return true;
		}

		return false;
	}

	/**
	 * 获取完整消息
	 * 
	 * @return 完整消息
	 * @throws InvocationTargetException
	 */
	private HlrOssBizMessage doGet() throws InvocationTargetException {
		if (exception != null) {
			throw exception;
		} else {
			return completeMessage;
		}
	}

}
