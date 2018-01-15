package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.TConfLoadBalance;
/**
 * 负载均衡配置基本业务信息门面
 * @author yinbinqiang
 *
 */
public interface LoadBalanceFacade extends Remote {
	
	/**
	 * 查询负载均衡基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfLoadBalance queryByMoId(Long moId) throws RemoteException, Exception;
	
	/**
	 * 配置负载均衡基本信息
	 * @param loadBalance
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfLoadBalance loadBalance) throws RemoteException, Exception;
}
