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
 * ���Ҫ����
 * 
 * <p>
 * ����ϸ����
 * </p>
 * 
 * @author chenshaohua
 * 
 */

public interface NeighbourProxy {

	/**
	 * �����ڽӱ���Ϣ����վ
	 * 
	 * @param moId
	 * @param data
	 * @throws Exception
	 */
	public void configNeighbour(Long moId, NeighborMessage data) throws Exception;
	
	/**
	 * ����PECCH��վ��������Ϣ����վ
	 * @param moId
	 * @param data
	 * @throws Exception
	 */
	public void configPECCHNeighbor(Long moId, NeighborPECCHMessage data) throws Exception;
	
	/**
	 * ���ø����ڽӱ���Ϣ����վ
	 * @param moId
	 * @param data
	 * @throws Exception
	 */
	public void configAppendNeighbor(Long moId, AppendNeighborMessage data) throws Exception;
	
	/**
	 * ����PECCH������վ��������Ϣ����վ
	 * @param moId
	 * @param data
	 * @throws Exception
	 */
	public void configPECCHAppendNeighbor(Long moId, AppendNeighborPECCHMessage data) throws Exception;
	/**
	 * �����ڽ�ͬƵ��Ϣ����վ
	 * 
	 * @param moId
	 * @param data
	 * @throws Exception
	 */
	public void configNbSameFreq(Long moId, NbSameFreqMessage data) throws Exception;
	
	/**
	 * ������վС���ŵ�����վ
	 * @param moId
	 * @param data
	 * @throws Exception
	 */
	public void configSmallPKGReq(Long moId, NeighborSmallPack data) throws Exception;
}
