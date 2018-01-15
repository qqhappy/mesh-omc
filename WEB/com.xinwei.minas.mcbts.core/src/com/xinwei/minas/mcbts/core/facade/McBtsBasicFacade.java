/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsCondition;
import com.xinwei.minas.sxc.core.model.SxcBasic;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * McBts基本业务门面
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsBasicFacade extends Remote {

	/**
	 * 增加一个基站
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	@Loggable
	public void add(OperObject operObject, McBts mcBts) throws RemoteException,
			Exception;

	/**
	 * 修改一个基站
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	@Loggable
	public void modify(OperObject operObject, McBts mcBts)
			throws RemoteException, Exception;

	/**
	 * 删除一个基站
	 * 
	 * @param moId
	 * @throws Exception
	 */
	@Loggable
	public void delete(OperObject operObject, Long moId)
			throws RemoteException, Exception;

	/**
	 * 修改管理状态
	 * 
	 * @param moId
	 * @param manageState
	 * @throws Exception
	 */
	@Loggable
	public void changeManageState(OperObject operObject, Long moId,
			ManageState manageState) throws RemoteException, Exception;

	/**
	 * 查询McBts基本信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public McBts queryByMoId(Long moId) throws RemoteException, Exception;

	/**
	 * 根据BtsId查询McBts信息
	 * 
	 * @param btsId
	 * @return
	 * @throws Exception
	 */
	public McBts queryByBtsId(Long btsId) throws RemoteException, Exception;

	/**
	 * 根据相同的频点查询McBts
	 * 
	 * @param freq
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryBySameFreq(int freq) throws RemoteException,
			Exception;

	// /**
	// * 配置McBts基本信息
	// *
	// * @param mcBts
	// * @throws Exception
	// */
	// public void config(McBts mcBts) throws RemoteException, Exception;

	/**
	 * 查询所有基站
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryAll() throws RemoteException, Exception;

	/**
	 * 按基站类型查询基站
	 * 
	 * @param btsType
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryByBtsType(int btsType) throws RemoteException,
			Exception;

	/**
	 * 获取以moId为key的基站map
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Long, McBts> getMapByMoId() throws RemoteException, Exception;

	/**
	 * 获取以btsId为key的基站map
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Long, McBts> getMapByBtsId() throws RemoteException, Exception;

	/**
	 * 根据条件查询基站
	 * 
	 * @param mcBtsCondition
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public PagingData<McBts> queryAllByCondition(McBtsCondition mcBtsCondition)
			throws RemoteException, Exception;

	/**
	 * 查询所有符合条件的基站
	 * 
	 * @param condition
	 *            查询条件
	 * @return 符合条件的基站列表
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryMcBtsBy(McBtsCondition condition)
			throws RemoteException, Exception;

	/**
	 * 根据基站MoId列表查询一列基站的最新状态
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryByMoIdList(List<Long> moIds)
			throws RemoteException, Exception;

	/**
	 * 更新SAG信息
	 * 
	 * @param oldSxc
	 *            旧SAG信息
	 * @param newSxc
	 *            新SAG信息
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void updateSagInfo(SxcBasic oldSxc, SxcBasic newSxc)
			throws RemoteException, Exception;

	/**
	 * 查找自发现的基站列表
	 * 
	 * @return 自发现的基站列表
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryAutomaticFindMcBts() throws RemoteException,
			Exception;

	/**
	 * 删除指定的自发现基站
	 * 
	 * @param btsId
	 *            基站ID
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void deleteAutomaticFindMcBts(Long btsId) throws RemoteException,
			Exception;

	/**
	 * 根据基站的当前状态更新基站缓存内容
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public void updateMcBtsCache(OperObject operObject, McBts mcBts)
			throws Exception;

}
