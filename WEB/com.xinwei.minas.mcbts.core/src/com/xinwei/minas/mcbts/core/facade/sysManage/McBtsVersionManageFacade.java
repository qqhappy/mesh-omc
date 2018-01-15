/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-19	| chenshaohua 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.facade.sysManage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.xinwei.minas.core.annotation.Loggable;
import com.xinwei.minas.core.model.Mo;
import com.xinwei.minas.core.model.OperObject;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsCodeDownloadTask;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersion;
import com.xinwei.minas.mcbts.core.model.sysManage.McBtsVersionHistory;

/**
 * 
 * 基站版本管理facade
 * 
 * @author chenshaohua
 * 
 */

public interface McBtsVersionManageFacade extends Remote {

	/**
	 * 查询全部实体
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsVersion> queryAll() throws RemoteException, Exception;

	/**
	 * 删除一个实体
	 * 
	 * @param mcBtsVersion
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void delete(OperObject operObject, McBtsVersion mcBtsVersion)
			throws RemoteException, Exception;

	/**
	 * 保存基站版本实体
	 * 
	 * @param operObject
	 * @param mcBtsVersion
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void saveOrUpdate(OperObject operObject, McBtsVersion mcBtsVersion)
			throws RemoteException, Exception;

	/**
	 * 下载基站软件
	 * 
	 * @param mo
	 * @param btsId
	 * @param mcBtsVersion
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public int download(OperObject operObject, Mo mo, Long btsId,
			McBtsVersion mcBtsVersion) throws RemoteException, Exception;

	/**
	 * 基于基站的类型查询基站版本
	 * 
	 * @param btsType
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsVersion> queryByBtsType(Integer btsType)
			throws RemoteException, Exception;

	/**
	 * 基于基站ID查询所有这个基站的版本下载记录
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public List<McBtsVersionHistory> queryDownloadHistory(Long btsId)
			throws RemoteException, Exception;

	/**
	 * 获取最后一条任务的状态(时间,状态)
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public McBtsCodeDownloadTask getLatestStatus(Long btsId)
			throws RemoteException, Exception;

	/**
	 * 查询当前正在进行升级的基站及其对应版本号
	 * 
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public Map<Long, String> queryCurrentDownloadTasks()
			throws RemoteException, Exception;

	/**
	 * 删除某个基站下的所有下载记录
	 * 
	 * @param btsId
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public int deleteAllHistory(Long btsId) throws RemoteException, Exception;

	/**
	 * 删除某个历史记录
	 * 
	 * @param history
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public int deleteHistory(McBtsVersionHistory history)
			throws RemoteException, Exception;

	/**
	 * 向基站发送升级请求
	 * 
	 * @throws RemoteException
	 * @throws Exception
	 */
	@Loggable
	public void upgrade(OperObject operObject, Long moId, Integer ho_type)
			throws RemoteException, Exception;

	/**
	 * 向服务器和FTP添加文件
	 * 
	 * @param fileName
	 * @param fileContent
	 * @throws RemoteException
	 * @throws Exception
	 */
	public void add(String fileName, byte[] fileContent)
			throws RemoteException, Exception;

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public boolean deleteFile(String fileName) throws RemoteException,
			Exception;

	/**
	 * enb添加文件的版本(向服务器和FTP添加文件)
	 */
	@Loggable
	public void add(McBtsVersion mcBtsVersion) throws RemoteException,
			Exception;

}
