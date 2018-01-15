/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsRepeater;
import com.xinwei.minas.server.mcbts.dao.layer3.RepeaterDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * @author chenshaohua
 * 
 */
public class RepeaterDAOImpl extends GenericHibernateDAO<McBtsRepeater, Long>
		implements RepeaterDAO {

	private static transient final Log log = LogFactory
			.getLog(RepeaterDAOImpl.class);

	public List<McBtsRepeater> queryByMoId(Long moId) {
		List mcBtsRepeaterList = getHibernateTemplate().find(
				"from McBtsRepeater t where t.moId=" + moId);
		return mcBtsRepeaterList;
	}

	public void saveOrUpdate(List<McBtsRepeater> mcBtsRepeaterList, Long moId) {
		HibernateTemplate ht = getHibernateTemplate();
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {
			List<McBtsRepeater> oldList = ht
					.find("from McBtsRepeater where moId = " + moId);
			ht.deleteAll(oldList);
			ht.saveOrUpdateAll(mcBtsRepeaterList);
			tx.commit();
		} catch (Exception e) {
			log.error("保存数据错误。错误业务：直放站", e);
		} finally {
			if (session != null) {
				session.close();
			}

		}
	}

}
