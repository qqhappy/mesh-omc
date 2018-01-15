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
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����:�ն�������������Ĳ���
 * </p> 
 * 
 * @author qiwei
 * 
 */

public class UTBatchUpgradeResultDAOImpl extends GenericHibernateDAO<UTBacthUpgradeResult,Long> implements UTBathUpgradeResultDAO {
    /**
     * ��ѯ���е��ն��������
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<UTBacthUpgradeResult> queryUTBathUpgradeAll() {
		
		return getHibernateTemplate().find("from UTBacthUpgradeResult ubur where ubur.utUpgradeFlag=0 order by ubur.upgradeTime desc");
		
	}
	/**
	 *  ��ѯ�ն����������ļ�¼����;
	 */
	@Override
	public Integer queryUTBatchUpgradeTotalCounts(Integer flag) {
		// ���������еĲ�ѯ����ɹ���ʧ�ܵı�־�� 1---�ɹ�   -1----ʧ��   0---��������
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
	 * ��������ѯ�����������
	 */
	@Override
	public List<UTBacthUpgradeResult> queryUTBatchUpgradeByCondition(
			UTBatchUpgradeQueryModel queryModel) {
		// ���������еĲ�ѯ����ɹ���ʧ�ܵı�־�� 1---�ɹ�  -1----ʧ��  0--��������
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
	 * �����ݿ�����ն����������Ľ��
	 */
	@Override
	public void saveUTBatchUpgradeResult(UTBacthUpgradeResult utResult) {
		this.getHibernateTemplate().saveOrUpdate(utResult);
	}
	/**
	 * �����ݿ��ѯidx
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
	 * ����ɾ���ն��������
	 */
	@Override
	public void batchDeleteResults(List<UTBacthUpgradeResult> utResults) {
		this.getHibernateTemplate().deleteAll(utResults);
	}
	/**
	 * ��ѯ�Ƿ����������ı�־
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
