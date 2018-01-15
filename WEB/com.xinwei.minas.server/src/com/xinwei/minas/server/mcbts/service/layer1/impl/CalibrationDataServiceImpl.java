/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer1.CalibrationDataFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer1.AntennaCalibItem;
import com.xinwei.minas.mcbts.core.model.layer1.AntennaCalibrationConfig;
import com.xinwei.minas.mcbts.core.model.layer1.CalibGenConfigItem;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationDataInfo;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationGeneralConfig;
import com.xinwei.minas.mcbts.core.model.layer1.CalibrationResult;
import com.xinwei.minas.mcbts.core.model.layer1.PSConfigItem;
import com.xinwei.minas.mcbts.core.model.layer1.RFConfig;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.mcbts.core.utils.FreqConvertUtil;
import com.xinwei.minas.server.core.conf.service.MoCache;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfCalibAntDataConfigDAO;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfCalibGenConfigDAO;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfRfConfigDAO;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfSubCalibGenConfigDAO;
import com.xinwei.minas.server.mcbts.dao.layer1.TConfSubRfPsConfigDAO;
import com.xinwei.minas.server.mcbts.proxy.layer1.CalibDataProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer1.CalibrationDataService;
import com.xinwei.minas.server.mcbts.service.layer1.CalibrationResultService;
import com.xinwei.minas.server.mcbts.service.layer3.NeighborService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 校准类型配置业务服务接口实现
 * 
 * @author chenjunhua
 * 
 */

public class CalibrationDataServiceImpl implements CalibrationDataService {

	private static Log log = LogFactory
			.getLog(CalibrationDataServiceImpl.class);

	private CalibDataProxy calibDataProxy;

	private TConfRfConfigDAO rfConfigDAO;

	private TConfSubRfPsConfigDAO subPsConfigDAO;

	private TConfCalibGenConfigDAO genConfigDAO;

	private TConfSubCalibGenConfigDAO subGenConfigDAO;

	private TConfCalibAntDataConfigDAO antConfigDAO;

	/**
	 * @param settingDAO
	 *            the l1generalSettingDAO to set
	 */
	public void setRfConfigDAO(TConfRfConfigDAO settingDAO) {
		rfConfigDAO = settingDAO;
	}

	/**
	 * @param subPsConfigDAO
	 *            the subPsConfigDAO to set
	 */
	public void setSubPsConfigDAO(TConfSubRfPsConfigDAO subPsConfigDAO) {
		this.subPsConfigDAO = subPsConfigDAO;
	}

	/**
	 * @param calibDataProxy
	 *            the calibDataProxy to set
	 */
	public void setCalibDataProxy(CalibDataProxy calibDataProxy) {
		this.calibDataProxy = calibDataProxy;
	}

	/**
	 * @param genConfigDAO
	 *            the genConfigDAO to set
	 */
	public void setGenConfigDAO(TConfCalibGenConfigDAO genConfigDAO) {
		this.genConfigDAO = genConfigDAO;
	}

	/**
	 * @param subGenConfigDAO
	 *            the subGenConfigDAO to set
	 */
	public void setSubGenConfigDAO(TConfSubCalibGenConfigDAO subGenConfigDAO) {
		this.subGenConfigDAO = subGenConfigDAO;
	}

	/**
	 * @param antConfigDAO
	 *            the antConfigDAO to set
	 */
	public void setAntConfigDAO(TConfCalibAntDataConfigDAO antConfigDAO) {
		this.antConfigDAO = antConfigDAO;
	}

