package com.xinwei.minas.server.mcbts.dao.layer3;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqModule;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

public interface TSysFreqDao extends GenericDAO<TSysFreqModule, Long> {

	/**
	 * 配置系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configData(List<TSysFreqModule> sysFreqList, int FreqType);

	/**
	 * 查询系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */

	public List<TSysFreqModule> queryData(int freqType);

	/**
	 * 查询所有系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */

	public List<TSysFreqModule> queryAllData() throws Exception;

	/**
	 * 配置制定基站的系统有效频点
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configBtsData(Long moId, List<TSysFreqModule> sysFreqList);

	/**
	 * 删除所有系统有效频点
	 */
	public void deleteAll() throws Exception;
}
