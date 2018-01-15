/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-3	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.oamManage.impl;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wutka.jox.JOXBeanInputStream;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.oamManage.BtsMeta;
import com.xinwei.minas.mcbts.core.model.oamManage.ConfigBizName;
import com.xinwei.minas.mcbts.core.model.oamManage.ConfigServiceName;
import com.xinwei.minas.mcbts.core.model.oamManage.SyncConfigBizCollection;
import com.xinwei.minas.server.mcbts.net.McBtsStudyService;
import com.xinwei.minas.server.mcbts.service.ICustomService;
import com.xinwei.minas.server.mcbts.service.McBtsBasicService;
import com.xinwei.minas.server.mcbts.service.McBtsBizService;
import com.xinwei.minas.server.mcbts.service.oamManage.McBtsSynConfigService;
import com.xinwei.minas.server.mcbts.utils.McBtsUtils;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * ͬ������serviceʵ����
 * 
 * @author chenshaohua
 * 
 */

public class McBtsSynConfigServiceImpl implements McBtsSynConfigService {

	Log log = LogFactory.getLog(McBtsSynConfigServiceImpl.class);

	private McBtsStudyService mcBtsStudyService;

	private McBtsBizService mcBtsBizService;

	private McBtsBasicService mcBtsBasicService;

	private static SyncConfigBizCollection collection;

	private Map<String, String> serviceNameMap = new HashMap<String, String>();

	// ��Ϣ���ͼ��(��λ:����)
	private int interval = 50;

	public McBtsSynConfigServiceImpl() {
		super();
		try {
			collection = getSupportedServiceAndBizName();
		} catch (Exception e) {
			log.error(e);
		}
	}

	/**
	 * ͬ�����÷���
	 * 
	 */
	@Override
	public List<String> config(Integer restudy, Long moId) throws Exception {
		McBts mcBts = mcBtsBasicService.queryByMoId(moId);
		if (mcBts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		if (!mcBts.isOnlineManage()) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_is_offline_manage"));
		}
		if (!mcBts.isConnected()) {
			throw new Exception(OmpAppContext.getMessage("bts_unconnected"));
		}
		// �жϻ�վ�Ƿ��Ѿ�����
		// ��Ҫ��ѧϰ
		if (restudy == McBtsSynConfigService.NEED_RESTUDY) {
			// �ȸ���btsType��version������ݿ�ͻ���
			mcBtsStudyService.clearSupportedOperation(mcBts.getBtsType(),
					mcBts.getSoftwareVersion());
		}
		List<String> failMsg = new ArrayList<String>();

		// �Զ���serviceͬ������
		List<ICustomService> customServiceList = this
				.getSupportedServiceList(mcBts);
		if (!customServiceList.isEmpty()) {
			for (ICustomService customService : customServiceList) {
				try {
					McBtsUtils.log(mcBts.getBtsId(), "Sync Config",
							"Sync from ems to ne, service: "
									+ customService.getClass().getName());
					// ����ͬ���ӿ�
					customService.syncFromEMSToNE(moId);
					TimeUnit.MILLISECONDS.sleep(interval);
				} catch (UnsupportedOperationException e) {
					String currentImpl = customService.getClass()
							.getSimpleName();
					McBtsUtils.log(
							mcBts.getBtsId(),
							"Sync Config",
							"Current mcbts version does not support the operation: "
									+ currentImpl + ",reason:"
									+ e.getLocalizedMessage());
				} catch (Exception e) {
					log.error(e);
					String currentImpl = customService.getClass().getName()
							.toLowerCase();
					int lastIndex = currentImpl.lastIndexOf(".");
					currentImpl = currentImpl.substring(lastIndex + 1,
							currentImpl.length());

					Class<?>[] interfaces = customService.getClass()
							.getInterfaces();
					for (Class<?> c : interfaces) {
						String interfaceName = c.getSimpleName().toLowerCase();
						if (currentImpl.contains(interfaceName)) {
							String bizName = serviceNameMap.get(interfaceName);
							StringBuilder msgBuilder = new StringBuilder();
							msgBuilder.append(bizName).append("  ")
									.append(e.getLocalizedMessage());
							failMsg.add(msgBuilder.toString());
							log.error("failed to sync from ems to ne, biz="
									+ bizName, e);
						}
					}

				}
			}
		}

		// ͨ��serviceͬ������
		List<String> bizNameList = this.getSupportedBizNameList(mcBts);
		if (!bizNameList.isEmpty()) {
			for (String bizName : bizNameList) {
				try {
					McBtsUtils.log(mcBts.getBtsId(), "Sync Config",
							"Sync from ems to ne, bizName: " + bizName);
					// ��ѯ��������
					GenericBizData genericBizData = mcBtsBizService.queryAllBy(
							moId, bizName);
					// ����Ԫ��������ָ��
					mcBtsBizService.sendCommand(moId, genericBizData);
					TimeUnit.MILLISECONDS.sleep(interval);
				} catch (UnsupportedOperationException e) {
					McBtsUtils.log(mcBts.getBtsId(), "Sync Config",
							"Current mcbts version does not support the operation: "
									+ bizName);
				} catch (Exception e) {
					log.error("failed to sync from ems to ne, biz=" + bizName,
							e);
					StringBuilder msgBuilder = new StringBuilder();
					msgBuilder.append(serviceNameMap.get(bizName)).append("  ")
							.append(e.getLocalizedMessage());
					failMsg.add(msgBuilder.toString());
				}
			}
		}

		return failMsg;
	}

