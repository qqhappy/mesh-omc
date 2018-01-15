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
 * ÎÞ·ûºÅÊýTLV
 * 
 * @author chenjunhua
 * 
 */

public class UnsignedNumberTlvField extends TlvField {

	public UnsignedNumberTlvField(int tag, ByteBuffer buffer) {
		super(tag, buffer);
		byte[] valueBytes = new byte[length];
		buffer.get(valueBytes, 0, length);
		Long value = ByteUtils.toUnsignedNumber(valueBytes, 0, length);
		setValue(value);
	}

	public UnsignedNumberTlvField(int tag, int length, Object value) {
		super(tag, length, value);
	}


	@Override
	public byte[] getValueBytes() {
		int length = getLength();
		byte[] buf = new byte[length];
		ByteUtils.putNumber(buf, 0, getValue().toString(), length);
		return buf;
	}


}
