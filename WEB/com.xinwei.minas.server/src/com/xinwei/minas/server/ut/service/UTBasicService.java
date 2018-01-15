/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-30	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.service;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.ut.core.model.UTCondition;
import com.xinwei.minas.ut.core.model.UTQueryResult;
import com.xinwei.minas.ut.core.model.UserTerminal;

/**
 * 
 * McBts����ҵ�����ӿ�
 * 
 * 
 * @author tiance
 * 
 */

public interface UTBasicService {
	/**
	 * ��������HLR��ѯ�ն�
	 * 
	 * @param utc
	 * @return
	 * @throws Exception
	 */
	public UTQueryResult queryUTByCondition(UTCondition utc) throws Exception;

	/**
	 * �����ݿ��ѯ�����ն�����
	 * 
	 * @return
	 */
	public List<TerminalVersion> queryUTTypes();

	/**
	 * ���ĳ���ն˵�����״̬
	 * 
	 * @param pid
	 * @return
	 * @throws Exception
	 */
	public UserTerminal queryUTByPid(String pid) throws Exception;
}
