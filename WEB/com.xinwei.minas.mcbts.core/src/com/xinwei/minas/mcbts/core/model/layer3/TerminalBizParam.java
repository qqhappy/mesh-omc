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
	
	// 编号
	private Long idx;
	
	// 名称
	private String name;

	// 用户组别
	private Integer userClass;

	// 保留字段
	private Integer reserve;

	// 上行最大带宽
	private Integer upMaxBandWidth;

	// 上行最小带宽
	private Integer upMinBandWidth;

	// 下行最大带宽
	private Integer downMaxBandWidth;

	// 下行最小带宽
	private Integer downMinBandWidth;

	// 最小维持带宽开关
	private Integer minMaintainBWSwitch;

	// 上行最小维持带宽
	private Integer upMinMaintainBW;

	// 下行最小维持带宽
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
