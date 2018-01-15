/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.vo;

import java.util.Arrays;

import com.xinwei.minas.zk.core.xnode.common.ZkNodeVO;
import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * SAG session 内容
 * 
 * @author chenjunhua
 * 
 */

public class ZkSagSessionVO extends ZkNodeVO {

	public static final int LEN = 24;
	// 8个字节
	private long sessionId;
	
	private byte[] password = new byte[16];

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	@Override
	public void decode(byte[] buf, int offset) {
		this.sessionId = Long.valueOf(ByteUtils.toLong(buf, offset, 8));
		offset += 8;
		System.arraycopy(buf, offset, this.password, 0, 16);
		offset += 16;
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[256];
		int offset = 0;
		ByteUtils.putNumber(buf, offset, this.sessionId + "", 8);
		offset += 8;
		System.arraycopy(this.password, 0, buf, offset, 16);
		offset += 16;
		// copy到实际字节长度的缓存
		byte[] result = new byte[offset];
		System.arraycopy(buf, 0, result, 0, result.length);
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(password);
		result = prime * result + (int) (sessionId ^ (sessionId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkSagSessionVO))
			return false;
		ZkSagSessionVO other = (ZkSagSessionVO) obj;
		if (!Arrays.equals(password, other.password))
			return false;
		if (sessionId != other.sessionId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkSagSessionVO [sessionId=" + sessionId + ", password="
				+ Arrays.toString(password) + "]";
	}
	
	
}
