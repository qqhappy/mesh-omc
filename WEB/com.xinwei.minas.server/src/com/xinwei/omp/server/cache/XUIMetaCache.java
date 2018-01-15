/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.server.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xinwei.omp.core.model.meta.XCollection;
import com.xinwei.omp.core.model.meta.XList;

/**
 * 
 * UIԪ���ݻ���
 * 
 * @author chenjunhua
 * 
 */

public class XUIMetaCache {

	private static final XUIMetaCache instance = new XUIMetaCache();

	// ���ܹ�ϣ��(Key:�豸���� , Value:[key:�汾,value=���ܼ���])
	private Map<Integer, Map<String, XCollection>> uiMap = new HashMap<Integer, Map<String, XCollection>>();

	private boolean initialized = false;

	private XUIMetaCache() {
	}

	public static XUIMetaCache getInstance() {
		return instance;
	}

	/**
	 * ������Ԫ����ID��������ID����KEY
	 * 
	 * @param moTypeId
	 * @param subTypeId
	 * @return
	 */
	public int createKey(int moTypeId, int subTypeId) {
		return moTypeId * 65536 + subTypeId;
	}

	/**
	 * �����豸�˵�
	 * 
	 * @param moTypeId
	 * @param version
	 * @param collection
	 */
	public void addMoBizMetas(int moTypeId, int subTypeId, String version,
			XCollection collection) {
		int key = this.createKey(moTypeId, subTypeId);
		Map<String, XCollection> map = uiMap.get(key);
		if (map == null) {
			map = new HashMap<String, XCollection>();
			uiMap.put(key, map);
		}
		map.put(version, collection);
	}

	/**
	 * ��ȡָ���豸����ָ���汾�Ĺ��ܲ˵�
	 * 
	 * @param moTypeId
	 * @param version
	 * @return
	 */
	public XCollection getMoBizMetas(int moTypeId, int subTypeId, String version) {
		int key = this.createKey(moTypeId, subTypeId);
		Map<String, XCollection> map = uiMap.get(key);
		if (map == null)
			return null;
		return map.get(version);
	}

	/**
	 * ����UI���ƻ�ȡ����
	 * 
	 * @param moTypeId
	 * @param version
	 * @param bizName
	 * @return
	 */
	public String getBizDescByName(int moTypeId, int subTypeId, String version,
			String bizName) {
		try {
			XCollection moUICollection = this.getMoBizMetas(moTypeId,
					subTypeId, version);
			XList bizMeta = moUICollection.getBizMetaBy(bizName);
			return bizMeta.getDesc();
		} catch (Exception e) {
			return "!" + bizName + "!";
		}
	}

	/**
	 * ��ȡָ���汾ָ����������ֶ�
	 * 
	 * @param moTypeId
	 * @param version
	 * @param bizName
	 * @return
	 */
	public XList[] getFiledMetas(int moTypeId, int subTypeId, String version,
			String bizName) {
		try {
			XCollection moUICollection = this.getMoBizMetas(moTypeId,
					subTypeId, version);
			XList bizMeta = moUICollection.getBizMetaBy(bizName);
			XList[] fieldMetas = bizMeta.getList();
			return fieldMetas;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * ��ȡָ����Ԫ���͵�ָ���汾��ʹ��ҵ������set
	 * 
	 * @param moTypeId
	 * @param version
	 * @return
	 */
	public Set<String> getAllBizName(int moTypeId, int subTypeId, String version) {
		XCollection moUICollection = this.getMoBizMetas(moTypeId, subTypeId,
				version);
		Map<String, XList> bizMap = moUICollection.getBizMap();
		Set<String> bizNameSet = bizMap.keySet();
		return bizNameSet;
	}

	/**
	 * ��ȡָ���汾�����Ϣ
	 * 
	 * @param moTypeId
	 * @param version
	 * @param bizName
	 * @return
	 */
	public XList getBizMetaByName(int moTypeId, int subTypeId, String version,
			String bizName) {
		try {
			XCollection moUICollection = this.getMoBizMetas(moTypeId,
					subTypeId, version);
			XList bizMeta = moUICollection.getBizMetaBy(bizName);
			return bizMeta;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * ��ȡָ���汾���ֶε���Ϣ
	 * 
	 * @param moTypeId
	 * @param version
	 * @param bizName
	 * @param fieldName
	 * @return
	 */
	public XList getFiledMetaByName(int moTypeId, int subTypeId,
			String version, String bizName, String fieldName) {
		XList bizMeta = getBizMetaByName(moTypeId, subTypeId, version, bizName);
		return bizMeta.getFieldMeta(fieldName);
	}

	/**
	 * ��ȡָ���汾�б��ֶε���Ϣ
	 * 
	 * @param moTypeId
	 * @param version
	 * @param bizName
	 * @param fieldIndex
	 * @return
	 */
	public XList getFiledMetaByIndex(int moTypeId, int subTypeId,
			String version, String bizName, int fieldIndex) {
		try {
			XCollection moUICollection = this.getMoBizMetas(moTypeId,
					subTypeId, version);
			XList bizMeta = moUICollection.getBizMetaBy(bizName);
			List<XList> fieldMetas = bizMeta.getVisiableList();
			return fieldMetas.get(fieldIndex);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * ��ȡָ����Ԫ���͵�UIӳ��
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, XCollection> getUiMap(Integer key) {
		return uiMap.get(key);
	}

	public Map<Integer, Map<String, XCollection>> getUiMap() {
		return uiMap;
	}

	/**
	 * ��ȡ����֧��ָ����Ԫ������Э��汾��(ǰ��λ�汾��)
	 * 
	 * @param moTypeId
	 * @return
	 */
	public Set<String> getSupportedVersions(Integer moTypeId, int subTypeId) {
		int key = this.createKey(moTypeId, subTypeId);
		Map<String, XCollection> versionMap = uiMap.get(key);
		return versionMap.keySet();
	}

	/**
	 * ����������Ϊ�ѳ�ʼ�����
	 */
	public synchronized void setInitialized() {
		this.initialized = true;
	}

	/**
	 * �鿴�����Ƿ��ѳ�ʼ�����
	 * 
	 * @return
	 */
	public boolean isInitialized() {
		return this.initialized;
	}

}
