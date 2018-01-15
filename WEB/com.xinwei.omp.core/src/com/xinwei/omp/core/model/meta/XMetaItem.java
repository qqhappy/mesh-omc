/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-13	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * 
 * 网元业务字段元数据
 * 
 * @author chenjunhua
 * 
 */

public class XMetaItem implements java.io.Serializable{

	// 字段名称
	private String name;

	// 字段类型
	private String type;

	// 字段描述
	private String desc;
	
	// 字段属性（数组）
	private XMetaItemProperty[] property = new XMetaItemProperty[0];
	
	// 字段属性（哈希table）
	private Properties properties = new Properties();
	

	/**
	 * 判断是否是主键(默认不是主键)
	 * @return
	 */
	public boolean isPrimaryKey() {
		String key = properties.getProperty(XMetaItemProperty.KEY);
		if (key != null && key.equals("true")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否可见(默认可见)
	 * @return
	 */
	public boolean isVisible() {
		String visible = properties.getProperty(XMetaItemProperty.VISIBLE);
		if (visible != null && visible.equals("false")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否可编辑（默认可编辑）
	 * @return
	 */
	public boolean isEditable() {
		String editable = properties.getProperty(XMetaItemProperty.EDITABLE);
		if (editable != null && editable.equals("false")) {
			return false;
		}
		return true;
	}
	 
	/**
	 * 根据属性名获取属性值
	 * @param name
	 * @return
	 */
	public String getPropertyBy(String name) {
		return properties.getProperty(name);
	}
	
	
	/**
	 * 获取字段属性
	 * @return
	 */
	public Properties getProperties() {
		return properties;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setProperty(XMetaItemProperty[] property) {
		this.property = property;
		properties.clear();
		for (XMetaItemProperty p : property) {
			properties.setProperty(p.getName(), p.getValue());			
		}
	}

	/**
	 * 国际化资源
	 * @param resourceBundle
	 */
	public void i18n(ResourceBundle resourceBundle) {
		desc = XMetaUtils.replaceResource(desc, resourceBundle);
		if(property != null){
			for(XMetaItemProperty metaItemProperty : property){
				metaItemProperty.i18n(resourceBundle);
			}
		}
		//国际化另外一个properties
		if(properties != null){
			XMetaItemProperty localMetaItemProperty = new XMetaItemProperty();
			String propertyValue = properties.getProperty("input");
			if(propertyValue != null){
				localMetaItemProperty.setName("input");
				localMetaItemProperty.setValue(propertyValue);
				localMetaItemProperty.i18n(resourceBundle);
				properties.setProperty("input", localMetaItemProperty.getValue());
			}
		}
	}
	
	
}
