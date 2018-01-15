/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-19	| liuzhongyan 	| 	create the file                       
 */
package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqModule;

public interface SysFreqFacade extends Remote {

	/**
	 * 从基站查询基站的有效频点
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Object[] queryDataFromBts(long moId) throws RemoteException,
			Exception;

	/**
	 * 获取所有基站的频点的汇总
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TSysFreqModule> queryUsedFreqFromBts() throws Exception,
			RemoteException;

	/**
	 * 配置系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void configData(OperObject operObject, Long moId, List<TSysFreqModule> sysFreqList,
			int freqSwitch, boolean isConfig) throws RemoteException, Exception;

	/**
	 * 查询系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */

	public List<TSysFreqModule> queryData(int freqType) throws RemoteException,
			Exception;

	/**
	 * 查询所有系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */

	public List<TSysFreqModule> queryAllData() throws RemoteException,
			Exception;

	/**
	 * 查询是否使用系统有效频点
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public int queryFreqSwitch() throws RemoteException, Exception;

	/**
	 * 配置指定基站的系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configBtsData(Long moId, List<TSysFreqModule> sysFreqList)
			throws RemoteException, Exception;

	/**
	 * 配置制定基站的系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configFreqSwitch(int freqSwitch) throws RemoteException,
			Exception;
	
	/**
	 * 删除指定的系统有效频点
	 * @param freq
	 * @throws Exception
	 */
	@Loggable
	public void deleteData(OperObject operObject, TSysFreqModule freq) throws Exception;

	/**
	 * 添加指定的系统有效频点
	 * @param freq
	 * @throws Exception
	 */
	@Loggable
	public void saveData(OperObject operObject, TSysFreqModule freq) throws Exception;
	
	
	/**
	 * 添加指定的系统有效频点列表
	 * @param freqs
	 * @throws Exception
	 */
	@Loggable
	public void saveAllData(OperObject operObject, List<TSysFreqModule> freqs) throws Exception;
	
	
	/**
	 * 更新指定的系统有效频点
	 * @param freq
	 * @throws Exception
	 */
	@Loggable
	public void updateData(OperObject operObject, TSysFreqModule freq) throws Exception;
}
