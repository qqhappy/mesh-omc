
package com.xinwei.minas.server.mcbts.dao.oamManage.impl;

import java.util.List;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.dao.DataAccessException;

import com.xinwei.minas.mcbts.core.model.common.McBtsSN;
import com.xinwei.minas.server.mcbts.dao.common.McBtsSNDAO;
import com.xinwei.minas.server.mcbts.dao.layer3.impl.ACLDAOImpl;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;
import com.xinwei.minas.mcbts.core.model.oamManage.McBtsSateQuery;
import com.xinwei.minas.server.mcbts.dao.oamManage.McBtsStateQueryDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;



/**
 * 
 * McBtsStateQueryDAOImpl实现
 * 
 * @author fangping
 * 
 */

public class McBtsStateQueryDAOImpl extends GenericHibernateDAO<McBtsSateQuery, Long>
		implements McBtsStateQueryDAO {

	private static transient final Log log = LogFactory
			.getLog(McBtsStateQueryDAOImpl.class);



	@Override
	public List<McBtsSateQuery> queryStateQueryFromDB(long moId) {
		try {
			List<McBtsSateQuery> mcBtsSNList = getHibernateTemplate().find(
					"from McBtsSN t where t.moId=" + moId);
			return mcBtsSNList;
		} catch (DataAccessException e) {
			log.error(e);
		}
		return null;
	}

	@Override
	public void saveFixedCountRecord(McBtsSateQuery mcbtsstatequery) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public McBtsSateQuery queryNewestRecord(long moId) {
		// TODO Auto-generated method stub
		return null;
	}

	/*public McBtsSN queryNewestRecord(long moId) {

		Session session = getSessionFactory().openSession();
		try {
			Query query1 = session
					.createQuery("select max(m.timeStamp) from McBtsSN as m where m.moId=?");
			Long result = (Long) query1.setLong(0, moId).uniqueResult();
			if (result == null) {
				return null;
			}
			long maxTimeStamp = result.longValue();
			Query query2 = session
					.createQuery("from McBtsSN m where m.timeStamp=? and m.moId=?");
			query2.setLong(0, maxTimeStamp);
			query2.setLong(1, moId);
			return (McBtsSN) query2.uniqueResult();
		} catch (HibernateException e) {
			log.error(e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	*//**
	 * 保存十条记录
	 * 
	 * @mcBtsSN1 要保存的记录
	 *//*
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
				// 删除最旧的一条记录
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
			log.error(e);
			log.error("保存数据库错误。错误业务：基站序列号", e);
		} finally {
			session.close();
		}

	}*/

}
