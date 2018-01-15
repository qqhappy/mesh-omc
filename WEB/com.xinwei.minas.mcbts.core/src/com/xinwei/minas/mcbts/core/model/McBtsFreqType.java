/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| liuzhongyan 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model;

/**
 * 频点类型模型
 * 
 * @author liuzhongyan
 * 
 */

public class McBtsFreqType implements Comparable<McBtsFreqType> {
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

	@Override
	public int compareTo(McBtsFreqType other) {
		double thisFreq = Double.parseDouble(this.freqName.substring(0, 3));
		double otherFreq = Double.parseDouble(other.freqName.substring(0, 3));

		if (thisFreq < 100)
			thisFreq = thisFreq * 1000;

		if (otherFreq < 100)
			otherFreq = otherFreq * 1000;

		if (thisFreq < otherFreq)
			return -1;

		if (thisFreq > otherFreq)
			return 1;

		if (this.freqName.contains("(R20)"))
			return 1;

		if (other.freqName.contains("(R20)"))
			return -1;

		return 0;
	}
}
