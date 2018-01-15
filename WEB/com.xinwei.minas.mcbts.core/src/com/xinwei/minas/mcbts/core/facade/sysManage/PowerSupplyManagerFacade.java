/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-18	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.util.HashSet;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.PowerSupply;

/**
 * 
 * ��Դ��������ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p> 
 * 
 * @author zhuxiaozhan
 * 
 */

public interface PowerSupplyManagerFacade extends Remote {
	/**
	 * �����Դ��Ϣ
	 * @param data
	 */
	public void savePowerSupplyInfo(PowerSupply data) throws Exception;
	
	/**
	 * ��ѯ���е�Դ��Ϣ 
	 * @return
	 */
	public List<PowerSupply> queryAll() throws Exception;
	/**
	 * ɾ����Դ��Ϣ
	 */
	public void delPowerSupplyInfo(PowerSupply powerSupply) throws Exception;
	/**
	 * ���ݻ�վMoId��ѯ��Դ�����ࣨPowerSupply��
	 */
	public PowerSupply querybyMcBtsMoId(long moid)throws Exception;
	/**
	 * �����վ�͵�Դ�Ķ�Ӧ��Ϣ��ϵ��
	 */
	public void savePowerSupplyAndMcbtsRelation(PowerSupply powerSupply) throws Exception;
	/**
	 * ���ݵ�Դ��Ϣ��ѯ��Ӧ��վ��moId����
	 */
	public HashSet<Long> queryMcbtsByPowerSupply(PowerSupply powerSupply)throws Exception;
	/**
	 * ���¼��ص�Դ��ط���
	 * @param pollInterval
	 */
	public void reloadPowerSupplyServices(int pollInterval)throws Exception;;
}
