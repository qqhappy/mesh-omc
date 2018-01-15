/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-10-11	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

/**
 * @author zhuxiaozhan
 *
 */
public class AppendNeighborMessage implements Serializable {

	private int configFlag; // 2
	
	private int mAppendNeighborBTSnumber;		// 2
	 
	private BtsNeighbourItem item[];
	
	private int BTSMASK1;	//4   表示1-32
    private int BTSMASK2;	//2   表示33-40 
    
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
	public BtsNeighbourItem[] getItem() {
		return item;
	}
	public void setItem(BtsNeighbourItem[] item) {
		this.item = item;
	}
	
	public int getBTSMASK1() {
		return BTSMASK1;
	}

	public int getBTSMASK2() {
		return BTSMASK2;
	}
}
