/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.common;

import static com.xinwei.minas.zk.core.xnode.common.ZkNodeConstant.*;

import java.io.Serializable;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * ZK节点头部信息
 * 
 * @author chenjunhua
 * 
 */

public class ZkNodeHeader implements Serializable, Cloneable {

	// 节点数据Header长度
	public static final int LEN = 2 + 2 + NK_NODE_COMMENT_LEN;

	// 节点类型（2字节）
	private int nodeType;

	// 保留字段1
	private int reserve1;

	// 保留字节
	private byte[] reserveData = new byte[NK_NODE_COMMENT_LEN];

	// added by fanhaoyu begin
	public ZkNodeHeader(int nodeType) {
		this.nodeType = nodeType;
	}

	// end
	public ZkNodeHeader(byte[] buf) {
		int offset = 0;
		// 节点类型
		nodeType = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
		//
		reserve1 = ByteUtils.toInt(buf, offset, 2);
		offset += 2;
		//
		System.arraycopy(buf, offset, reserveData, 0, NK_NODE_COMMENT_LEN);
		offset += NK_NODE_COMMENT_LEN;
	}

	public int getNodeType() {
		return nodeType;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	public int getReserve1() {
		return reserve1;
	}

	public void setReserve1(int reserve1) {
		this.reserve1 = reserve1;
	}

	public byte[] getReserveData() {
		return reserveData;
	}

	public void setReserveData(byte[] reserveData) {
		this.reserveData = reserveData;
	}

	@Override
	public String toString() {
		return "ZkNodeHeader [nodeType=" + nodeType + "]";
	}

	public byte[] encode() {
		byte[] buf = new byte[LEN];
		int offset = 0;
		ByteUtils.putNumber(buf, offset, this.nodeType + "", 2);
		offset += 2;
		ByteUtils.putNumber(buf, offset, this.reserve1 + "", 2);
		offset += 2;
		System.arraycopy(reserveData, 0, buf, offset, NK_NODE_COMMENT_LEN);
		offset += NK_NODE_COMMENT_LEN;
		return buf;
	}

	public void decode(byte[] buf, int offset) {
		this.nodeType = Integer.valueOf(ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		this.reserve1 = Integer.valueOf(ByteUtils.toInt(buf, offset, 2));
		offset += 2;
		System.arraycopy(buf, offset, this.reserveData, 0, NK_NODE_COMMENT_LEN);
	}
	
	@Override
	public ZkNodeHeader clone() {
		try {
			return (ZkNodeHeader)super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
