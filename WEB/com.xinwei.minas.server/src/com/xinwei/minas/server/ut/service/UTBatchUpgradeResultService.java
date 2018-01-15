/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.service;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.ut.core.model.UTBacthUpgradeResult;
import com.xinwei.minas.ut.core.model.UTBatchUpgradeQueryModel;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述:终端批量升级服务
 * </p> 
 * 
 * @author qiwei
 * 
 */

public interface UTBatchUpgradeResultService {
	/**
	 * 查询终端批量升级的总结果
	 * @return
	 */
  public List<UTBacthUpgradeResult> queryUTBathUpgradeAll();
  /**
   * 向数据库查询终端批量升级的记录数
   * @param flag
   * @return
   */
  public Integer queryUTBatchUpgradeTotalCounts(Integer flag);
  /**
   * 根据条件查询终端批量升级的结果；
   * @param queryModel
   * @return
   */
  public List<UTBacthUpgradeResult> queryUTBatchUpgradeByCondition(
			UTBatchUpgradeQueryModel queryModel);
  /**
   * 向数据库保存终端升级结果
   */
  public void saveUTBatchUpgradeResult(UTBacthUpgradeResult utResult);
  /**
   * 查询idx
   */
  public Long getIdx();
  /**
   * 批量升级终端升级结果
   * @param utResults
   */
  public void batchDeleteResults(List<UTBacthUpgradeResult> utResults);
  /**
   * 查询是否正在升级的标志；
   * @param utResult
   * @return
   */
  public Integer queryIsUpgradingFlag(UTBacthUpgradeResult utResult);
}
