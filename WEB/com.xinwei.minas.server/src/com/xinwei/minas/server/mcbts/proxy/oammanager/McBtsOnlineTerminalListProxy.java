package com.xinwei.minas.server.mcbts.proxy.oammanager;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.ActiveUserInfo;

/**
 * 
 * BTS�����ն��б�proxy
 * 
 * @author fangping
 * 
 */

public interface McBtsOnlineTerminalListProxy {

	public List<ActiveUserInfo> queryOnlineTerminalList(Long moId) throws Exception,
			UnsupportedOperationException;

//	public McBtsOnlineTerminalList queryRRUOnlineTerminalList(Long moId) throws Exception,
//			UnsupportedOperationException;

}
