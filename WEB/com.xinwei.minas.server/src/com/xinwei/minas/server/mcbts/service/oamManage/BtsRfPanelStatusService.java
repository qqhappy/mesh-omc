/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-3	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.oamManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.McBtsRfPanelStatus;

/**
 * 
 * service
 * 
 * @author fangping
 * 
 */

public interface BtsRfPanelStatusService {

	public List<String> config(Integer restudy, Long moId) throws Exception;
	/**
	 * ���վ��ѯ��վ���к�
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public McBtsRfPanelStatus queryInfoFromNE(Long moId) throws Exception;

	/**
	 * �����ݿ��ѯ��վ���к�
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public List<McBtsRfPanelStatus> queryInfoFromDB(long moId) throws Exception;
}
