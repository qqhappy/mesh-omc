package com.xinwei.minas.server.mcbts.service.layer2.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer2.SDMAConfigFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer2.SDMAConfig;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.layer2.SDMAConfigDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.ICustomService;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer2.SDMAConfigService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * SDMA基本业务接口实现
 * 
 * @author fangping
 * 
 */
public class SDMAConfigServiceImpl implements SDMAConfigService, ICustomService {

	private Log log = LogFactory.getLog(SDMAConfigServiceImpl.class);

	private McBtsBizProxy mcBtsBizProxy;
	private SDMAConfigDAO sdmaDAO;

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	public void setSdmaDAO(SDMAConfigDAO sdmaDAO) {
		this.sdmaDAO = sdmaDAO;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		List<SDMAConfig> sdmaList = sdmaDAO.queryAll();

		for (SDMAConfig sdma : sdmaList) {
			long moId = sdma.getMoId();
			// 如果moId< 0或集合不包括当前moId,就进行下一个
			if (moId < 0 || !moIdList.contains(moId))
				continue;

			business.getCell("sdmaEnableFlag").putContent(
					moId,
					toJSON("sdmaEnableFlag",
							String.valueOf(sdma.getSdmaEnableFlag())));
			business.getCell("voiceSdmaFlag").putContent(
					moId,
					toJSON("voiceSdmaFlag",
							String.valueOf(sdma.getVoiceSdmaFlag())));
			business.getCell("videoSdmaFlag").putContent(
					moId,
					toJSON("videoSdmaFlag",
							String.valueOf(sdma.getVideoSdmaFlag())));
			business.getCell("sdmaSchType")
					.putContent(
							moId,
							toJSON("sdmaSchType",
									String.valueOf(sdma.getSdmaSchType())));
			business.getCell("inRadioResIndTh").putContent(
					moId,
					toJSON("inRadioResIndTh",
							String.valueOf(sdma.getInRadioResIndTh())));
			business.getCell("outRadioResIndTh").putContent(
					moId,
					toJSON("outRadioResIndTh",
							String.valueOf(sdma.getOutRadioResIndTh())));
			business.getCell("wlMatchUserMaxNum").putContent(
					moId,
					toJSON("wlMatchUserMaxNum",
							String.valueOf(sdma.getWlMatchUserMaxNum())));

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
		SDMAConfig sdma = this.queryByMoId(mcBts.getMoId());
		if (sdma == null) {
			sdma = new SDMAConfig();
			sdma.setMoId(mcBts.getMoId());
		}
		// 填充对象
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value)) {
				return;
			}

			if (key.equals("sdmaEnableFlag")) {
				sdma.setSdmaEnableFlag(Integer.valueOf(value));
			} else if (key.equals("voiceSdmaFlag")) {
				sdma.setVoiceSdmaFlag(Integer.valueOf(value));
			} else if (key.equals("videoSdmaFlag")) {
				sdma.setVideoSdmaFlag(Integer.valueOf(value));
			} else if (key.equals("sdmaSchType")) {
				sdma.setSdmaSchType(Integer.valueOf(value));
			} else if (key.equals("inRadioResIndTh")) {
				sdma.setInRadioResIndTh(Integer.valueOf(value));
			} else if (key.equals("outRadioResIndTh")) {
				sdma.setOutRadioResIndTh(Integer.valueOf(value));
			} else if (key.equals("wlMatchUserMaxNum")) {
				sdma.setWlMatchUserMaxNum(Integer.valueOf(value));
			}
		}

		// 保存对象
		SDMAConfigFacade facade = AppContext.getCtx().getBean(
				SDMAConfigFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, sdma);
	}

	public SDMAConfig queryByMoId(Long moId) throws Exception {
		return sdmaDAO.queryByMoId(moId);
	}

	public void config(SDMAConfig sdmaConfig) throws Exception {
		Long moId = sdmaConfig.getMoId();
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			GenericBizData data = new GenericBizData("mcbts_sdma_config");
			data.addEntity(sdmaConfig);
			try {
				mcBtsBizProxy.config(moId, data);
			} catch (UnsupportedOperationException e) {
				log.error(OmpAppContext.getMessage("unsupported_biz_operation")
						+ ":mcbts_sdma_config");
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		sdmaDAO.saveOrUpdate(sdmaConfig);
	}

	private SDMAConfig queryFromNE(Long moId) throws Exception {
		GenericBizData genericBizData = new GenericBizData("mcbts_sdma_config");
		GenericBizData fromNE = mcBtsBizProxy.query(moId, genericBizData);

		SDMAConfig sdmaConfig = new SDMAConfig();
		sdmaConfig.setMoId(moId);

		sdmaConfig.setSdmaEnableFlag(Integer.valueOf(String.valueOf(fromNE
				.getProperty("sdmaEnableFlag").getValue())));
		sdmaConfig.setVoiceSdmaFlag(Integer.valueOf(String.valueOf(fromNE
				.getProperty("voiceSdmaFlag").getValue())));
		sdmaConfig.setVideoSdmaFlag(Integer.valueOf(String.valueOf(fromNE
				.getProperty("videoSdmaFlag").getValue())));
		sdmaConfig.setSdmaSchType(Integer.valueOf(String.valueOf(fromNE
				.getProperty("sdmaSchType").getValue())));

		sdmaConfig.setInRadioResIndTh(Integer.valueOf(String.valueOf(fromNE
				.getProperty("inRadioResIndTh").getValue())));
		sdmaConfig.setOutRadioResIndTh(Integer.valueOf(String.valueOf(fromNE
				.getProperty("outRadioResIndTh").getValue())));
		sdmaConfig.setWlMatchUserMaxNum(Integer.valueOf(String.valueOf(fromNE
				.getProperty("wlMatchUserMaxNum").getValue())));

		return sdmaConfig;
	}

	// private static int parsePropToInt(GenericBizRecord record,
	// String propertyName) {
	// if (record == null)
	// return 0;
	// Object obj = record.getPropertyValue(propertyName).getValue();
	// String str = String.valueOf(obj);
	// return Integer.parseInt(str);
	// }

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {//
		SDMAConfig sdmaConfig = this.queryByMoId(moId);
		if (sdmaConfig != null) {
			this.config(sdmaConfig);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		SDMAConfig result = queryFromNE(moId);
		SDMAConfig dataFromDB = queryByMoId(moId);

		if (dataFromDB == null) {
			SequenceService sequenceService = AppContext.getCtx().getBean(
					SequenceService.class);
			result.setIdx(sequenceService.getNext());
		} else {
			result.setIdx(dataFromDB.getIdx());
		}

		result.setMoId(moId);

		sdmaDAO.saveOrUpdate(result);
	}

}
