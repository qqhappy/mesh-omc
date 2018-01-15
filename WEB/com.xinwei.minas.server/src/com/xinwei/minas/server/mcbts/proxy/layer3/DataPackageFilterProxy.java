package com.xinwei.minas.server.mcbts.proxy.layer3;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.DataPackageFilter;

/**
 * DataPackageFilterProxy
 * 
 * @author fangping
 */
public interface DataPackageFilterProxy {
	/**
	 * 查询NE
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Object[] queryFromNE(Long moId) throws RemoteException, Exception;

	/**
	 * 根据过滤类型和数据包过滤List配置NE
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void config(Long moId, int filterType,
			List<DataPackageFilter> filterList) throws RemoteException,
			Exception;
}
