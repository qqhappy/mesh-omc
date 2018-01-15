/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer1.L1GeneralSettingFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.mcbts.core.model.layer1.L1GeneralSetting;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfL1GeneralSettingDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer1.L1GeneralSettingService;
import com.xinwei.minas.server.mcbts.service.layer2.AirlinkService;
import com.xinwei.minas.server.mcbts.service.layer3.NeighborService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * L1配置业务服务接口实现
 * 
 * @author chenjunhua
 * 
 */

public class L1GeneralSettingServiceImpl implements L1GeneralSettingService {

	private Log log = LogFactory.getLog(L1GeneralSettingServiceImpl.class);

	private McBtsBizProxy mcBtsBizProxy;

	private TConfL1GeneralSettingDAO l1generalSettingDAO;

	/**
	 * @param settingDAO
	 *            the l1generalSettingDAO to set
	 */
	public void setL1generalSettingDAO(TConfL1GeneralSettingDAO settingDAO) {
		l1generalSettingDAO = settingDAO;
	}

	/**
	 * @param mcBtsBizProxy
	 *            the mcBtsBizProxy to set
	 */
	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		List<L1GeneralSetting> l1List = l1generalSettingDAO.queryAll();

		for (L1GeneralSetting setting : l1List) {
			long moId = setting.getMoId();
			// 如果moId< 0或集合不包括当前moId,就进行下一个
			if (moId < 0 || !moIdList.contains(moId))
				continue;

			business.getCell("syncSrc").putContent(moId,
					toJSON("syncSrc", String.valueOf(setting.getSyncSrc())));

			business.getCell("gpsOffset")
					.putContent(
							moId,
							toJSON("gpsOffset",
									String.valueOf(setting.getGpsOffset())));

			business.getCell("antennaMask").putContent(
					moId,
					toJSON("antennaMask",
							String.valueOf(setting.getAntennaMask())));
		}
	}

	private static String toJSON(String key, String value) {
		return "\"" + key + "\":\"" + value + "\"";
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// 获得基站模型
		long btsId = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		// 创建对象
		L1GeneralSetting l1 = this.queryByMoId(mcBts.getMoId());
		if (l1 == null) {
			l1 = new L1GeneralSetting();
			l1.setMoId(mcBts.getMoId());
		}

		// 填充对象
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value)) {
				return;
			}

			if (key.equals("syncSrc")) {
				l1.setSyncSrc(Long.valueOf(value));
			} else if (key.equals("gpsOffset")) {
				l1.setGpsOffset(Long.valueOf(value));
			} else if (key.equals("antennaMask")) {
				l1.setAntennaMask(Long.valueOf(value));
			}
		}

		// 保存对象
		L1GeneralSettingFacade facade = AppContext.getCtx().getBean(
				L1GeneralSettingFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, l1);

	}

	/**
	 * 查询L1配置信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public L1GeneralSetting queryByMoId(Long moId) throws Exception {
		return l1generalSettingDAO.queryByMoId(moId);
	}

	/**
	 * 配置L1配置信息
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	public void config(L1GeneralSetting setting, boolean isSyncConfig)
			throws Exception {
		Long moId = setting.getMoId();
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

			// 转换模型
			GenericBizData data = new GenericBizData("mcbts_l1general");
			data.addEntity(setting);
			try {
				mcBtsBizProxy.config(moId, data);
			} catch (Exception e) {
				log.error(e);
				throw e;
			}
		}

		l1generalSettingDAO.saveOrUpdate(setting);

		NeighborService neighborService = AppContext.getCtx().getBean(
				NeighborService.class);
				
		AirlinkService airlinkService = AppContext.getCtx().getBean(
				AirlinkService.class);

		// 不是模板才需要进行下面的配置
		if (moId >= 0) {
			if (bts.getBtsType() == McBtsTypeDD.MICRO_BEEHIVE_MCBTS) {
				airlinkService.syncFromEMSToNE(bts.getMoId());
			} else {
				neighborService.sendNeighborConfig(setting.getMoId(),
						isSyncConfig);
			}
		}
	}

	/**
	 * 从网元查询L1配置信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return GenericBizData
	 */
	@Override
	public L1GeneralSetting queryFromNE(Long moId) throws Exception {
		GenericBizData genericBizData = new GenericBizData("mcbts_l1general");
		GenericBizData result = mcBtsBizProxy.query(moId, genericBizData);
		L1GeneralSetting l1GeneralSetting = new L1GeneralSetting();

		l1GeneralSetting.setMoId(moId);
		l1GeneralSetting.setAntennaMask(Long.valueOf(result
				.getProperty("antennaMask").getValue().toString()));
		l1GeneralSetting.setGpsOffset(Long.valueOf(result
				.getProperty("gpsOffset").getValue().toString()));
		l1GeneralSetting.setSyncSrc(Long.valueOf(result.getProperty("syncSrc")
				.getValue().toString()));

		return l1GeneralSetting;
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		L1GeneralSetting l1GeneralSetting = this.queryByMoId(moId);
		if (l1GeneralSetting != null) {
			this.config(l1GeneralSetting, false);
		}

	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		L1GeneralSetting result = queryFromNE(moId);
		L1GeneralSetting dataFromDB = queryByMoId(moId);

		if (dataFromDB == null) {
			SequenceService sequenceService = AppContext.getCtx().getBean(
					SequenceService.class);
			result.setIdx(sequenceService.getNext());
		} else {
			result.setIdx(dataFromDB.getIdx());
		}

		result.setMoId(moId);

		l1generalSettingDAO.saveOrUpdate(result);
	}

}
