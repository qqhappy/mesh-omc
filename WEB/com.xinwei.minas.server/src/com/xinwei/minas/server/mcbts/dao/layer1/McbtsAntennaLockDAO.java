/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-6	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer1;

import com.xinwei.minas.mcbts.core.model.layer1.McBtsAntennaLock;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * ���߱�������DAO�ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface McbtsAntennaLockDAO extends GenericDAO<McBtsAntennaLock, Long> {

	/**
	 * ����moId��ѯ���߱���������Ϣ
	 * 
	 * @param moId
	 */
	public McBtsAntennaLock queryByMoId(Long moId);

}