/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-18	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.mcbts.core.model.sysManage.TDLHistoryKey;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersionType;
import com.xinwei.minas.mcbts.core.model.sysManage.UTCodeDownloadTask;

/**
 * 
 * �ն˰汾�������ӿ�
 * 
 * 
 * @author tiance
 * 
 */

public interface TerminalVersionManageService {

	/**
	 * ��ȡ�������ϵ������ն˰汾���б�
	 * 
	 */
	public List<TerminalVersion> queryAll();

	/**
	 * ����typeId��ȡĳ���ն˵����а汾
	 * 
	 */
	public List<TerminalVersion> queryByTypeId(Integer typeId);

	/**
	 * �ӷ�������ɾ��һ���ն˰汾
	 */
	public void delete(TerminalVersion terminalVersion);

	/**
	 * ����typeId��version,�ӷ�������ȡĳ���ն˰汾��Ϣ
	 */
	public TerminalVersion queryByTypeIdAndVersion(Integer typeId,
			String version, int fileType);

	/**
	 * �����ն˰汾
	 */
	public int modify(TerminalVersion terminalVersion);

	/**
	 * ����ն˰汾
	 */
	public int add(TerminalVersion terminalVersion);

	/**
	 * ���ն˰汾���ص�FTP������,��֪ͨ��վ��ȡ�ն˰汾
	 */
	public int download(Mo mo, Long btsId, TerminalVersion terminalVersion)
			throws Exception;

	/**
	 * ���شӻ�վ���ص������ն˰汾�����н��
	 * 
	 * @param mcBtsId
	 * @param terminalVersion
	 * @return
	 */
	public Map<TDLHistoryKey, UTCodeDownloadTask> queryDownloadHistory(
			Long mcBtsId);

	/**
	 * ͨ��TDLHistoryKey�Ӻ�̨�����в�ѯһ�����ذ汾������״̬
	 * 
	 * @param key
	 * @return
	 */
	public UTCodeDownloadTask getLatestStatus(Long btsId);

	/**
	 * ɾ����ʷ��¼��ļ�¼
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 */
	public int deleteHistory(Long btsId, UTCodeDownloadTask task);

	/**
	 * ɾ��ĳ����Ԫ��������ʷ��¼
	 * 
	 * @param btsId
	 * @return
	 */
	public int deleteAllHis(Long btsId);
	
	/**
	 * ��ѯ�����ն˰汾����
	 * 
	 */
	public List<TerminalVersionType> queryAllType();

	/**
	 * �����ն˰汾����
	 * @param model
	 * @return
	 */
	public void addType(TerminalVersionType model);

	/**
	 * �����ն˰汾����
	 */
	public int modifyType(TerminalVersionType model) throws RemoteException,
			Exception;

	/**
	 * ɾ���ն˰汾����
	 */
	public void deleteType(TerminalVersionType model) throws RemoteException,
			Exception;
	
}
