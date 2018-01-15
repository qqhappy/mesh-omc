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
 * ��Ԫҵ���ֶ�Ԫ����
 * 
 * @author chenjunhua
 * 
 */

public class XMetaItem implements java.io.Serializable{

	// �ֶ�����
	private String name;

	// �ֶ�����
	private String type;

	// �ֶ�����
	private String desc;
	
	// �ֶ����ԣ����飩
	private XMetaItemProperty[] property = new XMetaItemProperty[0];
	
	// �ֶ����ԣ���ϣtable��
	private Properties properties = new Properties();
	

	/**
	 * �ж��Ƿ�������(Ĭ�ϲ�������)
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
	 * �ж��Ƿ�ɼ�(Ĭ�Ͽɼ�)
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
	 * �ж��Ƿ�ɱ༭��Ĭ�Ͽɱ༭��
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
	 * ������������ȡ����ֵ
	 * @param name
	 * @return
	 */
	public String getPropertyBy(String name) {
		return properties.getProperty(name);
	}
	
	
	/**
	 * ��ȡ�ֶ�����
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
	 * ���ʻ���Դ
	 * @param resourceBundle
	 */
	public void i18n(ResourceBundle resourceBundle) {
		desc = XMetaUtils.replaceResource(desc, resourceBundle);
		if(property != null){
			for(XMetaItemProperty metaItemProperty : property){
				metaItemProperty.i18n(resourceBundle);
			}
		}
		//���ʻ�����һ��properties
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
