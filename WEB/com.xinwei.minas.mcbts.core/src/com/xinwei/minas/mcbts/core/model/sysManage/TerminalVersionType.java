package com.xinwei.minas.mcbts.core.model.sysManage;

import java.io.Serializable;

/**
 * 
 * 描述终端的版本类型的基本信息,对应数据库的terminal_version_type
 * 
 * @author fangping
 * 
 */

@SuppressWarnings("serial")
public class TerminalVersionType implements Serializable {
	// 类型ID
	private int idx;
	// 终端类型
	private String type;
	// 终端类型描述
	private String desc;

	public TerminalVersionType() {
	}

	public TerminalVersionType(int idx, String type, String desc) {
		this.idx = idx;
		this.type = type;
		this.desc = desc;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idx;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TerminalVersionType other = (TerminalVersionType) obj;
		if (idx != other.idx)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