	@Override
	public List<String> syncFromNEToEMS(Long moId) throws Exception {
		McBts mcBts = mcBtsBasicService.queryByMoId(moId);
		if (mcBts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		if (!mcBts.isOnlineManage()) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_is_offline_manage"));
		}
		if (!mcBts.isConnected()) {
			throw new Exception(OmpAppContext.getMessage("bts_unconnected"));
		}
		List<String> failMsg = new ArrayList<String>();

		// �Զ���serviceͬ������
		List<ICustomService> customServiceList = this
				.getSupportedServiceList(mcBts);
		if (!customServiceList.isEmpty()) {
			for (ICustomService customService : customServiceList) {
				try {
					McBtsUtils.log(mcBts.getBtsId(), "Sync Config",
							"Sync ne data to database, service: "
									+ customService.getClass().getName());

					customService.syncFromNEToEMS(moId);
					TimeUnit.MILLISECONDS.sleep(interval);
				} catch (UnsupportedOperationException e) {
					String currentImpl = customService.getClass()
							.getSimpleName();
					McBtsUtils.log(mcBts.getBtsId(), "Sync Config",
							"Current mcbts version does not support the operation: "
									+ currentImpl);
				} catch (Exception e) {
					String currentImpl = customService.getClass().getName()
							.toLowerCase();
					Class<?>[] interfaces = customService.getClass()
							.getInterfaces();
					for (Class<?> c : interfaces) {
						String interfaceName = c.getSimpleName().toLowerCase();
						if (currentImpl.contains(interfaceName)) {
							String bizName = serviceNameMap.get(interfaceName);
							StringBuilder msgBuilder = new StringBuilder();
							msgBuilder.append(bizName).append("  ")
									.append(e.getLocalizedMessage());
							failMsg.add(msgBuilder.toString());
							log.error("failed to sync from ne to ems, biz="
									+ bizName, e);
						}
					}
				}
			}
		}

		// ͨ��serviceͬ������
		List<String> bizNameList = this.getSupportedBizNameList(mcBts);
		if (!bizNameList.isEmpty()) {
			for (String bizName : bizNameList) {
				try {
					// �ݲ�֧��С��վ���߱�����ѯ����
					if (bizName.equals("mcbts_antenna_lock")) {
						throw new UnsupportedOperationException(
								OmpAppContext
										.getMessage("unsupported_biz_operation"));
					}
					GenericBizData data = new GenericBizData(bizName);
					// ����Ԫ��ȡ����
					GenericBizData genericBizData = mcBtsBizService
							.queryFromNE(moId, data);

					McBtsUtils.log(mcBts.getBtsId(), "Sync Config",
							"Sync ne data to database, table: " + bizName);

					// �������ݿ�
					mcBtsBizService.saveToDB(moId, genericBizData);
					TimeUnit.MILLISECONDS.sleep(interval);
				} catch (UnsupportedOperationException e) {
					McBtsUtils.log(mcBts.getBtsId(), "Sync Config",
							"Current mcbts version does not support the operation: "
									+ bizName);
				} catch (Exception e) {
					log.error("failed to sync from ne to ems, biz=" + bizName,
							e);
					StringBuilder msgBuilder = new StringBuilder();
					msgBuilder.append(serviceNameMap.get(bizName)).append("  ")
							.append(e.getLocalizedMessage());
					failMsg.add(msgBuilder.toString());
				}
			}
		}

		return failMsg;

	}

