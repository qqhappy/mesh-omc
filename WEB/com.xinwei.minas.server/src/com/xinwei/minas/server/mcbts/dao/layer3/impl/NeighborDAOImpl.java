/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsNeighbour;
import com.xinwei.minas.server.mcbts.dao.layer3.NeighbourDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * @author chenshaohua
 * 
 */
public class NeighborDAOImpl extends GenericHibernateDAO<McBtsNeighbour, Long>
		implements NeighbourDAO {

	private static transient final Log log = LogFactory
			.getLog(NeighborDAOImpl.class);

	public List<McBtsNeighbour> queryNeighbour(Long moId) {
		return (List<McBtsNeighbour>) getHibernateTemplate().find(
				"from McBtsNeighbour m where m.moId=" + moId + " order by m.idx asc");
	}

	/**
	 * 判断两个基站是否具有邻接关系
	 * 
	 * @param basicMoId
	 * @param neighbourMoId
	 * @return
	 */
	@Override
	public boolean isNeighborRelation(Long basicMoId, Long neighbourMoId) {
		@SuppressWarnings("rawtypes")
		List result = getHibernateTemplate().find(
				"from McBtsNeighbour m where m.moId=" + basicMoId
						+ " and m.neighbourMoId=" + neighbourMoId);
		if (result == null || result.size() == 0)
			return false;
		return true;
	}

	@Override
	public void deleteOld(Long moId) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			String delhql1 = "delete McBtsNeighbour m where m.moId=" + moId;
			Query query1 = session.createQuery(delhql1);
			query1.executeUpdate();
			String delhql2 = "delete McBtsNeighbour m where m.neighbourMoId="
					+ moId;
			Query query2 = session.createQuery(delhql2);
			query2.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			log.error("Neighbor: delete data failed.", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	@Override
	public void delNbRelationship(Long basicMoId, Long neighbourMoId) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			String delSql = "delete McBtsNeighbour m where m.moId=" + basicMoId + " and " + "m.neighbourMoId=" + neighbourMoId;
			Query query = session.createQuery(delSql);
			query.executeUpdate();
			String delSql1 = "delete McBtsNeighbour m where m.moId=" + neighbourMoId + " and " + "m.neighbourMoId=" + basicMoId;
			Query query1 = session.createQuery(delSql1);
			query1.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			log.error("Neighbor: delete data failed.", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
	}

}
