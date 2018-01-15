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

public class XMetaUICollection {

	// ��Ԫҵ��Ԫ����
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
	 * �ϲ�����
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
	 * ����ҵ�����Ʋ��Ҷ�Ӧ��Ԫ����
	 * 
	 * @param bizName
	 * @return
	 */
	public XMetaUI getMetaData(String bizName) {
		return metaDataMap.get(bizName);
	}


	/**
	 * ���ʻ���Դ
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
