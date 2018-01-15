/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| zhaolingling 	| 	create the file                       
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
import com.xinwei.minas.server.core.conf.service.SystemPropertyService;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.sysManage.PreventBroadcastService;
import com.xinwei.minas.server.mcbts.utils.McBtsUtils;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhaolingling
 * 
 */

public class PreventBroadcastServiceImpl  implements PreventBroadcastService{

	/**
	 * 
	 */
	private static Log log = LogFactory.getLog(PreventBroadcastServiceImpl.class);

	public static final String PREVENTBROADCAST_BIZ_NAME = "mcbts_preventbroadcast";

	public static final String CATEGORY = "platform";

	private McBtsBizProxy mcBtsBizProxy;

	private SystemPropertyService systemPropertyService;
	
	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		List<GenericBizData> config = queryAll();
		if (config == null || config.size() == 0) {
			return;
		}
		config(moId, config.get(0));
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		//do nothing
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		//do nothing
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		//do nothing
	}

	@Override
	public List<GenericBizData> queryAll() {
		List<GenericBizData> configList = new ArrayList<GenericBizData>();
		GenericBizData config = new GenericBizData(PREVENTBROADCAST_BIZ_NAME);
		SystemProperty property = systemPropertyService.getProperty(CATEGORY,
				null, "mcbts_preventbroadcast_flowControlSwitch");
		if (property != null) {
			GenericBizProperty gp = new GenericBizProperty();
			gp.setName("flowControlSwitch");
			gp.setValue(Integer.valueOf(property.getValue()));
			config.addProperty(gp);
			configList.add(config);
		}
		 property = systemPropertyService.getProperty(CATEGORY,
				null, "mcbts_preventbroadcast_MAXBroadcastPackets");
		if (property != null) {
			GenericBizProperty gp = new GenericBizProperty();
			gp.setName("MAXBroadcastPackets");
			gp.setValue(Long.valueOf(property.getValue()));
			config.addProperty(gp);
			configList.add(config);
		}
		
		return configList;
	}

	
	@Override
	public void config(Long moId, GenericBizData config)
			throws RemoteException, Exception {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts.isConfigurable()) {
			try {
				mcBtsBizProxy.config(moId, config);
			} catch (Exception e) {
				log.error(e.getLocalizedMessage());
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
	public void configAll(GenericBizData config) throws RemoteException,
			Exception {
		// 存库
		try {
			systemPropertyService.setProperty(CATEGORY, null,
					"mcbts_preventbroadcast_flowControlSwitch", config.getProperty("flowControlSwitch").getValue().toString());
			systemPropertyService.setProperty(CATEGORY, null,
					"mcbts_preventbroadcast_MAXBroadcastPackets", config.getProperty("MAXBroadcastPackets").getValue().toString());
		} catch (Exception e) {
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}

		// 失败的基站及失败信息
		Map<McBts, String> failedMap = new HashMap<McBts, String>();
		// 向所有在线已连接基站下发
		List<McBts> btsList = McBtsCache.getInstance().queryAll();
		for (McBts mcBts : btsList) {
			if (mcBts.isConfigurable()) {
				try {
					mcBtsBizProxy.config(mcBts.getMoId(), config);
				} catch (Exception e) {
					if (e instanceof UnsupportedOperationException) {
						log.error(e.getLocalizedMessage());
					} else {
						failedMap.put(mcBts, e.getLocalizedMessage());
					}
				}
			}
		}
		// 处理失败信息
		McBtsUtils.fireBatchUpdateException(failedMap);
	
		
	}

	@Override
	public GenericBizData queryFromNE(Long moId) throws RemoteException,
			Exception {
		GenericBizData data = mcBtsBizProxy.query(moId, new GenericBizData(
				PREVENTBROADCAST_BIZ_NAME));
		if (data.getRecords().isEmpty()) {
			return null;
		}
		GenericBizData config = new GenericBizData(PREVENTBROADCAST_BIZ_NAME);
		data.getModel(config);
		return config;
	}

	/**
	 * @param mcBtsBizProxy the mcBtsBizProxy to set
	 */
	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	/**
	 * @param systemPropertyService the systemPropertyService to set
	 */
	public void setSystemPropertyService(SystemPropertyService systemPropertyService) {
		this.systemPropertyService = systemPropertyService;
	}

}
