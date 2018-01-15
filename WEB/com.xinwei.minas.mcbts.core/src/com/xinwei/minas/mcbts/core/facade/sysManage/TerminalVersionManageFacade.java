/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-18	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.TDLHistoryKey;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersionType;
import com.xinwei.minas.mcbts.core.model.sysManage.UTCodeDownloadTask;

/**
 * 
 * �ն˰汾��������
 * 
 * 
 * @author tiance
 * 
 */

public interface TerminalVersionManageFacade extends Remote {

	/**
	 * ��ȡ�������ϵ������ն˰汾���б�
	 */
	public List<TerminalVersion> queryAll() throws RemoteException, Exception;

	/**
	 * ����typeId��ȡĳ���ն˵����а汾
	 * 
	 * @param typeId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<TerminalVersion> queryByTypeId(int typeId)
			throws RemoteException, Exception;

	/**
	 * �ӷ�������ɾ��һ���ն˰汾
	 */
	@Loggable
	public void delete(OperObject operObject, TerminalVersion terminalVersion) throws RemoteException,
			Exception;

	/**
	 * ����typeId��version,�ӷ�������ȡĳ���ն˰汾��Ϣ
	 */
	public TerminalVersion queryByTypeIdAndVersion(Integer typeId,
			String version, int fileType) throws RemoteException, Exception;

	/**
	 * �����ն˰汾
	 */
	public int modify(TerminalVersion terminalVersion) throws RemoteException,
			Exception;

	/**
	 * ����ն˰汾
	 */
	@Loggable
	public int add(OperObject operObject, TerminalVersion terminalVersion) throws RemoteException,
			Exception;

	/**
	 * ���ն˰汾���ص�FTP������,��֪ͨ��վ��ȡ�ն˰汾
	 */
	@Loggable
	public int download(OperObject operObject, Mo mo, Long btsId, TerminalVersion terminalVersion)
			throws RemoteException, Exception;

	/**
	 * ���شӻ�վ���ص������ն˰汾�Ľ��
	 * 
	 * @param mcBtsId
	 * @param terminalVersion
	 * @return
	 */
	public Map<TDLHistoryKey, UTCodeDownloadTask> queryDownloadHistory(
			Long mcBtsId) throws RemoteException, Exception;

	/**
	 * ͨ��TDLHistoryKey�Ӻ�̨�����в�ѯһ�����ذ汾������״̬
	 * 
	 * @param key
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public UTCodeDownloadTask getLatestStatus(Long btsId)
			throws RemoteException, Exception;

	/**
	 * ɾ����ʷ��¼��ļ�¼
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public int deleteHistory(Long btsId, UTCodeDownloadTask task)
			throws RemoteException, Exception;

	/**
	 * ɾ��ĳ����Ԫ��������ʷ��¼
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public int deleteAllHis(Long btsId) throws RemoteException, Exception;
	
	/**
	 * ��ѯ�����ն˰汾����
	 */
	public List<TerminalVersionType> queryAllType() throws RemoteException,
			Exception;
	
	/**
	 * ����ն˰汾����
	 */
	@Loggable
	public void addType(OperObject operObject, TerminalVersionType model) throws RemoteException, Exception;
	/**
	 * �����ն˰汾����
	 */
	@Loggable
	public int modifyType(OperObject operObject, TerminalVersionType model) throws RemoteException,
			Exception;

	/**
	 * ɾ���ն˰汾����
	 */
	@Loggable
	public void deleteType(OperObject operObject, TerminalVersionType model) throws RemoteException,
			Exception;
}
