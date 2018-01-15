/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-15	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.net.model;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * ByteArray TLV
 * 
 * @author chenjunhua
 * 
 */

public class ByteArrayTlvField extends TlvField {

	public ByteArrayTlvField(int tag, ByteBuffer buffer) {
		super(tag, buffer);
		byte[] valueBytes = new byte[length];
		buffer.get(valueBytes, 0, length);
		setValue(valueBytes);
	}

	public ByteArrayTlvField(int tag, int length, Object value) {
		super(tag, length, value);
	}
	
	@Override
	public int getLength() {
		try {
			return getValueBytes().length;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public byte[] getValueBytes() {
		return (byte[])getValue();
	}


}
