/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2014-04-15	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.task;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.server.enb.helper.EnbStatusChangeHelper;
import com.xinwei.minas.server.enb.helper.EnbUtils;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.omp.server.cache.XUIMetaCache;

/**
 * 
 * eNBע�����������
 * 
 * @author chenjunhua
 * 
 */

public class EnbRegisterTaskManager {

	private static final Log log = LogFactory
			.getLog(EnbRegisterTaskManager.class);

	private static final EnbRegisterTaskManager instance = new EnbRegisterTaskManager();

	// ע������Map
	private Map<Long, EnbRegisterTask> taskMap = new ConcurrentHashMap<Long, EnbRegisterTask>();

	// ע�������̳߳�
	private ThreadPoolExecutor threadPoolExecutor;

	// ����վͬʱע�����
	private int maxRegisterNum = 10;

	// ���Դ���
	private int retryNum = 10;

	// ���Լ��
	private long retryInterval = 15000;

	private EnbRegisterTaskManager() {
		threadPoolExecutor = new ThreadPoolExecutor(20, 20, 10,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(128),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public static EnbRegisterTaskManager getInstance() {
		return instance;
	}

	/**
	 * ����ע��֪ͨ
	 * 
	 * @param message
	 */
	public void handleRegisterNotify(EnbAppMessage message) {
		Long enbId = message.getEnbId();
		debug(enbId, "received enb register notification.");
		Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
		if (enb == null) {
			debug(enbId, "enb does not exist");
			return;
		}
		// ���δ��վ��������ע��
		if (!enb.isActive()) {
			debug(enbId, "enb is not active");
			return;
		}
		// ��վ����δ��ʼ����ȫ��������ע��
		boolean configOk = XUIMetaCache.getInstance().isInitialized();
		if (!configOk) {
			debug(enbId, "XUIMetaCache doesn't finish initializing.");
			return;
		}
		// ����eNB�Ĺ���IP�Ͷ˿�
		enb.setPublicIp(message.getPublicIp());
		enb.setPublicPort(message.getPublicPort());
		// ���������ִ�е�ע�����������ע��֪ͨ��Ϣ
		if (taskMap.containsKey(enbId)) {
			debug(enbId, "enb is registering");
			return;
		}
		// �������ִ�е�ע��������������ֵ
		if (taskMap.size() >= maxRegisterNum) {
			debug(enbId, "registering bts num is over threshold.");
			return;
		}
		// ֻ��δ����״̬�£��Ž���ע��֪ͨ��Ϣ
		if (enb.isDisconnected()) {
			// ����״̬Ϊע����
			EnbStatusChangeHelper.setRegistering(enb);
			EnbRegisterTask task = new EnbRegisterTask(enbId, this);
			taskMap.put(enbId, task);
			debug(enbId, "create register task.");
			threadPoolExecutor.execute(task);
		}
	}

	public void removeTask(Long btsId) {
		taskMap.remove(btsId);
	}

	private void debug(Long btsId, String message) {
		EnbUtils.log(btsId, "Register", message);
	}

	public void setMaxRegisterNum(int maxRegisterNum) {
		this.maxRegisterNum = maxRegisterNum;
	}

	public int getRetryNum() {
		return retryNum;
	}

	public void setRetryNum(int retryNum) {
		this.retryNum = retryNum;
	}

	public long getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(long retryInterval) {
		this.retryInterval = retryInterval;
	}

}
