/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer3;

/**
 * @author chenshaohua
 * 
 */
@SuppressWarnings("serial")
public class TerminalBizParam implements java.io.Serializable {
	
	// ���
	private Long idx;
	
	// ����
	private String name;

	// �û����
	private Integer userClass;

	// �����ֶ�
	private Integer reserve;

	// ����������
	private Integer upMaxBandWidth;

	// ������С����
	private Integer upMinBandWidth;

	// ����������
	private Integer downMaxBandWidth;

	// ������С����
	private Integer downMinBandWidth;

	// ��Сά�ִ�����
	private Integer minMaintainBWSwitch;

	// ������Сά�ִ���
	private Integer upMinMaintainBW;

	// ������Сά�ִ���
	private Integer downMinMaintainBW;

	// public Object getPropertyValueByName(String propertyName){
	// if(propertyName.equals("idx")){
	// return this.idx;
	// }
	// if(propertyName.equals("name")){
	// return this.name;
	// }
	// if(propertyName.equals("userClass")){
	// return this.userClass;
	// }
	// if(propertyName.equals("upMaxBandWidth")){
	// return this.upMaxBandWidth;
	// }
	// if(propertyName.equals("upMinBandWidth")){
	// return this.upMinBandWidth;
	// }
	// if(propertyName.equals("downMaxBandWidth")){
	// return this.downMaxBandWidth;
	// }
	// if(propertyName.equals("downMinBandWidth")){
	// return this.downMinBandWidth;
	// }
	//
	// return null;
	// }

	public Integer getDownMaxBandWidth() {
		return downMaxBandWidth;
	}

	public void setDownMaxBandWidth(Integer downMaxBandWidth) {
		this.downMaxBandWidth = downMaxBandWidth;
	}

	public Integer getDownMinBandWidth() {
		return downMinBandWidth;
	}

	public void setDownMinBandWidth(Integer downMinBandWidth) {
		this.downMinBandWidth = downMinBandWidth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUpMaxBandWidth() {
		return upMaxBandWidth;
	}

	public void setUpMaxBandWidth(Integer upMaxBandWidth) {
		this.upMaxBandWidth = upMaxBandWidth;
	}

	public Integer getUpMinBandWidth() {
		return upMinBandWidth;
	}

	public void setUpMinBandWidth(Integer upMinBandWidth) {
		this.upMinBandWidth = upMinBandWidth;
	}

	public Integer getUserClass() {
		return userClass;
	}

	public void setUserClass(Integer userClass) {
		this.userClass = userClass;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Integer getReserve() {
		return reserve;
	}

	public void setReserve(Integer reserve) {
		this.reserve = reserve;
	}

	public Integer getDownMinMaintainBW() {
		return downMinMaintainBW;
	}

	public void setDownMinMaintainBW(Integer downMinMaintainBW) {
		this.downMinMaintainBW = downMinMaintainBW;
	}

	public Integer getMinMaintainBWSwitch() {
		return minMaintainBWSwitch;
	}

	public void setMinMaintainBWSwitch(Integer minMaintainBWSwitch) {
		this.minMaintainBWSwitch = minMaintainBWSwitch;
	}

	public Integer getUpMinMaintainBW() {
		return upMinMaintainBW;
	}

	public void setUpMinMaintainBW(Integer upMinMaintainBW) {
		this.upMinMaintainBW = upMinMaintainBW;
	}

}
