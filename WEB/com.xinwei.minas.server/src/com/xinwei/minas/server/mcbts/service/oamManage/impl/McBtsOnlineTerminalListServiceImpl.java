package com.xinwei.minas.server.mcbts.service.oamManage.impl;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.common.ActiveUserInfo;
import com.xinwei.minas.server.mcbts.proxy.oammanager.McBtsOnlineTerminalListProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.oamManage.McBtsOnlineTerminalListService;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * BTS在线终端列表service实现
 * 
 * @author fangping
 * 
 */

public class McBtsOnlineTerminalListServiceImpl implements
		McBtsOnlineTerminalListService {

	private McBtsOnlineTerminalListProxy mcBtsOnlineTerminalListProxy;

	public List<ActiveUserInfo> queryOnlineTerminalListFromNE(Long moId)
			throws Exception {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		if (mcBts == null && moId >= 0) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		// TODO
		if (mcBts != null && mcBts.isConfigurable()) {
			List<ActiveUserInfo> activeUserInfo = mcBtsOnlineTerminalListProxy
					.queryOnlineTerminalList(moId);
			return activeUserInfo;
		}
		return null;
	}

	public void setMcBtsOnlineTerminalListProxy(
			McBtsOnlineTerminalListProxy mcBtsOnlineTerminalListProxy) {
		this.mcBtsOnlineTerminalListProxy = mcBtsOnlineTerminalListProxy;
	}

}
