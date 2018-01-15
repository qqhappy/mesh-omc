/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-11	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer1;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer1.CalibGenConfigItem;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * У׼����RF������ϢDAO
 * 
 * @author chenjunhua
 * 
 */

public interface TConfSubCalibGenConfigDAO extends
		GenericDAO<CalibGenConfigItem, Long> {

	/**
	 * ����moId��ѯУ׼����RF���û�����Ϣ
	 * 
	 * @param moId
	 */
	public List<CalibGenConfigItem> queryByMoId(Long moId);

	/**
	 * �������û�����Ϣ
	 * 
	 * @param moId
	 */
	public void saveOrUpdate(List<CalibGenConfigItem> psConfigList);
}