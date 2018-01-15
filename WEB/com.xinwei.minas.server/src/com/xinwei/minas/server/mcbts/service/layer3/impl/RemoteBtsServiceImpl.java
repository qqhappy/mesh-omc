package com.xinwei.minas.server.mcbts.service.layer3.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.layer3.MBMSBtsFacade;
import com.xinwei.minas.mcbts.core.facade.layer3.RemoteBtsFacade;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.mcbts.core.model.sysManage.Cell;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfMBMSBtsDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.TConfRemoteBtsDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.NeighborService;
import com.xinwei.minas.server.mcbts.service.layer3.RemoteBtsService;
import com.xinwei.minas.server.mcbts.service.sysManage.SimulcastManageService;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 远距离基站配置基本业务接口实现
 * 
 * @author yinbinqiang
 * 
 */
public class RemoteBtsServiceImpl implements RemoteBtsService {

	private TConfRemoteBtsDAO remoteBtsDAO;
	private TConfMBMSBtsDAO mbmsBtsDAO;
	private McBtsBizProxy mcBtsBizProxy;

	public void setRemoteBtsDAO(TConfRemoteBtsDAO remoteBtsDAO) {
		this.remoteBtsDAO = remoteBtsDAO;
	}

	public void setMbmsBtsDAO(TConfMBMSBtsDAO mbmsBtsDAO) {
		this.mbmsBtsDAO = mbmsBtsDAO;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	@Override
	public TConfRemoteBts queryByMoId(Long moId) throws Exception {
		return remoteBtsDAO.queryByMoId(moId);
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		List<TConfRemoteBts> remoteList = remoteBtsDAO.queryAll();
		if (remoteList == null || remoteList.isEmpty())
			return;

		for (TConfRemoteBts remote : remoteList) {
			long moId = remote.getMoId();
			// 如果moId< 0或集合不包括当前moId,就进行下一个
			if (moId < 0 || !moIdList.contains(moId))
				continue;

			business.getCell("flag").putContent(moId,
					toJSON("flag", String.valueOf(remote.getFlag())));
		}
	}

	private static String toJSON(String key, String value) {
		return "\"" + key + "\":\"" + value + "\"";
	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// 获取基站信息
		long btsId = Long.parseLong(hexBtsId, 16);
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);

		// 创建对象
		TConfRemoteBts remoteBts = this.queryByMoId(mcBts.getMoId());
		if (remoteBts == null) {
			remoteBts = new TConfRemoteBts();
			remoteBts.setMoId(mcBts.getMoId());
		}

		// 填充对象
		Map<String, Cell> cellMap = business.getCellMap();
		for (Entry<String, Cell> entry : cellMap.entrySet()) {
			String value = entry.getValue().getContentByBID(btsId);

			if (StringUtils.isBlank(value)) {
				return;
			}
			// REMOTE BTS只有一个cell:flag,因此不用判断KEY
			remoteBts.setFlag(Integer.parseInt(value));
		}

		// 保存对象
		RemoteBtsFacade facade = AppContext.getCtx().getBean(
				RemoteBtsFacade.class);
		OperObject operObject = OperObject.createBtsOperObject(hexBtsId);
		facade.config(operObject, remoteBts);
	}

	@Override
	public void config(TConfRemoteBts remoteBts, boolean isSyncConfig)
			throws Exception {
		Long moId = remoteBts.getMoId();
		McBts bts = McBtsCache.getInstance().queryByMoId(moId);
		if (bts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		TConfMBMSBts mbmsBts = mbmsBtsDAO.queryByMoId(remoteBts.getMoId());
		// 是否与支持同播基站冲突
		if (mbmsBts != null) {
			if (mbmsBts.getFlag() == TConfMBMSBts.FLAG_SUPPORT
					&& remoteBts.getFlag() == TConfRemoteBts.FLAG_SUPPORT) {
				throw new Exception(
						OmpAppContext
								.getMessage("mcbts_cor_broadcast_unsupported"));
			}
		}

		// 可配置状态下，需要通过Proxy向MO发送配置消息
		if (bts != null && bts.isConfigurable()) {
			// 转换模型
			GenericBizData data = new GenericBizData("t_conf_remote_bts");
			data.addEntity(remoteBts);
			try {
				mcBtsBizProxy.config(moId, data);
			} catch (UnsupportedOperationException e) {
				throw e;
			} catch (Exception e) {
				throw new Exception(
						OmpAppContext.getMessage("mcbts_config_failed_reason")
								+ e.getLocalizedMessage());
			}
		}

		remoteBtsDAO.saveOrUpdate(remoteBts);

		NeighborService neighborService = AppContext.getCtx().getBean(
				NeighborService.class);
		neighborService.sendNeighborConfig(remoteBts.getMoId(), isSyncConfig);

	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		TConfRemoteBts remoteBts = this.queryByMoId(moId);
		if (remoteBts != null) {
			this.config(remoteBts, false);
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		GenericBizData data = new GenericBizData("t_conf_remote_bts");
		GenericBizData mcbtsBizData = mcBtsBizProxy.query(moId, data);
		int flag = Integer.parseInt(String.valueOf(mcbtsBizData.getProperty(
				"flag").getValue()));

		TConfRemoteBts remoteBts = new TConfRemoteBts();
		remoteBts.setFlag(flag);
		remoteBts.setMoId(moId);

		TConfMBMSBts queryFromDB = mbmsBtsDAO.queryByMoId(moId);

		if (queryFromDB != null)
			remoteBts.setIdx(queryFromDB.getIdx());
		else {
			SequenceService sequenceService = AppContext.getCtx().getBean(
					SequenceService.class);

			remoteBts.setIdx(sequenceService.getNext());
		}

		remoteBtsDAO.saveOrUpdate(remoteBts);
	}

}
