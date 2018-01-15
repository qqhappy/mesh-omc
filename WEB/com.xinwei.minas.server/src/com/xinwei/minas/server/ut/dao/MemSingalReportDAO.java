/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-13	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.dao;

import com.xinwei.minas.server.platform.generic.dao.GenericDAO;
import com.xinwei.minas.ut.core.model.MemSingalReport;

/**
 * 
 * �ն˵�����Ϣ�ϱ�DAO�ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface MemSingalReportDAO extends GenericDAO<MemSingalReport, Long>{
	/**
	 * �����ն˵�pid��ѯ
	 * @param pid
	 * @return
	 */
	public MemSingalReport queryByPid(Long pid);
}
