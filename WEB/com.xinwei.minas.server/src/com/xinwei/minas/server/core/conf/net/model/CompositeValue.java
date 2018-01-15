/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-12-23	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.net.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.xinwei.omp.core.utils.StringUtils;

/**
 * 
 * 复合Tag值
 * 
 * @author fanhaoyu
 * 
 */

public class CompositeValue {

	private Map<Integer, List<Object>> tvMap = new HashMap<Integer, List<Object>>();

	/**
	 * 增加Tag Value
	 * 
	 * @param field
	 */
	public void addTagValue(int tag, Object value) {
		List<Object> valueList = tvMap.get(tag);
		if (valueList == null) {
			valueList = new LinkedList<Object>();
			tvMap.put(tag, valueList);
		}
		valueList.add(value);
	}

	/**
	 * 添加tag值，值的形式为列表
	 * 
	 * @param tag
	 * @param valueList
	 */
	public void addTagListValue(int tag, List<Object> valueList) {
		tvMap.put(tag, valueList);
	}

	/**
	 * 获取整型数值
	 * 
	 * @param tag
	 * @return
	 */
	public int getIntValue(int tag) {
		Object object = getSingleValue(tag);
		return Integer.valueOf(object.toString());
	}

	/**
	 * 获取长整型数值
	 * 
	 * @param tag
	 * @return
	 */
	public long getLongValue(int tag) {
		Object object = getSingleValue(tag);
		return Long.valueOf(object.toString());
	}

	/**
	 * 获取长整型时间数值(yyyyMMddhhmmss)
	 * 
	 * @param tag
	 * @return
	 */
	public long getDateTimeValue(int tag) {
		Object object = getSingleValue(tag);
		Long oTime = Long.valueOf(object.toString());
		String timeStr = Long.toHexString(oTime);
		for (int i = timeStr.length(); i < 16; i++) {
			timeStr = "0" + timeStr;
		}
		long year = Long.valueOf(timeStr.substring(0, 4), 16);
		long month = Long.valueOf(timeStr.substring(4, 6), 16);
		long day = Long.valueOf(timeStr.substring(6, 8), 16);
		long hour = Long.valueOf(timeStr.substring(8, 10), 16);
		long minute = Long.valueOf(timeStr.substring(10, 12), 16);
		long second = Long.valueOf(timeStr.substring(12, 14), 16);
		String str = StringUtils.appendPrefix(String.valueOf(year), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(month), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(day), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(hour), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(minute), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(second), "0", 2);
		return Long.valueOf(str);
	}
	
	/**
	 * 获取长整型时间数值(yyyyMMdd)
	 * @param tag
	 * @return
	 */
	public long getDateValue(int tag) {
		Object object = getSingleValue(tag);
		Long oTime = Long.valueOf(object.toString());
		String timeStr = Long.toHexString(oTime);
		for (int i = timeStr.length(); i < 8; i++) {
			timeStr = "0" + timeStr;
		}
		long year = Long.valueOf(timeStr.substring(0, 4), 16);
		long month = Long.valueOf(timeStr.substring(4, 6), 16);
		long day = Long.valueOf(timeStr.substring(6, 8), 16);
		String str = StringUtils.appendPrefix(String.valueOf(year), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(month), "0", 2)
				+ StringUtils.appendPrefix(String.valueOf(day), "0", 2);
		return Long.valueOf(str);
	}
	

	/**
	 * 获取字符串
	 * 
	 * @param tag
	 * @return
	 */
	public String getStringValue(int tag) {
		Object object = getSingleValue(tag);
		if (object == null)
			return null;
		return object.toString();
	}

	/**
	 * 获取字节数组
	 * 
	 * @param tag
	 * @return
	 */
	public byte[] getByteValue(int tag) {
		Object object = getSingleValue(tag);
		return (byte[]) object;
	}

	/**
	 * 获取列表
	 * 
	 * @param tag
	 * @return
	 */
	public List<Object> getListValue(int tag) {
		return tvMap.get(tag);
	}

	private Object getSingleValue(int tag) {
		List<Object> valueList = tvMap.get(tag);
		if (valueList == null)
			return null;
		return valueList.get(0);
	}

	public Map<Integer, List<Object>> getTagValueMap() {
		return tvMap;
	}

}
