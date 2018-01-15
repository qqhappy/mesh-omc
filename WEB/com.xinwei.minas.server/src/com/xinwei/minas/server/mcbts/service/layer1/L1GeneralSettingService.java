/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2012-12-4	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.layer1;

import com.xinwei.minas.mcbts.core.model.layer1.L1GeneralSetting;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * L1����ҵ�����ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface L1GeneralSettingService extends ICustomService {

	/**
	 * ��ѯL1������Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return
	 */
	public L1GeneralSetting queryByMoId(Long moId) throws Exception;

	/**
	 * ����L1������Ϣ
	 * 
	 * @param locationArea
	 * @throws Exception
	 */
	public void config(L1GeneralSetting setting, boolean isSyncConfig)
			throws Exception;

	/**
	 * ����Ԫ��ѯL1������Ϣ
	 * 
	 * @param moId
	 * @throws Exception
	 * @return GenericBizData
	 */
	public L1GeneralSetting queryFromNE(Long moId) throws Exception;
}
