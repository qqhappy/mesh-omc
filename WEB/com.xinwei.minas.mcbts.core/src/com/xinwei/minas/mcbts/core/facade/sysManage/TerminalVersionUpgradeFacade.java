/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-1	| zhuxiaozhan 	| 	create the file                       
 */
package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.TerminalVersion;
import com.xinwei.minas.ut.core.model.UserTerminal;

/**
 * BootLoader���ն������������
 * 
 * @author zhuxiaozhan
 * 
 */
public interface TerminalVersionUpgradeFacade extends Remote {
	/**
	 * �ն������������
	 * 
	 * @param moId
	 *            ��վmoid
	 * @param eid
	 *            �ն˵�pid(eid)
	 * @param tv
	 *            Ŀ������汾
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void upgradeConfig(OperObject operObject, Long moId, String eid, TerminalVersion tv)
			throws RemoteException, Exception;

	/**
	 * ��ȡ�ն���������
	 * 
	 * @param utList
	 * @return Map<pid, ����>
	 */
	public Map<String, String> getUTProgress(List<UserTerminal> utList)
			throws RemoteException;

	/**
	 * BootLoader��������
	 * 
	 * @param moId
	 *            ��վmoid
	 * @param eid
	 *            �ն˵�pid(eid)
	 * @param tv
	 *            Ŀ������汾
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void bootloaderUpgrade(OperObject operObject, Long moId, String eid, TerminalVersion tv)
			throws RemoteException, Exception;

}
