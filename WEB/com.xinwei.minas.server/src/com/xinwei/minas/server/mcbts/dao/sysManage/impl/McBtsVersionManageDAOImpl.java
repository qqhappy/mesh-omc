/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-19	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage.impl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersion;
import com.xinwei.minas.server.mcbts.dao.sysManage.McBtsVersionManageDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 基站版本管理DAO
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author chenshaohua
 * 
 */

public class McBtsVersionManageDAOImpl extends
		GenericHibernateDAO<McBtsVersion, Long> implements
		McBtsVersionManageDAO {

	@Override
	@SuppressWarnings("unchecked")
	public List<McBtsVersion> queryByBtsType(Integer btsType) {
		return getHibernateTemplate().find(
				"from McBtsVersion where btsType = " + btsType);
	}

	@Override
	public void delete(McBtsVersion mcBtsVersion) {
		final McBtsVersion entity = mcBtsVersion;
		HibernateTemplate ht = getHibernateTemplate();
		ht.execute(new HibernateCallback<Object>() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "delete McBtsVersion m where m.versionName=? and m.btsType=? and m.fileName=?";
				Query query = session.createQuery(hql);
				query.setParameter(0, entity.getVersionName());
				query.setParameter(1, entity.getBtsType());
				query.setParameter(2, entity.getFileName());
				query.executeUpdate();
				return null;
			}

		});
	}

	@Override
	public McBtsVersion queryByBtsTypeAndVersion(int btsType, String version) {
		@SuppressWarnings("unchecked")
		List<McBtsVersion> list = getHibernateTemplate().find(
				"from McBtsVersion where btsType = " + btsType
						+ " and versionName = '" + version + "'");

		if (list == null || list.isEmpty())
			return null;

		return list.get(0);
	}

}
