/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-28	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.facade.conf;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.model.Mo;

/**
 * 
 * MO基本门面
 * 
 * @author chenjunhua
 * 
 */

public interface MoBasicFacade extends Remote {

	/**
	 * 查找所有MO
	 * 
	 * @return
	 */
	public List<Mo> queryAll() throws RemoteException, Exception;

	/**
	 * 获取指定moId的 MO
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Mo queryByMoId(Long moId) throws RemoteException, Exception;

	/**
	 * 获取指定类型的MO
	 * 
	 * @param moTypeId
	 *            类型
	 * @return
	 */
	public List<Mo> queryByType(int moTypeId) throws RemoteException, Exception;

	/**
	 * 增加MO
	 * 
	 * @param mo
	 *            被管对象
	 * @param regionId
	 *            归属地区编号
	 */
	public void add(Mo mo, long regionId) throws RemoteException, Exception;

	/**
	 * 修改MO
	 * 
	 * @param mo
	 */
	public void modify(Mo mo) throws RemoteException, Exception;

	/**
	 * 删除指定ID的MO
	 * 
	 * @param moId
	 */
	public void delete(Long moId) throws RemoteException, Exception;

	/**
	 * 修改MO管理状态
	 * 
	 * @param mo
	 */
	public void changeManageState(Mo mo) throws RemoteException, Exception;

}
