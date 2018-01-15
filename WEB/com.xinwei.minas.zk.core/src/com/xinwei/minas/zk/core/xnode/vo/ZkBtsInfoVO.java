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
 * BTS INFO 节点内容
 * 
 * @author chenjunhua
 * 
 */

public class ZkBtsInfoVO extends ZkNodeVO {	
	
	// 最大子节点数目, 即最大基站组个数(1-50)
	private int maxSubNode;

	public int getMaxSubNode() {
		return maxSubNode;
	}

	public void setMaxSubNode(int maxSubNode) {
		this.maxSubNode = maxSubNode;
	}

	@Override
	public void decode(byte[] buf, int offset) {
		this.maxSubNode = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.zkNodeReserve.decode(buf, offset);
		offset += ZkNodeReserve.LEN;
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[4096];
		int offset = 0;
		ByteUtils.putNumber(buf, 0, maxSubNode + "", 4);
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
		result = prime * result + maxSubNode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkBtsInfoVO))
			return false;
		ZkBtsInfoVO other = (ZkBtsInfoVO) obj;
		if (maxSubNode != other.maxSubNode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkBtsInfoVO [maxSubNode=" + maxSubNode + "]";
	}
	
}
