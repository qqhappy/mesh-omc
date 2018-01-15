/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-29	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.LocationArea;

/**
 * 
 * 位置区门面接口
 * 
 * 
 * @author chenshaohua
 * 
 */

public interface LocAreaFacade extends Remote {

	/**
	 * 查询全部记录
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<LocationArea> queryAll() throws RemoteException, Exception;

	/**
	 * 新增或更新位置区信息
	 * 
	 * @param la
	 */
	@Loggable
	public void saveOrUpdate(OperObject operObject, LocationArea la) throws RemoteException, Exception;

	/**
	 * 删除位置区
	 * 
	 * @param la
	 */
	@Loggable
	public void delete(OperObject operObject, LocationArea la) throws RemoteException, Exception;;

	/**
	 * 保存配置
	 * 
	 */
	public void config(List<LocationArea> locationAreaList)
			throws RemoteException, Exception;

	/**
	 * 查询所有locationId
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	// public List<Long> queryAllLocationId() throws RemoteException, Exception;
}
