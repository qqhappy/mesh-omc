/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.vo;

import com.xinwei.minas.zk.core.xnode.common.ZkNodeReserve;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeVO;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * SAG是否在位零时节点内容
 * 
 * @author chenjunhua
 * 
 */

public class ZkSagExistVO extends ZkNodeVO {

	private long sagId;

	public long getSagId() {
		return sagId;
	}

	public void setSagId(long sagId) {
		this.sagId = sagId;
	}

	@Override
	public void decode(byte[] buf, int offset) {
		this.sagId = ByteUtils.toLong(buf, offset, 4);
		offset += 4;
		this.zkNodeReserve.decode(buf, offset);
		offset += ZkNodeReserve.LEN;
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[4096];
		int offset = 0;
		ByteUtils.putNumber(buf, 0, this.sagId + "", 4);
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
		result = prime * result + (int) (sagId ^ (sagId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkSagExistVO))
			return false;
		ZkSagExistVO other = (ZkSagExistVO) obj;
		if (sagId != other.sagId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkSagExistVO [sagId=" + sagId + "]";
	}
	
	

}
