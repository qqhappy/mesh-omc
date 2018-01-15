/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-10	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsAppendNeighbor;
import com.xinwei.minas.server.mcbts.dao.layer3.AppendNeighborDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * @author zhuxiaozhan
 *
 */
public class AppendNeighborDAOImpl extends GenericHibernateDAO<McBtsAppendNeighbor, Long>implements AppendNeighborDAO {
	
	private static transient final Log log = LogFactory
	.getLog(AppendNeighborDAOImpl.class);
	
	@Override
	public List<McBtsAppendNeighbor> queryNeighbour(Long moId) {
		return (List<McBtsAppendNeighbor>) getHibernateTemplate().find(
				"from McBtsAppendNeighbor m where m.moId=" + moId + " order by m.idx asc");
	}

	@Override
	public void deleteOld(Long moId) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			String delhql1 = "delete McBtsAppendNeighbor m where  m.moId=" + moId;
			Query query1 = session.createQuery(delhql1);
			query1.executeUpdate();
			String delhql2 = "delete McBtsAppendNeighbor m where m.appendNeighborMoId="
					+ moId;
			Query query2 = session.createQuery(delhql2);
			query2.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			log.error("Append Neighbor : delete data failed.", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public boolean isAppendNeighborRelation(Long basicMoId, Long appendNeighborMoId) {
		@SuppressWarnings("rawtypes")
		List result = getHibernateTemplate().find(
				"from McBtsAppendNeighbor m where m.moId=" + basicMoId
						+ " and m.appendNeighborMoId=" + appendNeighborMoId);
		if (result == null || result.size() == 0)
			return false;
		return true;
	}

	@Override
	public void delAppendNbRelationship(Long basicMoId, Long appendNeighborMoId) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			String delSql = "delete McBtsAppendNeighbor m where m.moId=" + basicMoId + " and " + "m.appendNeighborMoId=" + appendNeighborMoId;
			Query query = session.createQuery(delSql);
			query.executeUpdate();
			String delSql1 = "delete McBtsAppendNeighbor m where m.moId=" + appendNeighborMoId + " and " + "m.appendNeighborMoId=" + basicMoId;
			Query query1 = session.createQuery(delSql1);
			query1.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			log.error("Append Neighbor : delete data failed.", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
