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
import java.util.ResourceBundle;

/**
 * 
 * 网元业务元数据
 * 
 * @author chenjunhua
 * 
 */

public class XMetaData {

	// 业务名称(默认对于表名)
	private String name;

	// 业务描述
	private String desc;

	// 业务属性集合
	private XMetaItem[] item = new XMetaItem[0];

	// 业务面板
	private XMetaPanel panel;
	
	private Map<String, XMetaItem> itemMapByName = new HashMap();
	
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

	
	
	
	public void setItem(XMetaItem[] item) {
		this.item = item;
		for (XMetaItem p : item) {
			itemMapByName.put(p.getName(), p);
		}
	}
	
	
	public XMetaItem[] getItems() {
		return item;
	}
	
	public XMetaItem getMetaItemBy(String name) {
		return itemMapByName.get(name);
	}

	public XMetaPanel getPanel() {
		return panel;
	}

	public void setPanel(XMetaPanel panel) {
		this.panel = panel;
	}

	/**
	 * 国际化资源
	 * @param resourceBundle
	 */
	public void i18n(ResourceBundle resourceBundle) {
		desc = this.replaceResource(desc, resourceBundle);
		if (item != null) {
			for (XMetaItem metaItem : item) {
				String metaItemDesc = metaItem.getDesc();
				metaItemDesc = this.replaceResource(metaItemDesc, resourceBundle);
				metaItem.setDesc(metaItemDesc);
			}
		}
	}
	
	private String replaceResource(String key, ResourceBundle resourceBundle) {
		if (key.startsWith("%")) {
			key = key.substring(1);
			key = getString(key, resourceBundle);
		}
		return key;
	}
	

	private String getString(String key, ResourceBundle resourceBundle) {
		try {
			return resourceBundle.getString(key);
		} catch (Exception e) {
			return "!" + key + "!";
		}
	}
}
