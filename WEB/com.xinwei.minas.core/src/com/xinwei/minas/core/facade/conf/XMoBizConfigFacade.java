/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.core.facade.conf;

import java.net.InetSocketAddress;
import java.rmi.Remote;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.conf.KeyDesc;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XCollection;
import com.xinwei.omp.core.model.meta.XMetaRef;

/**
 * 
 * ͨ����Ԫ��������
 * 
 * @author chenjunhua
 * 
 */

public interface XMoBizConfigFacade extends Remote {
	
	/**
	 * ��ѯָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪId
	 * @param bizName
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public XBizTable queryFromEms(Long moId, String bizName,
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
	 * ����ָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪId
	 * @param bizName
	 *            ����
	 * @param bizRecord
	 *            ��¼
	 * @throws Exception
	 */
	@Loggable
	public void add(Long moId, String bizName, XBizRecord bizRecord)
			throws Exception;
	
	/**
	 * ����ָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪId
	 * @param bizName
	 *            ����
	 * @param bizRecord
	 *            ��¼
	 * @throws Exception
	 */
	@Loggable
	public void update(Long moId, String bizName, XBizRecord bizRecord)
			throws Exception;
	
	/**
	 * ɾ��ָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪId
	 * @param bizName
	 *            ����
	 * @param bizKey
	 *            ����
	 * @throws Exception
	 */
	@Loggable
	public void delete(Long moId, String bizName, XBizRecord bizKey)
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
	 * ��ѯUIӳ��(Key:�汾�� , Value:���ܼ���)
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Map<String, XCollection> queryUIMap(int moTypeId, int subTypeId)
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
	 * @param moTypeId
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, List<String>> querySupportedProtocolVersion(int moTypeId)
			throws Exception;
	
	/**
	 * ��ѯָ�������б������ҵ������
	 * 
	 * @param moTypeId
	 * @param moIdList
	 * @return
	 * @throws Exception
	 */
	public Map<Long, List<XBizTable>> queryDataByMoIdList(int moTypeId,
			List<Long> moIdList) throws Exception;
	
	/**
	 * �Ƚϲ�ͬ���������ݵ���Ԫ
	 * 
	 * @param moId
	 * @throws Exception
	 */
	@Loggable
	public void compareAndSyncEmsDataToNe(Long moId) throws Exception;
	
	/**
	 * �Ƚϲ�ͬ����Ԫ���ݵ�����
	 * 
	 * @param moId
	 * @throws Exception
	 */
	@Loggable
	public void compareAndSyncNeDataToEms(Long moId) throws Exception;
	
	/**
	 * ��ȡһ�����е�С������ID
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getFreePci(int enbType, String protocolVersion) throws Exception;
	
	/**
	 * ��ȡһ�����е��߼�������
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getFreeRsi(int enbType, String protocolVersion) throws Exception;
}
