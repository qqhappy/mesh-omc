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
 * 基站服务SAG的节点内容
 * 对应_st_nk_sagsrf_data
 * @author chenjunhua
 * 
 */

public class ZkBtsServiceSagVO extends ZkNodeVO {
	
	// 上次服务的SAG ID
    private long lastServiceSagId;
    
	// 4字节保留字段
	private long reserve;
	
	private ZkSagSessionVO sagSession;

	public long getLastServiceSagId() {
		return lastServiceSagId;
	}

	public void setLastServiceSagId(long lastServiceSagId) {
		this.lastServiceSagId = lastServiceSagId;
	}

	@Override
	public void decode(byte[] buf, int offset) {
		this.lastServiceSagId = Long.valueOf(ByteUtils.toLong(buf, offset, 4));
		offset += 4;
		// 跳过reserve
		offset += 4;
		//
		this.sagSession = new ZkSagSessionVO();
		this.sagSession.decode(buf, offset);
		offset += ZkSagSessionVO.LEN;
		
		this.zkNodeReserve.decode(buf, offset);
		offset += ZkNodeReserve.LEN;
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[4096];
		int offset = 0;
		ByteUtils.putNumber(buf, 0, lastServiceSagId + "", 4);
		offset += 4;
		// 跳过reserve
		offset += 4;
		//
		if(this.sagSession == null)
			this.sagSession = new ZkSagSessionVO();
		System.arraycopy(this.sagSession.encode(), 0, buf, offset, ZkSagSessionVO.LEN);
		offset += ZkSagSessionVO.LEN;
		
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
				+ (int) (lastServiceSagId ^ (lastServiceSagId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkBtsServiceSagVO))
			return false;
		ZkBtsServiceSagVO other = (ZkBtsServiceSagVO) obj;
		if (lastServiceSagId != other.lastServiceSagId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkBtsServiceSagVO [lastServiceSagId=" + lastServiceSagId + "]";
	}

	
}
