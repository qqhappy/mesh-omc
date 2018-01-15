/**
 * 
 */
package com.xinwei.minas.mcbts.core.facade.layer2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.facade.MoBizFacade;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer2.TConfChangeAlgParam;

/**
 * �л��㷨��������
 * 
 * @author chenshaohua
 * 
 */
public interface ChangeAlgParamFacade extends MoBizFacade<TConfChangeAlgParam> {

	/**
	 * �����ݿ��ѯ�㷨������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfChangeAlgParam queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * �����㷨������Ϣ
	 * 
	 * @param changeAlgParam
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfChangeAlgParam changeAlgParam)
			throws RemoteException, Exception;

	/**
	 * ��ȡ��������־
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public String getRestrictAreaFlag() throws RemoteException, Exception;
}
