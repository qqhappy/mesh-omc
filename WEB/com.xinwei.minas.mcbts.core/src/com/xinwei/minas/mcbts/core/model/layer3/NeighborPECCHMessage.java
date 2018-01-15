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
 * PECCH邻站表配置请求消息实体
 * 
 * @author zhuxiaozhan
 * 
 */

public class NeighborPECCHMessage implements Serializable{
	private int mNeighborBTSnumber; // 2
	
	private BtsNeighborPECCHITtem[] items;
	
	private int LongDistModFlag; // 2

	private int BTSMASK; //3
	
	public void setAllNeighbourBtsMask(long neighborIndex) {
		if (neighborIndex < 24) {
			BTSMASK = BTSMASK | 1 << neighborIndex;
		}
	}

	public int getmNeighborBTSnumber() {
		return mNeighborBTSnumber;
	}

	public void setmNeighborBTSnumber(int mNeighborBTSnumber) {
		this.mNeighborBTSnumber = mNeighborBTSnumber;
	}

	public BtsNeighborPECCHITtem[] getItems() {
		return items;
	}

	public void setItems(BtsNeighborPECCHITtem[] items) {
		this.items = items;
	}

	public int getLongDistModFlag() {
		return LongDistModFlag;
	}

	public void setLongDistModFlag(int longDistModFlag) {
		LongDistModFlag = longDistModFlag;
	}

	public int getBTSMASK() {
		return BTSMASK;
	}
	
	
}
