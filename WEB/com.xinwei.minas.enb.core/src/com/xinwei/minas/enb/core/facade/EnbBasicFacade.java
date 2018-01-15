/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbCondition;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * ENodeB基本服务门面接口
 * 
 * @author chenjunhua
 * 
 */

public interface EnbBasicFacade extends Remote {

	/**
	 * 增加一个基站
	 * 
	 * @param enb
	 * @throws Exception
	 */
	@Loggable
	public void add(OperObject operObject, Enb enb) throws Exception;

	/**
	 * 修改一个基站
	 * 
	 * @param enb
	 * @throws Exception
	 */
	@Loggable
	public void modify(OperObject operObject, Enb enb) throws Exception;

	/**
	 * 删除一个基站
	 * 
	 * @param moId
	 * @throws Exception
	 */
	@Loggable
	public void delete(OperObject operObject, Long moId) throws Exception;

	/**
	 * 修改管理状态
	 * 
	 * @param moId
	 * @param manageState
	 * @throws Exception
	 */
	@Loggable
	public void changeManageState(OperObject operObject, Long moId,
			ManageState manageState) throws Exception;

	/**
	 * 查询系统中所有eNB基站
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Enb> queryAllEnb() throws Exception;

	/**
	 * 查询基站基本信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public Enb queryByMoId(Long moId) throws Exception;

	/**
	 * 根据enbId查询基站信息
	 * 
	 * @param enbId
	 * @return
	 * @throws Exception
	 */
	public Enb queryByEnbId(Long enbId) throws Exception;

	/**
	 * 根据条件查询基站
	 * 
	 * @param condition
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public PagingData<Enb> queryAllByCondition(EnbCondition condition)
			throws Exception;

	/**
	 * 根据基站MoId列表查询一列基站的最新状态
	 * 
	 * @param moIds
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<Enb> queryByMoIdList(List<Long> moIds) throws RemoteException,
			Exception;
}
