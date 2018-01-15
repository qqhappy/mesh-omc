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
	 * ��ѯ������Դ������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfResmanagement queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * ����������Դ��Ϣ
	 * 
	 * @param resManagement
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfResmanagement resManagement)
			throws RemoteException, Exception;

	/**
	 * ����Ԫ���������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfResmanagement queryFromNE(Long moId) throws RemoteException,
			Exception;
}
