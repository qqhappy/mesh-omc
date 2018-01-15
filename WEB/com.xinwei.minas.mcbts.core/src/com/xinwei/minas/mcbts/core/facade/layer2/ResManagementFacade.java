/**
 * 
 */
package com.xinwei.minas.mcbts.core.facade.layer2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer2.TConfResmanagement;

/**
 * @author chenshaohua
 * 
 */
public interface ResManagementFacade extends MoBizFacade<TConfResmanagement> {
	/**
	 * 查询无线资源配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfResmanagement queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * 配置无线资源信息
	 * 
	 * @param resManagement
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfResmanagement resManagement)
			throws RemoteException, Exception;

	/**
	 * 从网元获得配置信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfResmanagement queryFromNE(Long moId) throws RemoteException,
			Exception;
}
