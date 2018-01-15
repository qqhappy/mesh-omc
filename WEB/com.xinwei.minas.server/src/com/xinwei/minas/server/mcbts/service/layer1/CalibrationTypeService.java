/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1;

import com.xinwei.minas.mcbts.core.model.layer1.CalibrationType;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * У׼��������ҵ�����ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface CalibrationTypeService extends ICustomService{

	/**
	 * ��ѯУ׼����������Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public CalibrationType queryByMoId(Long moId) throws Exception;

	/**
	 * ����У׼����������Ϣ
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	public void config(CalibrationType setting) throws Exception;

	/**
	 * ����Ԫ���������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public CalibrationType queryFromNE(Long moId) throws Exception;
}
