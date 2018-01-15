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
 * ��վҵ��ģ��
 * 
 * 
 * @author tiance
 * 
 */

public class McBtsOperation implements Serializable {
	// ��վ����
	private int type;
	// ���Ľ���
	private String remark;

	private Operation o;

	// ÿ�ֻ�վ�õ���ҵ�񼯺�
	private List<Operation> operList = new ArrayList<Operation>();

	private Map<String, Operation> operMap = new HashMap<String, Operation>();

	// ÿ�ֻ�վ�õ��ı���
	private Set<String> tableSet = new HashSet<String>();

	public void setOperationsParam(String type, String remark) {
		this.type = Integer.parseInt(type.trim());
		this.remark = remark.trim();
	}

	public void setOperationParam(String name, String desc) {
		// ����<operation>��ǩ
		o.setName(name.trim());
		o.setDesc(desc.trim());

		operList.add(o);

		operMap.put(name.trim(), o);

		o = null;
	}

	public void addTableName(String name) {
		// ����<table>��ǩ
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
	 * �����ĳ��ҵ��,��:������·
	 * 
	 * @author tiance
	 * 
	 */
	public static class Operation implements Serializable {
		// ҵ����common-ui.xml�е�name
		private String name;
		// ҵ����common-ui.xml�е�desc
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
