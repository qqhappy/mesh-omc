/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-18	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.ut.core.model;

import java.io.Serializable;

/**
 * 
 * Àà¼òÒªÃèÊö
 * 
 * <p>
 * ÀàÏêÏ¸ÃèÊö
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class SNRCI implements Serializable {
	//1
	private int snr;
	
	//2
	private int ci;

	public int getSnr() {
		return snr;
	}

	public void setSnr(int snr) {
		this.snr = snr;
	}

	public int getCi() {
		return ci;
	}

	public void setCi(int ci) {
		this.ci = ci;
	}
}
