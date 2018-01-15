/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-19	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsCodeDownloadTask;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersionHistory;

/**
 * 
 * ��վ�汾����facade
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsVersionManageFacade extends Remote {

	/**
	 * ��ѯȫ��ʵ��
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsVersion> queryAll() throws RemoteException, Exception;

	/**
	 * ɾ��һ��ʵ��
	 * 
	 * @param mcBtsVersion
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void delete(OperObject operObject, McBtsVersion mcBtsVersion)
			throws RemoteException, Exception;

	/**
	 * �����վ�汾ʵ��
	 * 
	 * @param operObject
	 * @param mcBtsVersion
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void saveOrUpdate(OperObject operObject, McBtsVersion mcBtsVersion)
			throws RemoteException, Exception;

	/**
	 * ���ػ�վ���
	 * 
	 * @param mo
	 * @param btsId
	 * @param mcBtsVersion
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public int download(OperObject operObject, Mo mo, Long btsId,
			McBtsVersion mcBtsVersion) throws RemoteException, Exception;

	/**
	 * ���ڻ�վ�����Ͳ�ѯ��վ�汾
	 * 
	 * @param btsType
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsVersion> queryByBtsType(Integer btsType)
			throws RemoteException, Exception;

	/**
	 * ���ڻ�վID��ѯ���������վ�İ汾���ؼ�¼
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsVersionHistory> queryDownloadHistory(Long btsId)
			throws RemoteException, Exception;

	/**
	 * ��ȡ���һ�������״̬(ʱ��,״̬)
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public McBtsCodeDownloadTask getLatestStatus(Long btsId)
			throws RemoteException, Exception;

	/**
	 * ��ѯ��ǰ���ڽ��������Ļ�վ�����Ӧ�汾��
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Map<Long, String> queryCurrentDownloadTasks()
			throws RemoteException, Exception;

	/**
	 * ɾ��ĳ����վ�µ��������ؼ�¼
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public int deleteAllHistory(Long btsId) throws RemoteException, Exception;

	/**
	 * ɾ��ĳ����ʷ��¼
	 * 
	 * @param history
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public int deleteHistory(McBtsVersionHistory history)
			throws RemoteException, Exception;

	/**
	 * ���վ������������
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void upgrade(OperObject operObject, Long moId, Integer ho_type)
			throws RemoteException, Exception;

	/**
	 * ���������FTP����ļ�
	 * 
	 * @param fileName
	 * @param fileContent
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void add(String fileName, byte[] fileContent)
			throws RemoteException, Exception;

	/**
	 * ɾ���ļ�
	 * 
	 * @param fileName
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean deleteFile(String fileName) throws RemoteException,
			Exception;

	/**
	 * enb����ļ��İ汾(���������FTP����ļ�)
	 */
	@Loggable
	public void add(McBtsVersion mcBtsVersion) throws RemoteException,
			Exception;

}
