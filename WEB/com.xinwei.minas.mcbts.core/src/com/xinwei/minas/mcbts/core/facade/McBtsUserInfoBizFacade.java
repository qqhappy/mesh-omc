/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserAllInfo;
import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserGroup;

/**
 * 
 * ��վ�û�����ҵ������
 * 
 * @author fanhaoyu
 * 
 */

public interface McBtsUserInfoBizFacade extends Remote {

	/**
	 * ����һ���û�������Ϣ
	 * 
	 * @param btsId
	 * @param info
	 * @throws Exception
	 */
	public void add(Long btsId, McBtsUserAllInfo info) throws RemoteException,
			Exception;

	/**
	 * �޸�һ���û�������Ϣ
	 * 
	 * @param btsId
	 * @param info
	 * @throws Exception
	 */
	public void modify(Long btsId, McBtsUserAllInfo info)
			throws RemoteException, Exception;

	/**
	 * ɾ��һ���û�������Ϣ
	 * 
	 * @param btsId
	 * @param pid
	 * @throws Exception
	 */
	public void delete(Long btsId, Long pid) throws RemoteException, Exception;

	/**
	 * �����վ�û������б�
	 * 
	 * @param btsId
	 * @param infoList
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void importUserAllInfoList(Long btsId,
			List<McBtsUserAllInfo> infoList) throws RemoteException, Exception;

	/**
	 * �ӻ��������û�������Ϣ
	 * 
	 * @param btsId
	 * @return
	 * @throws Exception
	 */
	public List<McBtsUserAllInfo> queryAllInfoFromCache(Long btsId)
			throws RemoteException, Exception;

	/**
	 * ���վ�����û����ݲ�ѯ���󣬵ȴ���վ�ϴ����ݵ�FTP��������Ȼ���FTP�����������û������ļ�
	 * 
	 * @param btsId
	 * @return
	 * @throws Exception
	 */
	public List<McBtsUserAllInfo> queryUserInfo(Long btsId)
			throws RemoteException, Exception;

	/**
	 * �����û����ݣ����û������ļ��ϴ���FTP��������Ȼ��֪ͨ��վ����
	 * 
	 * @param btsId
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void configUserInfo(Long btsId) throws RemoteException, Exception;

	/**
	 * ��ѯָ����վ���û����б�
	 * 
	 * @param btsId
	 * @return
	 * @throws Exception
	 */
	public List<McBtsUserGroup> queryAllGroups(Long btsId)
			throws RemoteException, Exception;
}