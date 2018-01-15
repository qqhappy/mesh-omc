/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.McBtsSN;

/**
 * 
 * ��վ���к�service
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsSNService {

	/**
	 * ���վ��ѯ��վ���к�
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public McBtsSN querySNFromNE(Long moId) throws Exception;

	/**
	 * �����ݿ��ѯ��վ���к�
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<McBtsSN> querySNFromDB(long moId) throws Exception;

}
