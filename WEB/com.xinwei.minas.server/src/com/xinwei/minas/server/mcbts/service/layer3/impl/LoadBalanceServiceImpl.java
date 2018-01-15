package com.xinwei.minas.server.mcbts.service.layer3.impl;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.LoadBalanceFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer2.TConfChangeAlgParam;
import com.xinwei.minas.mcbts.core.model.layer3.TConfLoadBalance;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.core.conf.service.ISyncService;
import com.xinwei.minas.server.mcbts.dao.layer2.ChangeAlgParamDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.LoadBalanceDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.LoadBalanceService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizRecord;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 负载均衡基本业务接口实现
 * 
 * @author yinbinqiang
 * 
 */
public class LoadBalanceServiceImpl implements LoadBalanceService, ISyncService {

	private McBtsBizProxy mcBtsBizProxy;
	private LoadBalanceDAO loadBalanceDAO;
	private ChangeAlgParamDAO changeAlgParamDAO;

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	public void setLoadBalanceDAO(LoadBalanceDAO loadBalanceDAO) {
		this.loadBalanceDAO = loadBalanceDAO;
	}

	public void setChangeAlgParamDAO(ChangeAlgParamDAO changeAlgParamDAO) {
		this.changeAlgParamDAO = changeAlgParamDAO;
	}

	public void syncFromEms2Ne(Long moId) throws Exception {
		config(queryByMoId(moId));
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		List<TConfLoadBalance> balanceList = loadBalanceDAO.queryAll();

		for (TConfLoadBalance balance : balanceList) {
			long moId = balance.getMoId();
			// 如果moId< 0或集合不包括当前moId,就进行下一个
			if (moId < 0 || !moIdList.contains(moId))
				continue;

			business.getCell("algorithm_switch").putContent(
					moId,
					toJSON("algorithm_switch",
							String.valueOf(balance.getAlgorithm_switch())));
			business.getCell("load_high_threshold").putContent(
					moId,
					toJSON("load_high_threshold",
							String.valueOf(balance.getLoad_high_threshold())));
			business.getCell("load_msg_send_period").putContent(
					moId,
					toJSON("load_msg_send_period",
							String.valueOf(balance.getLoad_msg_send_period())));
			business.getCell("load_diff_threshold").putContent(
					moId,
					toJSON("load_diff_threshold",
							String.valueOf(balance.getLoad_diff_threshold())));
			business.getCell("neighbor_bts_power_over_num")
					.putContent(
							moId,
							toJSON("neighbor_bts_power_over_num", String
									.valueOf(balance
											.getNeighbor_bts_power_over_num())));
			business.getCell("load_balance_signal_remains")
					.putContent(
							moId,
							toJSON("load_balance_signal_remains", String
									.valueOf(balance
											.getLoad_balance_signal_remains())));
			business.getCell("ut_load_balance_period")
					.putContent(
							moId,
							toJSON("ut_load_balance_period", String
									.valueOf(balance
											.getUt_load_balance_period())));
			business.getCell("user_algorithm_param").putContent(
					moId,
					toJSON("user_algorithm_param",
							String.valueOf(balance.getUser_algorithm_param())));
			business.getCell("arithmetic_switch").putContent(
					moId,
					toJSON("arithmetic_switch",
							String.valueOf(balance.getArithmetic_switch())));
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
		TConfLoadBalance balance = this.queryByMoId(mcBts.getMoId());
		if (balance == null) {
			balance = new TConfLoadBalance();
			balance.setMoId(mcBts.getMoId());
		}

		// 填充对象
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value)) {
				return;
			}

