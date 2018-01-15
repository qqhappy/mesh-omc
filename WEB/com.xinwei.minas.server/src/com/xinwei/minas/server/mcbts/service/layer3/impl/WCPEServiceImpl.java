package com.xinwei.minas.server.mcbts.service.layer3.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.WCPEFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfWCPE;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfWCPEDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.WCPEService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.model.biz.GenericBizRecord;
import com.xinwei.omp.server.OmpAppContext;

/**
 * WCPE业务层接口实现
 * 
 * @author yinbinqiang
 * 
 */
public class WCPEServiceImpl implements WCPEService {

	private TConfWCPEDAO wcpedao;
	private McBtsBizProxy mcBtsBizProxy;

	public void setWcpedao(TConfWCPEDAO wcpedao) {
		this.wcpedao = wcpedao;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	@Override
	public TConfWCPE queryByMoId(Long moId) throws Exception {
		return wcpedao.queryByMoId(moId);
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		List<TConfWCPE> wcpeList = wcpedao.queryAll();
		for (TConfWCPE wcpe : wcpeList) {
			long moId = wcpe.getMoId();
			// 如果moId< 0或集合不包括当前moId,就进行下一个
			if (moId < 0 || !moIdList.contains(moId))
				continue;

			business.getCell("workMode").putContent(moId,
					toJSON("workMode", String.valueOf(wcpe.getWorkMode())));
			business.getCell("primaryWCPE").putContent(moId,
					toJSON("primaryWCPE", wcpe.getPrimaryWCPE()));
			business.getCell("standbyWCPE").putContent(moId,
					toJSON("standbyWCPE", wcpe.getStandbyWCPE()));
			business.getCell("sacMac").putContent(moId,
					toJSON("sacMac", wcpe.getSacMac()));
			business.getCell("sacMac2").putContent(moId,
					toJSON("sacMac2", wcpe.getSacMac2()));
			business.getCell("sacMac3").putContent(moId,
					toJSON("sacMac3", wcpe.getSacMac3()));
			business.getCell("sacMac4").putContent(moId,
					toJSON("sacMac4", wcpe.getSacMac4()));
			business.getCell("sacMac5").putContent(moId,
					toJSON("sacMac5", wcpe.getSacMac5()));
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
		TConfWCPE wcpe = this.queryByMoId(mcBts.getMoId());
		if (wcpe == null) {
			wcpe = new TConfWCPE();
			wcpe.setMoId(mcBts.getMoId());
		}

		// 填充对象
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value)) {
				return;
			}

			if (key.equals("workMode")) {
				wcpe.setWorkMode(Integer.parseInt(value));
			} else if (key.equals("primaryWCPE")) {
				wcpe.setPrimaryWCPE(value);
			} else if (key.equals("standbyWCPE")) {
				wcpe.setStandbyWCPE(value);
			} else if (key.equals("sacMac")) {
				wcpe.setSacMac(value);
			} else if (key.equals("sacMac2")) {
				wcpe.setSacMac2(value);
			} else if (key.equals("sacMac3")) {
				wcpe.setSacMac3(value);
			} else if (key.equals("sacMac4")) {
				wcpe.setSacMac4(value);
			} else if (key.equals("sacMac5")) {
				wcpe.setSacMac5(value);
			}
		}

		// 保存对象
		WCPEFacade facade = AppContext.getCtx().getBean(WCPEFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, wcpe);
	}

	@Override
	public void config(TConfWCPE wcpe) throws Exception {
		Long moId = wcpe.getMoId();
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			GenericBizData data = new GenericBizData("t_conf_wcpe");
			data.addEntity(wcpe);
			try {
				mcBtsBizProxy.config(moId, data);
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		wcpedao.saveOrUpdate(wcpe);
	}

	@Override
	public TConfWCPE query(Long moId) throws Exception {
		TConfWCPE wcpe = null;
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			GenericBizData data = new GenericBizData("t_conf_wcpe");
			try {
				GenericBizData resposeData = mcBtsBizProxy.query(moId, data);
				if (resposeData != null) {
					wcpe = convertBizDataToModel(resposeData);
				}
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("query_data_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		return wcpe;
	}

	private TConfWCPE convertBizDataToModel(GenericBizData resposeData) {
		TConfWCPE wcpe = new TConfWCPE();
		GenericBizRecord record = null;
		List<GenericBizRecord> records = resposeData.getRecords();
		if (!records.isEmpty()) {
			record = records.get(0);
		}
		GenericBizProperty property = record.getPropertyValue("workMode");
		wcpe.setWorkMode(Integer.parseInt(property.getValue().toString()));
		property = record.getPropertyValue("primaryWCPE");
		wcpe.setPrimaryWCPE(property.getValue().toString());
		property = record.getPropertyValue("standbyWCPE");
		wcpe.setStandbyWCPE(property.getValue().toString());
		property = record.getPropertyValue("sacMac");
		wcpe.setSacMac(property.getValue().toString());
		property = record.getPropertyValue("sacMac2");
		wcpe.setSacMac2(property.getValue().toString());
		property = record.getPropertyValue("sacMac3");
		wcpe.setSacMac3(property.getValue().toString());
		property = record.getPropertyValue("sacMac4");
		wcpe.setSacMac4(property.getValue().toString());
		property = record.getPropertyValue("sacMac5");
		wcpe.setSacMac5(property.getValue().toString());
		return wcpe;
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		TConfWCPE wCPE = this.queryByMoId(moId);
		if (wCPE != null) {
			this.config(wCPE);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		TConfWCPE result = query(moId);
		TConfWCPE dataFromDB = queryByMoId(moId);

		if (dataFromDB == null) {
			SequenceService sequenceService = AppContext.getCtx().getBean(
					SequenceService.class);
			result.setIdx(sequenceService.getNext());
		} else {
			result.setIdx(dataFromDB.getIdx());
		}
		result.setMoId(moId);

		wcpedao.saveOrUpdate(result);
	}

}
