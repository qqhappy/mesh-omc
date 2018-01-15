/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.task;

import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.server.enb.helper.EnbAlarmHelper;
import com.xinwei.minas.server.enb.helper.EnbBizDataConvertor;
import com.xinwei.minas.server.enb.helper.EnbBizHelper;
import com.xinwei.minas.server.enb.helper.EnbStatusChangeHelper;
import com.xinwei.minas.server.enb.helper.EnbUtils;
import com.xinwei.minas.server.enb.model.message.EnbRegisterRequest;
import com.xinwei.minas.server.enb.model.message.EnbRegisterResponse;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbConnector;
import com.xinwei.minas.server.enb.service.EnbAlarmService;
import com.xinwei.minas.server.enb.service.EnbBasicService;
import com.xinwei.minas.server.enb.service.EnbBizConfigService;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbExtBizService;
import com.xinwei.minas.server.enb.service.EnbFullTableConfigService;
import com.xinwei.minas.server.enb.service.EnbStudyDataConfigCache;
import com.xinwei.minas.server.enb.stat.service.EnbRealtimeMonitorService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * eNB注册任务
 * 
 * @author chenjunhua
 * 
 */

public class EnbRegisterTask implements Runnable {

	private static final Log log = LogFactory.getLog(EnbRegisterTask.class);

	private Long enbId;

	private EnbRegisterTaskManager manager;

	/**
	 * 实现自学习的eNB最小版本号
	 */
	private String compatibleMinVersion = "2.0.4.0";

	// 执行次数
	private int num = 1;

	public EnbRegisterTask(Long enbId, EnbRegisterTaskManager manager) {
		this.enbId = enbId;
		this.manager = manager;
	}

