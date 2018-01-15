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
 * HLR��Ϣͬ���ȴ����
 * 
 * @author chenjunhua
 * 
 */
public class HlrOssSyncResult {
	
	// �Ƿ�׼�����
	private boolean ready;

	// �쳣
	private InvocationTargetException exception;

	// Ӧ������Ϣ�ֽ������б�
	private List<HlrOssBizMessage> messageList = new LinkedList();

	// ������Ӧ�ò㱨��
	private HlrOssBizMessage completeMessage;

	// ������
	private int sessionId;

	public HlrOssSyncResult(int sessionId) {
		this.sessionId = sessionId;
		this.ready = false;
		this.exception = null;
	}

	/**
	 * ����Oss��Ϣ
	 * 
	 * @param hlrMessage
	 *            Oss��Ϣ
	 * @return ���Ӧ�ò㱨���Ѿ��ռ��������򷵻�������Ӧ�ò㱨��
	 */
	public synchronized HlrOssBizMessage append(HlrOssBizMessage hlrMessage) {
		Collections.sort(messageList, new Comparator<HlrOssBizMessage>() {
			public int compare(HlrOssBizMessage hlrBizMessage1,
					HlrOssBizMessage hlrBizMessage2) { // �Ƚϰ����

				int pkgNo1 = hlrBizMessage1.getPrivateHeader().getPackageNo();
				int pkgNo2 = hlrBizMessage2.getPrivateHeader().getPackageNo();
				return (pkgNo1 - pkgNo2);
			}
		});
		// �����Ϣ�Ѿ��ռ���ϣ�����notifyAll
		if (isMessageComplete()) {
			// �ϲ�Ӧ�ò㱨��
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
	 * �ȴ����
	 * 
	 * @param timeout
	 *            ��ʱʱ��, ��λ: ����
	 * @return �������
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
	 * �����쳣
	 * 
	 * @param ex
	 */
	public synchronized void setException(Throwable ex) {
		exception = new InvocationTargetException(ex);
		ready = true;
		notifyAll();
	}

	/**
	 * ������ҵ�����Ϣ�ϲ�Ϊһ��
	 * 
	 * @return �ϲ����ҵ�����Ϣ�ֽ���
	 */
	private HlrOssBizMessage mergeMessage() {
		// ���ֻ��һ����Ϣ��˵������Ҫ�ϲ���ֱ�ӷ���
		if (messageList.size() == 1) {
			return messageList.get(0);
		}
		// ��Ϣ��ƫ����
		int PUBLIC_LEN = 4; // ���б�ͷ����
		int PRIVATE_LEN = 24; // ˽�б�ͷ����
		int BIZ_OFFSET = PUBLIC_LEN + PRIVATE_LEN; // ��Ϣ��ƫ����

		// ҵ����Ϣ����Ϣ���ܳ���
		int totalBizLength = 0;
		for (HlrOssBizMessage message : messageList) {
			// ������Ϣҵ����Ϣ�峤��
			int bufbizLength = message.getBodyLength();
			totalBizLength += bufbizLength;
		}
		int totalLength = PUBLIC_LEN + PRIVATE_LEN + totalBizLength;
		byte[] buf = new byte[totalLength];
		int offset = 0;
		// ���ƹ��а�ͷ��˽�а�ͷ
		{
			HlrOssBizMessage lastMessage = messageList
					.get(messageList.size() - 1);
			byte[] lastBuf = lastMessage.toBytes();
			for (int i = 0; i < (PUBLIC_LEN + PRIVATE_LEN - 2); i++) {
				buf[offset] = lastBuf[offset];
				offset++;
			}
			// ��ֵ˽�а�ͷ����Ϣ�峤��
			buf[offset++] = (byte) (totalBizLength >>> 8);
			buf[offset++] = (byte) (totalBizLength);
		}
		// ������Ϣ��
		for (HlrOssBizMessage message : messageList) {
			// ������Ϣҵ����Ϣ�峤��
			int bodyLength = message.getBodyLength();
			System.arraycopy(message, BIZ_OFFSET, buf, offset, bodyLength);
			offset += bodyLength;
		}
		HlrOssBizMessage mergedMessage = new HlrOssBizMessage(buf, 0);
		return mergedMessage;
	}

	/**
	 * �ж���Ϣ�Ƿ��Ѿ��ռ�����
	 * 
	 * @return ����ռ�����������true�����򷵻�false
	 */
	private boolean isMessageComplete() {
		int totalNum = messageList.size();
		HlrOssBizMessage hlrBizMessage = messageList.get(totalNum - 1);
		// ��������һ���������һ���İ��ŵ��ڰ�����Ŀ-1��������������
		if (hlrBizMessage.getPrivateHeader().isLastPackage()
				&& hlrBizMessage.getPrivateHeader().getPackageNo() == (totalNum - 1)) {
			return true;
		}

		return false;
	}

	/**
	 * ��ȡ������Ϣ
	 * 
	 * @return ������Ϣ
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
