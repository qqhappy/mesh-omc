/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-10-22	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.model;

import java.io.Serializable;

/**
 * 
 * ���ܶ���(MO)ģ��
 * 
 * @author chenjunhua
 * 
 */

public class Mo implements Serializable, Cloneable {

	// MO��ţ�ȫ��Ψһ,ϵͳ�Զ����ɣ�
	private long moId;

	// MO���ͣ��μ�MO���������ֵ䶨�壩
	private int typeId;

	// MO����
	private String name;

	// MO����
	private String description;

	// ����״̬(Ĭ��Ϊ���߹���״̬)
	private int manageStateCode = ManageState.OFFLINE_STATE;

	public Mo() {

	}

	public Mo(long moId, int typeId, String name, String desc,
			int manageStateCode) {
		this.setMoId(moId);
		this.setTypeId(typeId);
		this.setName(name);
		this.setDescription(desc);
		this.setManageStateCode(manageStateCode);
	}
	
	/**
	 * �ж��Ƿ�����ʵ����Ԫ
	 * @return
	 */
	public boolean isRealMo() {
		return moId > 0;
	}
	
	/**
	 * ��ȡ��ʾ������
	 * @return
	 */
	public String getDisplayName() {
		return name;
	}

	/**
	 * �Ƿ����߹���
	 * 
	 * @return
	 */
	public boolean isOnlineManage() {
		return manageStateCode == ManageState.ONLINE_STATE;
	}

	/**
	 * �Ƿ����߹���
	 * 
	 * @return
	 */
	public boolean isOfflineManage() {
		return manageStateCode == ManageState.OFFLINE_STATE;
	}

	public long getMoId() {
		return moId;
	}

	public void setMoId(long moId) {
		this.moId = moId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getManageStateCode() {
		return manageStateCode;
	}

	public void setManageStateCode(int manageStateCode) {
		this.manageStateCode = manageStateCode;
	}

	@Override
	public String toString() {
		return "Mo [moId=" + moId + ", typeId=" + typeId + ", name=" + name
				+ ", description=" + description + ", manageStateCode="
				+ manageStateCode + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + manageStateCode;
		result = prime * result + (int) (moId ^ (moId >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + typeId;
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
		Mo other = (Mo) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (manageStateCode != other.manageStateCode)
			return false;
		if (moId != other.moId)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (typeId != other.typeId)
			return false;
		return true;
	}
}
