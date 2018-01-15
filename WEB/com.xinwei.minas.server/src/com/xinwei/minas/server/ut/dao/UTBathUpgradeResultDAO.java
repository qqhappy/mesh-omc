/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.dao;

import java.util.List;

import com.xinwei.minas.ut.core.model.UTBacthUpgradeResult;
import com.xinwei.minas.ut.core.model.UTBatchUpgradeQueryModel;



/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述：批量升级结果查询，包括成功升级和升级失败的
 * </p> 
 * 
 * @author qiwei
 * 
 */

public interface UTBathUpgradeResultDAO {
public List<UTBacthUpgradeResult> queryUTBathUpgradeAll();
/**
 *  查询终端批量升级的记录总数;
 */
public Integer queryUTBatchUpgradeTotalCounts(Integer flag);
/**
 * 按条件查询批量升级结果
 */
public List<UTBacthUpgradeResult> queryUTBatchUpgradeByCondition(
		UTBatchUpgradeQueryModel queryModel);
/**
 * 向数据库插入终端批量升级的结果
 */
public void saveUTBatchUpgradeResult(UTBacthUpgradeResult utResult);
/**
 * 从数据库查询idx
 * 
 * 
 */
public Long getIdx();
/**
 * 批量删除终端升级结果
 * @param utResults
 */
public void batchDeleteResults(List<UTBacthUpgradeResult> utResults);
/**
 * 查询是否正在升级的标志；
 */
public Integer queryIsUpgradingFlag(UTBacthUpgradeResult utResult);
}
