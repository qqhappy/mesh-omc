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
	 * 查询所有记录
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsRepeater> queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * 配置到基站，并保存数据库
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
	 * 删除一条记录
	 * 
	 * @param temp
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void delete(McBtsRepeater temp) throws RemoteException, Exception;
}
