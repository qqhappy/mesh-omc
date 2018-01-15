/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-8	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.proxy.sysManage;

import com.xinwei.minas.ut.core.model.UserTerminal;
import com.xinwei.omp.core.model.biz.GenericBizData;

/**
 * 
 * �ն�̽�����ӿ�
 * 
 * 
 * @author tiance
 * 
 */

public interface TerminalDetectiveProxy {

	/**
	 * ��ѯ��Ԫҵ������
	 * 
	 * @param genericBizData
	 *            ��Ԫҵ������
	 * @return ��¼��
	 * @throws Exception
	 */
	UserTerminal query(Long moId, GenericBizData genericBizData)
			throws Exception;

}
