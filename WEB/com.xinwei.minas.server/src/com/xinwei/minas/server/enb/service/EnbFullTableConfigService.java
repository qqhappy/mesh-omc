/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-12	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.service;

import java.util.List;
import java.util.Map;

import com.xinwei.minas.enb.core.model.FullTableConfigInfo;

/**
 * 
 * ��������ҵ�����ӿ�
 * 
 * <p>
 * ����ϸ����
 * </p>
 * 
 * @author zhuxiaozhan
 * 
 */

public interface EnbFullTableConfigService {
	/**
	 * ������������
	 * 
	 * @param moId
	 * @throws Exception
	 */
	public void config(Long moId) throws Exception;

	/**
	 * ɾ��ָ��������������Ϣ
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void delete(FullTableConfigInfo data) throws Exception;

	/**
	 * ��ѯָ��eNB������������Ϣ
	 * 
	 * @param moId
	 * @return
	 * @throws Exception
	 */
	public FullTableConfigInfo queryByMoId(Long moId) throws Exception;

	/**
	 * ��ѯ���е�����������Ϣ
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<FullTableConfigInfo> queryAll() throws Exception;

	/**
	 * ��������״̬��ѯ����������Ϣ
	 * 
	 * @param status
	 *            ���ɹ���ʧ�ܡ�������
	 * @return
	 * @throws Exception
	 */
	public List<FullTableConfigInfo> queryByStatus(int status) throws Exception;

	/**
	 * Ϊָ����Ԫ��Ŀ��汾��ʽ�������������ļ�,���ŵ�FTP��,�������ɵ��ļ�����
	 * 
	 * @param moId
	 * @param targetVersion
	 * @return
	 * @throws Exception
	 */
	public String generateFullTableSqlFile(Long moId, String targetVersion)
			throws Exception;

	/**
	 * Ϊָ����Ԫ����Ŀ��汾��ʽ�������б���������õ�sql�ű��б�
	 * 
	 * @param moId
	 * @param targetVersion
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<String>> generateFullTableSql(Long moId,
			String targetVersion) throws Exception;

	/**
	 * Ϊָ����Ԫ����Ŀ��汾��ʽ����ָ������������õ�sql�ű��б�
	 * 
	 * @param moId
	 * @param targetVersion
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public List<String> generateFullTableSql(Long moId, String targetVersion,
			String tableName) throws Exception;

	/**
	 * ���������������ҵ����Ϣ
	 * 
	 * @param data
	 * @throws Exception
	 */
	public void saveUpdate(FullTableConfigInfo data) throws Exception;

}
