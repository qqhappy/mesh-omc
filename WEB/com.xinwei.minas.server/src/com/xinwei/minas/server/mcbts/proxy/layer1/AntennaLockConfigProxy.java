/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2013-8-7	| fanhaoyu		 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.layer1;

import com.xinwei.minas.mcbts.core.model.layer1.McBtsAntennaLock;

/**
 * ���߱�������ҵ�����Proxy
 * 
 * @author fanhaoyu
 * 
 */

public interface AntennaLockConfigProxy {

	/**
	 * ������Ԫҵ������
	 * 
	 * @param lockConfig
	 * @throws Exception
	 */
	public void config(McBtsAntennaLock lockConfig) throws Exception;

}