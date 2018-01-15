/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer3;

/**
 * @author chenshaohua
 *
 */
public class MinMaintainBW implements java.io.Serializable{
	
	//由TerminalBizParam生成的主键
	private Long idx;

	//最小维持带宽开关
	private Integer minMaintainBWSwitch;
	
	//上行最小维持带宽
	private Integer upMinMaintainBW;
	
	//下行最小维持带宽
	private Integer downMinMaintainBW;

	//主键关联
	TerminalBizParam terminalBizParam;
	

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
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

	public TerminalBizParam getTerminalBizParam() {
		return terminalBizParam;
	}

	public void setTerminalBizParam(TerminalBizParam terminalBizParam) {
		this.terminalBizParam = terminalBizParam;
	}

	
}
