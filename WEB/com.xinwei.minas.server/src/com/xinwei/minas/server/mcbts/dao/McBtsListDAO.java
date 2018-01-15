/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-27	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsCondition;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;
import com.xinwei.omp.core.model.biz.PagingData;

/**
 * ������ѯ��վ
 * 
 * @author liuzhongyan
 * 
 */

public interface McBtsListDAO extends GenericDAO<McBtsCondition, Long> {

	/**
	 * 
	 * ������ѯ��վ
	 * 
	 * @return
	 */
	public PagingData queryAllByCondition(McBtsCondition btsConditon);
}
