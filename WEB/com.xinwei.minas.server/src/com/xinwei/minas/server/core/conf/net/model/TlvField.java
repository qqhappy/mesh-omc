/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.net.model;

import java.nio.ByteBuffer;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * TLV模型
 * 
 * @author chenjunhua
 * 
 */

public abstract class TlvField implements Field {

	protected int tag;

	protected int length;

	protected Object value;

	public TlvField(int tag, ByteBuffer buffer) {
		this.tag = tag;
		this.length = buffer.getShort();
	}

	public TlvField(int tag, int length, Object value) {
		this.tag = tag;
		this.length = length;
		this.value = value;
	}

	public int getTag() {
		return tag;
	}

	public int getLength() {
		return length;
	}

	public Object getValue() {
		return value;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * 将TLV转换为字节数组
	 */
	public byte[] toBytes() {
		byte[] buf = new byte[4 + getLength()];
		ByteUtils.putNumber(buf, 0, String.valueOf(getTag()), 2);
		ByteUtils.putNumber(buf, 2, String.valueOf(getLength()), 2);
		byte[] valueBytes = getValueBytes();
		System.arraycopy(valueBytes, 0, buf, 4, valueBytes.length);
		return buf;
	}


	/**
	 * 获取数值的字节数组，由子类实现
	 * 
	 * @return
	 */
	public abstract byte[] getValueBytes();


}
