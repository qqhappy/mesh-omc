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
 * 基站注册任务
 * 
 * @author chenjunhua
 * 
 */

public class McBtsRegisterTask implements Runnable {

	private Long btsId;

	private McBtsRegisterTaskManager manager;

	private static final String SERVER_BOOT_MODE_SIMPLE = "simple";

	// 执行次数
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
	 * 执行注册任务
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
			// 基站不是注册中状态，退出流程
			debug("bts is not in registering state, exit.");
			return false;
		}
		McBtsMessage response = null;
		McBtsRegisterResponse respContent = null;
		while (num <= manager.getRetryNum()) {
			try {
				// 发送注册请求
				debug("send register request to bts.");
				response = connector.syncInvoke(request);
				debug("receive register response.");
				// 注册应答失败，改变基站状态为未连接
				if (!response.isSuccessful()) {
					debug("register failed, change bts state to disconnected.");
					McBtsStatusChangeHelper.setDisconnected(mcBts);
					return false;
				}
				// 分析注册应答结果
				respContent = new McBtsRegisterResponse(btsId,
						response.getContent());
				// 收到以下情况的应答，将基站状态修改为已连接
				// 1.启动源为EMS
				// 2.启动源未定义
				// 3.启动源为BTS，运行状态为Running
				if (respContent.isBootupFromEms()
						|| respContent.isBootupUndefined()
						|| (respContent.isBootupFromBts() && respContent
								.isBtsRunning())) {
					// debug("基站状态修改为已连接");
					debug("change bts state to connected.");
					McBtsStatusChangeHelper.setConnected(mcBts);
					break;
				}
				// 连续N次BTS(INIT)，将基站状态修改为未连接，生成告警
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
				// 发生异常，将基站状态设置为未连接
				McBtsStatusChangeHelper.setDisconnected(mcBts);
				error("register error", e);
				return false;
			} finally {
				
			}
		}

		// 开始同步数据
		debug("begin sync data.");
		
		// 注册成功后执行数据同步操作
		doSync(mcBts, respContent);
		
		// 注册任务结束
		debug("register task finished.");

		return true;
	}

	/**
	 * 完成注册任务后的清理工作
	 * 
	 */
	private void finishWork() {
		manager.removeTask(btsId);
	}

	/**
	 * 执行数据同步操作
	 * 
	 * @param mcBts
	 */
	private void doSync(McBts mcBts, McBtsRegisterResponse respContent) {
		// 如果是简易模式且基站是小基站，则从基站获取数据到网管
		if (EnvironmentUtils.isSimpleMinas()
				&& mcBts.getTypeId() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
			try {
				McBtsModeService mcbtsModeService = AppContext.getCtx()
						.getBean(McBtsModeService.class);
				// 获取基站工作模式等信息
				mcBts = mcbtsModeService.queryMcBtsMode(mcBts);
				// 基站启动源不是BTS或基站工作模式不是单站,则返回
				if (mcBts.getBootSource() != McBts.BOOT_SOURCE_BTS
						|| mcBts.getWorkMode() != McBts.WORK_MODE_SINGLE) {
					return;
				}
				// 同步基站数据到网管
				McBtsSynConfigService mcBtsSynConfigService = AppContext
						.getCtx().getBean(McBtsSynConfigService.class);
				mcBtsSynConfigService.syncFromNEToEMS(mcBts.getMoId());
			} catch (Exception e) {				
				error("failed to sync data (bts-->ems).", e);
			}
		}
		// 如果启动源为BTS，运行状态为Running，则需要同步网管数据到基站
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
	 * 构造注册请求消息
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
