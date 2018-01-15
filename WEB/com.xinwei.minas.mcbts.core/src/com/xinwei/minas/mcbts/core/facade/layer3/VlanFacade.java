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
	 * 查询全部记录
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsVlan> queryAllByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * 配置基站，并保存到数据库
	 * 
	 * @param mcBtsVlanList
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, List<McBtsVlan> mcBtsVlanList, Long moId) throws RemoteException,
			Exception;

	/**
	 * 查询开关、eytpe
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsVlanAttach queryAttacheByMoId(long moId)
			throws RemoteException, Exception;

}
