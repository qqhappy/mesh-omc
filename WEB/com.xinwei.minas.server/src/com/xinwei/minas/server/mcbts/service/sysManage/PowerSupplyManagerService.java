/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-18	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.util.HashSet;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.PowerSupply;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * 
 * ��Դ�������ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p>
 * 
 * @author zhuxiaozhan
 * 
 */

public interface PowerSupplyManagerService extends ICustomService {
	/**
	 * �����Դ��Ϣ
	 * 
	 * @param data
	 */
	public void savePowerSupplyInfo(PowerSupply data);

	/**
	 * ��ѯ���е�Դ��Ϣ
	 * 
	 * @return
	 */
	public List<PowerSupply> queryAll();

	/**
	 * ɾ����Դ��Ϣ
	 * 
	 * @param powerSupply
	 */
	public void delPowerSupplyInfo(PowerSupply powerSupply);

	/**
	 * ���ݻ�վMoId��ѯ��Դ�����ࣨPowerSupply��
	 */
	public PowerSupply querybyMcBtsMoId(long moid);

	/**
	 * �����վ�͵�Դ�Ķ�Ӧ��Ϣ��ϵ��
	 */
	public void savePowerSupplyAndMcbtsRelation(PowerSupply powerSupply);

	/**
	 * ���ݵ�Դ��Ϣ��ѯ��Ӧ��վ��moId����
	 */
	public HashSet<Long> queryMcbtsByPowerSupply(PowerSupply powerSupply);

	/**
	 * ���¼��ص�Դ��ط���
	 * 
	 * @param pollInterval
	 */
	public void reloadPowerSupplyServices(int pollInterval);
}
