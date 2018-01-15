/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-29	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.facade;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.minas.server.ut.service.UTBasicService;
import com.xinwei.minas.server.ut.service.UTBatchUpgradeResultService;
import com.xinwei.minas.ut.core.facade.UTBasicFacade;
import com.xinwei.minas.ut.core.model.UTBacthUpgradeResult;
import com.xinwei.minas.ut.core.model.UTBatchUpgradeQueryModel;
import com.xinwei.minas.ut.core.model.UTCondition;
import com.xinwei.minas.ut.core.model.UTQueryResult;
import com.xinwei.minas.ut.core.model.UserTerminal;

/**
 * 
 * 终端基础门面类
 * 
 * 
 * @author tiance
 * 
 */

public class UTBasicFacadeImpl extends UnicastRemoteObject implements
		UTBasicFacade {

	private UTBasicService service;
    private UTBatchUpgradeResultService  utBatchUpgradeResultService;
	public UTBasicFacadeImpl() throws RemoteException {
		service = AppContext.getCtx().getBean(UTBasicService.class);
		utBatchUpgradeResultService=AppContext.getCtx().getBean(UTBatchUpgradeResultService.class);
	}

	@Override
	public UTQueryResult queryUTByCondition(UTCondition utc)
			throws RemoteException, Exception {
		return service.queryUTByCondition(utc);
	}

	/**
	 * 获得某个终端的最新状态
	 * 
	 * @param pid
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public UserTerminal queryUTByPid(String pid) throws RemoteException,
			Exception {
		return service.queryUTByPid(pid);
	}

	/**
	 * 向数据库查询所有终端类型
	 * 
	 * @return
	 */
	@Override
	public List<TerminalVersion> queryUTTypes() throws RemoteException,
			Exception {
		return service.queryUTTypes();
	}
	/**
	 * 向数据库查询所有终端批量升级的结果
	 */

	@Override
	public List<UTBacthUpgradeResult> queryUTBathUpgradeAll()
			throws RemoteException, Exception {
		return utBatchUpgradeResultService.queryUTBathUpgradeAll();
	}
    /**
     * 向数据库查询终端批量升级的记录数
     */
	@Override
	public Integer queryUTBatchUpgradeTotalCounts(Integer flag)
			throws RemoteException, Exception {
		return utBatchUpgradeResultService.queryUTBatchUpgradeTotalCounts(flag);
	}
     /**
      * 根据条件查询终端批量升级的结果；
      */
	@Override
	public List<UTBacthUpgradeResult> queryUTBatchUpgradeByCondition(
			UTBatchUpgradeQueryModel queryModel) throws RemoteException,
			Exception {
		return utBatchUpgradeResultService.queryUTBatchUpgradeByCondition(queryModel);
	}
	/**
	 * 存储终端升级结果
	 */
	@Override
	public void saveUTBatchUpgradeResult(UTBacthUpgradeResult utResult)throws RemoteException,
	Exception {
		utBatchUpgradeResultService.saveUTBatchUpgradeResult(utResult);
	}
     /**
      * 批量删除终端升级结果
      */
	@Override
	public void batchDeleteResults(List<UTBacthUpgradeResult> utResults)
			throws RemoteException, Exception {
		utBatchUpgradeResultService.batchDeleteResults(utResults);
	}
    /**
     * 从数据库查询判断是否正在升级标志
     */
	@Override
	public Integer queryIsUpgradingFlag(UTBacthUpgradeResult utResult)
			throws RemoteException, Exception {
		return utBatchUpgradeResultService.queryIsUpgradingFlag(utResult);
	}
	
}
