/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-8	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.rruManage;

import com.xinwei.minas.mcbts.core.model.common.McBtsSN;

/**
 * 
 * RRUӲ����Ϣ��ѯservice
 * 
 * @author chenshaohua
 * 
 */

// TODO:����
public interface RRUHWInfoService {

	/**
	 * ���վ��ѯRRUӲ����Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public McBtsSN queryHWInfoFromNE(Long moId) throws Exception;
}