	public void run() {
		try {
			doWork();
		} catch (Throwable e) {
			log.error("EnbRegisterTask error.", e);
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
		EnbAppMessage request = buildRegisterRequestMessage();
		if (request == null) {
			return false;
		}
		EnbConnector connector = AppContext.getCtx()
				.getBean(EnbConnector.class);
		Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
		if (enb == null || !enb.isRegistering()) {
			// 基站不是注册中状态，退出流程
			debug("enb is not in registering state, exit.");
			return false;
		}
		boolean versionCompatible = false;
		EnbAppMessage response = null;
		EnbRegisterResponse respContent = null;
		while (num <= manager.getRetryNum()) {
			try {
				// 发送注册请求
				debug("send register request to enb.");
				try {
					response = connector.syncInvoke(request);
				} catch (TimeoutException e) {
					// 如果应答超时，则继续执行
					num++;
					continue;
				}
				// 分析注册应答结果
				respContent = new EnbRegisterResponse(response);
				String receivedVersion = respContent.getSortwareVersion()
						.trim();
				// 无论版本是否兼容都更新动态的软件版本号
				enb.setSoftwareVersion(receivedVersion);
				// 判断版本是否兼容
				versionCompatible = EnbBizHelper
						.isVersionSupported(enb.getEnbType(), receivedVersion);
				// 根据版本是否支持生成告警或告警恢复
				createVersionIncompatibleAlarm(enb, versionCompatible);
				if (versionCompatible) {
					// 原协议版本号
					String protocolVersion = enb.getProtocolVersion();
					// 基站上报的协议版本号
					String newProtocolVersion = EnbBizHelper
							.getProtocolVersion(receivedVersion);

					// 原协议版本号和基站上报的协议版本号不一致,需要将基站配置数据按照新版本号格式进行转换
					if (!protocolVersion.equals(newProtocolVersion)) {
						// 转换数据成功才设置为新版本号，否则直接报错
						try {
							convertBizData(enb, newProtocolVersion);
						} catch (Exception e) {
							// TODO: 生成告警
							log.error("convert biz data failed.", e);
							error("convert biz data failed.", e);
						}
						// FIXME: 当转换失败时，不能更新协议版本号，且需要回滚
						//版本兼容时才更新协议版本号
						enb.setProtocolVersion(newProtocolVersion);
						updateEnbBasicInfo(enb);
					}
				}
				// 将基站状态修改为已连接
				debug("change enb state to connected.");
				EnbStatusChangeHelper.setConnected(enb);
				break;
			} catch (Exception e) {
				// 发生异常，将基站状态设置为未连接
				EnbStatusChangeHelper.setDisconnected(enb);
				error("register error", e);
				return false;
			} finally {

			}
		}
		if (!enb.isConnected()) {
			// 注册过程结束仍未连接，则将基站状态设置为未连接
			EnbStatusChangeHelper.setDisconnected(enb);
			return false;
		}
		// 版本兼容才进行数据同步、告警同步等操作
		if (versionCompatible) {
			// 如果eNB的数据未加载，还需要执行整表配置操作
			boolean dataLoaded = respContent.isDataLoaded();
			if (!dataLoaded) {
				loadFullDataToNe(enb);
			}
			// 否则执行比较并进行数据同步
			else {
				compareAndSyncData(enb);
			}
			// // 自学习，自学习成功则进行整表配置或进行数据同步
			// studyDataConfig(enb, dataLoaded);
			// 同步eNB告警
			syncEnbAlarm(enb.getMoId());
		}
		// 基站注册时，如果基站处于监控状态， 则下发开始命令，如果未监控，则下发停止命令
		try {
			// 基站从2.1.4版本开始支持实时性能监控
			boolean supportMonitor = compareVersion(enb.getProtocolVersion(),
					"2.1.4") >= 0;
			if (supportMonitor) {
				EnbRealtimeMonitorService monitorService = OmpAppContext
						.getCtx().getBean(EnbRealtimeMonitorService.class);
				if (enb.isMonitoring()) {
					monitorService.startMonitor("", enb.getMoId());
				} else {
					monitorService.stopMonitor("", enb.getMoId());
				}
			}
		} catch (Exception e) {
			String action = enb.isMonitoring() ? "start" : "stop";
			error("send " + action + " monitor request failed.", e);
		}

		// 注册任务结束
		debug("register task finished.");
		return true;
	}

	/**
	 * 将基站基本信息更新到数据库和缓存
	 * 
	 * @param enb
	 * @throws Exception
	 */
	private void updateEnbBasicInfo(Enb enb) throws Exception {
		EnbBasicService enbBasicService = OmpAppContext.getCtx().getBean(
				EnbBasicService.class);
		enbBasicService.modify(enb);
	}

	/**
	 * 按照新版本号格式对基站配置数据进行转换
	 * 
	 * @param enb
	 * @param targetVersion
	 * @throws Exception
	 */
	private void convertBizData(Enb enb, String targetVersion) throws Exception {
		EnbBizDataConvertor enbBizDataConvertor = OmpAppContext.getCtx()
				.getBean(EnbBizDataConvertor.class);
		enbBizDataConvertor.convert(enb.getMoId(), targetVersion);
	}

	/**
	 * 自学习
	 * 
	 * @throws Exception
	 */
	private void studyDataConfig(Enb enb, boolean dataLoaded) throws Exception {
		String version = enb.getSoftwareVersion();
		// 判断当前基站版本是否支持自学习
		boolean compatible = compareVersion(enb.getSoftwareVersion(),
				compatibleMinVersion) >= 0;
		// 支持自学习的版本才下发自学习命令
		// 支持自学习的基站才进行数据配置
		if (compatible) {
			try {
				// 相同版本需要做同步
				synchronized (version) {
					boolean configExist = EnbStudyDataConfigCache.getInstance()
							.isConfigExist(version);
					// 如果数据配置不存在则先学习再下发配置数据
					if (!configExist) {
						EnbExtBizService enbExtBizService = OmpAppContext
								.getCtx().getBean(EnbExtBizService.class);
						enbExtBizService.studyEnbDataConfig(enb.getMoId(),
								false);
					}
				}
				// 如果eNB的数据未加载，还需要执行整表配置操作
				if (!dataLoaded) {
					loadFullDataToNe(enb);
				}
				// 否则执行比较并进行数据同步
				else {
					compareAndSyncData(enb);
				}
				// 自学习成功，自学习失败告警恢复
				createStudyAlarm(enb, false);
			} catch (Exception e) {
				error("study error. version=" + enb.getSoftwareVersion(), e);
				// 如果自学习失败，生成自学习失败告警
				createStudyAlarm(enb, true);
			}
		}
		// 如果不支持则生成告警，如果支持则告警恢复
		createVersionIncompatibleAlarm(enb, compatible);
	}

	/**
	 * 同步eNB告警
	 * 
	 * @param enbId
	 */
	private void syncEnbAlarm(Long moId) {
		try {
			debug("sync enb alarm.");
			EnbAlarmService enbAlarmService = OmpAppContext.getCtx().getBean(
					EnbAlarmService.class);
			enbAlarmService.syncAlarm(moId);
			debug("sync enb alarm finished.");
		} catch (Exception e) {
			error("failed to sync enb alarm.", e);
		}
	}

	/**
	 * 完成注册任务后的清理工作
	 * 
	 */
	private void finishWork() {
		manager.removeTask(enbId);
	}

	/**
	 * 执行整表配置操作
	 * 
	 * @param mcBts
	 */
	private void loadFullDataToNe(Enb enb) {
		try {
			debug("load data to enb.");
			// 执行整表配置操作
			EnbFullTableConfigService enbFullTableConfigService = OmpAppContext
					.getCtx().getBean(EnbFullTableConfigService.class);
			enbFullTableConfigService.config(enb.getMoId());
			debug("load data to enb successfully.");
		} catch (Exception e) {
			error("failed to load data to enb.", e);
		}
	}

	/**
	 * 比较并同步数据
	 * 
	 * @param enb
	 */
	private void compareAndSyncData(Enb enb) {
		try {
			EnbBizConfigService enbBizConfigService = OmpAppContext.getCtx()
					.getBean(EnbBizConfigService.class);
			// 网管到基站
			if (enb.getSyncDirection() == Enb.SYNC_DIRECTION_EMS_TO_NE) {
				debug("compare and synchronize ems data to enb.");
				// 执行操作
				enbBizConfigService.compareAndSyncEmsDataToNe(enb.getMoId());
				debug("compare and synchronize ems data to enb successfully.");
			} else {
				// 基站到网管
				debug("compare and synchronize enb data to ems.");
				// 执行操作
				enbBizConfigService.compareAndSyncNeDataToEms(enb.getMoId());
				debug("compare and synchronize enb data to ems successfully.");
			}
			// 数据同步告警恢复
			createDataSyncAlarm(enb, false);
		} catch (Exception e) {
			error("failed to compare and synchronize data.", e);
			// 数据同步告警
			createDataSyncAlarm(enb, true);
		}
	}

	/**
	 * 创建数据同步告警
	 * 
	 * @param enb
	 * @param failed
	 *            同步是否失败
	 */
	private void createDataSyncAlarm(Enb enb, boolean failed) {
		EnbAlarmHelper enbAlarmHelper = OmpAppContext.getCtx().getBean(
				EnbAlarmHelper.class);
		if (failed) {
			enbAlarmHelper.fireDataSyncFailedAlarm(enb);
		} else {
			enbAlarmHelper.fireDataSyncFailedAlarmRestored(enb);
		}
	}

	/**
	 * 创建自学习失败告警
	 * 
	 * @param enb
	 * @param failed
	 *            自学习是否失败
	 */
	private void createStudyAlarm(Enb enb, boolean failed) {
		EnbAlarmHelper enbAlarmHelper = OmpAppContext.getCtx().getBean(
				EnbAlarmHelper.class);
		if (failed) {
			enbAlarmHelper.fireStudyFailedAlarm(enb);
		} else {
			enbAlarmHelper.fireStudyFailedAlarmRestored(enb);
		}
	}

	/**
	 * 创建基站版本不兼容告警
	 * 
	 * @param enb
	 * @param compatible
	 *            是否兼容
	 */
	private void createVersionIncompatibleAlarm(Enb enb, boolean compatible) {
		EnbAlarmHelper enbAlarmHelper = OmpAppContext.getCtx().getBean(
				EnbAlarmHelper.class);
		if (compatible) {
			enbAlarmHelper.fireEnbVersionIncompatibleAlarmRestored(enb);
		} else {
			enbAlarmHelper.fireEnbVersionIncompatibleAlarm(enb);
		}
	}

	/**
	 * 构造注册请求消息
	 * 
	 * @return
	 */
	private EnbAppMessage buildRegisterRequestMessage() {
		EnbRegisterRequest req = new EnbRegisterRequest(enbId);
		return req.toEnbAppMessage();
	}

	/**
	 * 比较版本号大小
	 * 
	 * @param ver1
	 * @param ver2
	 * @return
	 */
	private int compareVersion(String ver1, String ver2) {

		if (ver1.equals("") || ver2.equals("")) {
			return -1;
		}

		ver1 = ver1.trim();
		ver2 = ver2.trim();

		String[] v1 = ver1.split("\\.");
		String[] v2 = ver2.split("\\.");

		for (int i = 0; i < v1.length; i++) {
			String num1 = v1[i];
			String num2 = v2[i];
			int result = num1.compareTo(num2);
			if (result < 0) {
				return -1;
			}
			if (result > 0) {
				return 1;
			}
		}
		return 0;
	}

	private void debug(String message) {
		EnbUtils.log(enbId, "Register", message);
	}

	private void error(String message, Exception e) {
		EnbUtils.log(enbId, "Register", message + ": " + e);
	}

}
