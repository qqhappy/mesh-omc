package com.xinwei.lte.web.enb.model;

import java.util.LinkedList;
import java.util.List;

/**
 * 关联的外键模型  ，用于传递关联的外键信息
 * 
 * @author zhangqiang
 *
 */
public class ReferFkModel {
	
	/**
	 *表名
	 */
	private String tableName;
	
	/**
	 * 被查询的外键
	 */
	private String fkName;
	
	/**
	 * 表为TOPO表时，区分主从号, 0 ：主 1：从
	 */
	private int isMain;
	
	/**
	 * 外键集合
	 */
	private List<String> fkList = new LinkedList<String>();
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<String> getFkList() {
		return fkList;
	}

	public void setFkList(List<String> fkList) {
		this.fkList = fkList;
	}

	public String getFkName() {
		return fkName;
	}

	public void setFkName(String fkName) {
		this.fkName = fkName;
	}

	public int getIsMain() {
		return isMain;
	}

	public void setIsMain(int isMain) {
		this.isMain = isMain;
	}
	
	
}
