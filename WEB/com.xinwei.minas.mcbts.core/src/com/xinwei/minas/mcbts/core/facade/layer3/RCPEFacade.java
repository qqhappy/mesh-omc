package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPE;
import com.xinwei.minas.mcbts.core.model.layer3.TConfRCPEItem;
/**
 * RCPE����ҵ������
 * @author yinbinqiang
 *
 */
public interface RCPEFacade extends Remote {
	/**
	 * ��ѯRCPE���û�����Ϣ
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfRCPE queryByMoId(Long moId) throws RemoteException, Exception;
	
	/**
	 * ����RCPE������Ϣ
	 * @param remoteBts
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfRCPE rcpe) throws RemoteException, Exception;
	
	/**
	 * ɾ��ָ��RCPE��Ϣ
	 * @param rcpeItem
	 * @throws Exception
	 */
	@Loggable
	public void deleteRcpe(OperObject operObject, TConfRCPEItem rcpeItem) throws Exception;
}
