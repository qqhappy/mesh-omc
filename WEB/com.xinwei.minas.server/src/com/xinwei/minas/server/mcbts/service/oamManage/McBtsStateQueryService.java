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

/**
 * 
 * service
 * 
 * @author fangping
 * 
 */

public interface McBtsStateQueryService {

/*	public static final int NEED_RESTUDY = 1;

	public static final int NOT_NEED_RESTUDY = 0;*/

	public List<String> config(Integer restudy, Long moId) throws Exception;
	/**
	 * 向基站查询基站序列号
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public McBtsSateQuery queryInfoFromNE(Long moId) throws Exception;

	/**
	 * 向数据库查询基站序列号
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<McBtsSateQuery> queryInfoFromDB(long moId) throws Exception;
}
