/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-16	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.oss.adapter.model.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 适配配置模型Item
 * 
 * @author chenshaohua
 * 
 */

public class OssBizItem {

	private String name;

	private short tag;

	private List<OssInner> innerList = new ArrayList<OssInner>();

	private String parser;

	public OssInner getOssInnerByName(String name) {
		for (OssInner inner : innerList) {
			if (inner.getName().equals(name)) {
				return inner;
			}
		}
		return null;
	}

	public void addInner(OssInner inner) {
		innerList.add(inner);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public short getTag() {
		return tag;
	}

	public void setTag(short tag) {
		this.tag = tag;
	}

	public String getParser() {
		return parser;
	}

	public void setParser(String parser) {
		this.parser = parser;
	}

	public List<OssInner> getInnerList() {
		return innerList;
	}

	public void setInnerList(List<OssInner> innerList) {
		this.innerList = innerList;
	}

}
