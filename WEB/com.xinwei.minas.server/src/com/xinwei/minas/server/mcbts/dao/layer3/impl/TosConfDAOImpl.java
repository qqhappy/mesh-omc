package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.xinwei.minas.mcbts.core.model.layer3.McBtsTos;
import com.xinwei.minas.mcbts.core.model.sysManage.LocationArea;
import com.xinwei.minas.server.mcbts.dao.layer3.TosConfDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

public class TosConfDAOImpl extends GenericHibernateDAO<McBtsTos, Long>
		implements TosConfDAO {

	public List<McBtsTos> queryAllTos() {
		return (List<McBtsTos>) getHibernateTemplate().find("from McBtsTos");
	}

	public void saveOrUpdate(List<McBtsTos> mcBtsTosList) {
		// HibernateTemplate ht = getHibernateTemplate();
		// Session session = getSessionFactory().openSession();
		// Transaction tx = session.beginTransaction();
		// List<McBtsTos> oldList = ht.find("from McBtsTos");
		// ht.deleteAll(oldList);
		getHibernateTemplate().saveOrUpdateAll(mcBtsTosList);
		// tx.commit();
	}

}
