package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;
/**
 * rcpe uid ��Ŀ��Ϣ����
 * 
 * @author yinbinqiang
 *
 */
public class TConfRCPEItemPK implements Serializable {
	// ��վ ID
	private java.lang.Long parentId;
	// uid ��Ŀ������
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
	