	/**
	 * 查询设备校准数据配置信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public CalibrationDataInfo queryDeviceCalibrationDataConfigByMoId(Long moId)
			throws Exception {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		CalibrationDataInfo result = new CalibrationDataInfo();
		// 在线管理状态下，需要通过Proxy向MO发送查询消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			try {
				result = calibDataProxy.query(moId);
			} catch (Exception e) {
				log.error(e);
				throw e;
			}
		} else {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_is_offline_manage"));
		}

		// 从校准结果文件加载天线校准参数
		CalibrationResult calibResult = getCalibrationResultFromFile(moId);
		List<AntennaCalibrationConfig> antConfigs = new ArrayList<AntennaCalibrationConfig>();
		if (calibResult != null) {
			calibResult.setMoId(moId);
			calibResult.populateAntennaConfigList();
			antConfigs = calibResult.getAntConfigList();
			result.setAntConfigList(antConfigs);
		}
		if (antConfigs == null || antConfigs.isEmpty()) {
			// 无结果文件，填写默认值
			antConfigs = loadDefaultAntennaCalibrationData(moId);
		}

		return result;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		for (long moId : moIdList) {
			CalibrationDataInfo dataInfo = this
					.queryCalibrationDataConfigByMoId(moId);

			McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);

			// RF Config
			RFConfig rfConfig = dataInfo.getRfConfig();
			business.getCell("middleFreq").putContent(moId,
					toJSON("middleFreq", getMiddleFreq(mcBts).toString()));

			business.getCell("antennaPower").putContent(
					moId,
					toJSON("antennaPower",
							String.valueOf(rfConfig.getAntennaPower())));

			business.getCell("rxSensitivity").putContent(
					moId,
					toJSON("rxSensitivity",
							String.valueOf(rfConfig.getRxSensitivity())));

			business.getCell("cableLoss")
					.putContent(
							moId,
							toJSON("cableLoss",
									String.valueOf(rfConfig.getCableLoss())));

			business.getCell("psLoss").putContent(moId,
					toJSON("psLoss", String.valueOf(rfConfig.getPsLoss())));

			StringBuilder psBuilder = new StringBuilder();
			psBuilder.append("\"psConfig\":[");
			for (PSConfigItem item : rfConfig.getPsConfigList()) {
				if (psBuilder.indexOf("}") > 0) {
					psBuilder.append(",");
				}
				psBuilder.append("{\"x\":\"").append(item.getPsNormX());
				psBuilder.append("\",\"y\":\"").append(item.getPsNormY());
				psBuilder.append("\"}");
			}
			psBuilder.append("]");
			business.getCell("psConfig").putContent(moId, psBuilder.toString());

			// Generic Config
			CalibrationGeneralConfig genConfig = dataInfo.getGenConfig();

			business.getCell("synRxGain").putContent(
					moId,
					toJSON("synRxGain",
							String.valueOf(genConfig.getSynRxGain())));

			business.getCell("synTxGain").putContent(
					moId,
					toJSON("synTxGain",
							String.valueOf(genConfig.getSynTxGain())));

			// gen item
			StringBuilder genBuilder = new StringBuilder();
			genBuilder.append("\"gainInfo\":[");
			for (CalibGenConfigItem item : genConfig.getGenItemList()) {
				if (genBuilder.indexOf("}") > 0) {
					genBuilder.append(",");
				}
				genBuilder.append("{\"rxGain\":\"").append(item.getRxGain());
				genBuilder.append("\",\"txGain\":\"").append(item.getTxGain());
				genBuilder.append("\",\"calibResult\":\"").append(
						item.getCalibrationResult());
				genBuilder.append("\"}");
			}
			genBuilder.append("]");
			business.getCell("gainInfo")
					.putContent(moId, genBuilder.toString());
		}

	}

	private static Double getMiddleFreq(McBts mcBts) {
		FreqConvertUtil freqUtil = new FreqConvertUtil();
		freqUtil.setFreqType(mcBts.getBtsFreqType());
		return freqUtil.getMidFreqValue(mcBts.getBtsFreq());
	}

	private static int getFreqOffset(McBts mcBts, double middleFreq) {
		FreqConvertUtil freqUtil = new FreqConvertUtil();
		freqUtil.setFreqType(mcBts.getBtsFreqType());
		return freqUtil.getMiddleFreqOffset(middleFreq);
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
		CalibrationDataInfo calibData = this
				.queryCalibrationDataConfigByMoId(mcBts.getMoId());
		if (calibData == null) {
			calibData = new CalibrationDataInfo();
		}

		// 填充对象
		RFConfig rfConfig = calibData.getRfConfig();
		if (rfConfig == null) {
			rfConfig = new RFConfig();
			rfConfig.setMoId(mcBts.getMoId());
		}
		CalibrationGeneralConfig genConfig = calibData.getGenConfig();
		if (genConfig == null) {
			genConfig = new CalibrationGeneralConfig();
			genConfig.setMoId(mcBts.getMoId());
		}

		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getContentByBID(btsId);

			if (key.equals("middleFreq")) {
				rfConfig.setFreqOffset(getFreqOffset(mcBts,
						Double.valueOf(value)));
			} else if (key.equals("antennaPower")) {
				rfConfig.setAntennaPower(Integer.valueOf(value));
			} else if (key.equals("rxSensitivity")) {
				rfConfig.setRxSensitivity(Integer.valueOf(value));
			} else if (key.equals("cableLoss")) {
				rfConfig.setCableLoss(Integer.valueOf(value));
			} else if (key.equals("psLoss")) {
				rfConfig.setPsLoss(Integer.valueOf(value));
			} else if (key.equals("psConfig")) {
				if (StringUtils.isBlank(value)) {
					continue;
				}
				List<PSConfigItem> itemList = new ArrayList<PSConfigItem>();
				String[] psStrs = value.replaceAll("(\\[|\\])", "").split(";");
				for (int i = 0; i < psStrs.length; i++) {
					String psStr = psStrs[i];
					String[] psValues = psStr.split(",");

					PSConfigItem item = new PSConfigItem();
					item.setMoId(mcBts.getMoId());
					item.setAntennaIndex(i);
					item.setPsNormX(Integer.valueOf(psValues[0]));
					item.setPsNormY(Integer.valueOf(psValues[1]));

					itemList.add(item);
				}
				rfConfig.setPsConfigList(itemList);

			} else if (key.equals("synRxGain")) {
				genConfig.setSynRxGain(Integer.valueOf(value));
			} else if (key.equals("synTxGain")) {
				genConfig.setSynTxGain(Integer.valueOf(value));
			} else if (key.equals("gainInfo")) {
				if (StringUtils.isBlank(value)) {
					continue;
				}
				List<CalibGenConfigItem> itemList = new ArrayList<CalibGenConfigItem>();
				String[] genStrs = value.replaceAll("(\\[|\\])", "").split(";");
				for (int i = 0; i < genStrs.length; i++) {
					String genStr = genStrs[i];
					String[] genValues = genStr.split(",");

					CalibGenConfigItem item = new CalibGenConfigItem();
					item.setMoId(mcBts.getMoId());
					item.setAntennaIndex(i);
					item.setRxGain(Integer.valueOf(genValues[0]));
					item.setTxGain(Integer.valueOf(genValues[1]));
					item.setCalibrationResult(Integer.valueOf(genValues[2]));
					item.setPredH("");

					itemList.add(item);
				}
				genConfig.setGenItemList(itemList);

			}
		}

		calibData.setRfConfig(rfConfig);
		calibData.setGenConfig(genConfig);

		// 保存对象
		CalibrationDataFacade facade = AppContext.getCtx().getBean(
				CalibrationDataFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, mcBts.getMoId(), calibData);
	}

	/**
	 * 查询网管校准数据配置信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public CalibrationDataInfo queryCalibrationDataConfigByMoId(Long moId)
			throws Exception {
		try {
			CalibrationDataInfo result = new CalibrationDataInfo();

			// 射频信息
			RFConfig rfConfig = rfConfigDAO.queryByMoId(moId);
			if (rfConfig != null) {
				List<PSConfigItem> psConfigList = subPsConfigDAO
						.queryByMoId(moId);
				// 按照天线号进行排序
				Collections.sort(psConfigList, new Comparator<PSConfigItem>() {
					public int compare(PSConfigItem item1, PSConfigItem item2) {
						return item1.getAntennaIndex()
								- item2.getAntennaIndex();
					}
				});
				rfConfig.setPsConfigList(psConfigList);
			}
			result.setRfConfig(rfConfig);

			// 发送接收功率信息
			CalibrationGeneralConfig genConfig = genConfigDAO.queryByMoId(moId);
			if (genConfig != null) {
				List<CalibGenConfigItem> genConfigItemList = subGenConfigDAO
						.queryByMoId(moId);
				// 按照天线号进行排序
				Collections.sort(genConfigItemList,
						new Comparator<CalibGenConfigItem>() {
							public int compare(CalibGenConfigItem item1,
									CalibGenConfigItem item2) {
								return item1.getAntennaIndex()
										- item2.getAntennaIndex();
							}
						});
				genConfig.setGenItemList(genConfigItemList);
			}
			result.setGenConfig(genConfig);

			// 从校准结果文件加载天线校准参数
			CalibrationResult calibResult = getCalibrationResultFromFile(moId);
			List<AntennaCalibrationConfig> antConfigs = new ArrayList<AntennaCalibrationConfig>();
			if (calibResult != null) {
				calibResult.setMoId(moId);
				calibResult.populateAntennaConfigList();
				antConfigs = calibResult.getAntConfigList();
			}
			if (antConfigs == null || antConfigs.isEmpty()) {
				// 无结果文件，从数据库中获取
				antConfigs = antConfigDAO.queryByMoId(moId);
			}
			if (antConfigs == null || antConfigs.isEmpty()) {
				// 数据库中无数据，填写默认值
				antConfigs = loadDefaultAntennaCalibrationData(moId);
			}

			// 按照天线号进行排序
			Collections.sort(antConfigs,
					new Comparator<AntennaCalibrationConfig>() {
						public int compare(AntennaCalibrationConfig config1,
								AntennaCalibrationConfig config2) {
							return config1.getAntennaIndex()
									- config2.getAntennaIndex();
						}
					});
			result.setAntConfigList(antConfigs);

			return result;

		} catch (Exception e) {
			log.error("query CalibrationDataConfig failed. ", e);
			throw new Exception(e.getLocalizedMessage());
		}
	}

	/**
	 * 配置校准类型配置信息
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	public void config(Long moId, CalibrationDataInfo setting,
			boolean isSyncConfig) throws Exception {
		Mo mo = MoCache.getInstance().queryByMoId(moId);
		if (mo == null) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}

		addIdx(setting);

		McBts bts = McBtsCache.getInstance().queryByMoId(moId);

		// 在线管理状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {

			// 检查BTS的工作状态是否为抗干扰跳频状态
			if (bts != null && bts.isAntijamming()) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_jumping_hold"));
			}

			// 特殊配置
			try {
				calibDataProxy.config(moId, setting);
			} catch (Exception e) {
				log.error("Error configuring calibration data.", e);
				throw e;
			}
		}

		RFConfig rfConfig = setting.getRfConfig();
		if (rfConfig != null) {
			rfConfigDAO.saveOrUpdate(rfConfig);
			subPsConfigDAO.saveOrUpdate(rfConfig.getPsConfigList());

			// 更新缓存中的基站频率
			if (moId > 0) {
				bts.setBtsFreq(rfConfig.getFreqOffset());
			}
		}
		CalibrationGeneralConfig genConfig = setting.getGenConfig();
		if (genConfig != null) {
			genConfigDAO.saveOrUpdate(genConfig);
			subGenConfigDAO.saveOrUpdate(genConfig.getGenItemList());
		}
		List<AntennaCalibrationConfig> antConfigs = setting.getAntConfigList();
		if (antConfigs != null) {
			antConfigDAO.saveOrUpdate(antConfigs);
		}

		NeighborService neighborService = AppContext.getCtx().getBean(
				NeighborService.class);

		neighborService.sendNeighborConfig(moId, isSyncConfig);

		// TODO: 更新为同播基站链路未同步状态
	}

	/**
	 * 为所有数据添加主键
	 * 
	 * @param data
	 */
	private static void addIdx(CalibrationDataInfo data) {
		SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);

