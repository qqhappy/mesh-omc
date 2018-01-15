/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-27	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.layer3;

import com.xinwei.minas.mcbts.core.model.layer3.AppendNeighborMessage;
import com.xinwei.minas.mcbts.core.model.layer3.AppendNeighborPECCHMessage;
import com.xinwei.minas.mcbts.core.model.layer3.NbSameFreqMessage;
import com.xinwei.minas.mcbts.core.model.layer3.NeighborMessage;
import com.xinwei.minas.mcbts.core.model.layer3.NeighborPECCHMessage;
import com.xinwei.minas.mcbts.core.model.layer3.NeighborSmallPack;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author chenshaohua
 * 
 */

public interface NeighbourProxy {

	/**
	 * 配置邻接表消息到基站
	 * 
	 * @param moId
	 * @param data
	 * @throws Exception
	 */
	public void configNeighbour(Long moId, NeighborMessage data) throws Exception;
	
	/**
	 * 配置PECCH邻站表请求消息到基站
	 * @param moId
	 * @param data
	 * @throws Exception
	 */
	public void configPECCHNeighbor(Long moId, NeighborPECCHMessage data) throws Exception;
	
	/**
	 * 配置附加邻接表消息到基站
	 * @param moId
	 * @param data
	 * @throws Exception
	 */
	public void configAppendNeighbor(Long moId, AppendNeighborMessage data) throws Exception;
	
	/**
	 * 配置PECCH附加邻站表请求消息到基站
	 * @param moId
	 * @param data
	 * @throws Exception
	 */
	public void configPECCHAppendNeighbor(Long moId, AppendNeighborPECCHMessage data) throws Exception;
	/**
	 * 配置邻接同频消息到基站
	 * 
	 * @param moId
	 * @param data
	 * @throws Exception
	 */
	public void configNbSameFreq(Long moId, NbSameFreqMessage data) throws Exception;
	
	/**
	 * 配置邻站小包信道到基站
	 * @param moId
	 * @param data
	 * @throws Exception
	 */
	public void configSmallPKGReq(Long moId, NeighborSmallPack data) throws Exception;
}
