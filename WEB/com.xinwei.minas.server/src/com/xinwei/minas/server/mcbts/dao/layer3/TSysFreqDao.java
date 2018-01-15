package com.xinwei.minas.server.mcbts.dao.layer3;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqModule;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

public interface TSysFreqDao extends GenericDAO<TSysFreqModule, Long> {

	/**
	 * ����ϵͳ��ЧƵ��
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configData(List<TSysFreqModule> sysFreqList, int FreqType);

	/**
	 * ��ѯϵͳ��ЧƵ��
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */

	public List<TSysFreqModule> queryData(int freqType);

	/**
	 * ��ѯ����ϵͳ��ЧƵ��
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */

	public List<TSysFreqModule> queryAllData() throws Exception;

	/**
	 * �����ƶ���վ��ϵͳ��ЧƵ��
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configBtsData(Long moId, List<TSysFreqModule> sysFreqList);

	/**
	 * ɾ������ϵͳ��ЧƵ��
	 */
	public void deleteAll() throws Exception;
}