		RFConfig rfConfig = data.getRfConfig();
		if (rfConfig != null) {
			if (rfConfig.getIdx() == null) {
				rfConfig.setIdx(sequenceService.getNext());
			}
			List<PSConfigItem> psConfigList = rfConfig.getPsConfigList();
			for (PSConfigItem psConfig : psConfigList) {
				if (psConfig != null && psConfig.getIdx() == null) {
					psConfig.setIdx(sequenceService.getNext());
				}
			}
		}
		CalibrationGeneralConfig genConfig = data.getGenConfig();
		if (genConfig != null) {
			if (genConfig.getIdx() == null) {
				genConfig.setIdx(sequenceService.getNext());
			}
			List<CalibGenConfigItem> genItemList = genConfig.getGenItemList();
			for (CalibGenConfigItem genItem : genItemList) {
				if (genItem != null && genItem.getIdx() == null) {
					genItem.setIdx(sequenceService.getNext());
				}
			}
		}
		List<AntennaCalibrationConfig> antConfigs = data.getAntConfigList();
		if (antConfigs != null) {
			for (AntennaCalibrationConfig config : antConfigs) {
				if (config.getIdx() == null) {
					config.setIdx(sequenceService.getNext());
				}
			}
		}
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		CalibrationDataInfo calibrationDataInfo = this
				.queryCalibrationDataConfigByMoId(moId);

