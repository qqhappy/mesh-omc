/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-20	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

import java.util.ResourceBundle;

/**
 * 
 * 网元业务字段属性元数据
 * 
 * @author chenjunhua
 * 
 */

public class XMetaItemProperty implements java.io.Serializable{
	
	// 范围
	public static final String RANGE = "range";
	
	// 主键
	public static final String KEY = "key";
	
	// 是否可见
	public static final String VISIBLE = "visible";
	
	// 是否可编辑
	public static final String EDITABLE = "editable";
	
	// 属性名
	private String name;
	
	// 属性值
	private String value;
	
	public void i18n(ResourceBundle resourceBundle) {
		value = XMetaUtils.replacePropertyResource(value, resourceBundle);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
