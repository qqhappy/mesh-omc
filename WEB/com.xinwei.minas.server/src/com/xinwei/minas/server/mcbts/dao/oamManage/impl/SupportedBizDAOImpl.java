/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-2	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.oamManage.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.xinwei.minas.mcbts.core.model.oamManage.McbtsSupportedBiz;
import com.xinwei.minas.server.mcbts.dao.oamManage.SupportedBizDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 同步配置DAO实现类
 * 
 * @author chenshaohua
 * 
 */

public class SupportedBizDAOImpl extends
		GenericHibernateDAO<McbtsSupportedBiz, Long> implements SupportedBizDAO {

	private static transient final Log log = LogFactory
			.getLog(SupportedBizDAOImpl.class);

	@Override
	public McbtsSupportedBiz queryByCondition(int btsType, String version,
			int moc) {
		List<McbtsSupportedBiz> mcbtsSupportedBizList = getHibernateTemplate()
				.find("from McbtsSupportedBiz m where m.btsType=? "
						+ "and m.softwareVersion=? and m.moc=?", btsType,
						version, moc);
		if (mcbtsSupportedBizList.isEmpty()) {
			return null;
		} else {
			return mcbtsSupportedBizList.get(0);
		}
	}

	@Override
	public void clearByBtsTypeAndVersion(int btsType, String version) {
		Session session = getSessionFactory().openSession();
		try {
			String delhql = "delete McbtsSupportedBiz t where t.btsType=? and t.softwareVersion=?";
			Query query = session.createQuery(delhql);
			query.setInteger(0, btsType);
			query.setString(1, version);
			query.executeUpdate();
		} catch (Exception e) {
			log.error("删除数据错误。错误业务：同步配置", e);
			log.error(e);
		} finally {
			if (session != null) {
				session.close();
			}

		}

		// Transaction tx = session.beginTransaction();
		// HibernateTemplate ht = getHibernateTemplate();
		// List<McbtsSupportedBiz> delList = ht
		// .find("from McbtsSupportedBiz t where t.btsType=" + btsType);
		// ht.deleteAll(delList);
		// tx.commit();
	}

	@Override
	public void updateVlaue(int btsType, String version, int moc, Integer value) {
		final int fbtsType = btsType;
		final String fversion = version;
		final int fmoc = moc;
		final Integer fvalue = value;
		final String sql = "update mcbts_supported_biz m set m.support = ? "
				+ "where m.btsType = ? and m.softwareVersion = ? and m.moc = ?";
		getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(org.hibernate.Session session)
					throws HibernateException, SQLException {
				SQLQuery squery = session.createSQLQuery(sql);
				squery.setInteger(0, fvalue);
				squery.setInteger(1, fbtsType);
				squery.setString(2, fversion);
				squery.setInteger(3, fmoc);
				return squery.executeUpdate();
			}
		});

	}

}
