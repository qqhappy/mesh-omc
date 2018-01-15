/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-18	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.sysManage.TDLHistoryKey;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersionType;
import com.xinwei.minas.mcbts.core.model.sysManage.UTCodeDownloadTask;

/**
 * 
 * �ն˰汾����Dao�ӿ�
 * 
 * @author tiance
 * 
 */

public interface TerminalVersionManageDAO {
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
	 * ����typeId��ѯ�ն˰汾
	 */
	public TerminalVersion queryByTypeIdAndVersion(Integer typeId,
			String version, int fileType);

	/**
	 * ������������������ն˰汾
	 * 
	 */
	public int add(TerminalVersion terminalVersion);

	/**
	 * �ӷ�����ɾ���ն˰汾
	 * 
	 */
	public int delete(TerminalVersion terminalVersion);

	/**
	 * �޸����ݿ��е�ĳ���ն˰汾
	 * 
	 */
	public int update(TerminalVersion terminalVersion);

	/**
	 * ��terminal_version_type���в�ѯ�Ƿ��Ѿ�����typeId��Ӧ������ ���û��,��Ҫ�����û��ȼ�����Ӧ������
	 * 
	 */
	public int queryForType(Integer typeId);

	/**
	 * ��ȡȫ��������ʷ
	 * 
	 * @return ����btsId, version�洢��MAP
	 */
	public Map<TDLHistoryKey, UTCodeDownloadTask> queryHistroy(Long btsId);

	/**
	 * ��ӵ���ʷ��¼��
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 */
	public int insertToHistory(Long btsId, UTCodeDownloadTask task);

	/**
	 * ����ʷ��¼��ɾ��
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 */
	public int deleteFromHistory(Long btsId, UTCodeDownloadTask task);

	/**
	 * ɾ��ĳ����Ԫ��������ʷ��¼
	 * 
	 * @param btsId
	 * @return
	 */
	public int deleteAllFromHistory(Long btsId);

	/**
	 * �޸�ĳ����ʷ��¼
	 * 
	 * @param btsId
	 * @param task
	 * @return
	 */
	public int updateHistory(Long btsId, UTCodeDownloadTask task);

	/**
	 * ͨ��TDLHistoryKey����ĳһ��history��¼
	 * 
	 * @param key
	 * @return
	 */
	public UTCodeDownloadTask queryHistoryByKey(TDLHistoryKey key);

	/**
	 * ��ѯ�����ն˰汾����
	 * @return
	 */
	public List<TerminalVersionType> queryAllType();
	
	/**
	 * ����ն˰汾����
	 */
	public int addType(TerminalVersionType model);
	/**
	 * �����ն˰汾����
	 */
	public int modifyType(TerminalVersionType model);

	/**
	 * ɾ���ն˰汾����
	 */
	public int deleteType(int typeId);

}
