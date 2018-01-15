/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.McBtsSN;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 基站序列号DAO
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsSNDAO extends GenericDAO<McBtsSN, Long> {

	/**
	 * 从数据库查询记录
	 * 
	 * @param moId
	 * @return
	 */
	public List<McBtsSN> querySNFromDB(long moId);

	/**
	 * 保存记录到数据库
	 * 
	 * @param mcBtsSN1
	 */
	public void saveFixedCountRecord(McBtsSN mcBtsSN1);

	/**
	 * 查询最新记录
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsSN queryNewestRecord(long moId);

}