		McBts bts = McBtsCache.getInstance().queryByMoId(moId);

		if (calibrationDataInfo != null) {
			// 在线管理状态下，需要通过Proxy向MO发送配置消息
			if (bts.isConfigurable()) {

				// 检查BTS的工作状态是否为抗干扰跳频状态
				if (bts != null && bts.isAntijamming()) {
					throw new Exception(
							OmpAppContext.getMessage("mcbts_jumping_hold"));
				}

				// 特殊配置
				try {
					calibDataProxy.config(moId, calibrationDataInfo);
				} catch (Exception e) {
					log.error("Error configuring calibration data.", e);
					throw e;
				}
			}
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		CalibrationDataInfo calibrationDataInfo = this
				.queryDeviceCalibrationDataConfigByMoId(moId);

		addIdx(calibrationDataInfo);

		McBts bts = McBtsCache.getInstance().queryByMoId(moId);

		RFConfig rfConfig = calibrationDataInfo.getRfConfig();
		if (rfConfig != null && moId > 0) {

			// 获得数据库中现有主键
			RFConfig queryFromDB = rfConfigDAO.queryByMoId(moId);
			if (queryFromDB != null)
				rfConfig.setIdx(queryFromDB.getIdx());

			rfConfigDAO.saveOrUpdate(rfConfig);
			subPsConfigDAO.saveOrUpdate(rfConfig.getPsConfigList());

			// 更新缓存中的基站频率
			bts.setBtsFreq(rfConfig.getFreqOffset());
		}
		CalibrationGeneralConfig genConfig = calibrationDataInfo.getGenConfig();
		if (genConfig != null) {
			CalibrationGeneralConfig queryFromDB = genConfigDAO
					.queryByMoId(moId);

			if (queryFromDB != null)
				genConfig.setIdx(queryFromDB.getIdx());

			genConfigDAO.saveOrUpdate(genConfig);
			subGenConfigDAO.saveOrUpdate(genConfig.getGenItemList());
		}

		List<AntennaCalibrationConfig> antConfigs = calibrationDataInfo
				.getAntConfigList();
		if (antConfigs != null) {
			antConfigDAO.saveOrUpdate(antConfigs);
		}
	}

