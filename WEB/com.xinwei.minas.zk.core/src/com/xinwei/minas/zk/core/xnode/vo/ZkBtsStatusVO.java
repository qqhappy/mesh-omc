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
 * 基站状态节点内容
 * 
 * @author chenjunhua
 * 
 */

public class ZkBtsStatusVO extends ZkNodeVO {

	public static final int BTS_SIGNAL_LINK_CONNECTED = 0;
	public static final int BTS_SIGNAL_LINK_DISCONNECTED = 1;

	public static final int BTS_MEDIA_LINK_CONNECTED = 0;
	public static final int BTS_MEDIA_LINK_DISCONNECTED = 1;
	public static final int BTS_MEDIA_LINK_NSP_DISCONNECTED = 2;

	// 信令链路状态 0：连接正常/1：基站断连
	private int signalLinkState = BTS_SIGNAL_LINK_DISCONNECTED;

	// 媒体链路状态 0：连接正常/1：基站断连/2：NSP板断连
	private int mediaLinkState = BTS_MEDIA_LINK_DISCONNECTED;

	public boolean isSignalLinkConnected() {
		if (signalLinkState == BTS_SIGNAL_LINK_CONNECTED) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isMediaLinkConnected() {
		if (mediaLinkState == BTS_MEDIA_LINK_CONNECTED) {
			return true;
		} else {
			return false;
		}
	}

	public int getSignalLinkState() {
		return signalLinkState;
	}

	public void setSignalLinkState(int signalLinkState) {
		this.signalLinkState = signalLinkState;
	}

	public int getMediaLinkState() {
		return mediaLinkState;
	}

	public void setMediaLinkState(int mediaLinkState) {
		this.mediaLinkState = mediaLinkState;
	}

	@Override
	public void decode(byte[] buf, int offset) {
		this.signalLinkState = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.mediaLinkState = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
		this.zkNodeReserve.decode(buf, offset);
		offset += ZkNodeReserve.LEN;
	}

	@Override
	public byte[] encode() {
		byte[] buf = new byte[4096];
		int offset = 0;
		ByteUtils.putNumber(buf, offset, this.signalLinkState + "", 4);
		offset += 4;
		ByteUtils.putNumber(buf, offset, this.mediaLinkState + "", 4);
		offset += 4;
		if (this.zkNodeReserve == null)
			this.zkNodeReserve = new ZkNodeReserve();
		System.arraycopy(this.zkNodeReserve.encode(), 0, buf, offset,
				ZkNodeReserve.LEN);
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
		result = prime * result + mediaLinkState;
		result = prime * result + signalLinkState;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ZkBtsStatusVO))
			return false;
		ZkBtsStatusVO other = (ZkBtsStatusVO) obj;
		if (mediaLinkState != other.mediaLinkState)
			return false;
		if (signalLinkState != other.signalLinkState)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ZkBtsStatusVO [signalLinkState=" + signalLinkState
				+ ", mediaLinkState=" + mediaLinkState + "]";
	}

}
