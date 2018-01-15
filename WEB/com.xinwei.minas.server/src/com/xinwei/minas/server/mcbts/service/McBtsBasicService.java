/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.ManageState;
import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsCondition;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * 
 * McBts基本业务服务接口
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsBasicService extends ICustomService {

	/**
	 * 增加一个基站
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public void add(McBts mcBts) throws Exception;

	/**
	 * 修改一个基站
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public void modify(McBts mcBts) throws Exception;

	/**
	 * 删除一个基站
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void delete(Long moId) throws Exception;

	/**
	 * 修改管理状态
	 * 
	 * @param moId
	 * @param manageState
	 * @throws Exception
	 */
	public void changeManageState(Long moId, ManageState manageState)
			throws Exception;

	/**
	 * 查询McBts基本信息
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public McBts queryByMoId(Long moId) throws Exception;

	/**
	 * 根据BtsId查询McBts信息
	 * 
	 * @param btsId
	 * @return
	 * @throws Exception
	 */
	public McBts queryByBtsId(Long btsId) throws Exception;

	/**
	 * 根据相同的频点查询McBts
	 * 
	 * @param freq
	 * @return
	 * @throws Exception
	 */
	public List<McBts> queryBySameFreq(int freq) throws Exception;

	// /**
	// * 配置McBts基本信息
	// *
	// * @param mcBts
	// * @throws Exception
	// */
	// public void config(McBts mcBts) throws Exception;

	/**
	 * 查询所有基站
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<McBts> queryAll() throws Exception;

	/**
	 * 按基站类型查询基站
	 * 
	 * @param btsType
	 * @return
	 * @throws Exception
	 */
	public List<McBts> queryByBtsType(int btsType) throws Exception;

	/**
	 * 获取以moId为key的基站map
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Long, McBts> getMapByMoId() throws Exception;

	/**
	 * 获取以btsId为key的基站map
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Long, McBts> getMapByBtsId() throws Exception;

	/**
	 * 根据条件查询基站 新增方法
	 * 
	 * @param mcBtsCondition
	 * @return
	 * @throws Exception
	 * @author liuzhongyan
	 */
	public PagingData<McBts> queryAllByCondition(McBtsCondition mcBtsCondition)
			throws Exception;

	/**
	 * 查询所有符合条件的基站
	 * 
	 * @param condition
	 *            查询条件
	 * @return 符合条件的基站列表
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryMcBtsBy(McBtsCondition condition) throws Exception;

	/**
	 * 根据基站MoId列表查询一列基站的最新状态
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBts> queryByMoIdList(List<Long> moIds) throws Exception;

	/**
	 * 根据基站的当前状态更新基站缓存内容
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public void updateMcBtsCache(McBts mcBts) throws Exception;

	void fillExportList(Business business) throws Exception;

}
