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

import com.xinwei.minas.mcbts.core.model.layer3.McBtsACL;
import com.xinwei.minas.server.mcbts.dao.layer3.ACLDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * @author chenshaohua
 * 
 */
public class ACLDAOImpl extends GenericHibernateDAO<McBtsACL, Long> implements
		ACLDAO {

	private static transient final Log log = LogFactory
			.getLog(ACLDAOImpl.class);

	public void saveOrUpdate(List<McBtsACL> mcBtsACLList, Long moId) {
		HibernateTemplate ht = getHibernateTemplate();
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {
			List oldMcBtsACLList = ht
					.find("from McBtsACL where moId = " + moId);
			ht.deleteAll(oldMcBtsACLList);
			if (mcBtsACLList != null)
				ht.saveOrUpdateAll(mcBtsACLList);
			tx.commit();
		} catch (Exception e) {
			log.error("保存数据库错误。错误业务：ACL", e);
		} finally {
			if (session != null) {
				session.close();
			}

		}
	}

	@Override
	public List<McBtsACL> queryByMoId(Long moId) {
		List<McBtsACL> mcBtsACLList = getHibernateTemplate().find(
				"from McBtsACL t where t.moId=" + moId);
		for (McBtsACL obj : mcBtsACLList) {
			obj.setEntry(mcBtsACLList.size());
		}
		return mcBtsACLList;
	}

	/**
	 * 删除所有ACL记录
	 * 
	 * @param moId
	 */
	@Override
	public void deleteAll(Long moId) {
		List<McBtsACL> list = queryByMoId(moId);
		getHibernateTemplate().deleteAll(list);
	}
}
