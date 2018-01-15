/**
 * 
 */
package com.xinwei.minas.server.mcbts.service.layer2.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer2.ChangeAlgParamFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer2.TConfChangeAlgParam;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.layer2.ChangeAlgParamDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer2.ChangeAlgParamService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.GenericBizRecord;
import com.xinwei.omp.server.OmpAppContext;

/**
 * @author chenshaohua
 * 
 */
public class ChangeAlgParamServiceImpl implements ChangeAlgParamService {
	private Log log = LogFactory.getLog(ChangeAlgParamServiceImpl.class);
	private McBtsBizProxy mcBtsBizProxy;

	private ChangeAlgParamDAO changeAlgParamDAO;

	private String restrictFlag;

	public void config(TConfChangeAlgParam changeAlgParam) throws Exception {
		Long moId = changeAlgParam.getMoId();
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 在线管理状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			GenericBizData data = new GenericBizData("t_conf_alg_param");
			data.addEntity(changeAlgParam);
			try {
				mcBtsBizProxy.config(moId, data);
			} catch (Exception e) {
				log.error(e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		try {
			changeAlgParamDAO.saveOrUpdate(changeAlgParam);
		} catch (Exception e) {
			log.error(e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}

	}

	public TConfChangeAlgParam query(Long moId) throws Exception {
		TConfChangeAlgParam changeAlgParam = null;
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 在线管理下，向基站发送查询消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			GenericBizData data = new GenericBizData("t_conf_alg_param");
			try {
				GenericBizData resposeData = mcBtsBizProxy.query(moId, data);
				if (resposeData != null) {
					changeAlgParam = convertBizDataToModel(resposeData);
				}
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("query_data_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		return changeAlgParam;
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		TConfChangeAlgParam changeAlgParam = this.queryByMoId(moId);
		if (changeAlgParam != null) {
			this.config(changeAlgParam);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		// TODO Auto-generated method stub

	}

	private TConfChangeAlgParam convertBizDataToModel(GenericBizData resposeData) {
		TConfChangeAlgParam changeAlgParam = new TConfChangeAlgParam();
		GenericBizRecord record = null;
		List<GenericBizRecord> records = resposeData.getRecords();
		if (!records.isEmpty()) {
			record = records.get(0);
		}
		GenericBizProperty property = record.getPropertyValue("m_ho_pwr_thd");
		changeAlgParam.setM_ho_pwr_thd(Integer.parseInt(property.getValue()
				.toString()));
		property = record.getPropertyValue("m_ho_pwr_offset1");
		changeAlgParam.setM_ho_pwr_offset1(Integer.parseInt(property.getValue()
				.toString()));
		property = record.getPropertyValue("m_ho_pwr_offset2");
		changeAlgParam.setM_ho_pwr_offset2(Integer.parseInt(property.getValue()
				.toString()));
		property = record.getPropertyValue("m_cpe_cm_ho_period");
		changeAlgParam.setM_cpe_cm_ho_period(Integer.parseInt(property
				.getValue().toString()));
		property = record.getPropertyValue("m_ho_pwr_filtercoef_stat");
		changeAlgParam.setM_ho_pwr_filtercoef_stat(Integer.parseInt(property
				.getValue().toString()));
		property = record.getPropertyValue("m_ho_pwr_filtercoef_mobile");
		changeAlgParam.setM_ho_pwr_filtercoef_mobile(Integer.parseInt(property
				.getValue().toString()));
		property = record.getPropertyValue("time_to_trigger");
		changeAlgParam.setTime_to_trigger(Integer.parseInt(property.getValue()
				.toString()));

		return changeAlgParam;
	}

	@Override
	public String getRestrictAreaFlag() throws Exception {
		return getRestrictFlag();
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		List<TConfChangeAlgParam> algList = changeAlgParamDAO.queryAll();

		for (TConfChangeAlgParam alg : algList) {
			long moId = alg.getMoId();
			// 如果moId< 0或集合不包括当前moId,就进行下一个
			if (moId < 0 || !moIdList.contains(moId))
				continue;

			business.getCell("m_ho_pwr_thd").putContent(
					moId,
					toJSON("m_ho_pwr_thd",
							String.valueOf(alg.getM_ho_pwr_thd())));
			business.getCell("m_ho_pwr_offset1").putContent(
					moId,
					toJSON("m_ho_pwr_offset1",
							String.valueOf(alg.getM_ho_pwr_offset1())));
			business.getCell("m_ho_pwr_offset2").putContent(
					moId,
					toJSON("m_ho_pwr_offset2",
							String.valueOf(alg.getM_ho_pwr_offset2())));
			business.getCell("m_ho_pwr_filtercoef_stat").putContent(
					moId,
					toJSON("m_ho_pwr_filtercoef_stat",
							String.valueOf(alg.getM_ho_pwr_filtercoef_stat())));
			business.getCell("m_ho_pwr_filtercoef_mobile")
					.putContent(
							moId,
							toJSON("m_ho_pwr_filtercoef_mobile", String
									.valueOf(alg
											.getM_ho_pwr_filtercoef_mobile())));
			business.getCell("time_to_trigger").putContent(
					moId,
					toJSON("time_to_trigger",
							String.valueOf(alg.getTime_to_trigger())));
			business.getCell("m_cpe_cm_ho_period").putContent(
					moId,
					toJSON("m_cpe_cm_ho_period",
							String.valueOf(alg.getM_cpe_cm_ho_period())));
			business.getCell("strictArea_pwr_thd").putContent(
					moId,
					toJSON("strictArea_pwr_thd",
							String.valueOf(alg.getStrictArea_pwr_thd())));
			business.getCell("strictArea_time_to_trigger")
					.putContent(
							moId,
							toJSON("strictArea_time_to_trigger", String
									.valueOf(alg
											.getStrictArea_time_to_trigger())));
			business.getCell("strictArea_ho_pwr_offset").putContent(
					moId,
					toJSON("strictArea_ho_pwr_offset",
							String.valueOf(alg.getStrictArea_ho_pwr_offset())));
			business.getCell("strictArea_2_normal_pwr_thres").putContent(
					moId,
					toJSON("strictArea_2_normal_pwr_thres", String.valueOf(alg
							.getStrictArea_2_normal_pwr_thres())));

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
		TConfChangeAlgParam alg = this.queryByMoId(mcBts.getMoId());
		if (alg == null) {
			alg = new TConfChangeAlgParam();
			alg.setMoId(mcBts.getMoId());
		}

		// 填充对象
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value)) {
				return;
			}

			if (key.equals("m_ho_pwr_thd")) {
				alg.setM_ho_pwr_thd(Integer.valueOf(value));
			} else if (key.equals("m_ho_pwr_offset1")) {
				alg.setM_ho_pwr_offset1(Integer.valueOf(value));
			} else if (key.equals("m_ho_pwr_offset2")) {
				alg.setM_ho_pwr_offset2(Integer.valueOf(value));
			} else if (key.equals("m_ho_pwr_filtercoef_stat")) {
				alg.setM_ho_pwr_filtercoef_stat(Integer.valueOf(value));
			} else if (key.equals("m_ho_pwr_filtercoef_mobile")) {
				alg.setM_ho_pwr_filtercoef_mobile(Integer.valueOf(value));
			} else if (key.equals("time_to_trigger")) {
				alg.setTime_to_trigger(Integer.valueOf(value));
			} else if (key.equals("m_cpe_cm_ho_period")) {
				alg.setM_cpe_cm_ho_period(Integer.valueOf(value));
			} else if (key.equals("strictArea_pwr_thd")) {
				alg.setStrictArea_pwr_thd(Integer.valueOf(value));
			} else if (key.equals("strictArea_time_to_trigger")) {
				alg.setStrictArea_time_to_trigger(Integer.valueOf(value));
			} else if (key.equals("strictArea_ho_pwr_offset")) {
				alg.setStrictArea_ho_pwr_offset(Integer.valueOf(value));
			} else if (key.equals("strictArea_2_normal_pwr_thres")) {
				alg.setStrictArea_2_normal_pwr_thres(Integer.valueOf(value));
			}
		}

		// 保存对象
		ChangeAlgParamFacade facade = AppContext.getCtx().getBean(
				ChangeAlgParamFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, alg);
	}

	public TConfChangeAlgParam queryByMoId(Long moId) throws Exception {
		return changeAlgParamDAO.queryByMoId(moId);
	}

	public ChangeAlgParamDAO getChangeAlgParamDAO() {
		return changeAlgParamDAO;
	}

	public void setChangeAlgParamDAO(ChangeAlgParamDAO changeAlgParamDAO) {
		this.changeAlgParamDAO = changeAlgParamDAO;
	}

	public McBtsBizProxy getMcBtsBizProxy() {
		return mcBtsBizProxy;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	public String getRestrictFlag() {
		return restrictFlag;
	}

	public void setRestrictFlag(String restrictFlag) {
		this.restrictFlag = restrictFlag;
	}

}
