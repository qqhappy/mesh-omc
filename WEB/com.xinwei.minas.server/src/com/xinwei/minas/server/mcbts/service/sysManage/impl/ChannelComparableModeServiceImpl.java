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
 * ��վ����ģʽ���÷���ʵ��
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
	 * ����ָ����վ�Ļ�վ����ģʽ
	 * 
	 * @param moId ��վ��MO Id
	 * 
	 * @param config ��վ����ģʽ��Ϣ
	 * 
	 * @throws Exception
	 */
	public void config(long moId, ChannelComparableMode config)
			throws Exception {

		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		
		// ��վ������
		if (mcBts == null) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}

		// �Ƿ���Ҫ�·���վ����ģʽ
		if (config.isNeedConfig()) {

			// ����ǰ�Ļ�վ����ģʽ
			ChannelComparableMode oldMode = queryFromNE(moId);

			// ����ǴӼ���ģʽ1�޸�Ϊ��������ģʽ����Ҫ��ջ�վ���ڽӱ�
			if (oldMode.getChannelMode() == ChannelComparableMode.PECCH_ONLY
					&& config.getChannelMode() != oldMode.getChannelMode()) {
				NeighborService neighborService = AppContext.getCtx().getBean(
						NeighborService.class);
				neighborService.config(moId, new LinkedList<McBts>(), new LinkedList<McBts>());
			}

			// ��վ���߹���ģʽ
			if (mcBts.isOfflineManage()) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_is_offline_manage"));
			}

			// ��վδ����
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

				//����ģʽ���޸ĺ�
				// ���ػ�վ���Ͷ�Ӧ��Ĭ�Ͽ�����·����
				if (config.getChannelMode() != oldMode.getChannelMode()) {
					AirlinkConfig tempConfig = airlinkService
							.getDefaultConfigByComparableMode(mcBts, config);
					airlinkService.config(moId, tempConfig, false);
				} else {
					//����ģʽû�б仯������ǰ�Ŀ�����·��Ϣ���ø���վ
					AirlinkConfig tempConfig = airlinkService.queryFromEMS(moId);
					airlinkService.config(moId, tempConfig, false);
				}
				
			} catch (Exception e) {
				log.error(e);
				// ��֧�ֵ�ҵ��������
				if (!(e instanceof UnsupportedOperationException)) {
					throw new Exception(e.getLocalizedMessage());
				}
			}
		}
	}

	/**
	 * ����ϵͳ�Ļ�վ����ģʽ
	 * 
	 * @param config ��վ����ģʽ��Ϣ
	 * 
	 * @throws Exception
	 */
	public void config(ChannelComparableMode config) throws Exception {

		// ����ǰ�Ļ�վ����ģʽ
		ChannelComparableMode oldMode = queryFromEMS();

		// Ҫ��ѯ�����еĻ�վID
		List<McBts> btsList = McBtsCache.getInstance().queryAll();

		// ����ǴӼ���ģʽ1�޸�Ϊ��������ģʽ����Ҫ��ջ�վ���ڽӱ�
		if (oldMode.getChannelMode() == ChannelComparableMode.PECCH_ONLY
				&& config.getChannelMode() != oldMode.getChannelMode()) {

			NeighborService neighborService = AppContext.getCtx().getBean(
					NeighborService.class);

			// ������л�վ���ڽӱ�������Ϣ
			for (McBts mcbts : btsList) {
				try {
					neighborService.config(mcbts.getMoId(),
							new LinkedList<McBts>(), new LinkedList<McBts>());
				} catch (Exception e) {
				}
			}
		}

		// ʧ�ܵĻ�վ��ʧ����Ϣ
		Map<McBts, String> failedMap = new HashMap<McBts, String>();
		
		// �����վ����ģʽ
		SystemPropertyService systemPropertyService = AppContext.getCtx()
				.getBean(SystemPropertyService.class);

		systemPropertyService.setProperty(CATEGORY, null, PROPERTY,
				String.valueOf(config.getChannelMode()));

		// �Ƿ���Ҫ�·���վ����ģʽ
		if (config.isNeedConfig()) {
			
			GenericBizData data = new GenericBizData(COMPARABLEMODE_BIZ_NAME);
			data.addEntity(config);

			AirlinkService airlinkService = AppContext.getCtx().getBean(
					AirlinkService.class);
			
			// ��ÿ�������õĻ�վ�·���վ����ģʽ
			for (McBts mcbts : btsList) {

				// �жϻ�վ�Ƿ��ǿ����õ�
				if (mcbts.isConfigurable()) {
					try {
						mcBtsBizProxy.config(mcbts.getMoId(), data);
					} catch (Exception e) {
						failedMap.put(mcbts, e.getLocalizedMessage());
					}
				}

				try {
					AirlinkConfig airConfig = null;
					// ����ǴӼ���ģʽ3�޸�Ϊ��������ģʽ���������ֿ�������
					if (oldMode.getChannelMode() == ChannelComparableMode.PCCH_AND_PECCH
							&& config.getChannelMode() != ChannelComparableMode.PCCH_AND_PECCH) {
//						airConfig = airlinkService.queryByMoId(mcbts.getMoId());
						airConfig = airlinkService.queryFromEMS(mcbts.getMoId());
						if (config.getChannelMode() == ChannelComparableMode.PCCH_ONLY) {
							// ��֧�ֹ����ŵ�����ʱ�������ǿ�����ŵ�����
							airConfig.setPecchSetting(null);
						} else if (config.getChannelMode() == ChannelComparableMode.PECCH_ONLY) {
							// ��֧����ǿ�����ŵ�����ʱ����չ����ŵ�����
							airConfig.getScgChannelConfigList().clear();
						}
					} else if (oldMode.getChannelMode() == config.getChannelMode()) { //����ģʽû�б仯ʱ������Ҫ�ظ���Ĭ��ֵ
						airConfig = airlinkService.queryByMoId(mcbts.getMoId());
					}
					// ����Ǵ���������ģʽ�޸�Ϊ����ģʽ3��������������
//					if (oldMode.getChannelMode() != ChannelComparableMode.PCCH_AND_PECCH
//							&& config.getChannelMode() == ChannelComparableMode.PCCH_AND_PECCH) {
//						airConfig = airlinkService.queryByMoId(mcbts.getMoId());
//						ChannelComparableMode newMode = new ChannelComparableMode();
//						if (oldMode.getChannelMode() == ChannelComparableMode.PCCH_ONLY) {
//							// ԭ����֧�ֹ����ŵ�����ʱ������Ĭ����ǿ�����ŵ�����
//							newMode.setChannelMode(ChannelComparableMode.PECCH_ONLY);
//							AirlinkConfig tempConfig = airlinkService
//									.getDefaultConfigByComparableMode(mcbts,
//											newMode);
//							airConfig.setPecchSetting(tempConfig.getPecchSetting());
//						} else if (oldMode.getChannelMode() == ChannelComparableMode.PECCH_ONLY) {
//							// ԭ����֧����ǿ�����ŵ�����ʱ������Ĭ�Ϲ����ŵ����� ???
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
					// ���ݼ���ģʽ���첢�·���վ������·Ĭ������
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

		// �����������»�վ�쳣
		McBtsUtils.fireBatchUpdateException(failedMap);
	}

	/**
	 * �ӻ�վ��ѯ����ģʽ������Ϣ
	 * 
	 * @param moId ��վ��MO Id
	 * 
	 * @return ChannelComparableMode ��վ����ģʽ��Ϣ
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
	 * ���������ݿ��ѯ����ģʽ������Ϣ
	 * 
	 * @return ChannelComparableMode ��վ����ģʽ��Ϣ
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
