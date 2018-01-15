/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-14	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.District;

/**
 * 
 * 地域管理门面接口
 * 
 * 
 * @author tiance
 * 
 */

public interface DistrictManageFacade extends Remote {
	/**
	 * 查询所有的地域信息
	 * 
	 * @return
	 */
	public List<District> queryAll() throws RemoteException, Exception;

	/**
	 * 插入或者更新一个地域信息
	 * 
	 * @param district
	 */
	@Loggable
	public void saveOrUpdate(OperObject operObject, District district) throws RemoteException,
			Exception;

	/**
	 * 删除一个地域信息
	 * 
	 * @param district
	 */
	@Loggable
	public void delete(OperObject operObject, District district) throws RemoteException, Exception;
}
