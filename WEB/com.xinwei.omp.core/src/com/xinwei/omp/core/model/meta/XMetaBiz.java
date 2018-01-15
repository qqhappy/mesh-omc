/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * 
 * ҵ��Ԫ����
 * 
 * @author chenjunhua
 * 
 */

public class XMetaBiz implements java.io.Serializable{

	// ҵ������
	private String name;

	// ҵ������
	private String desc;
	
	// ҵ�����Լ���
	private XMetaItem[] item = new XMetaItem[0];

	// Key:��������, Value:����ֵ
	private Map<String, XMetaItem> itemMapByName = new HashMap();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
	
	/**
	 * ��ȡ�����б�
	 * @return
	 */
	public Set<String> getPrimaryKeys() {
		Set<String> keys = new HashSet();
		for (XMetaItem metaItem : item) {
			if (metaItem.isPrimaryKey()) {
				keys.add(metaItem.getName());
			}
		}
		return keys;
	}
	
	/**
	 * ���ʻ���Դ
	 * @param resourceBundle
	 */
	public void i18n(ResourceBundle resourceBundle) {
//		desc = this.replaceResource(desc, resourceBundle);
		desc = XMetaUtils.replaceResource(desc, resourceBundle);
		if (item != null) {
			for (XMetaItem metaItem : item) {
				metaItem.i18n(resourceBundle);
			}
		}

	}
}
