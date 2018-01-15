/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-19	| liuzhongyan 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqModule;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 系统频点下发service
 * 
 * @author liuzhongyan
 * 
 */
public interface SysFreqService extends ICustomService {

	/**
	 * 从基站查询基站的有效频点
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Object[] queryDataFromBts(long moId) throws Exception;

	/**
	 * 获取所有基站的频点的汇总
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TSysFreqModule> queryUsedFreqFromBts() throws Exception;

	/**
	 * 配置系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configData(Long moId, List<TSysFreqModule> sysFreqList,
			int freqSwitch, boolean isConfig) throws Exception;

	/**
	 * 查询系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */

	public List<TSysFreqModule> queryData(int freqType) throws Exception;

	/**
	 * 查询所有系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */

	public List<TSysFreqModule> queryAllData() throws Exception;

	/**
	 * 配置制定基站的系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configBtsData(Long moId, List<TSysFreqModule> sysFreqList)
			throws Exception;

	/**
	 * 查询是否使用系统有效频点
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public int queryFreqSwitch() throws Exception;

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
	public void deleteData(TSysFreqModule freq) throws Exception;

	/**
	 * 添加指定的系统有效频点
	 * @param freq
	 * @throws Exception
	 */
	public void saveData(TSysFreqModule freq) throws Exception;
	
	/**
	 * 添加指定的系统有效频点列表
	 * @param freqs
	 * @throws Exception
	 */
	public void saveAllData(List<TSysFreqModule> freqs) throws Exception;
	
	/**
	 * 更新指定的系统有效频点
	 * @param freq
	 * @throws Exception
	 */
	public void updateData(TSysFreqModule freq) throws Exception;
}
