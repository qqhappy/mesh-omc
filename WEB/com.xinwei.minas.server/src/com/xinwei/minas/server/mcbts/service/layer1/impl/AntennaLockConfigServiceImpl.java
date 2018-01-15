/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer1.McBtsAntennaLock;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.server.core.conf.service.MoCache;
import com.xinwei.minas.server.mcbts.dao.layer1.McbtsAntennaLockDAO;
import com.xinwei.minas.server.mcbts.proxy.layer1.AntennaLockConfigProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer1.AntennaLockConfigService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 天线闭锁配置服务实现类
 * 
 * @author fanhaoyu
 * 
 */

public class AntennaLockConfigServiceImpl implements AntennaLockConfigService {

	private static Log log = LogFactory
			.getLog(AntennaLockConfigServiceImpl.class);

	private McbtsAntennaLockDAO dao;

	private AntennaLockConfigProxy proxy;

	@Override
	public McBtsAntennaLock queryByMoId(Long moId) throws Exception {
		return dao.queryByMoId(moId);
	}

	@Override
	public void config(McBtsAntennaLock lockConfig) throws Exception {

		long moId = lockConfig.getMoId();
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}

		// 在线管理状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 检查BTS的工作状态是否为抗干扰跳频状态
			if (bts != null && bts.isAntijamming()) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_jumping_hold"));
			}
			try {
				proxy.config(lockConfig);
			} catch (Exception e) {
				log.error("config AntennalLock failed", e);
				throw e;
			}
		}
		dao.saveOrUpdate(lockConfig);
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		McBtsAntennaLock lockConfig = this.queryByMoId(moId);
		if (lockConfig != null) {
			this.config(lockConfig);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		// TODO Auto-generated method stub

	}

	public McbtsAntennaLockDAO getDao() {
		return dao;
	}

	public void setDao(McbtsAntennaLockDAO dao) {
		this.dao = dao;
	}

	public AntennaLockConfigProxy getProxy() {
		return proxy;
	}

	public void setProxy(AntennaLockConfigProxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		// 不需要实现,群配业务

	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// 不需要实现,群配业务
	}

}
