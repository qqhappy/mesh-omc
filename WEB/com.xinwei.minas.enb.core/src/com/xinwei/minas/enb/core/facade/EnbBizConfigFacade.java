/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.enb.core.facade;

import java.rmi.Remote;

import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;

/**
 * 
 * eNBͨ����Ԫ��������
 * 
 * @author chenjunhua
 * 
 */

public interface EnbBizConfigFacade extends Remote {

	/**
	 * ��ѯָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪId
	 * @param tableName
	 *            ����
	 * @param conditon
	 *            ��ѯ����
	 * @return
	 * @throws Exception
	 */
	public XBizTable queryFromEms(Long moId, String tableName,
			XBizRecord conditon) throws Exception;

	/**
	 * ���ӻ����ָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪId
	 * @param tableName
	 *            ����
	 * @param bizRecord
	 *            ��¼
	 * @throws Exception
	 */
	public void addOrUpdate(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception;

	/**
	 * ɾ��ָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪId
	 * @param tableName
	 *            ����
	 * @param conditon
	 *            ����
	 * @throws Exception
	 */
	public void delete(Long moId, String tableName, XBizRecord conditon)
			throws Exception;

}
