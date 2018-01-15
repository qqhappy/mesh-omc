/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.dao.sysManage;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;

/**
 * 
 * �ն˰汾ǿ������Dao�ӿ�
 * 
 * 
 * @author tiance
 * 
 */

public interface TerminalVersionForceUpdateDao {

	/**
	 * �����ݿ��ѯ����ǿ�������Ĺ���
	 * 
	 * @param btsId
	 * @return
	 */
	public List<TerminalVersion> queryList();

	/**
	 * �����ݿ��ѯһ��ǿ�������Ĺ���
	 * 
	 * @param btsId
	 * @param typeId
	 * @return TVForceUpdate
	 */
	public TerminalVersion query(Long btsId, Integer typeId);
	
	/**
	 * ���ݹ�����,�޸ı��еĹ���
	 * @param ruleList
	 */
	public void update(List<TerminalVersion> ruleList);
}
