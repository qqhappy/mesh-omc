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
 * Ȩ�޹���Dao�ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface AuthorityManageDao {

	/**
	 * ��ѯָ���û���Ȩ��
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public List<OperAction> queryAuthority(String userName) throws Exception;

	/**
	 * ��ѯָ����ɫ���͵�����Ȩ��
	 * 
	 * @param roleType
	 * @return
	 * @throws Exception
	 */
	public List<OperAction> queryAuthority(int roleType) throws Exception;

	/**
	 * ���ݲ���ǩ����ѯoperAction
	 * @param signature
	 * @return
	 * @throws Exception
	 */
	public OperAction queryOperAction(OperSignature signature) throws Exception;

	/**
	 * ����ҵ�����Ʋ�ѯҵ������
	 * 
	 * @param operName
	 * @return
	 * @throws Exception
	 */
	public String queryOperation(String operName) throws Exception;
}
