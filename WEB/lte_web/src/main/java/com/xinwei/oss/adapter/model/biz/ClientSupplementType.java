/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-25	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.model.biz;

/**
 * 
 * 前端SupplementType实体
 * 
 * @author chenshaohua
 * 
 */

public class ClientSupplementType {

	private ClientSupplementItem[] supplementItems = new ClientSupplementItem[18];// 业务编号以及属性列表

	public ClientSupplementItem[] getSupplementItems() {
		return supplementItems;
	}

	public void setSupplementItems(ClientSupplementItem[] supplementItems) {
		this.supplementItems = supplementItems;
	}
}
