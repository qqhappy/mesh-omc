/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-9	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.sxc.core.model;

/**
 * 
 * SXC����ģ�� 
 * 
 * @author chenjunhua
 * 
 */

public class SxcBasic implements java.io.Serializable{
	
	// MO��ţ�ȫ��Ψһ,ϵͳ�Զ����ɣ�
	private long moId;
	
	// SAG����
	private String sagName;

	// sag�����豸��
	private Long sagId;
	
	// sag��������
	private Long sagSignalPointCode;
	
	// SAG����IP(ý��IP?)
	private String sagVoiceIp;
	
	// SAG����IP
	private String sagSignalIp;
	
	// SAGĬ������
	private String sagDefaultGateway = "0.0.0.0";
	
	// SAG��������
	private String sagSubnetMask = "255.255.255.0";;
	

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}
	
	

	public String getSagName() {
		return sagName;
	}

	public void setSagName(String sagName) {
		this.sagName = sagName;
	}

	public Long getSagId() {
		return sagId;
	}

	public void setSagId(Long sagId) {
		this.sagId = sagId;
	}

	public Long getSagSignalPointCode() {
		return sagSignalPointCode;
	}

	public void setSagSignalPointCode(Long sagSignalPointCode) {
		this.sagSignalPointCode = sagSignalPointCode;
	}

	public String getSagVoiceIp() {
		return sagVoiceIp;
	}

	public void setSagVoiceIp(String sagVoiceIp) {
		this.sagVoiceIp = sagVoiceIp;
	}

	public String getSagSignalIp() {
		return sagSignalIp;
	}

	public void setSagSignalIp(String sagSignalIp) {
		this.sagSignalIp = sagSignalIp;
	}

	public String getSagDefaultGateway() {
		return sagDefaultGateway;
	}

	public void setSagDefaultGateway(String sagDefaultGateway) {
		this.sagDefaultGateway = sagDefaultGateway;
	}

	public String getSagSubnetMask() {
		return sagSubnetMask;
	}

	public void setSagSubnetMask(String sagSubnetMask) {
		this.sagSubnetMask = sagSubnetMask;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sagId == null) ? 0 : sagId.hashCode());
		result = prime * result
				+ ((sagSignalIp == null) ? 0 : sagSignalIp.hashCode());
		result = prime
				* result
				+ ((sagSignalPointCode == null) ? 0 : sagSignalPointCode
						.hashCode());
		result = prime * result
				+ ((sagVoiceIp == null) ? 0 : sagVoiceIp.hashCode());
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
		SxcBasic other = (SxcBasic) obj;
		if (sagId == null) {
			if (other.sagId != null)
				return false;
		} else if (!sagId.equals(other.sagId))
			return false;
		if (sagSignalIp == null) {
			if (other.sagSignalIp != null)
				return false;
		} else if (!sagSignalIp.equals(other.sagSignalIp))
			return false;
		if (sagSignalPointCode == null) {
			if (other.sagSignalPointCode != null)
				return false;
		} else if (!sagSignalPointCode.equals(other.sagSignalPointCode))
			return false;
		if (sagVoiceIp == null) {
			if (other.sagVoiceIp != null)
				return false;
		} else if (!sagVoiceIp.equals(other.sagVoiceIp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SxcBasic [sagName=" + sagName + ", sagId=" + sagId
				+ ", sagSignalPointCode=" + sagSignalPointCode
				+ ", sagVoiceIp=" + sagVoiceIp + ", sagSignalIp=" + sagSignalIp
				+ "]";
	}




	
	
	
	
	
}
