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
 * 切换算法参数门面
 * 
 * @author chenshaohua
 * 
 */
public interface ChangeAlgParamFacade extends MoBizFacade<TConfChangeAlgParam> {

	/**
	 * 向数据库查询算法参数信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfChangeAlgParam queryByMoId(Long moId) throws RemoteException,
			Exception;

	/**
	 * 配置算法参数信息
	 * 
	 * @param changeAlgParam
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, TConfChangeAlgParam changeAlgParam)
			throws RemoteException, Exception;

	/**
	 * 获取限制区标志
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public String getRestrictAreaFlag() throws RemoteException, Exception;
}
