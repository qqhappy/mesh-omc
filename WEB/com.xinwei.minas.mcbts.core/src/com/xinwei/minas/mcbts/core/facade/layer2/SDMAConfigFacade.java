package com.xinwei.minas.mcbts.core.facade.layer2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer2.SDMAConfig;
/**
 * SDMA配置基本业务信息门面
 * @author fangping
 *
 */
public interface SDMAConfigFacade extends Remote {
	
	/**
	 * 查询SDMA基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SDMAConfig queryByMoId(Long moId) throws RemoteException, Exception;
	
	/**
	 * 配置SDMA基本信息
	 * @param loadBalance
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, SDMAConfig sdmaConfig) throws RemoteException, Exception;
}
