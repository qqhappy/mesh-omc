/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-8	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.rruManage;

import com.xinwei.minas.mcbts.core.model.common.McBtsSN;

/**
 * 
 * RRUӲ����Ϣ��ѯproxy
 * 
 * @author chenshaohua
 * 
 */

// TODO:����
public interface RRUHWInfoProxy {

	/**
	 * ��ѯRRUӲ����Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 * @throws UnsupportedOperationException
	 */
	McBtsSN queryRRUHWInfo(Long moId) throws Exception,
			UnsupportedOperationException;

}
