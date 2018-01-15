/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-7-18	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.enb.core.model.xstat.CounterItemConfig;
import com.xinwei.minas.enb.core.model.xstat.KpiItemConfig;
import com.xinwei.minas.xstat.core.model.PreStatItem;
import com.xinwei.minas.xstat.core.model.StatDataQueryCondition;

/**
 * 
 * ����ͳ��ҵ�����������ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbStatBizFacade extends Remote {

	/**
	 * ��ѯ����Counter����
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<CounterItemConfig> queryCounterConfigs()
			throws RemoteException, Exception;

	/**
	 * ��ѯ����KPI����
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<KpiItemConfig> queryKpiConfigs() throws RemoteException,
			Exception;

	/**
	 * ��������ѯͳ������
	 * 
	 * @param condition
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<PreStatItem> queryStatData(StatDataQueryCondition condition)
			throws RemoteException, Exception;

}
