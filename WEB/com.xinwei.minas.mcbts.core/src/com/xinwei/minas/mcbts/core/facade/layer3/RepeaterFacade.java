/**
 * 
 */
package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsRepeater;

/**
 * @author chenshaohua
 * 
 */
public interface RepeaterFacade extends Remote {

	/**
	 * ��ѯ���м�¼
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsRepeater> queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * ���õ���վ�����������ݿ�
	 * 
	 * @param operObject
	 * @param moId
	 * @param mcBtsRepeaterList
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, long moId,
			List<McBtsRepeater> mcBtsRepeaterList) throws RemoteException,
			Exception;

	/**
	 * ɾ��һ����¼
	 * 
	 * @param temp
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void delete(McBtsRepeater temp) throws RemoteException, Exception;
}
