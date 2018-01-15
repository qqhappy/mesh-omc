/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-29	| jiayi 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.SystemProperty;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer2.AirlinkConfig;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.ChannelComparableMode;
import com.xinwei.minas.server.core.conf.service.SystemPropertyService;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer2.AirlinkService;
import com.xinwei.minas.server.mcbts.service.layer3.NeighborService;
import com.xinwei.minas.server.mcbts.service.sysManage.ChannelComparableModeService;
import com.xinwei.minas.server.mcbts.utils.McBtsUtils;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 基站兼容模式配置服务实现
 * 
 * @author jiayi
 * 
 */

public class ChannelComparableModeServiceImpl implements ChannelComparableModeService {

	private static Log log = LogFactory.getLog(ChannelComparableModeServiceImpl.class);

	public static final String COMPARABLEMODE_BIZ_NAME = "mcbts_channelcomparablemode";

	private McBtsBizProxy mcBtsBizProxy;

	private static final String CATEGORY = "platform";
	private static final String PROPERTY = "channel_comparable_mode";

	/**
	 * 配置指定基站的基站兼容模式
	 * 
	 * @param moId 基站的MO Id
	 * 
	 * @param config 基站兼容模式信息
	 * 
	 * @throws Exception
	 */
	public void config(long moId, ChannelComparableMode config)
			throws Exception {

		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		
		// 基站不存在
		if (mcBts == null) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}

		// 是否需要下发基站兼容模式
		if (config.isNeedConfig()) {

			// 配置前的基站兼容模式
			ChannelComparableMode oldMode = queryFromNE(moId);

			// 如果是从兼容模式1修改为其它兼容模式，需要清空基站的邻接表
			if (oldMode.getChannelMode() == ChannelComparableMode.PECCH_ONLY
					&& config.getChannelMode() != oldMode.getChannelMode()) {
				NeighborService neighborService = AppContext.getCtx().getBean(
						NeighborService.class);
				neighborService.config(moId, new LinkedList<McBts>(), new LinkedList<McBts>());
			}

			// 基站离线管理模式
			if (mcBts.isOfflineManage()) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_is_offline_manage"));
			}

			// 基站未连接
			if (mcBts.isDisconnected()) {
				throw new Exception(
						OmpAppContext.getMessage("bts_unconnected"));
			}
			
			GenericBizData data = new GenericBizData(COMPARABLEMODE_BIZ_NAME);
			data.addEntity(config);

			AirlinkService airlinkService = AppContext.getCtx().getBean(
					AirlinkService.class);
			
