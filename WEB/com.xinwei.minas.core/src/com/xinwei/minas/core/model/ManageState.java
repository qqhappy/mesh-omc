/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-11-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.model;

import java.io.Serializable;

/**
 * 
 * MO����״̬ģ��
 * 
 * @author chenjunhua
 * 
 */

public class ManageState implements Serializable{

	// δ֪
	public static final int UNKNOWN_STATE = 0;
	
	// ���߹���
	public static final int ONLINE_STATE = 1;
	
	// ���߹���
	public static final int OFFLINE_STATE = 2;
	
	/**
	 * ״̬��
	 */
	private int stateCode;
	
	public static ManageState ONLINE = new ManageState(ONLINE_STATE);
	
	public static ManageState OFFLINE = new ManageState(OFFLINE_STATE);
	
	public ManageState(int stateCode) {
		this.stateCode = stateCode;
	}
	
	public int getStateCode() {
		return stateCode;
	}
	
	public boolean isOnline() {
		return stateCode == ONLINE_STATE;
	}
	
	public boolean isOffline() {
		return stateCode == OFFLINE_STATE;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + stateCode;
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
		ManageState other = (ManageState) obj;
		if (stateCode != other.stateCode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ManageState [stateCode=" + stateCode + "]";
	}

	
}
