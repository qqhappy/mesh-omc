/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-23	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.net.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * 复合TLV
 * 
 * @author fanhaoyu
 * 
 */

public class CompositeTlvField extends TlvField {

	private Map<Integer, List<TlvField>> tlvMap = new HashMap<Integer, List<TlvField>>();

	public CompositeTlvField(int tagId, int length, CompositeValue value) {
		super(tagId, length, value);
	}

	/**
	 * 添加复合TLV内部的子TLV
	 * 
	 * @param tag
	 * @param value
	 */
	public void addInnerTlvField(int tag, TlvField value) {
		List<TlvField> fieldList = tlvMap.get(tag);
		if (fieldList == null) {
			fieldList = new LinkedList<TlvField>();
			tlvMap.put(tag, fieldList);
		}
		fieldList.add(value);
	}

	@Override
	public int getLength() {
		int length = 0;
		// 遍历tag
		for (Integer tag : tlvMap.keySet()) {
			List<TlvField> fieldList = tlvMap.get(tag);
			// 遍历tag的value列表
			for (TlvField tlvField : fieldList) {
				length += tlvField.getLength() + 4;
			}
		}
		return length;
	}

	@Override
	public byte[] getValueBytes() {
		int length = getLength();
		byte[] buf = new byte[length];
		int offset = 0;
		// 遍历tag
		for (Integer tag : tlvMap.keySet()) {
			List<TlvField> fieldList = tlvMap.get(tag);
			// 遍历tag的value列表
			for (TlvField tlvField : fieldList) {
				byte[] innerBytes = tlvField.toBytes();
				System.arraycopy(innerBytes, 0, buf, offset, innerBytes.length);
				offset += innerBytes.length;
			}
		}
		return buf;
	}

}
