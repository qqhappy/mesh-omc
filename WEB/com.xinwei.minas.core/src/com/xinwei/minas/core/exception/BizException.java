/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-8	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.exception;

/**
 * 
 * ҵ���쳣
 * 
 * @author chenjunhua
 * 
 */

public class BizException extends Exception {

	private Object target;

	public BizException(Object target, String message) {
		super(message);
		this.target = target;
	}

	public BizException(String message) {
		super(message);
	}

	/**
	 * @param target
	 *            �����쳣��Ŀ��
	 */
	public void setTarget(Object target) {
		this.target = target;
	}

	/**
	 * @return target
	 */
	public Object getTarget() {
		return target;
	}

	private static final long serialVersionUID = 2389016053273496242L;

}
