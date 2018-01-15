package com.xinwei.lte.web.lte.model;
/**
 * CPU门限模型
 * @author sunzhangbin
 *
 */
public class CpuThreshold {
	
	/**
	 * S1Overload Start CPU门限
	 */
	private int cpuUp;
	
	/**
	 * S1Overload Stop CPU门限
	 */
	private int cupDown;

	public int getCpuUp() {
		return cpuUp;
	}

	public void setCpuUp(int cpuUp) {
		this.cpuUp = cpuUp;
	}

	public int getCupDown() {
		return cupDown;
	}

	public void setCupDown(int cupDown) {
		this.cupDown = cupDown;
	}

	

}
