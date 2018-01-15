/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-19	| chenlong 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model;


/**
 * 
 * ��ʷ�ʲ�
 * 
 * @author chenlong
 * 
 */

public class EnbAssetHistory extends EnbAsset {
	
	private static final long serialVersionUID = 5887915877389216592L;
	
	// ȷ��ֹͣʹ�õ�ʱ�� yyyy-mm-dd hh:mm:ss
	private long confirmStopTime;
	
	// ȷ���û�
	private String confirmUser;
	
	public String getConfirmUser() {
		return confirmUser;
	}
	
	public void setConfirmUser(String confirmUser) {
		this.confirmUser = confirmUser;
	}
	
	public long getConfirmStopTime() {
		return confirmStopTime;
	}
	
	public void setConfirmStopTime(long confirmStopTime) {
		this.confirmStopTime = confirmStopTime;
	}
	
	
}
