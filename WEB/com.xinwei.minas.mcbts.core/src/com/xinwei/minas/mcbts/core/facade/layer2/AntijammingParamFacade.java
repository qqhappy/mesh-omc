/**
 * 
 */
package com.xinwei.minas.mcbts.core.facade.layer2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer2.TConfAntijammingParam;
import com.xinwei.minas.mcbts.core.model.layer2.TConfFreqSet;

/**
 * @author chenshaohua
 *
 */
public interface AntijammingParamFacade extends Remote {

	/**
	 * 从数据库中查询抗干扰信息
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public TConfAntijammingParam queryByMoId(Long moId) throws RemoteException, Exception;
	
	/**
	 * 向基站发送抗干扰配置消息，并保存到数据库
	 * @param antijamming
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfAntijammingParam antijamming) throws RemoteException, Exception;

	/**
	 * 向基站发送频点集配置消息，并保存到数据库
	 * @param freqSetParam
	 */
	public void config(TConfFreqSet freqSetParam) throws RemoteException, Exception;

	
}