	/**
	 * ��ȡservice�б�
	 * 
	 * @param mcBts
	 * @return
	 * @throws Exception
	 */
	private List<ICustomService> getSupportedServiceList(McBts mcBts)
			throws Exception {
		List<ICustomService> serviceList = new ArrayList<ICustomService>();

		BtsMeta[] btsMetaArr = collection.getBtsMeta();

		for (int i = 0; i < btsMetaArr.length; i++) {
			if (btsMetaArr[i].getType() == mcBts.getBtsType()) {
				ConfigServiceName[] serviceNameArr = btsMetaArr[i].getCustom()
						.getConfigServiceName();
				if (serviceNameArr == null)
					continue;

				for (ConfigServiceName service : serviceNameArr) {
					ICustomService customService = (ICustomService) AppContext
							.getCtx().getBean(service.getName());
					serviceList.add(customService);
//					serviceNameMap.put(service.getName().toLowerCase(),
//							service.getRemark());
					serviceNameMap.put(service.getName().toLowerCase(),
							OmpAppContext.getMessage(service.getDesc()));
				}
			}
		}
		return serviceList;
	}

	/**
	 * ��ȡbizName�б�
	 * 
	 * @param mcBts
	 * @return
	 * @throws Exception
	 */
	private List<String> getSupportedBizNameList(McBts mcBts) throws Exception {
		List<String> bizNameList = new ArrayList<String>();
		BtsMeta[] btsMetaArr = collection.getBtsMeta();
		for (int i = 0; i < btsMetaArr.length; i++) {
			if (btsMetaArr[i].getType() == mcBts.getBtsType()) {
				ConfigBizName[] bizNameArr = btsMetaArr[i].getUniversal()
						.getConfigBizName();
				if (bizNameArr != null) {
					for (ConfigBizName bizName : bizNameArr) {
						bizNameList.add(bizName.getName());
//						serviceNameMap.put(bizName.getName(),
//								bizName.getRemark());
						serviceNameMap.put(bizName.getName(),
								OmpAppContext.getMessage(bizName.getDesc()));
					}
				}

			}
		}
		return bizNameList;
	}

	/**
	 * ��ȡ֧�ֵ�service��bizName
	 * 
	 * @return
	 * @throws Exception
	 */
	public SyncConfigBizCollection getSupportedServiceAndBizName()
			throws Exception {
		String fileName = "./plugins/mcbts/syncConfig/synConfigBiz.xml";
		JOXBeanInputStream joxIn = null;
		try {
			FileInputStream inputStream = new FileInputStream(fileName);
			joxIn = new JOXBeanInputStream(inputStream);
			SyncConfigBizCollection supportedBizCollection = (SyncConfigBizCollection) joxIn
					.readObject(SyncConfigBizCollection.class);

			return supportedBizCollection;
		} catch (Exception e) {
			throw e;
		} finally {
			if (joxIn != null) {
				joxIn.close();
			}
		}
	}

	public McBtsStudyService getMcBtsStudyService() {
		return mcBtsStudyService;
	}

	public void setMcBtsStudyService(McBtsStudyService mcBtsStudyService) {
		this.mcBtsStudyService = mcBtsStudyService;
	}

	public McBtsBizService getMcBtsBizService() {
		return mcBtsBizService;
	}

	public void setMcBtsBizService(McBtsBizService mcBtsBizService) {
		this.mcBtsBizService = mcBtsBizService;
	}

	public McBtsBasicService getMcBtsBasicService() {
		return mcBtsBasicService;
	}

	public void setMcBtsBasicService(McBtsBasicService mcBtsBasicService) {
		this.mcBtsBasicService = mcBtsBasicService;
	}

}
