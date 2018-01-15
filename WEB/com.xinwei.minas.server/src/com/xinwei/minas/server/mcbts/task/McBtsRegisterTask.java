/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.task;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.server.mcbts.helper.McBtsStatusChangeHelper;
import com.xinwei.minas.server.mcbts.model.message.McBtsRegisterRequest;
import com.xinwei.minas.server.mcbts.model.message.McBtsRegisterResponse;
import com.xinwei.minas.server.mcbts.net.McBtsConnector;
import com.xinwei.minas.server.mcbts.net.McBtsMessage;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.common.McBtsModeService;
import com.xinwei.minas.server.mcbts.service.oamManage.McBtsSynConfigService;
import com.xinwei.minas.server.mcbts.utils.McBtsUtils;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.utils.EnvironmentUtils;

/**
 * 
 * ��վע������
 * 
 * @author chenjunhua
 * 
 */

public class McBtsRegisterTask implements Runnable {

	private Long btsId;

	private McBtsRegisterTaskManager manager;

	private static final String SERVER_BOOT_MODE_SIMPLE = "simple";

	// ִ�д���
	private int num = 1;

	public McBtsRegisterTask(Long btsId, McBtsRegisterTaskManager manager) {
		this.btsId = btsId;
		this.manager = manager;
	}

	public void run() {
		try {
			doWork();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			finishWork();
		}

	}

	/**
	 * ִ��ע������
	 * 
	 * @return
	 * @throws Exception
	 */
	private boolean doWork() throws Exception {
		McBtsMessage request = buildRegisterRequestMessage();
		if (request == null) {
			return false;
		}
		McBtsConnector connector = AppContext.getCtx().getBean(
				McBtsConnector.class);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		if (mcBts == null || !mcBts.isRegistering()) {
			// ��վ����ע����״̬���˳�����
			debug("bts is not in registering state, exit.");
			return false;
		}
		McBtsMessage response = null;
		McBtsRegisterResponse respContent = null;
		while (num <= manager.getRetryNum()) {
			try {
				// ����ע������
				debug("send register request to bts.");
				response = connector.syncInvoke(request);
				debug("receive register response.");
				// ע��Ӧ��ʧ�ܣ��ı��վ״̬Ϊδ����
				if (!response.isSuccessful()) {
					debug("register failed, change bts state to disconnected.");
					McBtsStatusChangeHelper.setDisconnected(mcBts);
					return false;
				}
				// ����ע��Ӧ����
				respContent = new McBtsRegisterResponse(btsId,
						response.getContent());
				// �յ����������Ӧ�𣬽���վ״̬�޸�Ϊ������
				// 1.����ԴΪEMS
				// 2.����Դδ����
				// 3.����ԴΪBTS������״̬ΪRunning
				if (respContent.isBootupFromEms()
						|| respContent.isBootupUndefined()
						|| (respContent.isBootupFromBts() && respContent
								.isBtsRunning())) {
					// debug("��վ״̬�޸�Ϊ������");
					debug("change bts state to connected.");
					McBtsStatusChangeHelper.setConnected(mcBts);
					break;
				}
				// ����N��BTS(INIT)������վ״̬�޸�Ϊδ���ӣ����ɸ澯
				else if (respContent.isBootupFromBts()
						&& respContent.isBtsInit() && num == manager.getRetryNum()) {
					debug("change bts state to disconnected. fire BTS(INIT) alarm.");
					McBtsStatusChangeHelper.setDisconnected(mcBts);
					break;
				}
				else {
					Thread.sleep(manager.getRetryInterval());
					num++;
				}
			} catch (Exception e) {
				// �����쳣������վ״̬����Ϊδ����
				McBtsStatusChangeHelper.setDisconnected(mcBts);
				error("register error", e);
				return false;
			} finally {
				
			}
		}

		// ��ʼͬ������
		debug("begin sync data.");
		
		// ע��ɹ���ִ������ͬ������
		doSync(mcBts, respContent);
		
		// ע���������
		debug("register task finished.");

		return true;
	}

	/**
	 * ���ע��������������
	 * 
	 */
	private void finishWork() {
		manager.removeTask(btsId);
	}

	/**
	 * ִ������ͬ������
	 * 
	 * @param mcBts
	 */
	private void doSync(McBts mcBts, McBtsRegisterResponse respContent) {
		// ����Ǽ���ģʽ�һ�վ��С��վ����ӻ�վ��ȡ���ݵ�����
		if (EnvironmentUtils.isSimpleMinas()
				&& mcBts.getTypeId() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
			try {
				McBtsModeService mcbtsModeService = AppContext.getCtx()
						.getBean(McBtsModeService.class);
				// ��ȡ��վ����ģʽ����Ϣ
				mcBts = mcbtsModeService.queryMcBtsMode(mcBts);
				// ��վ����Դ����BTS���վ����ģʽ���ǵ�վ,�򷵻�
				if (mcBts.getBootSource() != McBts.BOOT_SOURCE_BTS
						|| mcBts.getWorkMode() != McBts.WORK_MODE_SINGLE) {
					return;
				}
				// ͬ����վ���ݵ�����
				McBtsSynConfigService mcBtsSynConfigService = AppContext
						.getCtx().getBean(McBtsSynConfigService.class);
				mcBtsSynConfigService.syncFromNEToEMS(mcBts.getMoId());
			} catch (Exception e) {				
				error("failed to sync data (bts-->ems).", e);
			}
		}
		// �������ԴΪBTS������״̬ΪRunning������Ҫͬ���������ݵ���վ
		else if (respContent.isBootupFromBts() && respContent.isBtsRunning()) {
			try {
				McBtsSynConfigService mcBtsSynConfigService = AppContext
						.getCtx().getBean(McBtsSynConfigService.class);
				mcBtsSynConfigService
						.config(McBtsSynConfigService.NOT_NEED_RESTUDY,
								mcBts.getMoId());
			} catch (Exception e) {
				error("failed to sync data (ems-->bts).", e);
			}
		}
	}

	/**
	 * ����ע��������Ϣ
	 * 
	 * @return
	 */
	private McBtsMessage buildRegisterRequestMessage() {
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		if (mcBts == null) {
			return null;
		}
		McBtsRegisterRequest request = new McBtsRegisterRequest(btsId, 0,
				mcBts.getPublicIp(), mcBts.getPublicPort(), 3);
		return request.toMcBtsMessage();
	}

	private void debug(String message) {
		McBtsUtils.log(btsId, "Register", message);
	}

	private void error(String message, Exception e) {
		McBtsUtils.log(btsId, "Register",
				message + ": " + e);
	}

}
