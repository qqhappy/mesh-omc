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
 * SAG Group�ڵ�����
 * 
 * @author chenjunhua
 * 
 */

public class ZkSagGroupVO extends ZkNodeVO {

	public static final int SAGGROUP_STATUS_INITIALIZED = 0;
	public static final int SAGGROUP_STATUS_STARTED = 1;
	public static final int SAGGROUP_STATUS_STOPPED = 2;
	
	// 0:��ʼ��״̬��1-����״̬��2-ͣ��״̬������ʱΪ��ʼ��״̬��
	private int groupStatus = SAGGROUP_STATUS_INITIALIZED;
	
	//����ӽڵ���� ��Ĭ��Ϊ3
	private int maxSubNode = 3;
	
	// SAGȺ��ID
	private Long sagGroupId;

	public int getGroupStatus() {
		return groupStatus;
	}

	public void setGroupStatus(int groupStatus) {
		this.groupStatus = groupStatus;
	}

	public int getMaxSubNode() {
		return maxSubNode;
	}

	public void setMaxSubNode(int maxSubNode) {
		this.maxSubNode = maxSubNode;
	}

	public Long getSagGroupId() {
		return sagGroupId;
	}

	public void setSagGroupId(Long sagGroupId) {
		this.sagGroupId = sagGroupId;
	}

	public boolean isInitialized() {
		if(this.groupStatus == SAGGROUP_STATUS_INITIALIZED)
			return true;
		return false;
	}
	
	public boolean isStarted() {
		if(this.groupStatus == SAGGROUP_STATUS_STARTED)
			return true;
		return false;
	}

	public boolean isStopped() {
		if(this.groupStatus == SAGGROUP_STATUS_STOPPED)
			return true;
		return false;
	}

	@Override
	public void decode(byte[] buf, int offset) {
		this.groupStatus = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.maxSubNode = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.sagGroupId = Long.valueOf(ByteUtils.toLong(buf, offset, 4));
		offset += 4;
		this.zkNodeReserve.decode(buf, offset);
		offset += ZkNodeReserve.LEN;
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[4096];
		int offset = 0;
		ByteUtils.putNumber(buf, offset, groupStatus + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, maxSubNode + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, sagGroupId + "", 4);
		offset += 4;
		if(this.zkNodeReserve == null)
			this.zkNodeReserve = new ZkNodeReserve();
		System.arraycopy(this.zkNodeReserve.encode(), 0, buf, offset, ZkNodeReserve.LEN);
		offset += ZkNodeReserve.LEN;
		// copy��ʵ���ֽڳ��ȵĻ���
		byte[] result = new byte[offset];
		System.arraycopy(buf, 0, result, 0, result.length);
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + groupStatus;
		result = prime * result + maxSubNode;
		result = prime * result
				+ ((sagGroupId == null) ? 0 : sagGroupId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkSagGroupVO))
			return false;
		ZkSagGroupVO other = (ZkSagGroupVO) obj;
		if (groupStatus != other.groupStatus)
			return false;
		if (maxSubNode != other.maxSubNode)
			return false;
		if (sagGroupId == null) {
			if (other.sagGroupId != null)
				return false;
		} else if (!sagGroupId.equals(other.sagGroupId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkSagGroupVO [groupStatus=" + groupStatus + ", maxSubNode="
				+ maxSubNode + ", sagGroupId=" + sagGroupId + "]";
	}

	
	
}
