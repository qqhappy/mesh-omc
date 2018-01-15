
package com.xinwei.minas.mcbts.core.facade.oamManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.ActiveUserInfo;

/**
 * 
 * �����ն��б�����
 * 
 * @author fangping
 * 
 */

public interface McBtsOnlineTerminalListFacade extends Remote {

	/**
	 * ���վ��ѯ���к�
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<ActiveUserInfo> queryOnlineTerminalListFromNE(Long moId) throws RemoteException,
			Exception;

	/**
	 * �����ݿ��ѯ��վ���к�
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<ActiveUserInfo> queryOnlineTerminalListFromDB(long moId) throws RemoteException,
			Exception;

}
