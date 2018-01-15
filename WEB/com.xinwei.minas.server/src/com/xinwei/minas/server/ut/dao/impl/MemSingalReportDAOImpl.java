/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.dao.impl;

import java.util.List;

import com.xinwei.minas.server.platform.generic.dao.GenericHibernateDAO;
import com.xinwei.minas.server.ut.dao.MemSingalReportDAO;
import com.xinwei.minas.ut.core.model.MemSingalReport;

/**
 * 
 * 终端调试信息上报DAO接口
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class MemSingalReportDAOImpl extends GenericHibernateDAO<MemSingalReport, Long> implements MemSingalReportDAO{

	@Override
	public MemSingalReport queryByPid(Long pid) {
		@SuppressWarnings("unchecked")
		List<MemSingalReport> result = getHibernateTemplate().find("from MemSingalReport m where m.pid=" + pid);
		if (result == null || result.size() == 0) {
			return null;
		}
		return (MemSingalReport)result.get(0) ;
	}

}
