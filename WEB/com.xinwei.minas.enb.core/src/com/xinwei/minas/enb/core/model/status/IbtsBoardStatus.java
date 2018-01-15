package com.xinwei.minas.enb.core.model.status;

import com.xinwei.minas.enb.core.model.EnbDynamicInfo;

/**
 * ibts单板状态
 * @author zhangqiang
 *
 */
public class IbtsBoardStatus implements EnbDynamicInfo {
	
	//单板状态   0:正常   1:不正常
	private int status;
	
	//MBD硬件版本
	private String mbdHardWareVersion;
	
	//MBD生产序列号
	private String mbdProductionSn;
	
	//PAU硬件版本
	private String pauHardWareVersion;
	
	//PAU生产序列号
	private String pauProductionSn;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMbdHardWareVersion() {
		return mbdHardWareVersion;
	}

	public void setMbdHardWareVersion(String mbdHardWareVersion) {
		this.mbdHardWareVersion = mbdHardWareVersion;
	}

	public String getMbdProductionSn() {
		return mbdProductionSn;
	}

	public void setMbdProductionSn(String mbdProductionSn) {
		this.mbdProductionSn = mbdProductionSn;
	}

	public String getPauHardWareVersion() {
		return pauHardWareVersion;
	}

	public void setPauHardWareVersion(String pauHardWareVersion) {
		this.pauHardWareVersion = pauHardWareVersion;
	}

	public String getPauProductionSn() {
		return pauProductionSn;
	}

	public void setPauProductionSn(String pauProductionSn) {
		this.pauProductionSn = pauProductionSn;
	}
	
	
}
