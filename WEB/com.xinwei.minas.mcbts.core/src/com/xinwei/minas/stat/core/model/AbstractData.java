/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-22	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.stat.core.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 抽象数据类
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public abstract class AbstractData implements Serializable {

	private List<TargetAddress> udpAddressList;

	public List<TargetAddress> getUdpAddressList() {
		return udpAddressList;
	}

	public void setUdpAddressList(List<TargetAddress> udpAddressList) {
		this.udpAddressList = udpAddressList;
	}

	public abstract byte[] toBytes();

}
