/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-3	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.net.model;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * ×Ö·û´®TLV
 * 
 * @author chenjunhua
 * 
 */

public class StringTlvField extends TlvField {

	public static final String CHARRSET_NAME = "US-ASCII";

	public static final String CHARRSET_NAME_UTF8 = "UTF-8";

	public static final Character FILL_CHAR = '\0';

	public StringTlvField(int tag, ByteBuffer buffer) {
		super(tag, buffer);
		byte[] valueBytes = new byte[length];
		buffer.get(valueBytes, 0, length);
		String value = ByteUtils.toString(valueBytes, 0, length,
				CHARRSET_NAME_UTF8);
		try {
			value = value.trim();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		setValue(value);
	}

	public StringTlvField(int tag, int length, Object value) {
		super(tag, length, value);
	}

	@Override
	public int getLength() {
		try {
			return getValue().toString().getBytes(CHARRSET_NAME_UTF8).length;
		} catch (UnsupportedEncodingException e) {
			return 0;
		}
	}

	@Override
	public byte[] getValueBytes() {
		int length = getLength();
		byte[] buf = new byte[length];
		ByteUtils.putString(buf, 0, getValue().toString(), length, FILL_CHAR,
				CHARRSET_NAME_UTF8);
		return buf;
	}

}
