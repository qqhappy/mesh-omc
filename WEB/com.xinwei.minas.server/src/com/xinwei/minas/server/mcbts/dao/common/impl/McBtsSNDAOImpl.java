/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common.impl;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.dao.DataAccessException;

import com.xinwei.minas.mcbts.core.model.common.McBtsSN;
import com.xinwei.minas.server.mcbts.dao.common.McBtsSNDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.impl.ACLDAOImpl;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * ��վ���к�DAOʵ��
 * 
 * @author chenshaohua
 * 
 */

public class McBtsSNDAOImpl extends GenericHibernateDAO<McBtsSN, Long>
		implements McBtsSNDAO {

	private static transient final Log log = LogFactory
			.getLog(McBtsSNDAOImpl.class);

	@Override
	public List<McBtsSN> querySNFromDB(long moId) {
		try {
			List<McBtsSN> mcBtsSNList = getHibernateTemplate().find(
					"from McBtsSN t where t.moId=" + moId
							+ "order by t.timeStamp desc");
			return mcBtsSNList;
		} catch (DataAccessException e) {
			log.error(e);
		}
		return null;
	}

	public McBtsSN queryNewestRecord(long moId) {

		Session session = getSessionFactory().openSession();
		try {
			Query query = session
					.createQuery("from McBtsSN m where m.moId like :targetId order by m.timeStamp desc");
			query.setParameter("targetId", moId, Hibernate.LONG);
			List result = query.list();
			if (result == null || result.size() == 0) {
				return null;
			} 
			return (McBtsSN)result.get(0);
		} catch (HibernateException e) {
			log.error(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	/**
	 * ����ʮ����¼
	 * 
	 * @mcBtsSN1 Ҫ����ļ�¼
	 */
	@Override
	public void saveFixedCountRecord(McBtsSN mcBtsSN1) {

		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query = session
					.createQuery("select count(*) from McBtsSN as m where m.moId=?");
			query.setLong(0, mcBtsSN1.getMoId());
			Long longValue = (Long) query.uniqueResult();
			if (longValue == null) {
				session.save(mcBtsSN1);
				return;
			}
			long count = ((Long) query.uniqueResult()).longValue();
			if (count >= 10) {
				// ɾ����ɵ�һ����¼
				long minTimeStamp = (Long) session.createQuery(
						"select min(m.timeStamp) from McBtsSN as m")
						.uniqueResult();
				Query query2 = session
						.createQuery("delete McBtsSN m where m.timeStamp=?");
				query2.setLong(0, minTimeStamp);
				query2.executeUpdate();
			}
			session.save(mcBtsSN1);
			tx.commit();
		} catch (HibernateException e) {
			log.error("�������ݿ���󡣴���ҵ�񣺻�վ���к�", e);
		} finally {
			session.close();
		}

	}

}
