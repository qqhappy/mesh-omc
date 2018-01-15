/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-18	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.facade.sysManage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.facade.sysManage.TerminalVersionManageFacade;
import com.xinwei.minas.mcbts.core.model.sysManage.TDLHistoryKey;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersionType;
import com.xinwei.minas.mcbts.core.model.sysManage.UTCodeDownloadTask;
import com.xinwei.minas.server.mcbts.service.sysManage.TerminalVersionManageService;
import com.xinwei.minas.server.platform.AppContext;

/**
 * 
 * �ն˰汾����
 * 
 * @author tiance
 * 
 */

public class TerminalVersionManageFacadeImpl extends UnicastRemoteObject
		implements TerminalVersionManageFacade {

	private final TerminalVersionManageService terminalVersionManageService;

	protected TerminalVersionManageFacadeImpl() throws RemoteException {
		super();
		terminalVersionManageService = AppContext.getCtx().getBean(
				TerminalVersionManageService.class);
	}

	/**
	 * ��ȡ�������ϵ������ն˰汾���б�
	 * 
	 */
	@Override
	public List<TerminalVersion> queryAll() throws RemoteException, Exception {
		return terminalVersionManageService.queryAll();
	}

	/**
	 * ����typeId��ȡĳ���ն˵����а汾
	 * 
	 * @param typeId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Override
	public List<TerminalVersion> queryByTypeId(int typeId)
			throws RemoteException, Exception {
		return terminalVersionManageService.queryByTypeId(typeId);
	}

	/**
	 * �ӷ�������ɾ��һ���ն˰汾
	 */
	@Override
	public void delete(OperObject operObject, TerminalVersion terminalVersion) throws RemoteException,
			Exception {
		terminalVersionManageService.delete(terminalVersion);
	}

	/**
	 * ����typeId��version,�ӷ�������ȡĳ���ն˰汾��Ϣ
	 */
	@Override
	@Deprecated
	// �Ѹĳɴӷ���������֤
	public TerminalVersion queryByTypeIdAndVersion(Integer typeId,
			String version, int fileType) throws RemoteException, Exception {
		return terminalVersionManageService.queryByTypeIdAndVersion(typeId,
				version, fileType);
	}

	/**
	 * �����ն˰汾
	 */
	@Override
	public int modify(TerminalVersion terminalVersion) throws RemoteException,
			Exception {
		return terminalVersionManageService.modify(terminalVersion);
	}

	/**
	 * ����ն˰汾
	 */
	@Override
	public int add(OperObject operObject, TerminalVersion terminalVersion) throws RemoteException,
			Exception {
		return terminalVersionManageService.add(terminalVersion);
	}

	/**
	 * ���ն˰汾���ص�FTP������,��֪ͨ��վ��ȡ�ն˰汾
	 */
	@Override
	public int download(OperObject operObject, Mo mo, Long btsId, TerminalVersion terminalVersion)
			throws RemoteException, Exception {
		return terminalVersionManageService
				.download(mo, btsId, terminalVersion);
	}

	/**
	 * ���شӻ�վ���ص������ն˰汾�Ľ��
	 * 
	 * @param mcBtsId
	 * @param terminalVersion
	 * @return
	 */
	@Override
	public Map<TDLHistoryKey, UTCodeDownloadTask> queryDownloadHistory(
			Long mcBtsId) throws RemoteException, Exception {
		return terminalVersionManageService.queryDownloadHistory(mcBtsId);
	}

	/**
	 * ͨ��TDLHistoryKey�Ӻ�̨�����в�ѯһ�����ذ汾������״̬
	 * 
	 * @param key
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public UTCodeDownloadTask getLatestStatus(Long btsId)
			throws RemoteException, Exception {
		return terminalVersionManageService.getLatestStatus(btsId);
	}

	/**
	 * ɾ����ʷ��¼��ļ�¼
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */

	public int deleteHistory(Long btsId, UTCodeDownloadTask task)
			throws RemoteException, Exception {
		return terminalVersionManageService.deleteHistory(btsId, task);
	}

	/**
	 * ɾ��ĳ����Ԫ��������ʷ��¼
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public int deleteAllHis(Long btsId) throws RemoteException, Exception {
		return terminalVersionManageService.deleteAllHis(btsId);
	}

	/**
	 * ��ѯ�����ն˰汾����
	 */
	@Override
	public List<TerminalVersionType> queryAllType() throws RemoteException,
			Exception {
		return terminalVersionManageService.queryAllType();
	}

	/**
	 * ����ն˰汾����
	 */
	@Override
	public void addType(OperObject operObject, TerminalVersionType model) throws RemoteException,
			Exception {
		terminalVersionManageService.addType(model);

	}

	/**
	 * �����ն˰汾����
	 */
	@Override
	public int modifyType(OperObject operObject, TerminalVersionType model) throws RemoteException,
			Exception {
		return terminalVersionManageService.modifyType(model);
	}

	/**
	 * ɾ���ն˰汾����
	 */
	@Override
	public void deleteType(OperObject operObject, TerminalVersionType model) throws RemoteException,
			Exception {
		terminalVersionManageService.deleteType(model);
	}
}
