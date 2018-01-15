/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-7	| qiwei 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;
import com.xinwei.minas.server.ut.dao.UTBathUpgradeResultDAO;
import com.xinwei.minas.ut.core.model.UTBacthUpgradeResult;
import com.xinwei.minas.ut.core.model.UTBatchUpgradeQueryModel;


/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述:终端批量升级结果的操作
 * </p> 
 * 
 * @author qiwei
 * 
 */

public class UTBatchUpgradeResultDAOImpl extends GenericHibernateDAO<UTBacthUpgradeResult,Long> implements UTBathUpgradeResultDAO {
    /**
     * 查询所有的终端升级结果
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<UTBacthUpgradeResult> queryUTBathUpgradeAll() {
		
		return getHibernateTemplate().find("from UTBacthUpgradeResult ubur where ubur.utUpgradeFlag=0 order by ubur.upgradeTime desc");
		
	}
	/**
	 *  查询终端批量升级的记录总数;
	 */
	@Override
	public Integer queryUTBatchUpgradeTotalCounts(Integer flag) {
		// 批量升级中的查询结果成功或失败的标志； 1---成功   -1----失败   0---正在升级
		int successFlag=flag;
		Session session = this.getSession();
		Query query = session
				.createQuery("select count(*) from UTBacthUpgradeResult ubur where ubur.utUpgradeFlag="
						+ successFlag);
		@SuppressWarnings("unchecked")
		List<Long> results = query.list();
		session.close();
		if (results != null && results.size() != 0) {
			int result = results.get(0).intValue();
			return result;
		} else {
			return 0;
		}
			  
	}
	/**
	 * 按条件查询批量升级结果
	 */
	@Override
	public List<UTBacthUpgradeResult> queryUTBatchUpgradeByCondition(
			UTBatchUpgradeQueryModel queryModel) {
		// 批量升级中的查询结果成功或失败的标志； 1---成功  -1----失败  0--正在升级
		Integer successFlag = queryModel.getSuccessFlag();
		Session session = this.getSession();
		Query query = session
				.createQuery("from UTBacthUpgradeResult ubur where ubur.utUpgradeFlag="
						+ successFlag+" order by ubur.upgradeTime desc");
		query.setFirstResult(queryModel.getQueryFirstResults());
		query.setMaxResults(queryModel.getPageSize());
		@SuppressWarnings("unchecked")
		List<UTBacthUpgradeResult> result = query.list();
		session.close();
		return result;
	}
	/**
	 * 向数据库插入终端批量升级的结果
	 */
	@Override
	public void saveUTBatchUpgradeResult(UTBacthUpgradeResult utResult) {
		this.getHibernateTemplate().saveOrUpdate(utResult);
	}
	/**
	 * 从数据库查询idx
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long getIdx() {
	 List<Long> results=this.getHibernateTemplate().find("select max(idx)+1 from UTBacthUpgradeResult");
	 if(results!=null&& results.size()!=0){
		 return results.get(0);
	 }
		return 0l;
	}
	/**
	 * 批量删除终端升级结果
	 */
	@Override
	public void batchDeleteResults(List<UTBacthUpgradeResult> utResults) {
		this.getHibernateTemplate().deleteAll(utResults);
	}
	/**
	 * 查询是否正在升级的标志
	 */
	@Override
	public Integer queryIsUpgradingFlag(UTBacthUpgradeResult utResult) {
		List<UTBacthUpgradeResult> utResults=this.getHibernateTemplate().find("from UTBacthUpgradeResult ubur where ubur.idx="+utResult.getIdx());
		if(utResults!=null&&utResults.size()!=0){
			return utResults.get(0).getUtUpgradeFlag();
		}
		return null;
	}
	

}
