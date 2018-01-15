/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-19	| liuzhongyan 	| 	create the file                       
 */
package com.xinwei.minas.mcbts.core.facade.layer3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqModule;

public interface SysFreqFacade extends Remote {

	/**
	 * �ӻ�վ��ѯ��վ����ЧƵ��
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Object[] queryDataFromBts(long moId) throws RemoteException,
			Exception;

	/**
	 * ��ȡ���л�վ��Ƶ��Ļ���
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TSysFreqModule> queryUsedFreqFromBts() throws Exception,
			RemoteException;

	/**
	 * ����ϵͳ��ЧƵ��
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void configData(OperObject operObject, Long moId, List<TSysFreqModule> sysFreqList,
			int freqSwitch, boolean isConfig) throws RemoteException, Exception;

	/**
	 * ��ѯϵͳ��ЧƵ��
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */

	public List<TSysFreqModule> queryData(int freqType) throws RemoteException,
			Exception;

	/**
	 * ��ѯ����ϵͳ��ЧƵ��
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */

	public List<TSysFreqModule> queryAllData() throws RemoteException,
			Exception;

	/**
	 * ��ѯ�Ƿ�ʹ��ϵͳ��ЧƵ��
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public int queryFreqSwitch() throws RemoteException, Exception;

	/**
	 * ����ָ����վ��ϵͳ��ЧƵ��
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configBtsData(Long moId, List<TSysFreqModule> sysFreqList)
			throws RemoteException, Exception;

	/**
	 * �����ƶ���վ��ϵͳ��ЧƵ��
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configFreqSwitch(int freqSwitch) throws RemoteException,
			Exception;
	
	/**
	 * ɾ��ָ����ϵͳ��ЧƵ��
	 * @param freq
	 * @throws Exception
	 */
	@Loggable
	public void deleteData(OperObject operObject, TSysFreqModule freq) throws Exception;

	/**
	 * ���ָ����ϵͳ��ЧƵ��
	 * @param freq
	 * @throws Exception
	 */
	@Loggable
	public void saveData(OperObject operObject, TSysFreqModule freq) throws Exception;
	
	
	/**
	 * ���ָ����ϵͳ��ЧƵ���б�
	 * @param freqs
	 * @throws Exception
	 */
	@Loggable
	public void saveAllData(OperObject operObject, List<TSysFreqModule> freqs) throws Exception;
	
	
	/**
	 * ����ָ����ϵͳ��ЧƵ��
	 * @param freq
	 * @throws Exception
	 */
	@Loggable
	public void updateData(OperObject operObject, TSysFreqModule freq) throws Exception;
}
