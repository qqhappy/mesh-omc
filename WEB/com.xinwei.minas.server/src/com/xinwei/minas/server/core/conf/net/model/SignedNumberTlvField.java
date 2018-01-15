/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-15	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.net.model;

import java.nio.ByteBuffer;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * ÓÐ·ûºÅÊýTLV
 * 
 * @author fanhaoyu
 * 
 */

public class SignedNumberTlvField extends TlvField {

	public SignedNumberTlvField(int tag, ByteBuffer buffer) {
		super(tag, buffer);
		byte[] valueBytes = new byte[length];
		buffer.get(valueBytes, 0, length);
		Long value = ByteUtils.toSignedNumber(valueBytes, 0, length);
		setValue(value);
	}

	public SignedNumberTlvField(int tag, int length, Object value) {
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
