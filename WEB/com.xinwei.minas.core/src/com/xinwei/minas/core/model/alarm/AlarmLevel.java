/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.model.alarm;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * 
 * MO�澯����ģ��
 * 
 * @author chenjunhua
 * 
 */

public class AlarmLevel implements Serializable{


	// �澯���
	public static final int STATE_CLEARED = 0;

	// ���ظ澯
	public static final int STATE_CRITICAL = 1;
	
	// ��Ҫ�澯
	public static final int STATE_MAJOR = 2;
	
	// ��Ҫ�澯
	public static final int STATE_MINOR = 3;
	
	// һ��澯
	public static final int STATE_INFO = 4;


	/**
	 * ״̬��
	 */
	private int stateCode;

	public static AlarmLevel CLEARED = new AlarmLevel(STATE_CLEARED);

	public static AlarmLevel INFO = new AlarmLevel(STATE_INFO);

	public static AlarmLevel MINOR = new AlarmLevel(STATE_MINOR);
	
	public static AlarmLevel MAJOR = new AlarmLevel(STATE_MAJOR);
	
	public static AlarmLevel CRITICAL = new AlarmLevel(STATE_CRITICAL);
	

	public AlarmLevel(int stateCode) {
		this.stateCode = stateCode;
	}
	


	public int getStateCode() {
		return stateCode;
	}

	@Override
	public int hashCode() {
		return stateCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AlarmLevel)) {
			return false;
		}
		AlarmLevel manageStatus = (AlarmLevel) obj;
		return this.getStateCode() == manageStatus.getStateCode();
	}
}

