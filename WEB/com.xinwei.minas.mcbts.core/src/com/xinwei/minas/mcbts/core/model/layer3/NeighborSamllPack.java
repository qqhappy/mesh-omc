/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| chenshaohua 	| 	create the file                       
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

public class NeighborSamllPack implements Serializable {

	private int mNeighborBTSnumber; // 2

	private NeighborChannelItem items[];
	

	public int getmNeighborBTSnumber() {
		return mNeighborBTSnumber;
	}

	public void setmNeighborBTSnumber(int mNeighborBTSnumber) {
		this.mNeighborBTSnumber = mNeighborBTSnumber;
	}

	public NeighborChannelItem[] getItems() {
		return items;
	}

	public void setItems(NeighborChannelItem[] items) {
		this.items = items;
	}

}
