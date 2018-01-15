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
 * eNBע������
 * 
 * @author chenjunhua
 * 
 */

public class EnbRegisterTask implements Runnable {

	private static final Log log = LogFactory.getLog(EnbRegisterTask.class);

	private Long enbId;

	private EnbRegisterTaskManager manager;

	/**
	 * ʵ����ѧϰ��eNB��С�汾��
	 */
	private String compatibleMinVersion = "2.0.4.0";

	// ִ�д���
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
	 * ִ��ע������
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
			// ��վ����ע����״̬���˳�����
			debug("enb is not in registering state, exit.");
			return false;
		}
		boolean versionCompatible = false;
		EnbAppMessage response = null;
		EnbRegisterResponse respContent = null;
		while (num <= manager.getRetryNum()) {
			try {
				// ����ע������
				debug("send register request to enb.");
				try {
					response = connector.syncInvoke(request);
				} catch (TimeoutException e) {
					// ���Ӧ��ʱ�������ִ��
					num++;
					continue;
				}
				// ����ע��Ӧ����
				respContent = new EnbRegisterResponse(response);
				String receivedVersion = respContent.getSortwareVersion()
						.trim();
				// ���۰汾�Ƿ���ݶ����¶�̬������汾��
				enb.setSoftwareVersion(receivedVersion);
				// �жϰ汾�Ƿ����
				versionCompatible = EnbBizHelper
						.isVersionSupported(enb.getEnbType(), receivedVersion);
				// ���ݰ汾�Ƿ�֧�����ɸ澯��澯�ָ�
				createVersionIncompatibleAlarm(enb, versionCompatible);
				if (versionCompatible) {
					// ԭЭ��汾��
					String protocolVersion = enb.getProtocolVersion();
					// ��վ�ϱ���Э��汾��
					String newProtocolVersion = EnbBizHelper
							.getProtocolVersion(receivedVersion);

					// ԭЭ��汾�źͻ�վ�ϱ���Э��汾�Ų�һ��,��Ҫ����վ�������ݰ����°汾�Ÿ�ʽ����ת��
					if (!protocolVersion.equals(newProtocolVersion)) {
						// ת�����ݳɹ�������Ϊ�°汾�ţ�����ֱ�ӱ���
						try {
							convertBizData(enb, newProtocolVersion);
						} catch (Exception e) {
							// TODO: ���ɸ澯
							log.error("convert biz data failed.", e);
							error("convert biz data failed.", e);
						}
						// FIXME: ��ת��ʧ��ʱ�����ܸ���Э��汾�ţ�����Ҫ�ع�
						//�汾����ʱ�Ÿ���Э��汾��
						enb.setProtocolVersion(newProtocolVersion);
						updateEnbBasicInfo(enb);
					}
				}
				// ����վ״̬�޸�Ϊ������
				debug("change enb state to connected.");
				EnbStatusChangeHelper.setConnected(enb);
				break;
			} catch (Exception e) {
				// �����쳣������վ״̬����Ϊδ����
				EnbStatusChangeHelper.setDisconnected(enb);
				error("register error", e);
				return false;
			} finally {

			}
		}
		if (!enb.isConnected()) {
			// ע����̽�����δ���ӣ��򽫻�վ״̬����Ϊδ����
			EnbStatusChangeHelper.setDisconnected(enb);
			return false;
		}
		// �汾���ݲŽ�������ͬ�����澯ͬ���Ȳ���
		if (versionCompatible) {
			// ���eNB������δ���أ�����Ҫִ���������ò���
			boolean dataLoaded = respContent.isDataLoaded();
			if (!dataLoaded) {
				loadFullDataToNe(enb);
			}
			// ����ִ�бȽϲ���������ͬ��
			else {
				compareAndSyncData(enb);
			}
			// // ��ѧϰ����ѧϰ�ɹ�������������û��������ͬ��
			// studyDataConfig(enb, dataLoaded);
			// ͬ��eNB�澯
			syncEnbAlarm(enb.getMoId());
		}
		// ��վע��ʱ�������վ���ڼ��״̬�� ���·���ʼ������δ��أ����·�ֹͣ����
		try {
			// ��վ��2.1.4�汾��ʼ֧��ʵʱ���ܼ��
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

		// ע���������
		debug("register task finished.");
		return true;
	}

	/**
	 * ����վ������Ϣ���µ����ݿ�ͻ���
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
	 * �����°汾�Ÿ�ʽ�Ի�վ�������ݽ���ת��
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
	 * ��ѧϰ
	 * 
	 * @throws Exception
	 */
	private void studyDataConfig(Enb enb, boolean dataLoaded) throws Exception {
		String version = enb.getSoftwareVersion();
		// �жϵ�ǰ��վ�汾�Ƿ�֧����ѧϰ
		boolean compatible = compareVersion(enb.getSoftwareVersion(),
				compatibleMinVersion) >= 0;
		// ֧����ѧϰ�İ汾���·���ѧϰ����
		// ֧����ѧϰ�Ļ�վ�Ž�����������
		if (compatible) {
			try {
				// ��ͬ�汾��Ҫ��ͬ��
				synchronized (version) {
					boolean configExist = EnbStudyDataConfigCache.getInstance()
							.isConfigExist(version);
					// ����������ò���������ѧϰ���·���������
					if (!configExist) {
						EnbExtBizService enbExtBizService = OmpAppContext
								.getCtx().getBean(EnbExtBizService.class);
						enbExtBizService.studyEnbDataConfig(enb.getMoId(),
								false);
					}
				}
				// ���eNB������δ���أ�����Ҫִ���������ò���
				if (!dataLoaded) {
					loadFullDataToNe(enb);
				}
				// ����ִ�бȽϲ���������ͬ��
				else {
					compareAndSyncData(enb);
				}
				// ��ѧϰ�ɹ�����ѧϰʧ�ܸ澯�ָ�
				createStudyAlarm(enb, false);
			} catch (Exception e) {
				error("study error. version=" + enb.getSoftwareVersion(), e);
				// �����ѧϰʧ�ܣ�������ѧϰʧ�ܸ澯
				createStudyAlarm(enb, true);
			}
		}
		// �����֧�������ɸ澯�����֧����澯�ָ�
		createVersionIncompatibleAlarm(enb, compatible);
	}

	/**
	 * ͬ��eNB�澯
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
	 * ���ע��������������
	 * 
	 */
	private void finishWork() {
		manager.removeTask(enbId);
	}

	/**
	 * ִ���������ò���
	 * 
	 * @param mcBts
	 */
	private void loadFullDataToNe(Enb enb) {
		try {
			debug("load data to enb.");
			// ִ���������ò���
			EnbFullTableConfigService enbFullTableConfigService = OmpAppContext
					.getCtx().getBean(EnbFullTableConfigService.class);
			enbFullTableConfigService.config(enb.getMoId());
			debug("load data to enb successfully.");
		} catch (Exception e) {
			error("failed to load data to enb.", e);
		}
	}

	/**
	 * �Ƚϲ�ͬ������
	 * 
	 * @param enb
	 */
	private void compareAndSyncData(Enb enb) {
		try {
			EnbBizConfigService enbBizConfigService = OmpAppContext.getCtx()
					.getBean(EnbBizConfigService.class);
			// ���ܵ���վ
			if (enb.getSyncDirection() == Enb.SYNC_DIRECTION_EMS_TO_NE) {
				debug("compare and synchronize ems data to enb.");
				// ִ�в���
				enbBizConfigService.compareAndSyncEmsDataToNe(enb.getMoId());
				debug("compare and synchronize ems data to enb successfully.");
			} else {
				// ��վ������
				debug("compare and synchronize enb data to ems.");
				// ִ�в���
				enbBizConfigService.compareAndSyncNeDataToEms(enb.getMoId());
				debug("compare and synchronize enb data to ems successfully.");
			}
			// ����ͬ���澯�ָ�
			createDataSyncAlarm(enb, false);
		} catch (Exception e) {
			error("failed to compare and synchronize data.", e);
			// ����ͬ���澯
			createDataSyncAlarm(enb, true);
		}
	}

	/**
	 * ��������ͬ���澯
	 * 
	 * @param enb
	 * @param failed
	 *            ͬ���Ƿ�ʧ��
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
	 * ������ѧϰʧ�ܸ澯
	 * 
	 * @param enb
	 * @param failed
	 *            ��ѧϰ�Ƿ�ʧ��
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
	 * ������վ�汾�����ݸ澯
	 * 
	 * @param enb
	 * @param compatible
	 *            �Ƿ����
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
	 * ����ע��������Ϣ
	 * 
	 * @return
	 */
	private EnbAppMessage buildRegisterRequestMessage() {
		EnbRegisterRequest req = new EnbRegisterRequest(enbId);
		return req.toEnbAppMessage();
	}

	/**
	 * �Ƚϰ汾�Ŵ�С
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
