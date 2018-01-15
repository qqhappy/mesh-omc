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
 * ��վ��SAG���ӽڵ����� 
 * 
 * @author chenjunhua
 * 
 */

public class ZkBtsSagLinkVO extends ZkNodeVO {
	// SAG ID
	private long sagId;

	// SAG����IP
	private String sagSignalIp;

	// SAG����Port��Ҫ��ʹ����ͬ�˿�
	private int sagSignalPort;

	// SAGý��IP
	private String sagMediaIP;

	// SAGý��Port
	private int sagMediaPort;

	// SAG��DPID
	private long sagDPID;

	public long getSagId() {
		return sagId;
	}

	public void setSagId(long sagId) {
		this.sagId = sagId;
	}

	public String getSagSignalIp() {
		return sagSignalIp;
	}

	public void setSagSignalIp(String sagSignalIp) {
		this.sagSignalIp = sagSignalIp;
	}

	public int getSagSignalPort() {
		return sagSignalPort;
	}

	public void setSagSignalPort(int sagSignalPort) {
		this.sagSignalPort = sagSignalPort;
	}

	public String getSagMediaIP() {
		return sagMediaIP;
	}

	public void setSagMediaIP(String sagMediaIP) {
		this.sagMediaIP = sagMediaIP;
	}

	public int getSagMediaPort() {
		return sagMediaPort;
	}

	public void setSagMediaPort(int sagMediaPort) {
		this.sagMediaPort = sagMediaPort;
	}

	public long getSagDPID() {
		return sagDPID;
	}

	public void setSagDPID(long sagDPID) {
		this.sagDPID = sagDPID;
	}

	@Override
	public void decode(byte[] buf, int offset) {
		this.sagId = Long.valueOf(ByteUtils.toLong(buf, offset, 4));
		offset += 4;
		this.sagSignalIp = String.valueOf(ByteUtils.toIp(buf, offset, 4));
		offset += 4;
		this.sagSignalPort = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.sagMediaIP = String.valueOf(ByteUtils.toIp(buf, offset, 4));
		offset += 4;
		this.sagMediaPort = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.sagDPID = Long.valueOf(ByteUtils.toLong(buf, offset, 4));
		offset += 4;
		this.zkNodeReserve.decode(buf, offset);
		offset += ZkNodeReserve.LEN;
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[4096];
		int offset = 0;
		ByteUtils.putNumber(buf, offset, this.sagId + "", 4);
		offset += 4;
		ByteUtils.putIp(buf, offset, this.sagSignalIp);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.sagSignalPort + "", 4);
		offset += 4;
		ByteUtils.putIp(buf, offset, this.sagMediaIP);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.sagMediaPort + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.sagDPID + "", 4);
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
		result = prime * result + (int) (sagDPID ^ (sagDPID >>> 32));
		result = prime * result + (int) (sagId ^ (sagId >>> 32));
		result = prime * result
				+ ((sagMediaIP == null) ? 0 : sagMediaIP.hashCode());
		result = prime * result + sagMediaPort;
		result = prime * result
				+ ((sagSignalIp == null) ? 0 : sagSignalIp.hashCode());
		result = prime * result + sagSignalPort;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkBtsSagLinkVO))
			return false;
		ZkBtsSagLinkVO other = (ZkBtsSagLinkVO) obj;
		if (sagDPID != other.sagDPID)
			return false;
		if (sagId != other.sagId)
			return false;
		if (sagMediaIP == null) {
			if (other.sagMediaIP != null)
				return false;
		} else if (!sagMediaIP.equals(other.sagMediaIP))
			return false;
		if (sagMediaPort != other.sagMediaPort)
			return false;
		if (sagSignalIp == null) {
			if (other.sagSignalIp != null)
				return false;
		} else if (!sagSignalIp.equals(other.sagSignalIp))
			return false;
		if (sagSignalPort != other.sagSignalPort)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkBtsSagLinkVO [sagId=" + sagId + ", sagSignalIp="
				+ sagSignalIp + ", sagSignalPort=" + sagSignalPort
				+ ", sagMediaIP=" + sagMediaIP + ", sagMediaPort="
				+ sagMediaPort + ", sagDPID=" + sagDPID + "]";
	}
	
	

}
