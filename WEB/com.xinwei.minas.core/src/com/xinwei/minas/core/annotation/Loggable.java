/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-5	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * ��־ע��, ������Ҫϵͳ��¼��־��Facade��������Ҫ�������ע��
 * 
 * @author chenjunhua
 * 
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {

}
