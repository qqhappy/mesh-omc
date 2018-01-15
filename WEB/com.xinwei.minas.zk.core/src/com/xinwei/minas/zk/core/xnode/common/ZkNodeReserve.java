/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.xnode.common;

import java.io.Serializable;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * 保留结构体
 * 
 * @author fanhaoyu
 * 
 */

public class ZkNodeReserve implements Serializable {
	
	public static final int LEN = 8;
	
	private byte ucReserve1;
	
	private byte ucReserve2;
	
	private short usReserve1;
	
	private int ulReserve1;
	
	public byte getUcReserve1() {
		return ucReserve1;
	}
	public void setUcReserve1(byte ucReserve1) {
		this.ucReserve1 = ucReserve1;
	}
	public byte getUcReserve2() {
		return ucReserve2;
	}
	public void setUcReserve2(byte ucReserve2) {
		this.ucReserve2 = ucReserve2;
	}
	public short getUsReserve1() {
		return usReserve1;
	}
	public void setUsReserve1(short usReserve1) {
		this.usReserve1 = usReserve1;
	}
	public int getUlReserve1() {
		return ulReserve1;
	}
	public void setUlReserve1(int ulReserve1) {
		this.ulReserve1 = ulReserve1;
	}

	public void decode(byte[] buf, int offset)
	{
		this.ucReserve1 = Integer.valueOf(ByteUtils.toInt(buf, offset, 1)).byteValue();
		offset += 1;
		this.ucReserve2 = Integer.valueOf(ByteUtils.toInt(buf, offset, 1)).byteValue();
		offset += 1;
		this.usReserve1 = Integer.valueOf(ByteUtils.toInt(buf, offset, 2)).shortValue();
		offset += 2;
		this.ulReserve1 = Integer.valueOf(ByteUtils.toInt(buf, offset, 4));
		offset += 4;
	}
	
	public byte[] encode()
	{
		byte[] buf = new byte[LEN];
		int offset = 0;
		ByteUtils.putNumber(buf, offset, this.ucReserve1 + "", 1);
		offset += 1;
		ByteUtils.putNumber(buf, offset, this.ucReserve2 + "", 1);
		offset += 1;
		ByteUtils.putNumber(buf, offset, this.usReserve1 + "", 2);
		offset += 2;
		ByteUtils.putNumber(buf, offset, this.ulReserve1 + "", 4);
		offset += 4;
		return buf;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ucReserve1;
		result = prime * result + ucReserve2;
		result = prime * result + ulReserve1;
		result = prime * result + usReserve1;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ZkNodeReserve))
			return false;
		ZkNodeReserve other = (ZkNodeReserve) obj;
		if (ucReserve1 != other.ucReserve1)
			return false;
		if (ucReserve2 != other.ucReserve2)
			return false;
		if (ulReserve1 != other.ulReserve1)
			return false;
		if (usReserve1 != other.usReserve1)
			return false;
		return true;
	}
}
