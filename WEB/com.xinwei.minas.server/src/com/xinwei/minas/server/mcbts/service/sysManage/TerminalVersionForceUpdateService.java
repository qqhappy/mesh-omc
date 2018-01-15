/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-26	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.ut.core.model.UserTerminal;

/**
 * 
 * �ն˰汾ǿ����������ӿ�
 * 
 * @author tiance
 * 
 */

public interface TerminalVersionForceUpdateService {
	/**
	 * �����ݿ��ѯ����ǿ�������Ĺ���
	 * 
	 * @param btsId
	 * @return List<TerminalVersion>
	 */
	public List<TerminalVersion> queryList();

	/**
	 * �����ݿ��ѯ����ǿ�������Ĺ���
	 * 
	 * @param btsId
	 * @return boolean
	 */
	public boolean getSwitchStatus();

	/**
	 * �����ն˰汾ǿ������
	 * 
	 * @param status
	 * @param ruleList
	 */
	public void config(boolean status, List<TerminalVersion> ruleList) throws Exception;

	/**
	 * �ն�ע����������������������ն���������
	 * 
	 * @param eid
	 * @param tv
	 */
	public void upgradeConfig(Long moId, String eid, TerminalVersion tv) throws Exception;

	/**
	 * ��ȡ�ն���������
	 * 
	 * @param utList
	 * @return Map<pid, ����>
	 */
	public Map<String, String> getUTProgress(List<UserTerminal> utList);

	/**
	 * BootLoader��������
	 * 
	 * @param moId
	 *            ��վmoid
	 * @param eid
	 *            �ն˵�pid(eid)
	 * @param tv
	 *            Ŀ������汾
	 */
	public void bootloaderUpgrade(Long moId, String eid, TerminalVersion tv) throws Exception;
}
