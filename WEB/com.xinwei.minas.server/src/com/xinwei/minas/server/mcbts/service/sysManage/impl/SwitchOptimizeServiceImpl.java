/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.SystemProperty;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.SwitchOptimizeConfig;
import com.xinwei.minas.server.core.conf.service.SystemPropertyService;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.sysManage.SwitchOptimizeService;
import com.xinwei.minas.server.mcbts.utils.McBtsUtils;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * �л��Ż��������÷���ʵ��
 * 
 * @author fanhaoyu
 * 
 */

public class SwitchOptimizeServiceImpl implements SwitchOptimizeService {

	private static Log log = LogFactory.getLog(SwitchOptimizeServiceImpl.class);

	public static final String SWITCHOPT_BIZ_NAME = "mcbts_switchoptimize";

	public static final String CATEGORY = "platform";

	private McBtsBizProxy mcBtsBizProxy;

	private SystemPropertyService systemPropertyService;

	@Override
	public void config(Long moId, SwitchOptimizeConfig config)
			throws RemoteException, Exception {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// ������״̬�£���Ҫͨ��Proxy��MO����������Ϣ
		if (bts != null && bts.isConfigurable()) {
			GenericBizData data = new GenericBizData(SWITCHOPT_BIZ_NAME);
			data.addEntity(config);
			try {
				mcBtsBizProxy.config(moId, data);
			} catch (Exception e) {
				log.error(e);
				// ��֧�ֵ�ҵ��������
				if (!(e instanceof UnsupportedOperationException)) {
					throw new Exception(e.getLocalizedMessage());
				}
			}
		} else {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_is_offline_manage"));
		}
	}

	@Override
	public void configAll(SwitchOptimizeConfig config) throws RemoteException,
			Exception {
		GenericBizData data = new GenericBizData(SWITCHOPT_BIZ_NAME);
		data.addEntity(config);
		// ���
		try {
			systemPropertyService.setProperty(CATEGORY, null,
					SWITCHOPT_BIZ_NAME, config.getSwitchFlag().toString());
		} catch (Exception e) {
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}
		// �����Ҫ�����л�վ�·�
		if (config.isNeedConfig()) {
			// ʧ�ܵĻ�վ��ʧ����Ϣ
			Map<McBts, String> failedMap = new HashMap<McBts, String>();
			// ���������������ӻ�վ�·�
			List<McBts> btsList = McBtsCache.getInstance().queryAll();
			for (McBts mcBts : btsList) {
				if (mcBts.isConfigurable()) {
					try {
						mcBtsBizProxy.config(mcBts.getMoId(), data);
					} catch (Exception e) {
						if (e instanceof UnsupportedOperationException) {
							log.error(e.getLocalizedMessage());
						} else {
							failedMap.put(mcBts, e.getLocalizedMessage());
						}
					}
				}
			}
			// ����ʧ����Ϣ
			McBtsUtils.fireBatchUpdateException(failedMap);
		}
	}

	@Override
	public SwitchOptimizeConfig queryFromNE(Long moId) throws RemoteException,
			Exception {
		GenericBizData data = mcBtsBizProxy.query(moId, new GenericBizData(
				SWITCHOPT_BIZ_NAME));
		if (data.getRecords().isEmpty()) {
			return null;
		}
		SwitchOptimizeConfig config = new SwitchOptimizeConfig();
		data.getModel(config);
		return config;
	}

	@Override
	public List<SwitchOptimizeConfig> queryAll() {
		List<SwitchOptimizeConfig> configList = new ArrayList<SwitchOptimizeConfig>();
		SwitchOptimizeConfig config = new SwitchOptimizeConfig();
		SystemProperty property = systemPropertyService.getProperty(CATEGORY,
				null, SWITCHOPT_BIZ_NAME);
		if (property != null) {
			config.setSwitchFlag(Integer.valueOf(property.getValue()));
			configList.add(config);
		}
		return configList;
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		List<SwitchOptimizeConfig> config = this.queryAll();
		if (config == null || config.size() == 0) {
			return;
		}
		this.config(moId, config.get(0));
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		// do nothing
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	public void setSystemPropertyService(
			SystemPropertyService systemPropertyService) {
		this.systemPropertyService = systemPropertyService;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		// ����Ҫʵ��,Ⱥ��ҵ��

	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// ����Ҫʵ��,Ⱥ��ҵ��

	}
}
