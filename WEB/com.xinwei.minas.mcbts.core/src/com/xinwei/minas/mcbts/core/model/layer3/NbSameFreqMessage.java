/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-27	| chenshaohua 	| 	create the file                       
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

public class NbSameFreqMessage implements Serializable {

	private int mNeighborBTSnumber; // 2

	private BtsNeighbourItem item[];


	public BtsNeighbourItem[] getItem() {
		return item;
	}

	public void setItem(BtsNeighbourItem[] item) {
		this.item = item;
	}

	public int getmNeighborBTSnumber() {
		return mNeighborBTSnumber;
	}

	public void setmNeighborBTSnumber(int mNeighborBTSnumber) {
		this.mNeighborBTSnumber = mNeighborBTSnumber;
	}

}
