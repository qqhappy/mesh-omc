/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-22	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.common.UpgradeInfo;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * ��վ���������־û��Ľӿ�
 * 
 * 
 * @author tiance
 * 
 */

public interface McBtsBatchUpgradeDAO extends GenericDAO<UpgradeInfo, Long> {

	/**
	 * ����һ����վ�����������б����ݿ�
	 * 
	 * @param list
	 */
	public void saveAll(List<UpgradeInfo> list);

	/**
	 * ����moId��ѯ��վ����������Ϣ
	 * 
	 * @param moId
	 */
	public UpgradeInfo queryByMoId(long moId);

	/**
	 * ��ѯһ����δ����������,��scheduledTime��idx����
	 * 
	 * @return ��δִ�е�����
	 */
	public UpgradeInfo queryFreeUpgradeInfo();

	/**
	 * ��ѯ���Ա��鵵����������
	 * 
	 * @return ���Ա��鵵�������б�
	 */
	public List<UpgradeInfo> queryToArchive();

	/**
	 * ɾ��Ҫ�鵵����������
	 * 
	 * @param listToArchive
	 */
	public void deleteArchive(List<UpgradeInfo> listToArchive);

	/**
	 * ��ȡҪ����ĳ���汾����������б�
	 * 
	 * @return
	 */
	public List<UpgradeInfo> queryUpgradingInfoByVersion(String version);
}
