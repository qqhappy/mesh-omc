/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-8	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.model.stat;

import com.xinwei.minas.xstat.core.model.StatItem;

/**
 * 
 * eNB实时性能统计项数据模型
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class EnbRealtimeItemData extends StatItem {

	private int systemFrameNo;

	public void setSystemFrameNo(int systemFrameNo) {
		this.systemFrameNo = systemFrameNo;
	}

	public int getSystemFrameNo() {
		return systemFrameNo;
	}

}
