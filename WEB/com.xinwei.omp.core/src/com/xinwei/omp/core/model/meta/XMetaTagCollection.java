/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-14	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.omp.core.model.meta;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 标签集合类
 * 
 * @author fanhaoyu
 * 
 */

public class XMetaTagCollection {

	private XMetaTag[] tag;

	private Map<Integer, XMetaTag> tagMap = new HashMap<Integer, XMetaTag>();

	public void setTag(XMetaTag[] tags) {
		this.tag = tags;
		for (XMetaTag t : tag) {
			String id = t.getId();
			if (id.toLowerCase().startsWith("0x")) {
				id = id.substring(2, id.length());
			}
			tagMap.put(Integer.parseInt(id, 16), t);
		}
	}

	public XMetaTag[] getTags() {
		return tag;
	}

	public void setTagMap(Map<Integer, XMetaTag> tagMap) {
		this.tagMap = tagMap;
	}

	public XMetaTag getTag(int tag) {
		return tagMap.get(tag);
	}

}
