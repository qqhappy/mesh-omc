/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-5-8	| chenlong 	| 	create the file                       
 */

package com.xinwei.omp.core.model.biz;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * С��������
 * 
 * 
 * @author chenlong
 * 
 */

public class XBizSceneTable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7577966224301031938L;
	
	// ��ԪID
	private Long moId;
	
	// ��¼��
	private List<XBizRecord> records = new LinkedList();
	
	public XBizSceneTable(Long moId) {
		super();
		this.moId = moId;
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
	
	public Long getMoId() {
		return moId;
	}
	
	public void setMoId(Long moId) {
		this.moId = moId;
	}
	
	public List<XBizRecord> getRecords() {
		return records;
	}
	
	public void setRecords(List<XBizRecord> records) {
		this.records = records;
	}
	
}
