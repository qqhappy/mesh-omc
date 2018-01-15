/**
 * 
 */
package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsTos;

/**
 * @author chenshaohua
 * 
 */
public interface TosConfFacade extends Remote {

	/**
	 *  查询全部tos配置
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsTos> queryAllTos() throws RemoteException, Exception;

	/**
	 * 向基站配置tos
	 * @param mcBtsTosList
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, List<McBtsTos> mcBtsTosList) throws RemoteException,
			Exception;
}
