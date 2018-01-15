package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

public class TConfDnInfoPK implements Serializable {

	// 配置号码所属的语音故障弱化配置ID
	private Long parentid;

	// 配置号码信息的行索引
	private Integer rowindex;

	public TConfDnInfoPK() {
		super();
	}

	public TConfDnInfoPK(Long parentid, Integer rowindex) {
		super();
		this.parentid = parentid;
		this.rowindex = rowindex;
	}

	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	// ****************************************
	//
	// 以下为getter/setter
	//
	// ****************************************

	public Integer getRowindex() {
		return rowindex;
	}

	public void setRowindex(Integer rowindex) {
		this.rowindex = rowindex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((parentid == null) ? 0 : parentid.hashCode());
		result = prime * result
				+ ((rowindex == null) ? 0 : rowindex.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TConfDnInfoPK other = (TConfDnInfoPK) obj;
		if (parentid == null) {
			if (other.parentid != null)
				return false;
		} else if (!parentid.equals(other.parentid))
			return false;
		if (rowindex == null) {
			if (other.rowindex != null)
				return false;
		} else if (!rowindex.equals(other.rowindex))
			return false;
		return true;
	}

}
