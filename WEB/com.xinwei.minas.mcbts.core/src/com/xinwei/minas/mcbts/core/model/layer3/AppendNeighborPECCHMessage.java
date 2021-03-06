/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-23	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

/**
 * 
 * 
 * @author zhuxiaozhan
 * 
 */

public class AppendNeighborPECCHMessage implements Serializable{

	private int configFlag; // 2
	
	private int mAppendNeighborBTSnumber;		// 2
	
	private BtsNeighborPECCHITtem[] items;
	
	private int BTSMASK1;	//4   ��ʾ1-32
    private int BTSMASK2;	//2   ��ʾ33-40 
    
	public void setAllNeighbourBtsMask(long neighborIndex) {
		if (neighborIndex < 32){
        	BTSMASK1 = BTSMASK1 | 1 << neighborIndex;
    	} else if(neighborIndex < 40) {
    		BTSMASK2 = BTSMASK2 | 1 << (neighborIndex - 32);
    	}
	}

	public int getConfigFlag() {
		return configFlag;
	}

	public void setConfigFlag(int configFlag) {
		this.configFlag = configFlag;
	}

	public int getmAppendNeighborBTSnumber() {
		return mAppendNeighborBTSnumber;
	}

	public void setmAppendNeighborBTSnumber(int mAppendNeighborBTSnumber) {
		this.mAppendNeighborBTSnumber = mAppendNeighborBTSnumber;
	}

	public BtsNeighborPECCHITtem[] getItems() {
		return items;
	}

	public void setItems(BtsNeighborPECCHITtem[] items) {
		this.items = items;
	}

	public int getBTSMASK1() {
		return BTSMASK1;
	}

	public int getBTSMASK2() {
		return BTSMASK2;
	}
	
	
}
