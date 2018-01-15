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
 * ��վ���к�DAO
 * 
 * @author fangping
 * 
 */

public interface McBtsRfPanelStatusDAO extends GenericDAO<McBtsRfPanelStatus, Long> {

	/**
	 * �����ݿ��ѯ��¼
	 * 
	 * @param moId
	 * @return
	 */
	public List<McBtsRfPanelStatus> queryStateQueryFromDB(long moId);

	/**
	 * �����¼�����ݿ�
	 * 
	 */
	public void saveFixedCountRecord(McBtsRfPanelStatus mcbtsstatequery);

	/**
	 * ��ѯ���¼�¼
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsRfPanelStatus queryNewestRecord(long moId);

}
