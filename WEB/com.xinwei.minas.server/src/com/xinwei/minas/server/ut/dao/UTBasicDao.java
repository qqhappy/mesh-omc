/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-31	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.ut.dao;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;

/**
 * 
 * �ն˻����־ò�ӿ�
 * 
 * 
 * @author tiance
 * 
 */

public interface UTBasicDao {
	public List<TerminalVersion> queryUTTypes();
}
