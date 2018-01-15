/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-23	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.common;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.common.UpgradeInfo;
import com.xinwei.minas.mcbts.core.model.common.UpgradeInfoArchive;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * ��վ���������鵵�־ò�ӿ�
 * 
 * 
 * @author tiance
 * 
 */

public interface McBtsBatchUpgradeArchiveDAO extends
		GenericDAO<UpgradeInfoArchive, Long> {

	/**
	 * �鵵һ����������
	 * 
	 * @param list
	 */
	public void saveAll(List<UpgradeInfoArchive> list);

	/**
	 * ��ȡÿ����վ������һ���鵵
	 * 
	 * @return
	 */
	public List<UpgradeInfoArchive> queryLatestArchive();

	/**
	 * ��ȡһ����վ��������ʷ��¼
	 * 
	 * @return
	 */
	public List<UpgradeInfoArchive> queryArchiveByMoId(long moId);
}
