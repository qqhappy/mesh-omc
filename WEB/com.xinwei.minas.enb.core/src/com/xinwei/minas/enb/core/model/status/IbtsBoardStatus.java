package com.xinwei.minas.enb.core.model.status;

import com.xinwei.minas.enb.core.model.EnbDynamicInfo;

/**
 * ibts����״̬
 * @author zhangqiang
 *
 */
public class IbtsBoardStatus implements EnbDynamicInfo {
	
	//����״̬   0:����   1:������
	private int status;
	
	//MBDӲ���汾
	private String mbdHardWareVersion;
	
	//MBD�������к�
	private String mbdProductionSn;
	
	//PAUӲ���汾
	private String pauHardWareVersion;
	
	//PAU�������к�
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
