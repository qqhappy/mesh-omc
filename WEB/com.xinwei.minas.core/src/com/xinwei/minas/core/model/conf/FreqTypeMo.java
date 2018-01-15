/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.core.model.conf;

/**
 * 频点类型模型
 * 
 * @author liuzhongyan
 * 
 */

public class FreqTypeMo {
	private int freqType;
	private String freqName;

	public int getFreqType() {
		return freqType;
	}

	public void setFreqType(int freqType) {
		this.freqType = freqType;
	}

	public String getFreqName() {
		return freqName;
	}

	public void setFreqName(String freqName) {
		this.freqName = freqName;
	}

}
