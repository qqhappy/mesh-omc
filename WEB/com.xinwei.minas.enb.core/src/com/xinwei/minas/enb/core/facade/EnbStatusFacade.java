/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-12	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.model.status.EnbStatusQueryCondition;

/**
 * 
 * eNB状态信息门面接口
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbStatusFacade extends Remote {

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
	 * 设置eNB时间
	 * 
	 * @param moId
	 * @param enbTime
	 * @throws Exception
	 */
	@Loggable
	public void configEnbTime(OperObject operObject, long moId, Long enbTime)
			throws Exception;

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
	@Loggable
	public void configRfSwitch(OperObject operObject, long moId, int rackNo,
			int shelfNo, int slotNo, Integer rfSwitch) throws Exception;

	/**
	 * 开始灌包
	 * @param createEnbOperObject
	 * @param moId
	 * @param ipAddress
	 * @return
	 * @throws Exception
	 */
	List<Object> configAirFlowBegin(OperObject createEnbOperObject, long moId,
			String ipAddress) throws Exception;

	/**
	 * 停止灌包
	 * @param createEnbOperObject
	 * @param moId
	 * @return
	 * @throws Exception 
	 */
	public List<Object> configAirFlowEnd(OperObject createEnbOperObject,
			long moId) throws Exception;
	/**
	 * S1开始启动丢包率检测
	 * @param createEnbOperObject
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<Object> configS1Package(OperObject createEnbOperObject,
			long moId)throws Exception;

	/**
	 * S1时延检测
	 * @param createEnbOperObject
	 * @param moId
	 * @return
	 */
	public List<Object> configS1Time(OperObject createEnbOperObject, long moId)throws Exception;
	
}
