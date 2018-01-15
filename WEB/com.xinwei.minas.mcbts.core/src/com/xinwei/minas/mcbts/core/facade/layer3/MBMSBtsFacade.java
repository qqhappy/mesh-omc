package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
/**
 * 同播基站配置基本业务信息门面
 * @author yinbinqiang
 *
 */
public interface MBMSBtsFacade extends Remote {
	/**
	 * 查询同播基站配置基本信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfMBMSBts queryByMoId(Long moId) throws RemoteException, Exception;
	
	/**
	 * 配置同播基站基本信息
	 * @param remoteBts
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfMBMSBts mbmsBts) throws RemoteException, Exception;
}
