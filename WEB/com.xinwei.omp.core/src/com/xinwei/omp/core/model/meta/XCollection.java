/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * Collection模型
 * 
 * @author chenjunhua
 * 
 */

public class XCollection implements java.io.Serializable{

	private String name;
	
	private XList[] list = new XList[0];
	
	private Map<String, XList> bizMap = new LinkedHashMap();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public XList[] getList() {
		return list;
	}

	public void setList(XList[] list) {
		this.list = list;
		bizMap.clear();
		for (XList _list : list) {
			bizMap.put(_list.getName(), _list);
		}
	}
	
	/**
	 * 获取指定UI的元数据定义
	 * @param bizName
	 * @return
	 */
	public XList getBizMetaBy(String bizName) {
		return bizMap.get(bizName);
	}

	public Map<String, XList> getBizMap() {
		return bizMap;
	}
	
	
}
