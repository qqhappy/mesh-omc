/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-29	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.ut.core.model.UTBacthUpgradeResult;
import com.xinwei.minas.ut.core.model.UTBatchUpgradeQueryModel;
import com.xinwei.minas.ut.core.model.UTCondition;
import com.xinwei.minas.ut.core.model.UTQueryResult;
import com.xinwei.minas.ut.core.model.UserTerminal;

/**
 * 
 * 终端基础门面接口
 * 
 * 
 * @author tiance
 * 
 */

public interface UTBasicFacade extends Remote {
	/**
	 * 按条件向HLR查询终端
	 * 
	 * @param utc
	 * @return
	 * @throws Exception
	 */
	public UTQueryResult queryUTByCondition(UTCondition utc)
			throws RemoteException, Exception;

	/**
	 * 向数据库查询所有终端类型
	 * 
	 * @return
	 */
	public List<TerminalVersion> queryUTTypes() throws RemoteException,
			Exception;

	/**
	 * 获得某个终端的最新状态
	 * 
	 * @param pid
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public UserTerminal queryUTByPid(String pid) throws RemoteException,
			Exception;
	/**
	 * 获得终端批量升级的结果
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<UTBacthUpgradeResult> queryUTBathUpgradeAll()throws RemoteException,Exception;
    /**
     * 获得终端批量升级的总记录条数
     * flag--true  升级成功的记录条数    flag--false升级失败的记录条数
     * @return
     * @throws Exception
     * @throws Exception
     */
	public Integer queryUTBatchUpgradeTotalCounts(Integer flag)throws RemoteException,Exception;
	/**
	 * 有条件的查询终端批量升级的结果；
	 * @param queryModel
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<UTBacthUpgradeResult> queryUTBatchUpgradeByCondition(UTBatchUpgradeQueryModel queryModel) throws RemoteException,Exception;
	/**
	 * 存储终端升级结果
	 * @param utResult
	 */
	public void saveUTBatchUpgradeResult(UTBacthUpgradeResult utResult)throws RemoteException,Exception;
	/**
	 * 批量删除终端升级结果
	 * @param utResults
	 * @throws RemoteException
	 * @throws Exception
	 */
    public void batchDeleteResults(List<UTBacthUpgradeResult> utResults)throws RemoteException,Exception;
    /**
     * 从数据库查询判断是否正在升级标志
     * @param utResult
     * @return
     * @throws RemoteException
     * @throws Exception
     */
    public Integer queryIsUpgradingFlag(UTBacthUpgradeResult utResult) throws RemoteException,Exception;
}
