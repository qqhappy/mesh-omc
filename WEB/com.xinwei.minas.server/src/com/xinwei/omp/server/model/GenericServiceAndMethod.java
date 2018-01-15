/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-1-10	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.server.model;


/**
 * 
 * 通用类和方法模型
 * 
 * @author chenjunhua
 * 
 */

public class GenericServiceAndMethod {

	private Class serviceClazz;

	private String methodName;

	public GenericServiceAndMethod(Class serviceClazz, String methodName) {
		this.setServiceClazz(serviceClazz);
		this.setMethodName(methodName);
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class getServiceClazz() {
		return serviceClazz;
	}

	public void setServiceClazz(Class serviceClazz) {
		this.serviceClazz = serviceClazz;
	}




}
