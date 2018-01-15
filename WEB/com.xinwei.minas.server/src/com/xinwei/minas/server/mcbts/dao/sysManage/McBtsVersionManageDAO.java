/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-19	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.sysManage.McBtsCodeDownloadTask;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersionHistory;
import com.xinwei.minas.mcbts.core.model.sysManage.TDLHistoryKey;
import com.xinwei.minas.server.platform.generic.dao.GenericDAO;

/**
 * 
 * ��վ�汾����DAO�ӿ�
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsVersionManageDAO extends GenericDAO<McBtsVersion, Long> {

	public List<McBtsVersion> queryByBtsType(Integer btsType);

	public void delete(McBtsVersion mcBtsVersion);

	/**
	 * ���ڻ�վ���ͺͰ汾����ȡһ����վ���
	 * 
	 * @param btsType
	 * @param version
	 * @return
	 */
	public McBtsVersion queryByBtsTypeAndVersion(int btsType, String version);
}
