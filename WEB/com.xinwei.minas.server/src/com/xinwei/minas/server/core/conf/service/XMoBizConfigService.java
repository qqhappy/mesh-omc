/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-1-16	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.core.conf.service;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.conf.KeyDesc;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XMetaRef;

/**
 * 
 * ͨ����Ԫ���÷���
 * 
 * @author chenjunhua
 * 
 */

public interface XMoBizConfigService {

	/**
	 * �����ܲ�ѯָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪId
	 * @param tableName
	 *            ����
	 * @param condition
	 *            ����
	 * @return
	 * @throws Exception
	 */
	public XBizTable queryFromEms(Long moId, String tableName,
			XBizRecord condition) throws Exception;

	/**
	 * �����ܲ�ѯָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪId
	 * @param tableName
	 *            ����
	 * @param condition
	 *            ����
	 * @param queryStatus
	 *            �����վ���ߣ��Ƿ����վ��ѯ״̬�ֶε�ֵ
	 * @return
	 * @throws Exception
	 */
	public XBizTable queryFromEms(Long moId, String tableName,
			XBizRecord condition, boolean queryStatus) throws Exception;

	/**
	 * ����Ԫ��ѯ������
	 * 
	 * @param moId
	 *            ��ԪId
	 * @param tableName
	 *            ����
	 * @return
	 * @throws Exception
	 */
	public XBizTable queryFromNe(Long moId, String tableName) throws Exception;

	/**
	 * �Ƚϲ�ͬ���������ݵ���Ԫ
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void compareAndSyncEmsDataToNe(Long moId) throws Exception;

	/**
	 * �Ƚϲ�ͬ����Ԫ���ݵ�����
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void compareAndSyncNeDataToEms(Long moId) throws Exception;

	/**
	 * ����ָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪId
	 * @param tableName
	 *            ����
	 * @param bizRecord
	 *            ��¼
	 * @throws Exception
	 */
	public void add(Long moId, String tableName, XBizRecord bizRecord)
			throws Exception;

	/**
	 * ����ָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪId
	 * @param tableName
	 *            ����
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
	 *            ��ԪId
	 * @param tableName
	 *            ����
	 * @param bizKey
	 *            ����
	 * @throws Exception
	 */
	public void delete(Long moId, String tableName, XBizRecord bizKey)
			throws Exception;

	/**
	 * ��Ӧ�ö����ѯ����
	 * 
	 * @param moId
	 *            ��ԪId
	 * @param metaRefList
	 *            ���ö����б�
	 * @return KeyDesc�б�
	 * @throws Exception
	 */
	public List<KeyDesc> queryByMetaRef(Long moId, List<XMetaRef> metaRefList)
			throws Exception;

	/**
	 * ������Ԫ��ѯ���ܵ������ַ
	 * 
	 * @return ������ָ�����͵���Ԫͨ�ŵ������ַ(IP��ַ�Ͷ˿�)
	 * @throws Exception
	 */
	public InetSocketAddress queryEmsNetAddress(Long moId) throws Exception;

	/**
	 * ������Ԫ���ͻ�ȡ�ֶεȼ�����
	 * 
	 * @param moType
	 *            ��Ԫ����
	 * @return key:version, value:[key:tableName, value:[key:level,
	 *         value=fieldList]]
	 * @throws Exception
	 */
	public Map<String, Map<String, Map<String, List<String>>>> getTableFieldLevelConfig(
			int moType) throws Exception;

	/**
	 * ������Ԫ���ͻ�ȡҵ������
	 * 
	 * @param moTypeId
	 * @return key:version, value=Map[key:bizName, value=fieldNameList]
	 * @throws Exception
	 */
	public Map<String, Map<String, List<String>>> queryBizConfig(int moTypeId)
			throws Exception;

	/**
	 * ��ѯָ����Ԫ����֧�ֵ�Э��汾��
	 * 
	 * @return  �������Ԫ���� -- �汾�б�
	 * @throws Exception
	 */
	public Map<Integer, List<String>> querySupportedProtocolVersion() throws Exception;

	/**
	 * ��ѯָ�������б������ҵ������
	 * 
	 * @param moIdList
	 * @return
	 * @throws Exception
	 */
	public Map<Long, List<XBizTable>> queryDataByMoIdList(List<Long> moIdList)
			throws Exception;

}
