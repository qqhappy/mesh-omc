package com.xinwei.minas.server.mcbts.service.layer2.impl;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer2.ResManagementFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer2.TConfResmanagement;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.layer2.ResManagementDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer2.ResManagementService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizRecord;
import com.xinwei.omp.server.OmpAppContext;

public class ResManagementServiceImpl implements ResManagementService {

	Log log = LogFactory.getLog(ResManagementServiceImpl.class);

	private McBtsBizProxy mcBtsBizProxy;

	private ResManagementDAO resManagementDAO;

	public void setResManagementDAO(ResManagementDAO resManagementDAO) {
		this.resManagementDAO = resManagementDAO;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	public void config(TConfResmanagement resManagement)
			throws RemoteException, Exception {
		Long moId = resManagement.getMoId();
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			GenericBizData data = new GenericBizData("t_conf_resmanagement");
			data.addEntity(resManagement);
			try {
				mcBtsBizProxy.config(moId, data);
			} catch (Exception e) {
				log.error("Error configuring TConfResmanagement.", e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		try {
			resManagementDAO.saveOrUpdate(resManagement);
		} catch (Exception e) {
			log.error("Error saving resmanagement.", e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}

	}

	public TConfResmanagement queryByMoId(Long moId) throws RemoteException,
			Exception {
		return resManagementDAO.queryByMoId(moId);
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		List<TConfResmanagement> resList = resManagementDAO.queryAll();

		for (TConfResmanagement res : resList) {
			long moId = res.getMoId();
			// 如果moId< 0或集合不包括当前moId,就进行下一个
			if (moId < 0 || !moIdList.contains(moId))
				continue;

			business.getCell("bandwidth_requst_interval")
					.putContent(
							moId,
							toJSON("bandwidth_requst_interval",
									String.valueOf(res
											.getBandwidth_requst_interval())));
			business.getCell("session_release_threshold")
					.putContent(
							moId,
							toJSON("session_release_threshold",
									String.valueOf(res
											.getSession_release_threshold())));
			business.getCell("sustained_min_ul_signal_strength")
					.putContent(
							moId,
							toJSON("sustained_min_ul_signal_strength",
									String.valueOf(res
											.getSustained_min_ul_signal_strength())));
			business.getCell("max_dl_power_per_user").putContent(
					moId,
					toJSON("max_dl_power_per_user",
							String.valueOf(res.getMax_dl_power_per_user())));
			business.getCell("sustained_dl_bandwidth_per_user")
					.putContent(
							moId,
							toJSON("sustained_dl_bandwidth_per_user",
									String.valueOf(res
											.getSustained_dl_bandwidth_per_user())));
			business.getCell("sustained_ul_bandwidth_per_user")
					.putContent(
							moId,
							toJSON("sustained_ul_bandwidth_per_user",
									String.valueOf(res
											.getSustained_ul_bandwidth_per_user())));
			business.getCell("reserved_tch_for_access").putContent(
					moId,
					toJSON("reserved_tch_for_access",
							String.valueOf(res.getReserved_tch_for_access())));
			business.getCell("reserved_power_for_pc").putContent(
					moId,
					toJSON("reserved_power_for_pc",
							String.valueOf(res.getReserved_power_for_pc())));
			business.getCell("pc_range").putContent(moId,
					toJSON("pc_range", String.valueOf(res.getPc_range())));
			business.getCell("reassignment_step_size").putContent(
					moId,
					toJSON("reassignment_step_size",
							String.valueOf(res.getReassignment_step_size())));
			business.getCell("max_simultaneous_user").putContent(
					moId,
					toJSON("max_simultaneous_user",
							String.valueOf(res.getMax_simultaneous_user())));
			business.getCell("bandwidthClass0").putContent(
					moId,
					toJSON("bandwidthClass0",
							String.valueOf(res.getBandwidthClass0())));
			business.getCell("bandwidthClass1").putContent(
					moId,
					toJSON("bandwidthClass1",
							String.valueOf(res.getBandwidthClass1())));
			business.getCell("bandwidthClass2").putContent(
					moId,
					toJSON("bandwidthClass2",
							String.valueOf(res.getBandwidthClass2())));
			business.getCell("bandwidthClass3").putContent(
					moId,
					toJSON("bandwidthClass3",
							String.valueOf(res.getBandwidthClass3())));
			business.getCell("bandwidthClass4").putContent(
					moId,
					toJSON("bandwidthClass4",
							String.valueOf(res.getBandwidthClass4())));
			business.getCell("bandwidthClass5").putContent(
					moId,
					toJSON("bandwidthClass5",
							String.valueOf(res.getBandwidthClass5())));
			business.getCell("bandwidthClass6").putContent(
					moId,
					toJSON("bandwidthClass6",
							String.valueOf(res.getBandwidthClass6())));
			business.getCell("bandwidthClass7").putContent(
					moId,
					toJSON("bandwidthClass7",
							String.valueOf(res.getBandwidthClass7())));
			business.getCell("ul_modulation_mask").putContent(
					moId,
					toJSON("ul_modulation_mask",
							String.valueOf(res.getUl_modulation_mask())));
			business.getCell("dl_modulation_mask").putContent(
					moId,
					toJSON("dl_modulation_mask",
							String.valueOf(res.getDl_modulation_mask())));
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
		TConfResmanagement res = this.queryByMoId(mcBts.getMoId());
		if (res == null) {
			res = new TConfResmanagement();
			res.setMoId(mcBts.getMoId());
		}

		// 填充对象
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value)) {
				return;
			}
			
			if (key.equals("bandwidth_requst_interval")) {
				res.setBandwidth_requst_interval(Integer.parseInt(value));
			} else if (key.equals("session_release_threshold")) {
				res.setSession_release_threshold(Integer.parseInt(value));
			} else if (key.equals("sustained_min_ul_signal_strength")) {
				res.setSustained_min_ul_signal_strength(Integer.parseInt(value));
			} else if (key.equals("max_dl_power_per_user")) {
				res.setMax_dl_power_per_user(Integer.parseInt(value));
			} else if (key.equals("sustained_dl_bandwidth_per_user")) {
				res.setSustained_dl_bandwidth_per_user(Integer.parseInt(value));
			} else if (key.equals("sustained_ul_bandwidth_per_user")) {
				res.setSustained_ul_bandwidth_per_user(Integer.parseInt(value));
			} else if (key.equals("reserved_tch_for_access")) {
				res.setReserved_tch_for_access(Integer.valueOf(value));
			} else if (key.equals("reserved_power_for_pc")) {
				res.setReserved_power_for_pc(Integer.valueOf(value));
			} else if (key.equals("pc_range")) {
				res.setPc_range(Integer.valueOf(value));
			} else if (key.equals("reassignment_step_size")) {
				res.setReassignment_step_size(Integer.parseInt(value));
			} else if (key.equals("max_simultaneous_user")) {
				res.setMax_simultaneous_user(Integer.valueOf(value));
			} else if (key.equals("bandwidthClass0")) {
				res.setBandwidthClass0(Integer.valueOf(value));
			} else if (key.equals("bandwidthClass1")) {
				res.setBandwidthClass1(Integer.valueOf(value));
			} else if (key.equals("bandwidthClass2")) {
				res.setBandwidthClass2(Integer.valueOf(value));
			} else if (key.equals("bandwidthClass3")) {
				res.setBandwidthClass3(Integer.valueOf(value));
			} else if (key.equals("bandwidthClass4")) {
				res.setBandwidthClass4(Integer.valueOf(value));
			} else if (key.equals("bandwidthClass5")) {
				res.setBandwidthClass5(Integer.valueOf(value));
			} else if (key.equals("bandwidthClass6")) {
				res.setBandwidthClass6(Integer.valueOf(value));
			} else if (key.equals("bandwidthClass7")) {
				res.setBandwidthClass7(Integer.valueOf(value));
			} else if (key.equals("ul_modulation_mask")) {
				res.setUl_modulation_mask(Integer.parseInt(value));
			} else if (key.equals("dl_modulation_mask")) {
				res.setDl_modulation_mask(Integer.parseInt(value));
			}
		}

		// 保存对象
		ResManagementFacade facade = AppContext.getCtx().getBean(
				ResManagementFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, res);
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		TConfResmanagement resmanagement = this.queryByMoId(moId);
		if (resmanagement != null) {
			this.config(resmanagement);
		}

	}

	/**
	 * 同步基站数据到ems
	 */
	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		TConfResmanagement result = this.queryFromNE(moId);

		TConfResmanagement resFromDB = queryByMoId(moId);

		if (resFromDB != null)
			result.setIdx(resFromDB.getIdx());
		else {
			SequenceService sequenceService = AppContext.getCtx().getBean(
					SequenceService.class);
			long idx = sequenceService.getNext();

			result.setIdx(idx);
		}

		result.setMoId(moId);

		try {
			resManagementDAO.saveOrUpdate(result);
		} catch (Exception e) {
			log.error("Error saving resmanagement.", e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}
	}

	@Override
	public TConfResmanagement queryFromNE(Long moId) throws Exception {
		TConfResmanagement resManagement = null;
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			GenericBizData data = new GenericBizData("t_conf_resmanagement");
			try {
				GenericBizData resposeData = mcBtsBizProxy.query(moId, data);
				if (resposeData != null) {
					resManagement = convertBizDataToModel(resposeData);
				}
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("query_data_failed_reason")
								+ e.getLocalizedMessage());
			}

		}
		return resManagement;
	}

