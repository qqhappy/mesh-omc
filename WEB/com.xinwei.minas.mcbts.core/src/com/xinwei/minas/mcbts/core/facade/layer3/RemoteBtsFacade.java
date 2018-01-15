package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;

/**
 * 远距离基站配置基本业务信息门面
 * 
 * @author yinbinqiang
 * 
 */
public interface RemoteBtsFacade extends Remote {

	/**
	 * 查询远距离基站配置基本信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfRemoteBts queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * 配置远距离基站基本信息
	 * 
	 * @param remoteBts
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfRemoteBts remoteBts) throws RemoteException,
			Exception;

	/**
	 * 配置远距离基站基本信息
	 * 
	 * @param remoteBts
	 * @param isSyncConfig
	 *            是否同步配置邻接表
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfRemoteBts remoteBts, boolean isSyncConfig)
			throws RemoteException, Exception;
}
