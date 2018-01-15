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

public class NeighborChannelItem implements Serializable {

	public final static int CH_NUM = 10;
	
	private long mBtsId;

	private ChannelItem fachs[]; //1

	private ChannelItem rpchs[]; //1

	public ChannelItem[] getFachs() {
		return fachs;
	}

	public void setFachs(ChannelItem[] fachs) {
		this.fachs = fachs;
	}

	public ChannelItem[] getRpchs() {
		return rpchs;
	}

	public void setRpchs(ChannelItem[] rpchs) {
		this.rpchs = rpchs;
	}

	public void setmBtsId(long mBtsId) {
		this.mBtsId = mBtsId;
	}

	public long getmBtsId() {
		return mBtsId;
	}
}
