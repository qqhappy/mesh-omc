/**
 * 
 */
package com.xinwei.minas.server.mcbts.service.layer3.impl;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.layer3.TerminalBizParam;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.server.mcbts.dao.layer3.TerminalParamDAO;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.layer3.TerminalParamService;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.server.OmpAppContext;

/**
 * @author chenshaohua
 * 
 */
public class TerminalParamServiceImpl implements TerminalParamService {

	private McBtsBizProxy mcBtsBizProxy;

	private TerminalParamDAO terminalParamDAO;

	// private TerminalParamProxy terminalParamProxy;

	public List<TerminalBizParam> queryAllTerminalParam()
			throws RemoteException, Exception {
		return terminalParamDAO.queryAllTerminalParam();
	}

	public TerminalParamDAO getTerminalParamDAO() {
		return terminalParamDAO;
	}

	public void setTerminalParamDAO(TerminalParamDAO terminalParamDAO) {
		this.terminalParamDAO = terminalParamDAO;
	}

	// 向所有网元发送
	public void config(List<TerminalBizParam> terminalBizParamList)
			throws Exception {
		// 获取默认配置
		TerminalBizParam defaultParam = null;
		for (TerminalBizParam param : terminalBizParamList) {
			if (param.getIdx() == 0) {
				defaultParam = param;
				break;
			}
		}
		if (defaultParam == null) {
			throw new Exception(
					OmpAppContext
							.getMessage("TerminalParamService.no_default_param"));
		}
		GenericBizData data = new GenericBizData("mcbts_ut_biz_param");
		data.addEntity(defaultParam);
		// 下发默认配置
		try {
			List<McBts> btsList = McBtsCache.getInstance().queryAll();
			for (McBts mcBts : btsList) {
				if (mcBts.isConfigurable()) {
					mcBtsBizProxy.config(mcBts.getMoId(), data);
				}
			}
		} catch (Exception e) {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_config_failed_reason")
							+ e.getLocalizedMessage());
		}
		// 保存数据库
		try {
			terminalParamDAO.saveOrUpdate(terminalBizParamList);
		} catch (Exception e) {
			throw new Exception(
					OmpAppContext.getMessage("save_data_failed_reason")
							+ e.getLocalizedMessage());
		}
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		List<TerminalBizParam> terminalBizParams = this.queryAllTerminalParam();
		if (terminalBizParams == null || terminalBizParams.isEmpty()) {
			return;
		}
		TerminalBizParam defaultParam = null;
		for (TerminalBizParam param : terminalBizParams) {
			if (param.getIdx() == 0) {
				defaultParam = param;
			}
		}
		if (defaultParam == null) {
			throw new Exception(
					OmpAppContext
							.getMessage("TerminalParamService.no_default_param"));
		}
		GenericBizData data = new GenericBizData("mcbts_ut_biz_param");
		data.addEntity(defaultParam);
		mcBtsBizProxy.config(moId, data);
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		// to do nothing
	}

	public McBtsBizProxy getMcBtsBizProxy() {
		return mcBtsBizProxy;
	}

	public void setMcBtsBizProxy(McBtsBizProxy mcBtsBizProxy) {
		this.mcBtsBizProxy = mcBtsBizProxy;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		// 不需要实现,群配业务

	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// 不需要实现,群配业务

	}

}