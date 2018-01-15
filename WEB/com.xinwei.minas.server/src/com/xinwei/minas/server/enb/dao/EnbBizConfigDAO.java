/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-4-2	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.dao;

import java.util.List;

import com.xinwei.minas.core.model.conf.KeyDesc;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizSceneTable;
import com.xinwei.omp.core.model.biz.XBizTable;
import com.xinwei.omp.core.model.meta.XMetaRef;

/**
 * 
 * ͨ����Ԫ����DAO
 * 
 * @author chenjunhua
 * 
 */

public interface EnbBizConfigDAO {
	
	/**
	 * ��ѯָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪID
	 * @param tableName
	 *            ����
	 * @param condition
	 *            ����
	 * @return
	 * @throws Exception
	 */
	public XBizTable query(Long moId, String tableName, XBizRecord condition)
			throws Exception;
	
	/**
	 * ��������ѯָ����Ԫ������
	 * 
	 * @param moId
	 *            ��ԪID
	 * @param tableName
	 *            ����
	 * @param bizKey
	 *            ������
	 * @return
	 * @throws Exception
	 */
	public XBizRecord queryByKey(Long moId, String tableName, XBizRecord bizKey)
			throws Exception;
	
	/**
	 * ����ָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪID
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
	 *            ��ԪID
	 * @param tableName
	 *            ����
	 * @param bizKey
	 *            ����
	 * @param bizRecord
	 *            ��¼
	 * @throws Exception
	 */
	public void add(Long moId, String tableName, XBizRecord bizKey,
			XBizRecord bizRecord) throws Exception;
	
	/**
	 * ��������ָ����Ԫ����
	 * 
	 * @param moId
	 */
	public void batchAdd(Long moId, List<XBizTable> tableList) throws Exception;
	
	/**
	 * ����ָ����Ԫ�ı�����
	 * 
	 * @param moId
	 *            ��ԪID
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
	 *            ��ԪID
	 * @param tableName
	 *            ����
	 * @param bizKey
	 *            �������������Ϊnull����ɾ���������м�¼
	 * @throws Exception
	 */
	public void delete(Long moId, String tableName, XBizRecord bizKey)
			throws Exception;
	
	/**
	 * ɾ��ָ����Ԫ������ҵ������
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void deleteAll(Long moId) throws Exception;
	
	/**
	 * ɾ��ָ����Ԫ��ָ��ҵ������
	 * 
	 * @param moId
	 * @param tableName
	 * @throws Exception
	 */
	public void deleteAll(Long moId, String tableName) throws Exception;
	
	/**
	 * ��Ӧ�ö����ѯ����
	 * 
	 * @param moId
	 *            ��ԪID
	 * @param metaRefList
	 *            ���ö����б�
	 * @return KeyDesc�б�
	 * @throws Exception
	 */
	public List<KeyDesc> queryByMetaRef(Long moId, List<XMetaRef> metaRefList)
			throws Exception;
	
	/**
	 * ����С����վ��������
	 * 
	 * @param moId
	 * @param cid
	 * @param bizRecord
	 * @throws Exception
	 */
	public void addScene(long moId, int cid, XBizRecord bizRecord)
			throws Exception;
	
	/**
	 * ��ѯenb������С����������
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public XBizSceneTable querySceneByMoId(long moId) throws Exception;
	
	/**
	 * ����moId��cid��ѯС����������
	 * 
	 * @param moId
	 * @param cid
	 * @return
	 * @throws Exception
	 */
	public XBizRecord querySceneByMoIdAndCid(long moId, int cid)
			throws Exception;
	
	/**
	 * ɾ��С����������
	 * 
	 * @param moId
	 * @param cid
	 * @throws Exception
	 */
	public void deleteScene(long moId, int cid) throws Exception;
	
	/**
	 * ����С����������
	 * 
	 * @param moId
	 * @param cid
	 * @throws Exception
	 */
	public void updateScene(long moId, int cid, XBizRecord bizRecord)
			throws Exception;
	
}
