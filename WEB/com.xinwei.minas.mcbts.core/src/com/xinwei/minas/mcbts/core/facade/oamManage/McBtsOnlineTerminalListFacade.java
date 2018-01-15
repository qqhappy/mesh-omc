
package com.xinwei.minas.mcbts.core.facade.oamManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.ActiveUserInfo;

/**
 * 
 * 在线终端列表门面
 * 
 * @author fangping
 * 
 */

public interface McBtsOnlineTerminalListFacade extends Remote {

	/**
	 * 向基站查询序列号
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<ActiveUserInfo> queryOnlineTerminalListFromNE(Long moId) throws RemoteException,
			Exception;

	/**
	 * 向数据库查询基站序列号
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<ActiveUserInfo> queryOnlineTerminalListFromDB(long moId) throws RemoteException,
			Exception;

}
