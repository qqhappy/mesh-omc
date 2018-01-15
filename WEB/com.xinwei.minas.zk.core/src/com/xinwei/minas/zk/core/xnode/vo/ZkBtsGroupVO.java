/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-15	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.vo;

import com.xinwei.minas.zk.core.xnode.common.ZkNodeReserve;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeVO;
import com.xinwei.omp.core.utils.ByteUtils;


/**
 * 
 * BtsGroup 节点内容
 * 
 * @author chenjunhua
 * 
 */

public class ZkBtsGroupVO extends ZkNodeVO {
	
	public static final int NOT_JOIN_AUTO_SWITCH = 0;
	public static final int JOIN_AUTO_SWITCH = 1;
	
	// 最大子节点数目, 即最大基站个数(1-10)
	private int maxSubNode;
	
	// 基站组ID
	private Long btsGroupId;	
	
	// 规划的SAGID
	private Long homeSagId;
	
	// 是否参与RULES规则: 0-不参与,只能接入规划的SAG; 1-参与
	private int rulesFlag;

	public int getMaxSubNode() {
		return maxSubNode;
	}

	public void setMaxSubNode(int maxSubNode) {
		this.maxSubNode = maxSubNode;
	}

	public Long getBtsGroupId() {
		return btsGroupId;
	}

	public void setBtsGroupId(Long btsGroupId) {
		this.btsGroupId = btsGroupId;
	}

	public Long getHomeSagId() {
		return homeSagId;
	}

	public void setHomeSagId(Long homeSagId) {
		this.homeSagId = homeSagId;
	}

	public int getRulesFlag() {
		return rulesFlag;
	}

	public void setRulesFlag(int rulesFlag) {
		this.rulesFlag = rulesFlag;
	}

	@Override
	public void decode(byte[] buf, int offset) {
		this.maxSubNode = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.btsGroupId = Long.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.homeSagId = Long.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.rulesFlag = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.zkNodeReserve.decode(buf, offset);
		offset += ZkNodeReserve.LEN;
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[4096];
		int offset = 0;
		ByteUtils.putNumber(buf, offset, maxSubNode + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, btsGroupId + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, homeSagId + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, rulesFlag + "", 4);
		offset += 4;
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
		result = prime * result
				+ ((btsGroupId == null) ? 0 : btsGroupId.hashCode());
		result = prime * result
				+ ((homeSagId == null) ? 0 : homeSagId.hashCode());
		result = prime * result + maxSubNode;
		result = prime * result + rulesFlag;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkBtsGroupVO))
			return false;
		ZkBtsGroupVO other = (ZkBtsGroupVO) obj;
		if (btsGroupId == null) {
			if (other.btsGroupId != null)
				return false;
		} else if (!btsGroupId.equals(other.btsGroupId))
			return false;
		if (homeSagId == null) {
			if (other.homeSagId != null)
				return false;
		} else if (!homeSagId.equals(other.homeSagId))
			return false;
		if (maxSubNode != other.maxSubNode)
			return false;
		if (rulesFlag != other.rulesFlag)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkBtsGroupVO [maxSubNode=" + maxSubNode + ", btsGroupId="
				+ btsGroupId + ", homeSagId=" + homeSagId + ", rulesFlag="
				+ rulesFlag + "]";
	}
	
	
}
