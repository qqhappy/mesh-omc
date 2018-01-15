/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-11	| tiance 	| 	create the file                       
 */

package com.xinwei.omp.core.model.biz;

import java.util.List;

/**
 * 
 * 所有模型的接口
 * 
 * @author tiance
 * 
 */

public interface Listable {
	/**
	 * 罗列模型的所有成员变量的name和value到FirldProperty里,形成list
	 * 
	 * @return
	 */
	public List<FieldProperty> listAll();
	
	/**
	 * 获得模型的名称
	 */
	public String getBizName();
}
