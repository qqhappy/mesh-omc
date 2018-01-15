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
 * IpAddress TLV
 * 
 * @author chenjunhua
 * 
 */

public class IpAddressTlvField extends TlvField {

	public IpAddressTlvField(int tag, ByteBuffer buffer) {
		super(tag, buffer);
		byte[] valueBytes = new byte[length];
		buffer.get(valueBytes, 0, length);
		String value = ByteUtils.toIp(valueBytes, 0, length);
		setValue(value);
	}

	public IpAddressTlvField(int tag, int length, Object value) {
		super(tag, length, value);
	}

	@Override
	public byte[] getValueBytes() {
		int length = getLength();
		byte[] buf = new byte[length];
		ByteUtils.putIp(buf, 0, getValue().toString());
		return buf;
	}


}
