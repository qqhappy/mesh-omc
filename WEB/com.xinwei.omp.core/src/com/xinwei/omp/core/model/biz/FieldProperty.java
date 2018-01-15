/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-11	| tiance 	| 	create the file                       
 */

package com.xinwei.omp.core.model.biz;

/**
 * 
 * ���ģ�͵���
 * 
 * 
 * @author tiance
 * 
 */

public class FieldProperty {
	// ���б��еļ���
	private int level;
	// ��������
	private String name;
	// ��������Ӧ��ֵ
	private String value;

	public FieldProperty() {
	}

	public FieldProperty(int level, String name, String value) {
		this.level = level;
		this.name = name;
		this.value = value;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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
