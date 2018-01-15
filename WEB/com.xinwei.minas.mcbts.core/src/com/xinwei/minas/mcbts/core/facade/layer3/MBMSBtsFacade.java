package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.TConfMBMSBts;
/**
 * ͬ����վ���û���ҵ����Ϣ����
 * @author yinbinqiang
 *
 */
public interface MBMSBtsFacade extends Remote {
	/**
	 * ��ѯͬ����վ���û�����Ϣ
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfMBMSBts queryByMoId(Long moId) throws RemoteException, Exception;
	
	/**
	 * ����ͬ����վ������Ϣ
	 * @param remoteBts
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfMBMSBts mbmsBts) throws RemoteException, Exception;
}
