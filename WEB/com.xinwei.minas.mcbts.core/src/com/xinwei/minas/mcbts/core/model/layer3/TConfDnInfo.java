package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

/**
 * 配置号码信息类
 * 
 * @author yinbinqiang
 * 
 */
public class TConfDnInfo implements Serializable {

	// 主键
	private TConfDnInfoPK id;
	// 号码前缀
	private String dn_prefix;
	// 号码长度
	private Integer dn_len;

	public TConfDnInfo() {

	}

	public TConfDnInfo(TConfDnInfoPK id) {
		this.id = id;
	}

	// ****************************************
	//
	// 以下为getter/setter
	//
	// ****************************************

	public TConfDnInfoPK getId() {
		return id;
	}

	public void setId(TConfDnInfoPK id) {
		this.id = id;
	}

	public String getDn_prefix() {
		return dn_prefix;
	}

	public void setDn_prefix(String dn_prefix) {
		this.dn_prefix = dn_prefix;
	}

	public Integer getDn_len() {
		return dn_len;
	}

	public void setDn_len(Integer dn_len) {
		this.dn_len = dn_len;
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
		result = prime * result + ((dn_len == null) ? 0 : dn_len.hashCode());
		result = prime * result
				+ ((dn_prefix == null) ? 0 : dn_prefix.hashCode());
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
		TConfDnInfo other = (TConfDnInfo) obj;
		if (dn_len == null) {
			if (other.dn_len != null)
				return false;
		} else if (!dn_len.equals(other.dn_len))
			return false;
		if (dn_prefix == null) {
			if (other.dn_prefix != null)
				return false;
		} else if (!dn_prefix.equals(other.dn_prefix))
			return false;
		return true;
	}
}
