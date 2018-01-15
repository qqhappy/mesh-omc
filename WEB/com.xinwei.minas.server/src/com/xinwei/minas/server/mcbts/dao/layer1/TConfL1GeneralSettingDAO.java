/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-11	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer1;

import com.xinwei.minas.mcbts.core.model.layer1.L1GeneralSetting;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * L1������ϢDAO
 * 
 * @author chenjunhua
 * 
 */

public interface TConfL1GeneralSettingDAO extends
		GenericDAO<L1GeneralSetting, Long> {

	/**
	 * ����moId��ѯL1���û�����Ϣ
	 * @param moId
	 */
	public L1GeneralSetting queryByMoId(Long moId);
}
