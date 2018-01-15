/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-14	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

/**
 * 
 * 标签实体元数据定义
 * 
 * @author fanhaoyu
 * 
 */

public class XMetaTag {

	public static final String TYPE_STRING = "String";

	public static final String TYPE_IP_ADDRESS = "IpAddress";

	public static final String TYPE_BYTE_ARRAY = "ByteArray";

	public static final String SIGNED_NUMBER = "SignedNumber";

	public static final String UNSIGNED_NUMBER = "UnsignedNumber";

	public static final String TYPE_COMPOSITE = "Composite";

	private String name;

	private String id;

	private int length;

	private String type;

	private String desc;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
