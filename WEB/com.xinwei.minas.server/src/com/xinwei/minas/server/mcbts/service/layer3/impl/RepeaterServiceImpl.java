/**
 * 
 */
package com.xinwei.minas.server.mcbts.service.layer3.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.RepeaterFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsRepeater;
import com.xinwei.minas.mcbts.core.model.layer3.WrappedRepeater;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.mcbts.core.utils.FreqConvertUtil;
import com.xinwei.minas.server.mcbts.dao.layer3.RepeaterDAO;
import com.xinwei.minas.server.mcbts.proxy.layer3.RepeaterProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.RepeaterService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * @author chenshaohua
 * 
 */
public class RepeaterServiceImpl implements RepeaterService {

	private Log log = LogFactory.getLog(RepeaterServiceImpl.class);

	private RepeaterDAO repeaterDAO;

	private RepeaterProxy repeaterProxy;

	public List<McBtsRepeater> queryByMoId(Long moId) throws Exception {
		List<McBtsRepeater> list = repeaterDAO.queryByMoId(moId);

		FreqConvertUtil freqConvertUtil = new FreqConvertUtil();
		freqConvertUtil.setFreqType(McBtsCache.getInstance().queryByMoId(moId)
				.getBtsFreqType());

		for (McBtsRepeater rep : list) {
			rep.setMiddleFreq(freqConvertUtil.getMidFreqValue(rep.getOffset()));
		}

		return list;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		for (long moId : moIdList) {
			List<McBtsRepeater> repeaterList = queryByMoId(moId);
			if (repeaterList == null || repeaterList.isEmpty())
				continue;

			StringBuilder repeaterSb = new StringBuilder();
			for (McBtsRepeater repeater : repeaterList) {
				repeaterSb.append(",{\"offset\":\"")
						.append(repeater.getOffset()).append("\"}");
			}
			String content = "\"mcbts_repeater\":[" + repeaterSb.substring(1)
					+ "]";

			business.getCell("mcbts_repeater").putContent(moId, content);
		}

	}

	private static String toJSON(String key, String value) {
		return "\"" + key + "\":\"" + value + "\"";
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// 形如[1750];[1825]
		// 获得基站模型
		long btsId = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);

		// 创建模型
		List<McBtsRepeater> repeaterList = new ArrayList<McBtsRepeater>();

		// 填充对象
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value))
				continue;

			String[] repeaterStrs = value.replaceAll("(\\[|\\])", "")
					.split(";");
			for (String repeaterStr : repeaterStrs) {
				McBtsRepeater repeater = new McBtsRepeater();
				repeater.setMoId(mcBts.getMoId());
				repeater.setOffset(Integer.parseInt(repeaterStr));

				repeaterList.add(repeater);
			}
		}

		// 保存对象
		RepeaterFacade facade = AppContext.getCtx().getBean(
				RepeaterFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, mcBts.getMoId(), repeaterList);

	}

	private List<McBtsRepeater> queryFromNe(Long moId) throws Exception {
		WrappedRepeater wrappedRepeater = null;

		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			GenericBizData data = new GenericBizData("mcbts_repeater");

			// 发送消息
			try {
				wrappedRepeater = repeaterProxy.query(moId, data);
			} catch (Exception e) {
				log.error(e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}

		if (wrappedRepeater == null)
			return null;

		return wrappedRepeater.getMcBtsRepeaterList();
	}

	public void config(long moId, List<McBtsRepeater> mcBtsRepeaterList)
			throws Exception {
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			WrappedRepeater entity = new WrappedRepeater();
			entity.setMcBtsRepeaterList(mcBtsRepeaterList);
			GenericBizData data = new GenericBizData("mcbts_repeater");
			data.addEntity(entity);
			// 发送消息
			try {
				repeaterProxy.config(moId, data);
			} catch (Exception e) {
				log.error(e);
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}
		// 保存数据库
		try {
			repeaterDAO.saveOrUpdate(mcBtsRepeaterList, moId);

		} catch (Exception e) {
			log.error(e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		List<McBtsRepeater> mcBtsRepeaters = this.queryByMoId(moId);
		if (mcBtsRepeaters != null && !mcBtsRepeaters.isEmpty()) {
			config(moId, mcBtsRepeaters);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		List<McBtsRepeater> repeaterList = queryFromNe(moId);
		if (repeaterList == null) {
			return;
		}

		SequenceService sequenceService = AppContext.getCtx().getBean(
				SequenceService.class);
		for (McBtsRepeater repeater : repeaterList) {
			repeater.setIdx(sequenceService.getNext());
			repeater.setMoId(moId);
		}

		// 保存数据库
		try {
			repeaterDAO.saveOrUpdate(repeaterList, moId);

		} catch (Exception e) {
			log.error("Error saving repeater list.", e);
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}
	}

	public RepeaterDAO getRepeaterDAO() {
		return repeaterDAO;
	}

	public void setRepeaterDAO(RepeaterDAO repeaterDAO) {
		this.repeaterDAO = repeaterDAO;
	}

	public RepeaterProxy getRepeaterProxy() {
		return repeaterProxy;
	}

	public void setRepeaterProxy(RepeaterProxy repeaterProxy) {
		this.repeaterProxy = repeaterProxy;
	}

	@Override
	public void delete(McBtsRepeater temp) throws Exception {
		repeaterDAO.delete(temp);
	}

}