			try {
				mcBtsBizProxy.config(moId, data);

				//兼容模式被修改后
				// 加载基站类型对应的默认空中链路配置
				if (config.getChannelMode() != oldMode.getChannelMode()) {
					AirlinkConfig tempConfig = airlinkService
							.getDefaultConfigByComparableMode(mcBts, config);
					airlinkService.config(moId, tempConfig, false);
				} else {
					//兼容模式没有变化，将当前的空中链路信息配置给基站
					AirlinkConfig tempConfig = airlinkService.queryFromEMS(moId);
					airlinkService.config(moId, tempConfig, false);
				}
				
			} catch (Exception e) {
				log.error(e);
				// 不支持的业务不往上抛
				if (!(e instanceof UnsupportedOperationException)) {
					throw new Exception(e.getLocalizedMessage());
				}
			}
		}
	}

	/**
	 * 配置系统的基站兼容模式
	 * 
	 * @param config 基站兼容模式信息
	 * 
	 * @throws Exception
	 */
	public void config(ChannelComparableMode config) throws Exception {

		// 配置前的基站兼容模式
		ChannelComparableMode oldMode = queryFromEMS();

		// 要查询出所有的基站ID
		List<McBts> btsList = McBtsCache.getInstance().queryAll();

		// 如果是从兼容模式1修改为其它兼容模式，需要清空基站的邻接表
		if (oldMode.getChannelMode() == ChannelComparableMode.PECCH_ONLY
				&& config.getChannelMode() != oldMode.getChannelMode()) {

			NeighborService neighborService = AppContext.getCtx().getBean(
					NeighborService.class);

			// 清空所有基站的邻接表配置信息
			for (McBts mcbts : btsList) {
				try {
					neighborService.config(mcbts.getMoId(),
							new LinkedList<McBts>(), new LinkedList<McBts>());
				} catch (Exception e) {
				}
			}
		}

		// 失败的基站及失败信息
		Map<McBts, String> failedMap = new HashMap<McBts, String>();
		
		// 保存基站兼容模式
		SystemPropertyService systemPropertyService = AppContext.getCtx()
				.getBean(SystemPropertyService.class);

		systemPropertyService.setProperty(CATEGORY, null, PROPERTY,
				String.valueOf(config.getChannelMode()));

		// 是否需要下发基站兼容模式
		if (config.isNeedConfig()) {
			
			GenericBizData data = new GenericBizData(COMPARABLEMODE_BIZ_NAME);
			data.addEntity(config);

			AirlinkService airlinkService = AppContext.getCtx().getBean(
					AirlinkService.class);
			
			// 对每个可配置的基站下发基站兼容模式
			for (McBts mcbts : btsList) {

				// 判断基站是否是可配置的
				if (mcbts.isConfigurable()) {
					try {
						mcBtsBizProxy.config(mcbts.getMoId(), data);
					} catch (Exception e) {
						failedMap.put(mcbts, e.getLocalizedMessage());
					}
				}

				try {
					AirlinkConfig airConfig = null;
					// 如果是从兼容模式3修改为其它兼容模式，保留部分可用配置
					if (oldMode.getChannelMode() == ChannelComparableMode.PCCH_AND_PECCH
							&& config.getChannelMode() != ChannelComparableMode.PCCH_AND_PECCH) {
//						airConfig = airlinkService.queryByMoId(mcbts.getMoId());
						airConfig = airlinkService.queryFromEMS(mcbts.getMoId());
						if (config.getChannelMode() == ChannelComparableMode.PCCH_ONLY) {
							// 仅支持公共信道配置时，清空增强公共信道配置
							airConfig.setPecchSetting(null);
						} else if (config.getChannelMode() == ChannelComparableMode.PECCH_ONLY) {
							// 仅支持增强公共信道配置时，清空公共信道配置
							airConfig.getScgChannelConfigList().clear();
						}
					} else if (oldMode.getChannelMode() == config.getChannelMode()) { //兼容模式没有变化时，不需要回复到默认值
						airConfig = airlinkService.queryByMoId(mcbts.getMoId());
					}
					// 如果是从其他兼容模式修改为兼容模式3，保留可用配置
//					if (oldMode.getChannelMode() != ChannelComparableMode.PCCH_AND_PECCH
//							&& config.getChannelMode() == ChannelComparableMode.PCCH_AND_PECCH) {
//						airConfig = airlinkService.queryByMoId(mcbts.getMoId());
//						ChannelComparableMode newMode = new ChannelComparableMode();
//						if (oldMode.getChannelMode() == ChannelComparableMode.PCCH_ONLY) {
//							// 原来仅支持公共信道配置时，增加默认增强公共信道配置
//							newMode.setChannelMode(ChannelComparableMode.PECCH_ONLY);
//							AirlinkConfig tempConfig = airlinkService
//									.getDefaultConfigByComparableMode(mcbts,
//											newMode);
//							airConfig.setPecchSetting(tempConfig.getPecchSetting());
//						} else if (oldMode.getChannelMode() == ChannelComparableMode.PECCH_ONLY) {
//							// 原来仅支持增强公共信道配置时，增加默认公共信道配置 ???
//							newMode.setChannelMode(ChannelComparableMode.PCCH_ONLY);
//							AirlinkConfig tempConfig = airlinkService
//									.getDefaultConfigByComparableMode(mcbts,
//											newMode);
//							airConfig.setScgChannelConfigList(tempConfig
//									.getScgChannelConfigList());
//							airConfig.setScgScaleConfigList(tempConfig
//									.getScgScaleConfigList());
//						}
//					}
					// 根据兼容模式构造并下发基站空中链路默认配置
					if (airConfig == null) {
						airConfig = airlinkService
								.getDefaultConfigByComparableMode(mcbts, config);
					}
					airlinkService.config(mcbts.getMoId(), airConfig, false);
				} catch (Exception e) {
					failedMap.put(mcbts, e.getLocalizedMessage());
				}
			}
		}

		// 触发批量更新基站异常
		McBtsUtils.fireBatchUpdateException(failedMap);
	}

	/**
	 * 从基站查询兼容模式配置信息
	 * 
	 * @param moId 基站的MO Id
	 * 
	 * @return ChannelComparableMode 基站兼容模式信息
	 * 
	 * @throws Exception
	 */
	public ChannelComparableMode queryFromNE(long moId) throws Exception {
		GenericBizData data = mcBtsBizProxy.query(moId, new GenericBizData(
				COMPARABLEMODE_BIZ_NAME));
		if (data.getRecords().isEmpty()) {
			return null;
		}
		ChannelComparableMode config = new ChannelComparableMode();
		return (ChannelComparableMode) data.getModel(config);
	}

	/**
	 * 从网管数据库查询兼容模式配置信息
	 * 
	 * @return ChannelComparableMode 基站兼容模式信息
	 * 
	 * @throws Exception
	 */
	public ChannelComparableMode queryFromEMS() throws Exception {
		ChannelComparableMode comparableMode = new ChannelComparableMode();

		SystemPropertyService systemPropertyService = AppContext.getCtx()
				.getBean(SystemPropertyService.class);

		SystemProperty sp = systemPropertyService.getProperty(CATEGORY, null,
				PROPERTY);

		if (sp != null) {
			comparableMode.setChannelMode(Integer.parseInt(sp.getValue()));
		} else {
			comparableMode.setChannelMode(ChannelComparableMode.PCCH_ONLY);
		}
		comparableMode.setNeedConfig(true);

		return comparableMode;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
//		ChannelComparableMode neConfig = queryFromNE(moId);
//		ChannelComparableMode config = queryFromEMS();
//		if (neConfig.getChannelMode() != config.getChannelMode()) {
//			config(moId, config);
//		}
		ChannelComparableMode config = queryFromEMS();
		config(moId, config);
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		// do nothing
		
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// do nothing
		
	}
}
