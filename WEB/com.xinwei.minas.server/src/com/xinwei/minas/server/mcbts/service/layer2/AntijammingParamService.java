/**
 * 
 */
package com.xinwei.minas.server.mcbts.service.layer2;

import java.rmi.RemoteException;

import com.xinwei.minas.mcbts.core.model.layer2.TConfAntijammingParam;
import com.xinwei.minas.mcbts.core.model.layer2.TConfFreqSet;

/**
 * @author chenshaohua
 *
 */
public interface AntijammingParamService {

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
	 * @param freqSetEntity
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(TConfFreqSet freqSetEntity) throws RemoteException, Exception;;
}
