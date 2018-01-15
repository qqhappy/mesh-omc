/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-28	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.dao.impl;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.MoRegion;
import com.xinwei.minas.server.core.conf.dao.MoBasicDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 被管对象基本DAO
 * 
 * @author chenjunhua
 * 
 */

public class MoBasicDAOImpl extends GenericHibernateDAO<Mo, Long> implements
		MoBasicDAO {

	/**
	 * 查询所有Mo
	 * 
	 * @return
	 */
	public List<Mo> queryAllMo() {
		Session session = null;
		String sql = "select * from t_mo";
		try {
			session = getSession();
			// 采用原生的SQL查询，避免因为继承关系导致查询错误的问题
			List<Mo> list = session.createSQLQuery(sql).addEntity(Mo.class)
					.list();
			return list;
		} catch (Exception e) {
			return new LinkedList();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * 获取制定类型的MO
	 * 
	 * @param moTypeId
	 *            类型
	 * @return
	 */
	public List<Mo> queryByType(int moTypeId) {
		String sql = "from " + Mo.class + " as t "
				+ " where t.typeId=? order by t.moId";
		List<Mo> moList = getHibernateTemplate().find(sql,
				new Integer[] { moTypeId });
		return moList;
	}

	/**
	 * 增加被管对象
	 * 
	 * @param mo
	 *            被管对象
	 * @param regionId
	 *            归属地区编号
	 */
	public void add(Mo mo, long regionId) {
		super.saveOrUpdate(mo);
		MoRegion moRegion = new MoRegion(mo.getMoId(), regionId);
		getHibernateTemplate().saveOrUpdate(moRegion);
	}

}
