/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer3;

/**
 * @author chenshaohua
 *
 */
public class MinMaintainBW implements java.io.Serializable{
	
	//��TerminalBizParam���ɵ�����
	private Long idx;

	//��Сά�ִ�����
	private Integer minMaintainBWSwitch;
	
	//������Сά�ִ���
	private Integer upMinMaintainBW;
	
	//������Сά�ִ���
	private Integer downMinMaintainBW;

	//��������
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
