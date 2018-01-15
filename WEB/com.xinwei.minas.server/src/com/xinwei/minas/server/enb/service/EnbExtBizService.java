/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-16	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.util.Map;

import com.xinwei.omp.core.model.meta.XList;

/**
 * 
 * eNB��չҵ�����ӿ�
 * 
 * @author fanhaoyu
 * 
 */

public interface EnbExtBizService {

	/**
	 * ����ָ��eNB
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void reset(long moId) throws Exception;

	/**
	 * ����ָ��eNB��ָ������
	 * 
	 * @param moId
	 * @param rackId
	 * @param shelfId
	 * @param boardId
	 * @throws Exception
	 */
	public void reset(long moId, int rackId, int shelfId, int boardId)
			throws Exception;

	/**
	 * ������վ����
	 * 
	 * @param moId
	 * @return ���ؿ�վ�������ݣ���ǰ̨�����ļ�
	 * @throws Exception
	 */
	public String exportActiveData(long moId) throws Exception;

	/**
	 * ѧϰeNB�������ã�����֧����Щ��ͱ��е���Щ�ֶ�
	 * 
	 * @param moId
	 * @param reStudy
	 *            ������������Ѵ��ڣ��Ƿ�����ѧϰ
	 * @throws Exception
	 */
	public void studyEnbDataConfig(long moId, boolean reStudy) throws Exception;

	/**
	 * ��ѯ��ѧϰ��������
	 * 
	 * @return key=version,value=dataConfig
	 * @throws Exception
	 */
	public Map<String, Map<String, XList>> queryStudyDataConfig()
			throws Exception;

	/**
	 * ��ָ����վ����վ������������ݻָ���ģ������
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void recoverDefaultData(long moId) throws Exception;
}
