/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.proxy;

import java.util.List;

import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XList;

/**
 * 
 * ͨ����Ԫ����Proxy
 * 
 * @author chenjunhua
 * 
 */

public interface EnbBizConfigProxy {

	/**
	 * ���ݱ����ͼ�¼���ɲ���sql���
	 * 
	 * @param version
	 * @param tableName
	 * @param bizRecord
	 * @return
	 */
	public String generateInsertSql(int enbTypeId, String version, String tableName,
			XBizRecord bizRecord);

	/**
	 * ��ѯָ����Ԫ�ı�����
	 * 
	 * @param moId
	 * @param tableName
	 * @param fieldMetas
	 * @return
	 * @throws Exception
	 */
	public XBizTable query(Long moId, String tableName, List<XList> fieldMetas)
			throws Exception;

	/**
	 * ����ָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪID
	 * @param tableName
	 *            ҵ����
	 * @param bizRecord
	 *            ��¼
	 * @throws Exception
	 */
	public void add(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception;

	/**
	 * �޸�ָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪID
	 * @param tableName
	 *            ҵ����
	 * @param bizRecord
	 *            ��¼
	 * @throws Exception
	 */
	public void update(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception;

	/**
	 * ɾ��ָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪID
	 * @param tableName
	 *            ҵ����
	 * @param bizKey
	 *            ����
	 * @throws Exception
	 */
	public void delete(Long moId, String tableName, XBizRecord bizKey)
			throws Exception;

	/**
	 * ����ҵ������. flagΪtrue�����������ã�Ϊfalse����������
	 * 
	 * @param enId
	 * @param data
	 * @param bizFlag
	 * @throws Exception
	 */
	public void fullTableConfig(Long enbId, GenericBizData data, int bizFlag)
			throws Exception;
}
