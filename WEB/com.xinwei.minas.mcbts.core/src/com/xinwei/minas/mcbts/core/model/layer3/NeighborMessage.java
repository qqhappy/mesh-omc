/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-25	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

/**
 * 
 * ¿‡ºÚ“™√Ë ˆ
 * 
 * <p>
 * ¿‡œÍœ∏√Ë ˆ
 * </p>
 * 
 * @author chenshaohua
 * 
 */

public class NeighborMessage implements Serializable {

	private int mNeighborBTSnumber; // 2

	private BtsNeighbourItem item[];

	private int LongDistModFlag; // 2

	private int BTSMASK; //3

	public void setAllNeighbourBtsMask(long neighborIndex) {
		if (neighborIndex < 24) {
			BTSMASK = BTSMASK | 1 << neighborIndex;
		}
	}


	public BtsNeighbourItem[] getItem() {
		return item;
	}

	public void setItem(BtsNeighbourItem[] item) {
		this.item = item;
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

	public void setBTSMASK(int bTSMASK) {
		BTSMASK = bTSMASK;
	}


	public int getmNeighborBTSnumber() {
		return mNeighborBTSnumber;
	}


	public void setmNeighborBTSnumber(int mNeighborBTSnumber) {
		this.mNeighborBTSnumber = mNeighborBTSnumber;
	}




}
