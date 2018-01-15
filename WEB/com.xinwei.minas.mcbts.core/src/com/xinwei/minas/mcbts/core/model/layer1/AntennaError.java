/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-24	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer1;

import java.io.Serializable;

import com.xinwei.omp.core.utils.ByteUtils;

/**
 * 
 * 类简要描述
 * 
 * 
 * @author tiance
 * 
 */

public class AntennaError implements Serializable {
	public final static int LENGTH = 2;

	private int val;

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}

	public void decode(byte[] bt, int startindex) throws Exception {
		// 解析天线错误
		val = ByteUtils.toInt(bt, startindex, 2);
	}
}
