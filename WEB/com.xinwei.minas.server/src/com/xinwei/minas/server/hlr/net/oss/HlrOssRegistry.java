/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-07-31	| chenjunhua 	| 	create the file                    
 */
package com.xinwei.minas.server.hlr.net.oss;

import java.util.HashMap;
import java.util.Map;

import com.xinwei.minas.server.hlr.net.oss.model.HlrOssBizMessage;

/**
 * HLR��Ϣ����ע���
 * 
 * @author chenjunhua
 * 
 */
public class HlrOssRegistry {
	// ����ģʽ
	private static HlrOssRegistry instance = new HlrOssRegistry();

	private Map<Integer, HlrOssSyncResult> resultCache = new HashMap();

	private HlrOssRegistry() {

	}

	public static HlrOssRegistry getInstance() {
		return instance;
	}

	/**
	 * ע��һ����Ϣ
	 * 
	 * @param sessionId
	 *            ��Ϣ��������
	 * @return ��Ϣͬ���ȴ����
	 */
	public HlrOssSyncResult register(int sessionId) {
		if (!resultCache.containsKey(sessionId)) {
			resultCache.put(sessionId, new HlrOssSyncResult(sessionId));
		}
		return resultCache.get(sessionId);
	}

	/**
	 * ע��һ����Ϣ
	 * 
	 * @param sessionId
	 *            ��Ϣ��������
	 * @return ��Ϣͬ���ȴ����
	 */
	public HlrOssSyncResult deregister(int sessionId) {
		return resultCache.remove(sessionId);
	}

	/**
	 * ������Ϣ�������Ż�ȡͬ���ȴ����
	 * 
	 * @param sessionId
	 *            ��Ϣ��������
	 * @return ��Ϣͬ���ȴ����
	 */
	public HlrOssSyncResult getBy(int sessionId) {
		return resultCache.get(sessionId);
	}
	
	public void addResponse(HlrOssBizMessage hlrOssBizMessage) {
		int sessionId = hlrOssBizMessage.getSessionId();
		HlrOssSyncResult result = this.getBy(sessionId);
		if (result != null) {
			HlrOssBizMessage fullMessage = result.append(hlrOssBizMessage);
		}
	}
}