			if (key.equals("algorithm_switch")) {
				balance.setAlgorithm_switch(Integer.valueOf(value));
			} else if (key.equals("load_high_threshold")) {
				balance.setLoad_high_threshold(Integer.valueOf(value));
			} else if (key.equals("load_msg_send_period")) {
				balance.setLoad_msg_send_period(Integer.valueOf(value));
			} else if (key.equals("load_diff_threshold")) {
				balance.setLoad_diff_threshold(Integer.valueOf(value));
			} else if (key.equals("neighbor_bts_power_over_num")) {
				balance.setNeighbor_bts_power_over_num(Integer.valueOf(value));
			} else if (key.equals("load_balance_signal_remains")) {
				balance.setLoad_balance_signal_remains(Integer.valueOf(value));
			} else if (key.equals("ut_load_balance_period")) {
				balance.setUt_load_balance_period(Integer.valueOf(value));
			} else if (key.equals("user_algorithm_param")) {
				balance.setUser_algorithm_param(Integer.valueOf(value));
			} else if (key.equals("arithmetic_switch")) {
				balance.setArithmetic_switch(Integer.valueOf(value));
			}
		}

		// 保存对象
		LoadBalanceFacade facade = AppContext.getCtx().getBean(
				LoadBalanceFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, balance);

	}

	@Override
	public TConfLoadBalance queryByMoId(Long moId) throws Exception {
		return loadBalanceDAO.queryByMoId(moId);
	}

	@Override
	public void config(TConfLoadBalance loadBalance) throws Exception {
		Long moId = loadBalance.getMoId();
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		TConfChangeAlgParam changeAlgParam = changeAlgParamDAO
				.queryByMoId(moId);
		// 切换算法参数配置的切换测量周期值，对终端负载平衡周期的限制
		if (changeAlgParam != null) {
			// 切换测量周期
			int handoffPeriod = changeAlgParam.getM_cpe_cm_ho_period();
			// 终端负载平衡周期
			int balancePeriod = loadBalance.getUt_load_balance_period();
			if (handoffPeriod != 0) {
				if (balancePeriod < handoffPeriod
						|| (balancePeriod / handoffPeriod) % 10 != 0) {
					String param = bts.getName();
					String ret = OmpAppContext
							.getMessage("mcbts_load_balance_rule");
					throw new Exception(MessageFormat.format(ret, param));
				}
			}
		}

		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			GenericBizData data = new GenericBizData("mcbts_load_balance");
			data.addEntity(loadBalance);
			try {
				mcBtsBizProxy.config(moId, data);
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		loadBalanceDAO.saveOrUpdate(loadBalance);
	}

	private TConfLoadBalance queryFromNE(Long moId) throws Exception {
		GenericBizData genericBizData = new GenericBizData("mcbts_load_balance");
		GenericBizData result = mcBtsBizProxy.query(moId, genericBizData);

		TConfLoadBalance loadBalance = new TConfLoadBalance();

		GenericBizRecord record = null;
		List<GenericBizRecord> records = result.getRecords();
		if (!records.isEmpty()) {
			record = records.get(0);
		}

		loadBalance.setMoId(moId);
		loadBalance.setAlgorithm_switch(parsePropToInt(record,
				"algorithm_switch"));
		loadBalance.setLoad_high_threshold(parsePropToInt(record,
				"load_high_threshold"));
		loadBalance.setLoad_msg_send_period(parsePropToInt(record,
				"load_msg_send_period"));
		loadBalance.setLoad_diff_threshold(parsePropToInt(record,
				"load_diff_threshold"));
		loadBalance.setNeighbor_bts_power_over_num(parsePropToInt(record,
				"neighbor_bts_power_over_num"));
		loadBalance.setLoad_balance_signal_remains(parsePropToInt(record,
				"load_balance_signal_remains"));
		loadBalance.setUt_load_balance_period(parsePropToInt(record,
				"ut_load_balance_period"));
		loadBalance.setUser_algorithm_param(parsePropToInt(record,
				"user_algorithm_param"));
		loadBalance.setArithmetic_switch(parsePropToInt(record,
				"arithmetic_switch"));

		return loadBalance;
	}

	private static int parsePropToInt(GenericBizRecord record,
			String propertyName) {
		if (record == null)
			return 0;
		Object obj = record.getPropertyValue(propertyName).getValue();
		String str = String.valueOf(obj);
		return Integer.parseInt(str);
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		TConfLoadBalance loadBalance = this.queryByMoId(moId);
		if (loadBalance != null) {
			this.config(loadBalance);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		TConfLoadBalance result = queryFromNE(moId);
		TConfLoadBalance dataFromDB = queryByMoId(moId);

		if (dataFromDB == null) {
			SequenceService sequenceService = AppContext.getCtx().getBean(
					SequenceService.class);
			result.setIdx(sequenceService.getNext());
		} else {
			result.setIdx(dataFromDB.getIdx());
		}

		result.setMoId(moId);

		loadBalanceDAO.saveOrUpdate(result);
	}
}
