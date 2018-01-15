/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-5-7	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersionHistory;
import com.xinwei.minas.mcbts.core.model.sysManage.TDLHistoryKey;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * ¿‡ºÚ“™√Ë ˆ
 * 
 * 
 * @author tiance
 * 
 */

public interface McBtsVersionHistoryManageDAO extends
		GenericDAO<McBtsVersionHistory, Long> {
	public List<McBtsVersionHistory> queryHistroy(Long btsId);

	public McBtsVersionHistory queryHistoryByKey(TDLHistoryKey key);

	public McBtsVersionHistory queryByBtsIdAndVersion(Long btsId, String version);

	public int deleteAllHistory(Long btsId);
}
