/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-2	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.oamManage;

import com.xinwei.minas.mcbts.core.model.oamManage.McbtsSupportedBiz;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author chenshaohua
 * 
 */

public interface SupportedBizDAO extends GenericDAO<McbtsSupportedBiz, Long> {

	/**
	 * 根据btsType、version、moc查询数据库
	 * 
	 * @param btsType
	 * @param version
	 * @param moc
	 * @return
	 */
	public McbtsSupportedBiz queryByCondition(int btsType, String version,
			int moc);

	/**
	 * 根据btsType、version清空数据库
	 */
	public void clearByBtsTypeAndVersion(int btsType, String version);

	
	public void updateVlaue(int btsType, String version, int moc, Integer value);
}
