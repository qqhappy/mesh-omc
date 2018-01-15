/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 基站业务模型
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsOperation implements Serializable {
	// 基站类型
	private int type;
	// 中文解释
	private String remark;

	private Operation o;

	// 每种基站用到的业务集合
	private List<Operation> operList = new ArrayList<Operation>();

	private Map<String, Operation> operMap = new HashMap<String, Operation>();

	// 每种基站用到的表集合
	private Set<String> tableSet = new HashSet<String>();

	public void setOperationsParam(String type, String remark) {
		this.type = Integer.parseInt(type.trim());
		this.remark = remark.trim();
	}

	public void setOperationParam(String name, String desc) {
		// 解析<operation>标签
		o.setName(name.trim());
		o.setDesc(desc.trim());

		operList.add(o);

		operMap.put(name.trim(), o);

		o = null;
	}

	public void addTableName(String name) {
		// 解析<table>标签
		if (o == null)
			o = new Operation();

		tableSet.add(name.trim());

		o.tables.add(name.trim());
	}

	public Set<String> getTable() {
		return tableSet;
	}

	public Map<String, Operation> getOperationMap() {
		return operMap;
	}

	public List<Operation> getOperations() {
		return operList;
	}

	/**
	 * 具体的某个业务,如:空中链路
	 * 
	 * @author tiance
	 * 
	 */
	public static class Operation implements Serializable {
		// 业务在common-ui.xml中的name
		private String name;
		// 业务在common-ui.xml中的desc
		private String desc;

		public List<String> tables = new ArrayList<String>();

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	public int getType() {
		return type;
	}

	public String getRemark() {
		return remark;
	}

}
