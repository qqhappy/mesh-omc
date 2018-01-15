/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.xinwei.minas.core.model.SystemProperty;
import com.xinwei.minas.server.core.conf.dao.SystemPropertyDAO;
import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;

/**
 * 
 * 系统属性基本DAO实现
 * 
 * @author tiance
 * 
 */

public class SystemPropertyDAOImpl extends
		GenericHibernateDAO<SystemProperty, Long> implements SystemPropertyDAO {

	public SystemProperty queryByCategoryAndProperty(String category,
			String sub_category, String property) {
		String sql = "";
		List<SystemProperty> properties = null;
		if (StringUtils.isBlank(sub_category)) {
			sql = "from SystemProperty sp where sp.category = ? and sp.property = ?";
			properties = getHibernateTemplate().find(sql, category, property);
		} else {
			sql = "from SystemProperty sp where sp.category = ? and sp.sub_category = ? and sp.property = ?";
			properties = getHibernateTemplate().find(sql, category,
					sub_category, property);
		}

		if (properties == null || properties.size() == 0)
			return null;

		return properties.get(0);
	}

	public List<SystemProperty> queryByCategory(String category,
			String sub_category) {
		// TODO: 实现用两个类别字段,搜索所有匹配的内容
		return null;
	}
}
