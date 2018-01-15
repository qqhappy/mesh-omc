/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-7	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.zk.core.listener;

import com.xinwei.minas.zk.core.xnode.vo.ZkBtsSagLinkVO;
import com.xinwei.minas.zk.core.xnode.vo.ZkBtsVO;

/**
 * 
 * 基站与SAG间链路变化侦听器
 * 
 * @author chenjunhua
 * 
 */

public interface BtsSagLinkChangedListener {

	/**
	 * 基站与SAG间链路变化
	 * 
	 * @param zkBts
	 *            基站模型
	 * @param newBtsSagLink
	 *            基站与SAG间新的链路
	 */
	public void linkChanged(ZkBtsVO zkBts, ZkBtsSagLinkVO newBtsSagLink);
	
	
	/**
	 * 基站与SAG间链路删除
	 * 
	 * @param zkBts
	 *            基站模型
	 * @param oldBtsSagLink
	 *            基站与SAG间旧的链路
	 */
	public void linkDeleted(ZkBtsVO zkBts, ZkBtsSagLinkVO oldBtsSagLink);
}
