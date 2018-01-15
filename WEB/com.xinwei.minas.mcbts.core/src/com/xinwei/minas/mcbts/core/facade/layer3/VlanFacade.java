/**
 * 
 */
package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlan;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlanAttach;

/**
 * @author chenshaohua
 * 
 */
public interface VlanFacade extends Remote {

	/**
	 * ��ѯȫ����¼
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsVlan> queryAllByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * ���û�վ�������浽���ݿ�
	 * 
	 * @param mcBtsVlanList
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, List<McBtsVlan> mcBtsVlanList, Long moId) throws RemoteException,
			Exception;

	/**
	 * ��ѯ���ء�eytpe
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsVlanAttach queryAttacheByMoId(long moId)
			throws RemoteException, Exception;

}
