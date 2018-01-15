/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-11	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.util.List;

import com.xinwei.minas.enb.core.model.EnbDynamicInfo;
import com.xinwei.minas.enb.core.model.EnbDynamicInfoCondition;
import com.xinwei.minas.enb.core.model.status.EnbStatusConfigCondition;
import com.xinwei.minas.enb.core.model.status.EnbStatusQueryCondition;

/**
 * 
 * eNB状态信息服务接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbStatusService {

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

	/**
	 * 设置eNB时间
	 * 
	 * @param moId
	 * @param enbTime
	 * @throws Exception
	 */
	public void configEnbTime(long moId, Long enbTime) throws Exception;

	/**
	 * 设置功放开关
	 * 
	 * @param moId
	 * @param rackNo
	 * @param shelfNo
	 * @param slotNo
	 * @param rfSwitch
	 * @throws Exception
	 */
	public void configRfSwitch(long moId, int rackNo, int shelfNo, int slotNo,
			Integer rfSwitch) throws Exception;
	

	/**
	 * 设置空口流量测试开始灌注
	 * @param moId
	 * @param ipAddress
	 * @return 
	 * @throws Exception 
	 */
	public List<Object> configAirFlowBegin(long moId, String ipAddress) throws Exception;

	/**
	 * 设置空口流量测试结束灌注
	 * @param moId
	 * @return
	 * @throws Exception 
	 */
	public List<Object> configAirFlowEnd(long moId) throws Exception;
	
	/**
	 * 启动S1丢包测试
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<Object> configS1Package(long moId) throws Exception;

	public List<Object> configS1Time(long moId)throws Exception;

}