	private List<AntennaCalibrationConfig> loadDefaultAntennaCalibrationData(
			Long moId) {
		List<AntennaCalibrationConfig> result = new ArrayList<AntennaCalibrationConfig>();

		for (int i = 0; i < AntennaCalibrationConfig.ANT_COUNT; i++) {

			for (int j = AntennaCalibrationConfig.TXCAL_I; j <= AntennaCalibrationConfig.RXCAL_Q; j++) {
				AntennaCalibrationConfig antConfig = new AntennaCalibrationConfig();
				antConfig.setAntennaIndex(i);
				antConfig.setDataType(j);
				antConfig.setMoId(moId);
				int value = 0;
				if (j % 2 == 1) {
					value = 8192;
				}

				List<AntennaCalibItem> calibItems = new ArrayList<AntennaCalibItem>();
				for (int k = 0; k < AntennaCalibrationConfig.CALCARRIER_COUNT; k++) {
					AntennaCalibItem item = new AntennaCalibItem();
					item.setCarrierData(value);
					item.setRowIndex(k);
					calibItems.add(item);
				}
				antConfig.setAntItemList(calibItems);
				result.add(antConfig);
			}
		}

		return result;
	}

	private CalibrationResult getCalibrationResultFromFile(Long moId) {

		CalibrationResultService calibResultService = AppContext.getCtx()
				.getBean(CalibrationResultService.class);

		try {
			return calibResultService.getCalibrationResult(moId);
		} catch (Exception ex) {
			log.error("get calibration result from file error:", ex);
		}

		return null;
	}
}
