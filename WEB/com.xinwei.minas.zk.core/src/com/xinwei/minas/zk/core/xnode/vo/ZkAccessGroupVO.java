/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.vo;

import com.xinwei.minas.zk.core.xnode.common.ZkNodeReserve;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeVO;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * AccessGroup (GROUPEDBTS下的动态临时节点)
 * 
 * @author fanhaoyu
 * 
 */

public class ZkAccessGroupVO extends ZkNodeVO {
	
	private Long btsGroupId;

	public Long getBtsGroupId() {
		return btsGroupId;
	}

	public void setBtsGroupId(Long btsGroupId) {
		this.btsGroupId = btsGroupId;
	}

	@Override
	public void decode(byte[] buf, int offset) {
		this.btsGroupId = Long.valueOf(ByteUtils.toLong(buf, offset, 4));
		offset += 4;
		this.zkNodeReserve.decode(buf, offset);
		offset += ZkNodeReserve.LEN;
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[4096];
		int offset = 0;
		ByteUtils.putNumber(buf, 0, btsGroupId + "", 4);
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
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkAccessGroupVO))
			return false;
		ZkAccessGroupVO other = (ZkAccessGroupVO) obj;
		if (btsGroupId == null) {
			if (other.btsGroupId != null)
				return false;
		} else if (!btsGroupId.equals(other.btsGroupId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkAccessGroupVO [btsGroupId=" + btsGroupId + "]";
	}
	
	

}
