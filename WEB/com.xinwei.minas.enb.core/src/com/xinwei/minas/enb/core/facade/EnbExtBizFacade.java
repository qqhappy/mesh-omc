/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.omp.core.model.meta.XList;

/**
 * eNB��չҵ������ӿ�
 * 
 * @author fanhaoyu
 * 
 */
public interface EnbExtBizFacade extends Remote {

	/**
	 * ����ָ��eNB
	 * 
	 * @param moId
	 * @throws Exception
	 * @throws RemoteException
	 */
	@Loggable
	public void reset(OperObject operObject, long moId) throws Exception,
			RemoteException;

	/**
	 * ����ָ��eNB��ָ������
	 * 
	 * @param moId
	 * @param rackId
	 * @param shelfId
	 * @param boardId
	 * @throws Exception
	 * @throws RemoteException
	 */
	@Loggable
	public void reset(OperObject operObject, long moId, int rackId,
			int shelfId, int boardId) throws Exception, RemoteException;

	/**
	 * ������վ����
	 * 
	 * @param moId
	 * @return ���ؿ�վ�������ݣ���ǰ̨�����ļ�
	 * @throws Exception
	 * @throws RemoteException
	 */
	@Loggable
	public String exportActiveData(OperObject operObject, long moId)
			throws Exception, RemoteException;

	/**
	 * ѧϰeNB�������ã�����֧����Щ��ͱ��е���Щ�ֶ�
	 * 
	 * @param moId
	 * @param reStudy
	 *            ������������Ѵ��ڣ��Ƿ�����ѧϰ
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void studyEnbDataConfig(long moId, boolean reStudy)
			throws Exception, RemoteException;

	/**
	 * ��ѯ��ѧϰ��������
	 * 
	 * @return key=version,value=dataConfig
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Map<String, Map<String, XList>> queryStudyDataConfig()
			throws Exception, RemoteException;

	/**
	 * ��ָ����վ����վ������������ݻָ���ģ������
	 * 
	 * @param moId
	 * @throws Exception
	 * @throws RemoteException
	 */
	public void recoverDefaultData(long moId) throws Exception, RemoteException;
}