	private static int parsePropToInt(GenericBizRecord record,
			String propertyName) {
		if (record == null)
			return 0;
		Object obj = record.getPropertyValue(propertyName).getValue();
		String str = String.valueOf(obj);
		return Integer.parseInt(str);
	}

	private TConfResmanagement convertBizDataToModel(GenericBizData resposeData) {
		TConfResmanagement res = new TConfResmanagement();
		GenericBizRecord record = null;
		List<GenericBizRecord> records = resposeData.getRecords();
		if (!records.isEmpty()) {
			record = records.get(0);
		}

		res.setBandwidth_requst_interval(parsePropToInt(record,
				"bandwidth_requst_interval"));
		res.setBandwidthClass0(parsePropToInt(record, "bandwidthClass0"));
		res.setBandwidthClass1(parsePropToInt(record, "bandwidthClass1"));
		res.setBandwidthClass2(parsePropToInt(record, "bandwidthClass2"));
		res.setBandwidthClass3(parsePropToInt(record, "bandwidthClass3"));
		res.setBandwidthClass4(parsePropToInt(record, "bandwidthClass4"));
		res.setBandwidthClass5(parsePropToInt(record, "bandwidthClass5"));
		res.setBandwidthClass6(parsePropToInt(record, "bandwidthClass6"));
		res.setBandwidthClass7(parsePropToInt(record, "bandwidthClass7"));
		res.setDl_modulation_mask(parsePropToInt(record, "dl_modulation_mask"));
		res.setMax_dl_power_per_user(parsePropToInt(record,
				"max_dl_power_per_user"));
		res.setMax_simultaneous_user(parsePropToInt(record,
				"max_simultaneous_user"));
		res.setPc_range(parsePropToInt(record, "pc_range"));
		res.setReassignment_step_size(parsePropToInt(record,
				"reassignment_step_size"));
		res.setReserved_power_for_pc(parsePropToInt(record,
				"reserved_power_for_pc"));
		res.setReserved_tch_for_access(parsePropToInt(record,
				"reserved_tch_for_access"));
		res.setSession_release_threshold(parsePropToInt(record,
				"session_release_threshold"));
		res.setSustained_dl_bandwidth_per_user(parsePropToInt(record,
				"sustained_dl_bandwidth_per_user"));
		res.setSustained_min_ul_signal_strength(parsePropToInt(record,
				"sustained_min_ul_signal_strength"));
		res.setSustained_ul_bandwidth_per_user(parsePropToInt(record,
				"sustained_ul_bandwidth_per_user"));
		res.setUl_modulation_mask(parsePropToInt(record, "ul_modulation_mask"));

		return res;
	}
}
