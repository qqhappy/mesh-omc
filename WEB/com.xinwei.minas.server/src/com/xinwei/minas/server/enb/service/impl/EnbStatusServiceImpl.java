/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-11-12	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service.impl;

import java.util.List;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbDynamicInfo;
import com.xinwei.minas.enb.core.model.EnbDynamicInfoCondition;
import com.xinwei.minas.enb.core.model.status.EnbStatusConfigCondition;
import com.xinwei.minas.enb.core.model.status.EnbStatusConstants;
import com.xinwei.minas.enb.core.model.status.EnbStatusQueryCondition;
import com.xinwei.minas.server.enb.proxy.EnbStatusProxy;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbStatusService;

/**
 * 
 * eNB状态信息服务接口实现
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatusServiceImpl implements EnbStatusService {

	private EnbStatusProxy enbStatusProxy;

	@Override
	public List<Object> queryStatus(long moId, EnbStatusQueryCondition condition)
			throws Exception {
		return enbStatusProxy.queryStatus(moId, condition);
	}

	
	@Override
	public EnbDynamicInfo queryEnbDynamicInfo(EnbDynamicInfoCondition condition)
			throws Exception {
		return enbStatusProxy.queryEnbDynamicInfo(condition);
	}
	
	
	@Override
	public void configStatus(long moId, EnbStatusConfigCondition condition)
			throws Exception {
		enbStatusProxy.configStatus(moId, condition);
	}

	@Override
	public void configEnbTime(long moId, Long enbTime) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		EnbStatusConfigCondition condition = new EnbStatusConfigCondition();
		condition.setEnbId(enb.getEnbId());
		condition.setFlag(EnbStatusConstants.ENB_TIME);
		condition.setEnbTime(enbTime);

		enbStatusProxy.configStatus(moId, condition);
	}

	@Override
	public void configRfSwitch(long moId, int rackNo, int shelfNo, int slotNo,
			Integer rfSwitch) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		EnbStatusConfigCondition condition = new EnbStatusConfigCondition();
		condition.setEnbId(enb.getEnbId());
		condition.setFlag(EnbStatusConstants.RF_SWITCH);
		condition.setRackNo(rackNo);
		condition.setShelfNo(shelfNo);
		condition.setSlotNo(slotNo);
		condition.setPowerSwitch(rfSwitch);

		enbStatusProxy.configStatus(moId, condition);
	}

	public void setEnbStatusProxy(EnbStatusProxy enbStatusProxy) {
		this.enbStatusProxy = enbStatusProxy;
	}


	@Override
	public List<Object> configAirFlowBegin(long moId, String ipAddress) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		EnbStatusQueryCondition condition = new EnbStatusQueryCondition();
		condition.setEnbId(enb.getEnbId());
		condition.setFlag(EnbStatusConstants.AIR_FLOW_BEGIN);
		condition.setIpAddress(ipAddress);
	    return 	enbStatusProxy.queryStatus(moId, condition);
	}


	@Override
	public List<Object> configAirFlowEnd(long moId) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		EnbStatusQueryCondition condition = new EnbStatusQueryCondition();
		condition.setEnbId(enb.getEnbId());
		condition.setFlag(EnbStatusConstants.AIR_FLOW_END);
	    return 	enbStatusProxy.queryStatus(moId, condition);
	}


	@Override
	public List<Object> configS1Package(long moId) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		EnbStatusQueryCondition condition = new EnbStatusQueryCondition();
		condition.setEnbId(enb.getEnbId());
		condition.setFlag(EnbStatusConstants.S1_PACKAGE_TEST);
		return enbStatusProxy.queryStatus(moId, condition);
	}


	@Override
	public List<Object> configS1Time(long moId) throws Exception {
		Enb enb = EnbCache.getInstance().queryByMoId(moId);
		EnbStatusQueryCondition condition = new EnbStatusQueryCondition();
		condition.setEnbId(enb.getEnbId());
		condition.setFlag(EnbStatusConstants.S1_TIME_TEST);
		return enbStatusProxy.queryStatus(moId, condition);
	}



}
