/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-14	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.common;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.common.UpgradeInfo;
import com.xinwei.minas.mcbts.core.model.common.UpgradeInfoArchive;

/**
 * 
 * ��վ���������������
 * 
 * @author tiance
 * 
 */

public interface McBtsBatchUpgradeService {
	/**
	 * ��ӻ�վ������������
	 * 
	 * @param list
	 *            ��ӵĻ�վ������
	 * @throws Exception
	 */
	public void addTask(List<UpgradeInfo> list) throws Exception;

	/**
	 * ��ȡ���л�վ�������������Map
	 * 
	 * @return �������������Map,keyΪ��վ��moId
	 * @throws Exception
	 */
	public Map<Integer, Map<Long, UpgradeInfo>> queryAll() throws Exception;

	/**
	 * ��ȡ���л�վ���������Map
	 * 
	 * @return ���Map��btsTypeΪKey; �ڲ�ΪUpgradeInfo���б�
	 * @throws Exception
	 */
	public Map<Integer, List<UpgradeInfo>> queryAll2() throws Exception;

	/**
	 * ��ȡ���ڽ�������������
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<UpgradeInfo> queryUpgrading() throws Exception;

	/**
	 * ��ֹ��վ��������������
	 * 
	 * @param list
	 *            ����ֹ�����������б�
	 * @throws Exception
	 */
	public void terminate(List<UpgradeInfo> list) throws Exception;

	/**
	 * ���Ѿ�������������й鵵
	 * 
	 * @throws Exception
	 */
	public void archive() throws Exception;

	/**
	 * ��ȡÿ����վ������һ���鵵
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, List<UpgradeInfoArchive>> queryLatestArchive()
			throws Exception;

	/**
	 * ��ȡһ����վ��������ʷ��¼
	 * 
	 * @return һ����վ��������ʷ��¼
	 */
	public List<UpgradeInfoArchive> queryArchiveByMoId(long moId)
			throws Exception;

	/**
	 * ��ȡ��Ҫ����ĳ���汾����������б�
	 * 
	 * @param version
	 * @return
	 * @throws Exception
	 */
	public List<UpgradeInfo> queryUpgradingInfoByVersion(String version)
			throws Exception;
}
