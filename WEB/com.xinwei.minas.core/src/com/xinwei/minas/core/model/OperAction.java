/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.core.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * ����
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class OperAction implements Serializable {
	// ҵ��ID
	private String operName;
	// // ҵ������
	// private String operDesc;
	// ��������
	private List<String> actions;

	public void setOperName(String operName) {
		this.operName = operName;
	}

	public String getOperName() {
		return operName;
	}

	public void setActions(List<String> actions) {
		this.actions = actions;
	}

	public List<String> getActions() {
		return actions;
	}

	@Override
	public String toString() {
		return "OperAction [operName=" + operName + ", actions=" + actions
				+ "]";
	}

	// public void setOperDesc(String operDesc) {
	// this.operDesc = operDesc;
	// }
	//
	// public String getOperDesc() {
	// return operDesc;
	// }

}
