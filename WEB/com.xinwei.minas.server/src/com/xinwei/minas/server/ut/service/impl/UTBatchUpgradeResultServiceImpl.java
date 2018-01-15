/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.service.impl;

import java.util.List;

import com.xinwei.inms.commons.utils.service.SequenceService;
import com.xinwei.inms.commons.utils.service.impl.SequenceServiceImpl;
import com.xinwei.minas.server.ut.dao.UTBathUpgradeResultDAO;
import com.xinwei.minas.server.ut.service.UTBatchUpgradeResultService;
import com.xinwei.minas.ut.core.model.UTBacthUpgradeResult;
import com.xinwei.minas.ut.core.model.UTBatchUpgradeQueryModel;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述:终端批量升级服务业务
 * </p> 
 * 
 * @author qiwei
 * 
 */

public class UTBatchUpgradeResultServiceImpl implements UTBatchUpgradeResultService {
    private UTBathUpgradeResultDAO  utBathUpgradeResultDAO;
    private SequenceService sequenceService;
	public void setSequenceService(SequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}
	public UTBatchUpgradeResultServiceImpl() {
	this.utBathUpgradeResultDAO = OmpAppContext.getCtx().getBean(UTBathUpgradeResultDAO.class);
	this.sequenceService= OmpAppContext.getCtx().getBean(SequenceService.class);
     }
    /**
     * 查询终端批量升级结果
     */
	@Override
	public List<UTBacthUpgradeResult> queryUTBathUpgradeAll() {
		return utBathUpgradeResultDAO.queryUTBathUpgradeAll();
	}
	 /**
     * 查询终端批量升级的记录总数;
     * 1--升级成功  -1---升级失败  0---正在升级
     */
	@Override
	public Integer queryUTBatchUpgradeTotalCounts(Integer flag) {
		// TODO Auto-generated method stub
		return utBathUpgradeResultDAO.queryUTBatchUpgradeTotalCounts(flag);
	}
	
	/**
	 * 按条件查询批量升级结果
	 */
	@Override
	public List<UTBacthUpgradeResult> queryUTBatchUpgradeByCondition(
			UTBatchUpgradeQueryModel queryModel) {
		return utBathUpgradeResultDAO.queryUTBatchUpgradeByCondition(queryModel);
	}
	/**
	 * 向数据库保存终端升级结果
	 */
	@Override
	public void saveUTBatchUpgradeResult(UTBacthUpgradeResult utResult) {
		//如果是第一次存储，则设置idx主键
		if(utResult.getIdx()==null||utResult.getIdx()==0L){
			utResult.setIdx(sequenceService.getNext());
		}
		utBathUpgradeResultDAO.saveUTBatchUpgradeResult(utResult);
		
	}
	/**
	 * 向数据库查询idx
	 */
	
	@Override
	public Long getIdx() {
		return utBathUpgradeResultDAO.getIdx();
	}
	/**
	 * 批量删除终端升级结果
	 */
	@Override
	public void batchDeleteResults(List<UTBacthUpgradeResult> utResults) {
		utBathUpgradeResultDAO.batchDeleteResults(utResults);		
	}
	/**
	 * 查询是否正在升级的标志
	 */
	@Override
	public Integer queryIsUpgradingFlag(UTBacthUpgradeResult utResult) {
		return utBathUpgradeResultDAO.queryIsUpgradingFlag(utResult);
	}
}
