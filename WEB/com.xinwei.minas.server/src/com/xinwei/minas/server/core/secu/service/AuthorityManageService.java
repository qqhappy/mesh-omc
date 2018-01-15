/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-5	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.service;

import java.rmi.RemoteException;
import java.util.List;

import com.xinwei.minas.core.model.OperAction;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.core.model.secu.OperSignature;
import com.xinwei.minas.core.model.secu.syslog.LogQueryCondition;
import com.xinwei.minas.core.model.secu.syslog.SystemLogQueryResult;

/**
 * 
 * 权限管理服务接口
 * 
 * @author fanhaoyu
 * 
 */

public interface AuthorityManageService {

	/**
	 * 查询用户权限
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public List<OperAction> queryAuthority(String username) throws Exception;

	/**
	 * 根据操作签名查询对应的业务操作
	 * 
	 * @param signature
	 * @return
	 * @throws Exception
	 */
	public OperAction queryOperAction(OperSignature signature) throws Exception;

	/**
	 * 查询是否有指定操作签名的权限
	 * 
	 * @param sessionId
	 * @param signature
	 * @return
	 * @throws Exception
	 */
	public boolean checkPrivilege(String sessionId, OperSignature signature)
			throws Exception;

	/**
	 * 查询是否有指定业务操作的权限
	 * 
	 * @param sessionId
	 * @param operAction
	 * @return
	 * @throws Exception
	 */
	public boolean checkPrivilege(String sessionId, OperAction operAction)
			throws Exception;

	/**
	 * 增加系统日志
	 * 
	 * @param sessionId
	 * @param signature
	 * @param operObject
	 * @param params
	 * @throws Exception
	 */
	public void addLog(String sessionId, OperSignature signature,
			OperObject operObject, Object[] params) throws Exception;

	/**
	 * 根据查询条件查询系统日志
	 * 
	 * @param condition
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public SystemLogQueryResult queryLog(LogQueryCondition condition)
			throws Exception;
}