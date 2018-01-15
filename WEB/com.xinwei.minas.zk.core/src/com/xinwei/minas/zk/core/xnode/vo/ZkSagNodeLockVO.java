/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.vo;

import com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeReserve;
import com.xinwei.minas.zk.core.xnode.common.ZkNodeVO;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * SAG锁定节点内容
 * 
 * @author chenjunhua
 * 
 */

public class ZkSagNodeLockVO extends ZkNodeVO {

	private long sagId;
	
	// 4字节保留字段
	private long reserve;
	
	private ZkSagSessionVO sagSession;
	
	private ZkSagPayLoadVo sagPayLoad;
	
	// NK_MAX_NODENAME_LEN 个字节
	private String sagPath;

	public long getSagId() {
		return sagId;
	}

	public void setSagId(long sagId) {
		this.sagId = sagId;
	}

	public ZkSagSessionVO getSagSession() {
		return sagSession;
	}

	public void setSagSession(ZkSagSessionVO sagSession) {
		this.sagSession = sagSession;
	}

	public ZkSagPayLoadVo getSagPayLoad() {
		return sagPayLoad;
	}

	public void setSagPayLoad(ZkSagPayLoadVo sagPayLoad) {
		this.sagPayLoad = sagPayLoad;
	}

	public String getSagPath() {
		return sagPath;
	}

	public void setSagPath(String sagPath) {
		this.sagPath = sagPath;
	}

	@Override
	public void decode(byte[] buf, int offset) {
		this.sagId = Long.valueOf(ByteUtils.toLong(buf, offset, 4));
		offset += 4;
		// 跳过reserve
		offset += 4;
		//
		this.sagSession = new ZkSagSessionVO();
		this.sagSession.decode(buf, offset);
		offset += ZkSagSessionVO.LEN;
		this.sagPayLoad = new ZkSagPayLoadVo();
		this.sagPayLoad.decode(buf, offset);
		offset += ZkSagPayLoadVo.LEN;
		String charsetName = ZkNodeConstant.CHARSET_US_ASCII;
		this.sagPath = String.valueOf(
				ByteUtils.toString(buf, offset,
						ZkNodeConstant.NK_MAX_NODENAME_LEN, charsetName));
		offset += ZkNodeConstant.NK_MAX_NODENAME_LEN;
		this.zkNodeReserve.decode(buf, offset);
		offset += ZkNodeReserve.LEN;
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[4096];
		int offset = 0;
		ByteUtils.putNumber(buf, offset, this.sagId + "", 4);
		offset += 4;
		// 跳过reserve
		offset += 4;
		//
		if(this.sagSession == null)
			this.sagSession = new ZkSagSessionVO();
		System.arraycopy(this.sagSession.encode(), 0, buf, offset, ZkSagSessionVO.LEN);
		offset += ZkSagSessionVO.LEN;
		if(this.sagPayLoad == null)
			this.sagPayLoad = new ZkSagPayLoadVo();
		System.arraycopy(this.sagPayLoad.encode(), 0, buf, offset, ZkSagPayLoadVo.LEN);
		offset += ZkSagPayLoadVo.LEN;
		String charsetName = ZkNodeConstant.CHARSET_US_ASCII;
		char fillChar = '\0';
		ByteUtils.putString(buf, offset, this.sagPath,
				ZkNodeConstant.NK_MAX_NODENAME_LEN, fillChar, charsetName);
		offset += ZkNodeConstant.NK_MAX_NODENAME_LEN;
		if(this.zkNodeReserve == null)
			this.zkNodeReserve = new ZkNodeReserve();
		System.arraycopy(this.zkNodeReserve.encode(), 0, buf, offset, ZkNodeReserve.LEN);
		offset += ZkNodeReserve.LEN;
		// copy到实际字节长度的缓存
		byte[] result = new byte[offset];
		System.arraycopy(buf, 0, result, 0, result.length);
		return result;
	}

//	@Override
//	public int compareTo(ZkNodeVO other) {
//		if (other == null) {
//			return 1;
//		}
//		else if (other instanceof ZkSagNodeLockVO) {
//			ZkSagNodeLockVO otherVo = (ZkSagNodeLockVO)other;
//			return (int)(this.getSagId() - otherVo.getSagId());
//		}
//		return 0;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (sagId ^ (sagId >>> 32));
		result = prime * result + ((sagPath == null) ? 0 : sagPath.hashCode());
		result = prime * result
				+ ((sagPayLoad == null) ? 0 : sagPayLoad.hashCode());
		result = prime * result
				+ ((sagSession == null) ? 0 : sagSession.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkSagNodeLockVO))
			return false;
		ZkSagNodeLockVO other = (ZkSagNodeLockVO) obj;
		if (sagId != other.sagId)
			return false;
		if (sagPath == null) {
			if (other.sagPath != null)
				return false;
		} else if (!sagPath.equals(other.sagPath))
			return false;
		if (sagPayLoad == null) {
			if (other.sagPayLoad != null)
				return false;
		} else if (!sagPayLoad.equals(other.sagPayLoad))
			return false;
		if (sagSession == null) {
			if (other.sagSession != null)
				return false;
		} else if (!sagSession.equals(other.sagSession))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkSagNodeLockVO [sagId=" + sagId + ", sagSession=" + sagSession
				+ ", sagPayLoad=" + sagPayLoad + ", sagPath=" + sagPath + "]";
	}
	
	
}
