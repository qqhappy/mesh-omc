/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-24	| chenjunhua 	| 	create the file                       
 */

package com.xinwei.minas.micro.core.facade;

import java.rmi.Remote;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.EnbCellStart;
import com.xinwei.minas.core.model.EnbSceneDataShow;
import com.xinwei.omp.core.model.biz.XBizRecord;
import com.xinwei.omp.core.model.biz.XBizSceneTable;

/**
 * 
 * ͨ����Ԫ��������
 * 
 * @author chenjunhua
 * 
 */

public interface XMicroBizConfigFacade extends Remote {
	
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
	 * ����С��
	 * @param moId
	 * @param enbCellStart
	 * @throws Exception
	 */
	public void addSence(long moId, EnbCellStart enbCellStart) throws Exception;
	/**
	 * �޸�С��
	 * @param moId
	 * @param enbCellStart
	 * @throws Exception
	 */
	public void updateSence(long moId, EnbCellStart enbCellStart) throws Exception;
	
	/**
	 * ��ѯС����վ��Ϣ
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public XBizSceneTable querySceneData(Long moId) throws Exception;
	
	/**
	 * ����С��ID��ѯС����Ϣ
	 * @param moId
	 * @param cid
	 * @return
	 * @throws Exception
	 */
	public XBizRecord querySceneDataByCid(Long moId,int cid) throws Exception;
	
	/**
	 * ɾ��С����Ϣ
	 * @param moId
	 * @param cid
	 * @throws Exception
	 */
	public void deleteScene(Long moId,int cid) throws Exception;
	
	/**
	 * ��ѯ������ʾ����
	 * @return
	 * @throws Exception
	 */
	public EnbSceneDataShow querySceneDataShow() throws Exception;
	
	
}
