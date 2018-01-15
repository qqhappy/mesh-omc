/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-17	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.model.meta;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 适配配置模型Response
 * 
 * @author chenshaohua
 * 
 */

public class OssAdapterResponse {

	private List<OssBizItem> itemList = new ArrayList<OssBizItem>();

	public void addItem(OssBizItem item) {
		itemList.add(item);
	}

	public List<OssBizItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<OssBizItem> itemList) {
		this.itemList = itemList;
	}
}
