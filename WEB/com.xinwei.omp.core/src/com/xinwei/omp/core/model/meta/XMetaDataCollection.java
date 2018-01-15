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

public class XMetaDataCollection {

	// 网元业务元数据
	private XMetaData[] biz = new XMetaData[0];

	// KEY:bizName
	private Map<String, XMetaData> metaDataMap = new HashMap();

	public XMetaDataCollection() {

	}

	public XMetaData[] getBiz() {
		return biz;
	}

	public void setBiz(XMetaData[] biz) {
		this.biz = biz;
		metaDataMap.clear();
		for (XMetaData data : biz) {
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
	public XMetaDataCollection merge(XMetaDataCollection collection) {
		XMetaDataCollection newCollection = new XMetaDataCollection();
		XMetaData[] meta1 = this.getBiz();
		XMetaData[] meta2 = collection.getBiz();
		List<XMetaData> meta = new LinkedList();
		meta.addAll(Arrays.asList(meta1));
		meta.addAll(Arrays.asList(meta2));
		newCollection.setBiz(meta.toArray(new XMetaData[0]));
		return newCollection;
	}

	/**
	 * 根据业务名称查找对应的元数据
	 * 
	 * @param bizName
	 * @return
	 */
	public XMetaData getMetaData(String bizName) {
		return metaDataMap.get(bizName);
	}


	/**
	 * 国际化资源
	 * @param resourceBundle
	 */
	public void i18n(ResourceBundle resourceBundle) {
		if (biz != null) {
			for (XMetaData metaData : biz) {
				metaData.i18n(resourceBundle);
			}
		}
	}
}
