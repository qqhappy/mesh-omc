/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.proxy;

import java.util.List;

import com.xinwei.minas.enb.core.model.EnbDynamicInfo;
import com.xinwei.minas.enb.core.model.EnbDynamicInfoCondition;
import com.xinwei.minas.enb.core.model.status.EnbStatusConfigCondition;
import com.xinwei.minas.enb.core.model.status.EnbStatusQueryCondition;

/**
 * 
 * eNB状态代理接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbStatusProxy {

	/**
	 * 按照查询条件查询eNB状态
	 * 
	 * @param moId
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public List<Object> queryStatus(long moId, EnbStatusQueryCondition condition)
			throws Exception;
	
	/**
	 * 按条件查询动态信息
	 * 
	 * @param condition
	 * @return
	 */
	public EnbDynamicInfo queryEnbDynamicInfo(EnbDynamicInfoCondition condition)
			throws Exception;

	/**
	 * 配置eNB状态
	 * 
	 * @param moId
	 * @param condition
	 * @throws Exception
	 */
	public void configStatus(long moId, EnbStatusConfigCondition condition)
			throws Exception;

}
