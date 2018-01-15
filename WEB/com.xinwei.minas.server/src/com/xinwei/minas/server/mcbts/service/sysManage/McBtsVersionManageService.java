/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-19	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.sysManage;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsCodeDownloadTask;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersionHistory;

/**
 * 
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsVersionManageService {

	/**
	 * ��ѯ����ʵ��
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<McBtsVersion> queryAll() throws Exception;

	/**
	 * ɾ��һ��ʵ��
	 * 
	 * @param mcBtsVersion
	 * @throws Exception
	 */
	public void delete(McBtsVersion mcBtsVersion) throws Exception;

	/**
	 * ����ʵ��
	 * 
	 * @param mcbtsVersion
	 * @throws Exception
	 */
	public void saveOrUpdate(McBtsVersion mcbtsVersion) throws Exception;

	/**
	 * ���ػ�վ���
	 * 
	 * @param mo
	 * @param btsId
	 * @param mcBtsVersion
	 * @return
	 * @throws Exception
	 */
	public int download(Mo mo, Long btsId, McBtsVersion mcBtsVersion)
			throws Exception;

	/**
	 * ���ڻ�վ�����Ͳ�ѯ��վ�汾
	 * 
	 * @param btsType
	 * @return
	 */
	public List<McBtsVersion> queryByBtsType(Integer btsType);

	/**
	 * ���ڻ�վID��ѯ���������վ�İ汾���ؼ�¼
	 * 
	 * @param btsId
	 * @return
	 */
	public List<McBtsVersionHistory> queryDownloadHistory(Long btsId);

	/**
	 * ��ȡ���һ�������״̬
	 * 
	 * @param btsId
	 * @return
	 */
	public McBtsCodeDownloadTask getLatestStatus(Long btsId);

	/**
	 * ��ѯ��ǰ���ڽ��������Ļ�վ�����Ӧ�汾��
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Long, String> queryCurrentDownloadTasks() throws Exception;

	/**
	 * ɾ��ĳ����վ�µ��������ؼ�¼
	 * 
	 * @param btsId
	 * @return
	 */
	public int deleteAllHistory(Long btsId);

	/**
	 * ɾ����վ�µ�ĳ��������ʷ
	 * 
	 * @param history
	 * @return
	 */
	public int deleteHistory(McBtsVersionHistory history);

	/**
	 * ��RRU��վ������������
	 * 
	 * @param moId
	 * @param ho_type
	 */
	public void upgrade(Long moId, Integer ho_type) throws Exception;

	public void add(String fileName, byte[] fileContent) throws Exception;

	public boolean deleteFile(String fileName) throws Exception;

	/**
	 * enb����ļ��İ汾(���������FTP����ļ�)
	 */
	public void add(McBtsVersion mcBtsVersion) throws RemoteException,
			Exception;
}
