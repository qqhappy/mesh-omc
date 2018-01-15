/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-2-2	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model.corenet;

import java.io.Serializable;

/**
 * 
 * eNB���ȫ������
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbGlobalConfig implements Serializable {

	private Integer coreNetType;

	private String mcc;

	private String mnc;
	// �����㷨
	private String eea;

	// �����Ա����㷨
	private String eia;

	// �Ƿ�ͨ��Ƶ 0:�� 1:��
	private int videoSwitch = -1;

	// ��Ƶ����IP
	private String videoIp;

	// ��Ƶ����˿�
	private String videoPort;

	public int getVideoSwitch() {
		return videoSwitch;
	}

	public void setVideoSwitch(int videoSwitch) {
		this.videoSwitch = videoSwitch;
	}

	public String getVideoIp() {
		return videoIp;
	}

	public void setVideoIp(String videoIp) {
		this.videoIp = videoIp;
	}

	public String getVideoPort() {
		return videoPort;
	}

	public void setVideoPort(String videoPort) {
		this.videoPort = videoPort;
	}

	public Integer getCoreNetType() {
		return coreNetType;
	}

	public void setCoreNetType(Integer coreNetType) {
		this.coreNetType = coreNetType;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	public String getEea() {
		return eea;
	}

	public void setEea(String eea) {
		this.eea = eea;
	}

	public String getEia() {
		return eia;
	}

	public void setEia(String eia) {
		this.eia = eia;
	}

}
