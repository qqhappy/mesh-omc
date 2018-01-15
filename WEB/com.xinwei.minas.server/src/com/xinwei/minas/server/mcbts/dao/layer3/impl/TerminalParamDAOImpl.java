/**
 * 
 */
package com.xinwei.minas.server.mcbts.dao.layer3.impl;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.xinwei.minas.mcbts.core.model.layer3.TerminalBizParam;
import com.xinwei.minas.server.mcbts.dao.layer3.TerminalParamDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * @author chenshaohua
 * 
 */
public class TerminalParamDAOImpl extends
		GenericHibernateDAO<TerminalBizParam, Long> implements TerminalParamDAO {

	private static transient final Log log = LogFactory
			.getLog(TerminalParamDAOImpl.class);

	public List<TerminalBizParam> queryAllTerminalParam()
			throws RemoteException, Exception {
		List terminalBizParamList = getHibernateTemplate().find(
				"from TerminalBizParam");
		return terminalBizParamList;
	}

	public void saveOrUpdate(List<TerminalBizParam> terminalBizParamList) {

		HibernateTemplate ht = getHibernateTemplate();
		// TODO: changed by fanhaoyu
		// Session session = getSessionFactory().openSession();
		// Transaction tx = session.beginTransaction();
		// getHibernateTemplate().saveOrUpdateAll(terminalBizParamList);
		List<TerminalBizParam> oldList = ht.find("from TerminalBizParam");
		ht.deleteAll(oldList);
		ht.saveOrUpdateAll(terminalBizParamList);
		// tx.commit();
	}

}
