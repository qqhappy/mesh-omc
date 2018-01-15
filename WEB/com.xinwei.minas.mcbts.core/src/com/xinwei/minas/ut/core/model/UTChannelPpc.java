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
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public class UTChannelPpc implements Serializable {

	//子信道数,1
	private int subChannelNum;
	
	//信道功率,1
	private int ppc;

	public int getSubChannelNum() {
		return subChannelNum;
	}

	public void setSubChannelNum(int subChannelNum) {
		this.subChannelNum = subChannelNum;
	}

	public int getPpc() {
		return ppc;
	}

	public void setPpc(int ppc) {
		this.ppc = ppc;
	}
	
}
