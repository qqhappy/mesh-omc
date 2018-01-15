package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;
/**
 * rcpe uid 条目信息主键
 * 
 * @author yinbinqiang
 *
 */
public class TConfRCPEItemPK implements Serializable {
	// 基站 ID
	private java.lang.Long parentId;
	// uid 条目行索引
	private java.lang.Integer rowIndex;
	
	public TConfRCPEItemPK() {
		
	}
	
	public TConfRCPEItemPK(Long parentId, Integer rowIndex) {
		super();
		this.parentId = parentId;
		this.rowIndex = rowIndex;
	}

	public java.lang.Long getParentId() {
		return parentId;
	}

	public void setParentId(java.lang.Long parentId) {
		this.parentId = parentId;
	}

	public java.lang.Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(java.lang.Integer rowIndex) {
		this.rowIndex = rowIndex;
	}
}
	
