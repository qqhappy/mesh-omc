/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-17	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.Simulcast;

/**
 * 
 * 同播资源门面接口
 * 
 * 
 * @author tiance
 * 
 */

public interface SimulcastManageFacade extends Remote {
	/**
	 * 获取同播资源列表
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<Simulcast> queryAll() throws RemoteException, Exception;

	/**
	 * 根据查询地域ID下的所有同播资源
	 * 
	 * @param districtId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<Simulcast> queryByDistrictId(long districtId)
			throws RemoteException, Exception;

	/**
	 * 新增或修改同播资源
	 * 
	 * @param simulcast
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void saveOrUpdate(OperObject operObject, Simulcast simulcast) throws RemoteException,
			Exception;

	/**
	 * 删除同播资源
	 * 
	 * @param simulcast
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void delete(OperObject operObject, Simulcast simulcast) throws RemoteException, Exception;

	/**
	 * 查询同步的状态
	 * 
	 * @return
	 */
	public boolean[] querySyncStatus() throws RemoteException, Exception;

	/**
	 * 对同播资源进行同步
	 * <p>
	 * toSync[0]为基站链路信息,toSync[1]为同播资源信息
	 * </p>
	 * <p>
	 * 目前只支持同播资源信息,之后再实现基站链路信息的同步
	 * </p>
	 * 
	 * @param toSync
	 *            : 为true时同步,false不同步
	 */
	@Loggable
	public void sync(OperObject operObject, boolean[] toSync) throws RemoteException, Exception;
}
