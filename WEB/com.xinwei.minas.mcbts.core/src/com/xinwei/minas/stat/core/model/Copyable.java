/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-23	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.stat.core.model;

/**
 * 
 * 可复制的实体接口
 * 
 * @author fanhaoyu
 * 
 */

public interface Copyable<T> {

	public T copy();

}
