/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

/**
 * @author chenshaohua
 * 
 */
public class McBtsVlan implements Serializable {

	// 记录序号
	private Long idx;

	// 被管对象Id
	private Long moId;

	// vlan 组
	private Integer vlanGroup;

	// vlan id
	private Integer vlanID;

	public McBtsVlan() {

	}

	public Object getPropertyValueByName(String name) {
		if (name.equals("vlanGroup")) {
			return this.vlanGroup;
		}
		if (name.equals("vlanID")) {
			return this.vlanID;
		}
		return null;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Long getMoId() {
		return moId;
	}

	public void setMoId(Long moId) {
		this.moId = moId;
	}

	public Integer getVlanGroup() {
		return vlanGroup;
	}

	public void setVlanGroup(Integer vlanGroup) {
		this.vlanGroup = vlanGroup;
	}

	public Integer getVlanID() {
		return vlanID;
	}

	public void setVlanID(Integer vlanID) {
		this.vlanID = vlanID;
	}
}
