/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-14	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.net.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.exception.BizException;
import com.xinwei.omp.core.model.meta.XMetaTag;
import com.xinwei.omp.core.model.meta.XMetaTagCollection;

/**
 * 
 * TLV助手
 * 
 * @author fanhaoyu
 * 
 */

public class TlvFieldUtil {

	private XMetaTagCollection collection;

	/**
	 * Tag配置集合
	 * 
	 * @param collection
	 */
	public TlvFieldUtil(XMetaTagCollection collection) {
		this.collection = collection;
	}

	/**
	 * 将一串tlv格式的字节流解析成Tag-Value映射
	 * 
	 * @param buffer
	 * @return
	 * @throws BizException
	 */
	public Map<Integer, List<Object>> parse(ByteBuffer buffer)
			throws BizException {
		Map<Integer, List<Object>> fieldMap = new LinkedHashMap<Integer, List<Object>>();
		while (buffer.position() < buffer.capacity()) {
			// 把字节流每个字段解析成TlvField
			int tagId = buffer.getShort();
			XMetaTag tag = collection.getTag(tagId);
			if (tag == null) {
				throw new BizException("parse message failed. Invalid tag:"
						+ tagId);
			}
			TlvField field = null;
			String type = tag.getType();
			if (type.equals(XMetaTag.TYPE_STRING)) {
				field = new StringTlvField(tagId, buffer);
			} else if (type.equals(XMetaTag.TYPE_IP_ADDRESS)) {
				field = new IpAddressTlvField(tagId, buffer);
			} else if (type.equals(XMetaTag.TYPE_BYTE_ARRAY)) {
				field = new ByteArrayTlvField(tagId, buffer);
			} else if (type.equals(XMetaTag.SIGNED_NUMBER)) {
				field = new SignedNumberTlvField(tagId, buffer);
			} else if (type.equals(XMetaTag.UNSIGNED_NUMBER)) {
				field = new UnsignedNumberTlvField(tagId, buffer);
			} else if (type.equals(XMetaTag.TYPE_COMPOSITE)) {
				int length = buffer.getShort();
				// 解析复合tag
				byte[] valueBytes = new byte[length];
				buffer.get(valueBytes, 0, length);
				ByteBuffer innerBuffer = ByteBuffer.wrap(valueBytes);
				Map<Integer, List<Object>> tvMap = parse(innerBuffer);
				CompositeValue compositeValue = new CompositeValue();
				for (Integer innerTag : tvMap.keySet()) {
					compositeValue.addTagListValue(innerTag,
							tvMap.get(innerTag));
				}
				field = new CompositeTlvField(tagId, length, compositeValue);
			}
			List<Object> valueList = fieldMap.get(tagId);
			if (valueList == null) {
				valueList = new LinkedList<Object>();
				fieldMap.put(tagId, valueList);
			}
			valueList.add(field.getValue());
		}
		return fieldMap;
	}

	/**
	 * 将Tag-Value映射编码成字节流
	 * 
	 * @param fieldMap
	 * @return
	 * @throws BizException
	 */
	public byte[] encode(Map<Integer, List<Object>> fieldMap)
			throws BizException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byte[] fieldBytes = null;
		for (Integer tagId : fieldMap.keySet()) {
			List<Object> valueList = fieldMap.get(tagId);
			for (Object value : valueList) {
				TlvField tlvField = createTlvField(tagId, value);
				fieldBytes = tlvField.toBytes();
				try {
					byteStream.write(fieldBytes);
				} catch (IOException e) {
					throw new BizException("encode TlvField failed. tagId:"
							+ tagId);
				}
			}
		}
		return byteStream.toByteArray();
	}

	private TlvField createTlvField(int tagId, Object value) {
		XMetaTag tag = collection.getTag(tagId);
		String type = tag.getType();
		int length = tag.getLength();
		TlvField tlvField = null;
		if (type.equals(XMetaTag.TYPE_STRING)) {
			tlvField = new StringTlvField(tagId, length, value.toString());
		} else if (type.equals(XMetaTag.TYPE_IP_ADDRESS)) {
			tlvField = new IpAddressTlvField(tagId, length, value);
		} else if (type.equals(XMetaTag.TYPE_BYTE_ARRAY)) {
			tlvField = new ByteArrayTlvField(tagId, length, value);
		} else if (type.equals(XMetaTag.SIGNED_NUMBER)) {
			tlvField = new SignedNumberTlvField(tagId, length, value);
		} else if (type.equals(XMetaTag.UNSIGNED_NUMBER)) {
			tlvField = new UnsignedNumberTlvField(tagId, length, value);
		} else if (type.equals(XMetaTag.TYPE_COMPOSITE)) {
			CompositeValue compositeValue = (CompositeValue) value;
			// 创建复合TLV
			CompositeTlvField compositeTlvField = new CompositeTlvField(tagId,
					length, compositeValue);
			Map<Integer, List<Object>> tvMap = compositeValue.getTagValueMap();
			for (Integer innerTagId : tvMap.keySet()) {
				List<Object> valueList = tvMap.get(innerTagId);
				for (Object innerValue : valueList) {
					TlvField innerTlvField = createTlvField(innerTagId, innerValue);
					compositeTlvField.addInnerTlvField(innerTagId,
							innerTlvField);
				}
			}
			tlvField = compositeTlvField;
		}
		return tlvField;
	}

}
