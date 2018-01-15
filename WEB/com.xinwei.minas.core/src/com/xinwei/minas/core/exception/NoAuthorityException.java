/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-9-3	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.core.exception;

/**
 * 
 * 无权限异常
 * 
 * @author fanhaoyu
 * 
 */

public class NoAuthorityException extends Exception {

	private static final long serialVersionUID = 1888378642726105032L;

	public NoAuthorityException(String message) {
		super(message);
	}
}
