/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-3-19	| liuzhongyan 	| 	create the file                       
 */
package com.xinwei.minas.server.mcbts.service.layer3;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.layer3.TSysFreqModule;
import com.xinwei.minas.server.mcbts.service.ICustomService;

/**
 * ϵͳƵ���·�service
 * 
 * @author liuzhongyan
 * 
 */
public interface SysFreqService extends ICustomService {

	/**
	 * �ӻ�վ��ѯ��վ����ЧƵ��
	 * 
	 * @param moId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Object[] queryDataFromBts(long moId) throws Exception;

	/**
	 * ��ȡ���л�վ��Ƶ��Ļ���
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TSysFreqModule> queryUsedFreqFromBts() throws Exception;

	/**
	 * ����ϵͳ��ЧƵ��
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configData(Long moId, List<TSysFreqModule> sysFreqList,
			int freqSwitch, boolean isConfig) throws Exception;

	/**
	 * ��ѯϵͳ��ЧƵ��
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */

	public List<TSysFreqModule> queryData(int freqType) throws Exception;

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
	public void configBtsData(Long moId, List<TSysFreqModule> sysFreqList)
			throws Exception;

	/**
	 * ��ѯ�Ƿ�ʹ��ϵͳ��ЧƵ��
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public int queryFreqSwitch() throws Exception;

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
	public void deleteData(TSysFreqModule freq) throws Exception;

	/**
	 * ���ָ����ϵͳ��ЧƵ��
	 * @param freq
	 * @throws Exception
	 */
	public void saveData(TSysFreqModule freq) throws Exception;
	
	/**
	 * ���ָ����ϵͳ��ЧƵ���б�
	 * @param freqs
	 * @throws Exception
	 */
	public void saveAllData(List<TSysFreqModule> freqs) throws Exception;
	
	/**
	 * ����ָ����ϵͳ��ЧƵ��
	 * @param freq
	 * @throws Exception
	 */
	public void updateData(TSysFreqModule freq) throws Exception;
}
