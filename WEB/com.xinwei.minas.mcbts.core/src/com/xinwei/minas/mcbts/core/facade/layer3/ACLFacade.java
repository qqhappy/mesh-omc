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
	 * 查询全部实体
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsACL> queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * 向基站配置，并保存
	 * 
	 * @param mcBtsACLList
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Long moId, List<McBtsACL> mcBtsACLList)
			throws RemoteException, Exception;

	/**
	 * 删除一条记录
	 * 
	 * @param temp
	 */
	public void delete(McBtsACL temp) throws RemoteException, Exception;
}
