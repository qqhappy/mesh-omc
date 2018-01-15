/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;

/**
 * 
 * ��վ���������ֵ����ӿ�
 * 
 * @author chenjunhua
 * 
 */

public interface McBtsTypeDDService {

	/**
	 * ��ѯ���еĻ�վ���������ֵ䶨��
	 * 
	 * @return
	 */
	public List<McBtsTypeDD> queryAll();

	/**
	 * ���ݻ�վ���ͱ����ȡ��Ӧ�Ļ�վ���������ֵ䶨��
	 * 
	 * @param mcBtsType
	 *            ��վ���ͱ���
	 * @return
	 */
	public McBtsTypeDD queryByTypeId(int mcBtsType);
}
