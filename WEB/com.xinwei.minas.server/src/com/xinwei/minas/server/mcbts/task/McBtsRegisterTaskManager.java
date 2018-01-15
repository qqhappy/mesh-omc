/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.MoTypeDD;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.server.mcbts.helper.McBtsStatusChangeHelper;
import com.xinwei.minas.server.mcbts.model.message.McBtsRegisterNotify;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.service.AutomaticFindMcBtsCache;
import com.xinwei.minas.server.mcbts.service.McBtsBasicService;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.utils.McBtsUtils;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * ��վע�����������
 * 
 * @author chenjunhua
 * 
 */

public class McBtsRegisterTaskManager {

	private static final Log log = LogFactory
			.getLog(McBtsRegisterTaskManager.class);

	private static final McBtsRegisterTaskManager instance = new McBtsRegisterTaskManager();

	// ע������Map
	private Map<Long, McBtsRegisterTask> taskMap = new ConcurrentHashMap<Long, McBtsRegisterTask>();
	// �Է��ֻ�վ�ϴ�ע���ʱ��Map
	private Map<Long, Long> lastRegTimeMap = new ConcurrentHashMap<Long, Long>();

	// ע�������̳߳�
	private ThreadPoolExecutor threadPoolExecutor;

	// �Ƿ��Զ���������McBTS(ͨ����վע����Ϣ)
	private boolean createBtsAutomatically = true;

	// ����վͬʱע�����
	private int maxRegisterNum = 10;

	// ���Դ���������0x0602��
	private int retryNum = 10;

	// ���Լ��������0x0602��
	private long retryInterval = 15000;
	// ��վע�ᳬʱʱ���ݶ�Ϊ2���ӣ�����2����δ�յ���վע����Ϣ������վ���Է��ֻ������Ƴ�
	private int btsRegTimeout = 120000;
	// ִ��ɨ�賬ʱ��վ����Ŀ�ʼ��ʱ���������λ��
	private static final int DELAY = 5;

	private static final int PERIOD = 30;

	// ��վ��������ӿ�
	private McBtsBasicService mcBtsBasicService;

	private McBtsRegisterTaskManager() {
		threadPoolExecutor = new ThreadPoolExecutor(20, 20, 10,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(128),
				new ThreadPoolExecutor.CallerRunsPolicy());
		mcBtsBasicService = AppContext.getCtx()
				.getBean(McBtsBasicService.class);
		// ���ڶ�ʱɨ��ע�ᳬʱ��վ���߳�
		ScheduledExecutorService removeTimeOutBtsService = Executors
				.newScheduledThreadPool(1);
		removeTimeOutBtsService.scheduleAtFixedRate(new RemoveTimeOutBtsTask(),
				DELAY, PERIOD, TimeUnit.SECONDS);
	}

	public static McBtsRegisterTaskManager getInstance() {
		return instance;
	}

	/**
	 * ����ע��֪ͨ
	 * 
	 * @param message
	 */
	public void handleRegisterNotify(McBtsMessage message) {
		Long btsId = message.getBtsId();
		debug(btsId, "received mcbts register notification.");
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		if (mcBts == null) {
			// �����վ�����ܻ����в�����, �����վģ��
			mcBts = createDefaultMcBts(btsId);
			this.updateMcBts(mcBts, message);
			if (createBtsAutomatically) {
				// ������Զ�������վ����ֱ�ӵ��÷��񴴽���վ(��ģʽ�������ܲ���ʱʹ��)
				try {
					mcBtsBasicService.add(mcBts);
				} catch (Exception e) {
					log.error(
							"failed to add bts automatically. btsId=" + btsId,
							e);
				}
			} else {
				// ��������Զ�������վ, �򽫻�վ�����Է��ֻ�����
				AutomaticFindMcBtsCache.getInstance().addOrUpdate(mcBts);
				// ��¼��վ�ϴ�ע���ʱ��
				lastRegTimeMap.put(mcBts.getBtsId(), new Date().getTime());
			}
			return;
		}
		// ���»����л�վģ��
		this.updateMcBts(mcBts, message);
		// ���������ִ�е�ע�����������ע��֪ͨ��Ϣ
		if (taskMap.containsKey(btsId)) {
			log.warn("bts is in registering, btsId=0x" + mcBts.getHexBtsId());
			debug(btsId, "bts is in registering");
			return;
		}
		// �������ִ�е�ע��������������ֵ
		if (taskMap.size() >= maxRegisterNum) {
			log.warn("registering bts num is over threshold, reject btsId=0x"
					+ mcBts.getHexBtsId());
			debug(btsId, "registering bts num is over threshold.");
			return;
		}
		// ֻ��δ����״̬�£��Ž���ע��֪ͨ��Ϣ
		if (mcBts.isDisconnected()) {
			// ����״̬Ϊע����
			McBtsStatusChangeHelper.setRegistering(mcBts);
			McBtsRegisterTask task = new McBtsRegisterTask(btsId, this);
			taskMap.put(btsId, task);
			debug(btsId, "create register task.");
			threadPoolExecutor.execute(task);
		}
	}

