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
	 * 查询所有实体
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<McBtsVersion> queryAll() throws Exception;

	/**
	 * 删除一个实体
	 * 
	 * @param mcBtsVersion
	 * @throws Exception
	 */
	public void delete(McBtsVersion mcBtsVersion) throws Exception;

	/**
	 * 保存实体
	 * 
	 * @param mcbtsVersion
	 * @throws Exception
	 */
	public void saveOrUpdate(McBtsVersion mcbtsVersion) throws Exception;

	/**
	 * 下载基站软件
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
	 * 基于基站的类型查询基站版本
	 * 
	 * @param btsType
	 * @return
	 */
	public List<McBtsVersion> queryByBtsType(Integer btsType);

	/**
	 * 基于基站ID查询所有这个基站的版本下载记录
	 * 
	 * @param btsId
	 * @return
	 */
	public List<McBtsVersionHistory> queryDownloadHistory(Long btsId);

	/**
	 * 获取最后一个任务的状态
	 * 
	 * @param btsId
	 * @return
	 */
	public McBtsCodeDownloadTask getLatestStatus(Long btsId);

	/**
	 * 查询当前正在进行升级的基站及其对应版本号
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<Long, String> queryCurrentDownloadTasks() throws Exception;

	/**
	 * 删除某个基站下的所有下载记录
	 * 
	 * @param btsId
	 * @return
	 */
	public int deleteAllHistory(Long btsId);

	/**
	 * 删除基站下的某个下载历史
	 * 
	 * @param history
	 * @return
	 */
	public int deleteHistory(McBtsVersionHistory history);

	/**
	 * 向RRU基站发送升级请求
	 * 
	 * @param moId
	 * @param ho_type
	 */
	public void upgrade(Long moId, Integer ho_type) throws Exception;

	public void add(String fileName, byte[] fileContent) throws Exception;

	public boolean deleteFile(String fileName) throws Exception;

	/**
	 * enb添加文件的版本(向服务器和FTP添加文件)
	 */
	public void add(McBtsVersion mcBtsVersion) throws RemoteException,
			Exception;
}
