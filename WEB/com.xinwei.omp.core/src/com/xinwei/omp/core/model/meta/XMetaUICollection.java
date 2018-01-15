/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-13	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * 
 * 网元业务元数据集合
 * 
 * @author chenjunhua
 * 
 */

public class XMetaUICollection {

	// 网元业务元数据
	private XMetaUI[] ui = new XMetaUI[0];

	// KEY:bizName
	private Map<String, XMetaUI> metaDataMap = new HashMap();

	public XMetaUICollection() {

	}

	public XMetaUI[] getUI() {
		return ui;
	}

	public void setUI(XMetaUI[] ui) {
		this.ui = ui;
		metaDataMap.clear();
		for (XMetaUI data : ui) {
			String bizName = data.getName();
			metaDataMap.put(bizName, data);
		}
	}

	/**
	 * 合并集合
	 * 
	 * @param collection
	 * @return
	 */
	public XMetaUICollection merge(XMetaUICollection collection) {
		XMetaUICollection newCollection = new XMetaUICollection();
		XMetaUI[] meta1 = this.getUI();
		XMetaUI[] meta2 = collection.getUI();
		List<XMetaUI> meta = new LinkedList();
		meta.addAll(Arrays.asList(meta1));
		meta.addAll(Arrays.asList(meta2));
		newCollection.setUI(meta.toArray(new XMetaUI[0]));
		return newCollection;
	}

	/**
	 * 根据业务名称查找对应的元数据
	 * 
	 * @param bizName
	 * @return
	 */
	public XMetaUI getMetaData(String bizName) {
		return metaDataMap.get(bizName);
	}


	/**
	 * 国际化资源
	 * @param resourceBundle
	 */
	public void i18n(ResourceBundle resourceBundle) {
		if (ui != null) {
			for (XMetaUI metaData : ui) {
				metaData.i18n(resourceBundle);
			}
		}
	}
}
