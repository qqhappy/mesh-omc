/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-11-5	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.core.secu.dao;

import java.util.List;

import com.xinwei.minas.core.model.OperAction;
import com.xinwei.minas.core.model.secu.OperSignature;

/**
 * 
 * 权限管理Dao接口
 * 
 * @author fanhaoyu
 * 
 */

public interface AuthorityManageDao {

	/**
	 * 查询指定用户的权限
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public List<OperAction> queryAuthority(String userName) throws Exception;

	/**
	 * 查询指定角色类型的所有权限
	 * 
	 * @param roleType
	 * @return
	 * @throws Exception
	 */
	public List<OperAction> queryAuthority(int roleType) throws Exception;

	/**
	 * 根据操作签名查询operAction
	 * @param signature
	 * @return
	 * @throws Exception
	 */
	public OperAction queryOperAction(OperSignature signature) throws Exception;

	/**
	 * 根据业务名称查询业务描述
	 * 
	 * @param operName
	 * @return
	 * @throws Exception
	 */
	public String queryOperation(String operName) throws Exception;
}
