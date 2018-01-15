/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-3	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.oamManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.McBtsSateQuery;
import com.xinwei.minas.mcbts.core.model.common.McBtsUserCountsQuery;

/**
 * 
 * service
 * 
 * @author fangping
 * 
 */

public interface McBtsUserCountsQueryService {
	public List<String> config(Integer restudy, Long moId) throws Exception;
	/**
	 * 向基站查询基站序列号
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public McBtsUserCountsQuery queryInfoFromNE(Long moId) throws Exception;

	/**
	 * 向数据库查询基站序列号
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<McBtsUserCountsQuery> queryInfoFromDB(long moId) throws Exception;
}
