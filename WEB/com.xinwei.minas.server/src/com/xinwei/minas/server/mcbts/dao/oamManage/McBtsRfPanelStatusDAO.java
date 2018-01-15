/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-6-7	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.oamManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.McBtsRfPanelStatus;
import com.xinwei.minas.mcbts.core.model.common.McBtsSN;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * 基站序列号DAO
 * 
 * @author fangping
 * 
 */

public interface McBtsRfPanelStatusDAO extends GenericDAO<McBtsRfPanelStatus, Long> {

	/**
	 * 从数据库查询记录
	 * 
	 * @param moId
	 * @return
	 */
	public List<McBtsRfPanelStatus> queryStateQueryFromDB(long moId);

	/**
	 * 保存记录到数据库
	 * 
	 */
	public void saveFixedCountRecord(McBtsRfPanelStatus mcbtsstatequery);

	/**
	 * 查询最新记录
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsRfPanelStatus queryNewestRecord(long moId);

}
