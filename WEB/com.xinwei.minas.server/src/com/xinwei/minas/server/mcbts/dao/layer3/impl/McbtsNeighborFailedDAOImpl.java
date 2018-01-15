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

import com.xinwei.minas.mcbts.core.model.layer3.McBtsNeighborFailed;
import com.xinwei.minas.server.mcbts.dao.layer3.McbtsNeighborFailedDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * @author zhuxiaozhan
 *
 */
public class McbtsNeighborFailedDAOImpl extends GenericHibernateDAO<McBtsNeighborFailed, Long> implements McbtsNeighborFailedDAO {
	
	private static transient final Log log = LogFactory
	.getLog(McbtsNeighborFailedDAOImpl.class);
	
	@Override
	public McBtsNeighborFailed queryByMoId(Long moId) {
		List<McBtsNeighborFailed> result = getHibernateTemplate().find("from McBtsNeighborFailed m where m.moId=" + moId);
		if (result == null || result.size() == 0)
			return null;
		return result.get(0);
	}

	@Override
	public void delByMoId(Long moId) throws Exception {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		try {
			String delSql = "delete McBtsNeighborFailed m where m.moId=" + moId;
			Query query = session.createQuery(delSql);
			query.executeUpdate();
			tx.commit();
		}catch (HibernateException e) {
			log.error("Append Neighbor : delete data failed.", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
