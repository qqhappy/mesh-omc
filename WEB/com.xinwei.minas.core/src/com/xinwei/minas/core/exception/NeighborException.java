/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-25	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.core.exception;

/**
 * 
 * �ڽӱ�ҵ���쳣
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class NeighborException extends Exception {
	
	private static final long serialVersionUID = -8636764660395430904L;
	
	private String infoMessage;
	
	private String errorMessage;

	public void setInfoMessage(String infoMessage) {
		this.infoMessage = infoMessage;
	}

	public String getInfoMessage() {
		return infoMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public NeighborException(String infoMessage, String errorMessage) {
		super(errorMessage);
		this.infoMessage = infoMessage;
		this.errorMessage = errorMessage;
	}
	
	public String getPromptMessage() {
		return infoMessage + errorMessage;
	}

}