	public void removeTask(Long btsId) {
		taskMap.remove(btsId);
	}

	private void debug(Long btsId, String message) {
		McBtsUtils.log(btsId, "Register", message);
	}

	/**
	 * ���ݻ�վID������վģ��
	 * 
	 * @param btsId
	 * @return
	 */
	private McBts createDefaultMcBts(Long btsId) {
		McBts bts = new McBts();
		// ���ݻ�վID, ������վģ��
		bts.setTypeId(MoTypeDD.MCBTS);
		bts.setBtsId(btsId);
		bts.setBtsType(McBtsTypeDD.FDDI_MCBTS);
		bts.setName("bts-" + bts.getHexBtsId());
		bts.setNatAPKey(1);
		bts.setNetworkId(1);
		bts.setBtsConfigIp("0.0.0.0");
		// ����SAG��Ϣ
		bts.setSagDeviceId(0);
		bts.setSagSignalPointCode(0);
		bts.setSagSignalIp("0.0.0.0");
		bts.setSagVoiceIp("0.0.0.0");
		//
		int factor = (int) (btsId & 0x00000fff);
		bts.setSagMediaPort(10000 + ((factor * 2) % 3000));
		bts.setBtsMediaPort(10000 + ((factor * 2) % 3000));
		bts.setSagSignalPort(2000 + ((factor * 2) % 3000));
		bts.setBtsSignalPort(2000 + ((factor * 2) % 3000));
		bts.setBtsSignalPointCode(8192 + factor);
		// λ����
		bts.setLocationAreaId(-1);
		return bts;
	}

	/**
	 * ���»�վģ��
	 * 
	 * @param mcBts
	 * @param message
	 */
	private void updateMcBts(McBts mcBts, McBtsMessage message) {
		McBtsRegisterNotify content = new McBtsRegisterNotify(mcBts.getBtsId(),
				message.getContent());
		mcBts.setPublicIp(message.getPublicIp());
		mcBts.setPublicPort(message.getPublicPort());
		mcBts.setHardwareVersion(content.getHardwareVersion());
		mcBts.setSoftwareVersion(content.getSortwareVersion());
		mcBts.setBtsIp(content.getBtsConfigIp());
	}

	public void setCreateBtsAutomatically(boolean createBtsAutomatically) {
		this.createBtsAutomatically = createBtsAutomatically;
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

	private class RemoveTimeOutBtsTask implements Runnable {
		public RemoveTimeOutBtsTask() {
		}

		@Override
		public void run() {
			long currentTime = new Date().getTime();
			List<Long> toBeRemove = new ArrayList<Long>();
			for (Entry<Long, Long> entry : lastRegTimeMap.entrySet()) {
				if (currentTime - entry.getValue() > btsRegTimeout) {
					toBeRemove.add(entry.getKey());
				}
			}
			// ��Ҫ�Ƴ��Ļ�վ
			if (toBeRemove.size() > 0) {
				AutomaticFindMcBtsCache cache = AutomaticFindMcBtsCache
						.getInstance();
				for (Long btsId : toBeRemove) {
					lastRegTimeMap.remove(btsId);
					// ���Է��ֻ������Ƴ�
					cache.delete(btsId);
				}
			}
		}

	}
}
