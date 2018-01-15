/**
 * 
 */
package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsACL;

/**
 * @author chenshaohua
 * 
 */
public interface ACLFacade extends Remote {

	/**
	 * ��ѯȫ��ʵ��
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsACL> queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * ���վ���ã�������
	 * 
	 * @param mcBtsACLList
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Long moId, List<McBtsACL> mcBtsACLList)
			throws RemoteException, Exception;

	/**
	 * ɾ��һ����¼
	 * 
	 * @param temp
	 */
	public void delete(McBtsACL temp) throws RemoteException, Exception;
}
