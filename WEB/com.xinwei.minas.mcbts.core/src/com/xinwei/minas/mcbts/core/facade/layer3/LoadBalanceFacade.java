package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.TConfLoadBalance;
/**
 * ���ؾ������û���ҵ����Ϣ����
 * @author yinbinqiang
 *
 */
public interface LoadBalanceFacade extends Remote {
	
	/**
	 * ��ѯ���ؾ��������Ϣ
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfLoadBalance queryByMoId(Long moId) throws RemoteException, Exception;
	
	/**
	 * ���ø��ؾ��������Ϣ
	 * @param loadBalance
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfLoadBalance loadBalance) throws RemoteException, Exception;
}
