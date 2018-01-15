/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-11	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.layer1;

import com.xinwei.minas.mcbts.core.model.layer1.CalibrationType;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * У׼����������ϢDAO
 * 
 * @author chenjunhua
 * 
 */

public interface TConfCalibrationTypeDAO extends
		GenericDAO<CalibrationType, Long> {

	/**
	 * ����moId��ѯУ׼�������û�����Ϣ
	 * 
	 * @param moId
	 */
	public CalibrationType queryByMoId(Long moId);
}
