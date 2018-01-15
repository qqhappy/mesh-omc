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

import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlan;
import com.xinwei.minas.mcbts.core.model.layer3.McBtsVlanAttach;
import com.xinwei.minas.server.mcbts.dao.layer3.VlanDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * @author chenshaohua
 * 
 */
public class VlanDAOImpl extends GenericHibernateDAO<McBtsVlan, Long> implements
		VlanDAO {

	private static transient final Log log = LogFactory
			.getLog(VlanDAOImpl.class);

	public List<McBtsVlan> queryAllByMoId(Long moId) {
		List mcBtsVlanList = getHibernateTemplate().find(
				"from McBtsVlan m where m.moId=" + moId);
		return mcBtsVlanList;
	}

	public void persistent(long moId, List<McBtsVlan> mcBtsVlanList) {
		HibernateTemplate ht = getHibernateTemplate();
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {
			List<McBtsVlan> oldList = ht.find("from McBtsVlan where moId="
					+ moId);
			ht.deleteAll(oldList);
			getHibernateTemplate().saveOrUpdateAll(mcBtsVlanList);
			tx.commit();
		} catch (Exception e) {
			log.error("保存数据错误。错误业务：VLAN", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public McBtsVlanAttach queryAttachByMoId(long moId) {
		List<McBtsVlanAttach> mcBtsVlanAttachs = getHibernateTemplate().find(
				"from McBtsVlanAttach m where m.moId=" + moId);
		if (mcBtsVlanAttachs.size() == 0) {
			return null;
		}
		return mcBtsVlanAttachs.get(0);
	}

	@Override
	public void saveAttach(McBtsVlanAttach attach) {
		getHibernateTemplate().saveOrUpdate(attach);
	}

}
