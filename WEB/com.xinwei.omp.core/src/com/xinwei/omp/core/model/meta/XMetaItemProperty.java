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
 * ��Ԫҵ���ֶ�����Ԫ����
 * 
 * @author chenjunhua
 * 
 */

public class XMetaItemProperty implements java.io.Serializable{
	
	// ��Χ
	public static final String RANGE = "range";
	
	// ����
	public static final String KEY = "key";
	
	// �Ƿ�ɼ�
	public static final String VISIBLE = "visible";
	
	// �Ƿ�ɱ༭
	public static final String EDITABLE = "editable";
	
	// ������
	private String name;
	
	// ����ֵ
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
