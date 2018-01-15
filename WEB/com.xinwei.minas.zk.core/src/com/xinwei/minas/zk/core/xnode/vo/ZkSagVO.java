/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-15	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.vo;

import java.util.HashSet;
import java.util.Set;

import com.xinwei.minas.zk.core.xnode.common.ZkNodeReserve;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeVO;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * SAG节点内容
 * 
 * @author chenjunhua
 * 
 */

public class ZkSagVO extends ZkNodeVO {

	public static final int SAG_STATUS_INITIALIZED = 0;
	public static final int SAG_STATUS_STARTED = 1;
	public static final int SAG_STATUS_STOPPED = 2;
	
	// 0:初始化状态。1-启用状态。2-停用状态。创建时为初始化状态。
	private int sagStatus = SAG_STATUS_INITIALIZED;

	// SAGID
	private Long sagId;

	// 最大接入基站组数(1-60)
	private int maxBtsGroupNum;

	// 规划接入的BTS组ID列表
	private Set<Long> planBtsGroupIds = new HashSet();

	public int getSagStatus() {
		return sagStatus;
	}

	public void setSagStatus(int sagStatus) {
		this.sagStatus = sagStatus;
	}

	public Long getSagId() {
		return sagId;
	}

	public void setSagId(Long sagId) {
		this.sagId = sagId;
	}

	public int getMaxBtsGroupNum() {
		return maxBtsGroupNum;
	}

	public void setMaxBtsGroupNum(int maxBtsGroupNum) {
		this.maxBtsGroupNum = maxBtsGroupNum;
	}

	public Set<Long> getPlanBtsGroupIds() {
		return planBtsGroupIds;
	}

	public void setPlanBtsGroupIds(Set<Long> planBtsGroupIds) {
		this.planBtsGroupIds = planBtsGroupIds;
	}
	
	public String getPlanBtsGroupIdsString() {
		String ids = "";
		for (Long id : planBtsGroupIds) {
			ids += id.toString() + ",";
		}
		if (ids != "")
			ids = ids.substring(0, ids.length() - 1);
		return ids;
	}
	
	public void setPlanBtsGroupIds(String planBtsGroupIds) {
		if(planBtsGroupIds == null || planBtsGroupIds.equals(""))
			return;
		String[] ids = planBtsGroupIds.split(", *");
		for (String id : ids) {
			this.planBtsGroupIds.add(Long.valueOf(id));
		}
	}

	public boolean isInitialized() {
		if(this.sagStatus == SAG_STATUS_INITIALIZED)
			return true;
		return false;
	}
	
	public boolean isStarted() {
		if(this.sagStatus == SAG_STATUS_STARTED)
			return true;
		return false;
	}

	public boolean isStopped() {
		if(this.sagStatus == SAG_STATUS_STOPPED)
			return true;
		return false;
	}
	
	@Override
	public void decode(byte[] buf, int offset) {
		this.sagStatus = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.sagId = Long.valueOf(ByteUtils.toLong(buf, offset, 4));
		offset += 4;
		this.maxBtsGroupNum = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
//		while (offset < buf.length) {
//			Long id = Long.valueOf(ByteUtils.toLong(buf, offset, 4));
//			if(id == 0)
//				break;
//			planBtsGroupIds.add(id);
//			offset += 4;
//		}
		// 保留字节（原先是规划的基站组ID列表, 现在没有用, 20130617和王道静确认）
		offset += 80;
		
		this.zkNodeReserve.decode(buf, offset);
		offset += ZkNodeReserve.LEN;
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[4096];
		int offset = 0;
		ByteUtils.putNumber(buf, offset, sagStatus + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, sagId + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, maxBtsGroupNum + "", 4);
		offset += 4;
//		for (Long planBtsGroupId : planBtsGroupIds) {
//			ByteUtils.putNumber(buf, offset, planBtsGroupId + "", 4);
//			offset += 4;
//		}
		// // 保留字节（原先是规划的基站组ID列表, 现在没有用, 20130617和王道静确认）
		offset += 80;
		if(this.zkNodeReserve == null)
			this.zkNodeReserve = new ZkNodeReserve();
		System.arraycopy(this.zkNodeReserve.encode(), 0, buf, offset, ZkNodeReserve.LEN);
		offset += ZkNodeReserve.LEN;
		// copy到实际字节长度的缓存
		byte[] result = new byte[offset];
		System.arraycopy(buf, 0, result, 0, result.length);
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + maxBtsGroupNum;
		result = prime * result
				+ ((planBtsGroupIds == null) ? 0 : planBtsGroupIds.hashCode());
		result = prime * result + ((sagId == null) ? 0 : sagId.hashCode());
		result = prime * result + sagStatus;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkSagVO))
			return false;
		ZkSagVO other = (ZkSagVO) obj;
		if (maxBtsGroupNum != other.maxBtsGroupNum)
			return false;
		if (planBtsGroupIds == null) {
			if (other.planBtsGroupIds != null)
				return false;
		} else if (!planBtsGroupIds.equals(other.planBtsGroupIds))
			return false;
		if (sagId == null) {
			if (other.sagId != null)
				return false;
		} else if (!sagId.equals(other.sagId))
			return false;
		if (sagStatus != other.sagStatus)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkSagVO [sagStatus=" + sagStatus + ", sagId=" + sagId
				+ ", maxBtsGroupNum=" + maxBtsGroupNum + ", planBtsGroupIds="
				+ planBtsGroupIds + "]";
	}
	
	

}
