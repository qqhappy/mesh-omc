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
 * ��Ԫҵ��Ԫ���ݼ���
 * 
 * @author chenjunhua
 * 
 */

public class XMetaDataCollection {

	// ��Ԫҵ��Ԫ����
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
	 * �ϲ�����
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
	 * ����ҵ�����Ʋ��Ҷ�Ӧ��Ԫ����
	 * 
	 * @param bizName
	 * @return
	 */
	public XMetaData getMetaData(String bizName) {
		return metaDataMap.get(bizName);
	}


	/**
	 * ���ʻ���Դ
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
