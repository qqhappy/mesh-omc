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

import com.xinwei.minas.mcbts.core.model.common.McBtsSN;
import com.xinwei.minas.mcbts.core.model.oamManage.McBtsSateQuery;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * ��վ���к�DAO
 * 
 * @author fangping
 * 
 */

public interface McBtsStateQueryDAO extends GenericDAO<McBtsSateQuery, Long> {

	/**
	 * �����ݿ��ѯ��¼
	 * 
	 * @param moId
	 * @return
	 */
	public List<McBtsSateQuery> queryStateQueryFromDB(long moId);

	/**
	 * �����¼�����ݿ�
	 * 
	 */
	public void saveFixedCountRecord(McBtsSateQuery mcbtsstatequery);

	/**
	 * ��ѯ���¼�¼
	 * 
	 * @param moId
	 * @return
	 */
	public McBtsSateQuery queryNewestRecord(long moId);

}
