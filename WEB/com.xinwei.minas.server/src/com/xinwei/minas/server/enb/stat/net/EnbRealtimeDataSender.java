/*      						
 * Copyright 2015 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-1-7	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.stat.net;

import java.util.List;

import com.xinwei.minas.enb.core.model.stat.EnbRealtimeItemData;

/**
 * 
 * ʵʱ�������ݷ������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbRealtimeDataSender {

	/**
	 * ����ָ����Ԫ�����������б�
	 * 
	 * @param moId
	 * @param itemList
	 * @throws Exception
	 */
	public void sendData(long moId, List<EnbRealtimeItemData> itemDataList)
			throws Exception;

}
