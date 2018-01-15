package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRemoteBts;

/**
 * Զ�����վ���û���ҵ����Ϣ����
 * 
 * @author yinbinqiang
 * 
 */
public interface RemoteBtsFacade extends Remote {

	/**
	 * ��ѯԶ�����վ���û�����Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfRemoteBts queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * ����Զ�����վ������Ϣ
	 * 
	 * @param remoteBts
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfRemoteBts remoteBts) throws RemoteException,
			Exception;

	/**
	 * ����Զ�����վ������Ϣ
	 * 
	 * @param remoteBts
	 * @param isSyncConfig
	 *            �Ƿ�ͬ�������ڽӱ�
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfRemoteBts remoteBts, boolean isSyncConfig)
			throws RemoteException, Exception;
}
