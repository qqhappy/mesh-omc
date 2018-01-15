/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-19	| fangping 	| 	create the file                       
 */
package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.common.DataPackageFilter;

public interface DataPackageFilterFacade extends Remote {

	/**
	 * 获取过滤类型
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public int queryFilterType() throws RemoteException, Exception;

	/**
	 * 从数据库查询所有信息
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<DataPackageFilter> queryAllFormEMS() throws RemoteException,
			Exception;

	/**
	 * 向基站查询数据包过滤准则
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Object[] queryFromNE(Long moId) throws RemoteException, Exception;

	/**
	 * 配置数据包过滤规则给数据库,并判断是否下发给所有基站
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, int filterType, List<DataPackageFilter> filterList)
			throws RemoteException, Exception;

	/**
	 * 配置基站的数据包过滤规则
	 * 
	 * @param moId
	 * @param filterList
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void config(OperObject operObject, Long moId, int filterType,
			List<DataPackageFilter> filterList) throws RemoteException,
			Exception;

}
