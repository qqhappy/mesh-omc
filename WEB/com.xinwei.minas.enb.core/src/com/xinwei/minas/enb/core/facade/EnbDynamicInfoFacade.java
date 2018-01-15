/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;

import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.enb.core.model.EnbDynamicInfo;
import com.xinwei.minas.enb.core.model.EnbDynamicInfoCondition;

/**
 * 
 * eNodeB动态信息门面
 * 
 * @author chenjunhua
 * 
 */

public interface EnbDynamicInfoFacade extends Remote{

	/**
	 * 根据条件查询动态信息
	 * 
	 * @param condition
	 * @return
	 */
	public EnbDynamicInfo queryEnbDynamicInfo(EnbDynamicInfoCondition condition)
			throws Exception;
	/**
	 * 修改小基站时间
	 * @param operObject
	 * @param moId
	 * @param enbTime
	 * @throws Exception 
	 */
	public void changeEnbTime(OperObject operObject, long moId, Long enbTime) throws Exception;
    /**
     * 设置通道
     * @param operObject
     * @param moId
     * @param rackNo
     * @param shelfNo
     * @param slotNo
     * @param rfSwitch
     * @throws Exception
     */
	public void configRfSwitch(OperObject operObject, long moId, int rackNo,
			int shelfNo, int slotNo, Integer rfSwitch)throws Exception;
}
