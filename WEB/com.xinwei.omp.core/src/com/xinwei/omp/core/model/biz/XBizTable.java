/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-23	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.omp.core.model.biz;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.xinwei.omp.core.model.meta.XList;
import com.xinwei.omp.core.model.meta.XMetaSnmpTable;

/**
 * 
 * ҵ���ģ��
 * 
 * @author chenjunhua
 * 
 */

public class XBizTable implements java.io.Serializable {

	// ��ԪID
	private Long moId;

	// ����
	private String tableName;

	// ��¼��
	private List<XBizRecord> records = new LinkedList();

	public XBizTable(Long moId, String tableName) {
		this.setMoId(moId);
		this.setTableName(tableName);
	}

	/**
	 * ���Ӽ�¼
	 * 
	 * @param record
	 */
	public void addRecord(XBizRecord record) {
		records.add(record);
	}

	/**
	 * ɾ����¼
	 * 
	 * @param record
	 */
	public void removeRecord(XBizRecord record) {
		records.remove(record);
	}

	/**
	 * ���������˼�¼
	 * 
	 * @param conditon
	 */
	public void filterRecord(XBizRecord conditon, XMetaSnmpTable metaSnmpTable) {
		CopyOnWriteArrayList<XBizRecord> list = new CopyOnWriteArrayList(
				records);
		for (XBizRecord record : list) {
			if (!record.match(conditon, metaSnmpTable)) {
				this.removeRecord(record);
			}
		}
	}
	
	
	/**
	 * ���������˼�¼
	 * 
	 * @param conditon
	 */
	public void filterRecord(XBizRecord condition, XList bizMeta) {
		CopyOnWriteArrayList<XBizRecord> list = new CopyOnWriteArrayList(
				records);
		for (XBizRecord record : list) {
			if (!record.match(condition, bizMeta)) {
				this.removeRecord(record);
			}
		}
	}

	public Long getMoId() {
		return moId;
	}

	public void setMoId(Long moId) {
		this.moId = moId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<XBizRecord> getRecords() {
		return records;
	}

	public void setRecords(List<XBizRecord> records) {
		this.records = records;
	}

}
