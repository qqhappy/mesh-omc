/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-2	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer3;

import java.util.List;

/**
 * 
 * 同播集群DAO接口
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface TConfMBMSClusterDAO {
	
	/**
	 * 查询所有同播集群下的基站moId
	 * @return
	 * @throws Exception
	 */
	public List<Long> queryAllMBMSClusterBtsMoId() throws Exception;
}
