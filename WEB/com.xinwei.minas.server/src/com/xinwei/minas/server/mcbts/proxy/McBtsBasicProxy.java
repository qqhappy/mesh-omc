/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy;

import com.xinwei.minas.mcbts.core.model.McBts;

/**
 * 
 * McBts����ҵ�����Proxy
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsBasicProxy {
	/**
	 * ��ѯMcBts������Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public McBts queryByMoId(Long moId) throws Exception;

	/**
	 * ����McBts������Ϣ
	 * 
	 * @param mcBts
	 * @throws Exception
	 */
	public void config(McBts mcBts) throws Exception;
}
