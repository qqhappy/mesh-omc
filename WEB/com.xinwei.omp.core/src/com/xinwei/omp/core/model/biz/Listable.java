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
 * ����ģ�͵Ľӿ�
 * 
 * @author tiance
 * 
 */

public interface Listable {
	/**
	 * ����ģ�͵����г�Ա������name��value��FirldProperty��,�γ�list
	 * 
	 * @return
	 */
	public List<FieldProperty> listAll();
	
	/**
	 * ���ģ�͵�����
	 */
	public String getBizName();
}
