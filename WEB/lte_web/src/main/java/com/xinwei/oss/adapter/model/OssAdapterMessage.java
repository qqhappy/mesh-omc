package com.xinwei.oss.adapter.model;

/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-08	| chenjunhua 	| 	create the file                       
 */

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * OssAdapter消息模型
 * 
 * @author chenjunhua
 * 
 */
public class OssAdapterMessage {

	private int operId;

	private int action;

	private Map<String, Object> data;

	public OssAdapterMessage() {

	}

	public OssAdapterMessage(int operId, int action, Map<String, Object> data) {
		this.setOperId(operId);
		this.setAction(action);
		this.setData(data);
	}
	
	/**
	 * 增加参数-值对
	 * @param parameter
	 * @param value
	 */
	public void addParameterValue(String parameter, Object value) {
		data.put(parameter, value);
	}
	
	/**
	 * 删除参数
	 * @param parameter
	 */
	public void removeParameter(String parameter) {
		data.remove(parameter);	
	}
	
	/**
	 * 获取数值
	 * @param parameter
	 * @return
	 */
	public Object getValue(String parameter) {
		return data.get(parameter);
	}
	
	/**
	 * 获取字符串类型参数的数值
	 * @param parameter
	 * @return
	 */
	public String getStringValue(String parameter) {
		String value = (String)data.get(parameter);
		if (value == null) {
			value = "";
		}
		return value;
	}
	
	/**
	 * 获取字符串类型参数的数值
	 * @param parameter
	 * @return
	 */
	public int getIntValue(String parameter) {
		return Integer.parseInt(getStringValue(parameter));
	}
	
	
	/**
	 * 获取List<Map>类型的数值
	 * @param parameter
	 * @return
	 */
	public List<Map> getMapListValue(String parameter) {
		List<Map> mapList = (List<Map>)data.get(parameter);
		if (mapList == null) {
			return new LinkedList();
		}
		return mapList;
	}
	
	/**
	 * 获取参数列表
	 * @return
	 */
	public Set<String> getParameters() {
		return data.keySet();
	}

	public int getOperId() {
		return operId;
	}

	public void setOperId(int operId) {
		this.operId = operId;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
