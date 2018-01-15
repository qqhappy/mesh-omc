/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-29	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.xinwei.minas.mcbts.core.model.sysManage.LocationArea;
import com.xinwei.minas.server.mcbts.dao.sysManage.LocAreaDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 
 * @author chenshaohua
 * 
 */

public class LocAreaDAOImpl extends GenericHibernateDAO<LocationArea, Long>
		implements LocAreaDAO {

	private static transient final Log log = LogFactory
			.getLog(LocAreaDAOImpl.class);

	public List<LocationArea> queryAll() {
		List locationAreaList = getHibernateTemplate()
				.find("from LocationArea");
		return locationAreaList;
	}

	public void saveOrUpdate(List<LocationArea> locationAreaList) {
		HibernateTemplate ht = getHibernateTemplate();
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		try {
			List<LocationArea> oldList = ht.find("from LocationArea");
			ht.deleteAll(oldList);
			ht.saveOrUpdateAll(locationAreaList);
			tx.commit();
		} catch (Exception e) {
			log.error("保存数据错误。错误业务：位置区", e);
		} finally {
			if (session != null) {
				session.close();
			}

		}

	}

}
