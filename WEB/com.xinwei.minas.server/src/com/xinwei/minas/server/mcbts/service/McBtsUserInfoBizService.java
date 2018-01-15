/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service;

import java.util.List;

import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserAllInfo;
import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserGroup;

/**
 * 
 * 基站用户数据业务门面
 * 
 * @author fanhaoyu
 * 
 */

public interface McBtsUserInfoBizService extends ICustomService {

	/**
	 * 增加一条用户数据信息
	 * 
	 * @param btsId
	 * @param info
	 * @throws Exception
	 */
	public void add(Long btsId, McBtsUserAllInfo info) throws Exception;

	/**
	 * 修改一条用户数据信息
	 * 
	 * @param btsId
	 * @param info
	 * @throws Exception
	 */
	public void modify(Long btsId, McBtsUserAllInfo info) throws Exception;

	/**
	 * 删除一条用户数据信息
	 * 
	 * @param btsId
	 * @param pid
	 * @throws Exception
	 */
	public void delete(Long btsId, Long pid) throws Exception;

	/**
	 * 导入基站用户数据列表
	 * 
	 * @param btsId
	 * @param infoList
	 * @throws Exception
	 */
	public void importUserAllInfoList(Long btsId,
			List<McBtsUserAllInfo> infoList) throws Exception;

	/**
	 * 从缓存所有用户数据信息
	 * 
	 * @param btsId
	 * @return
	 * @throws Exception
	 */
	public List<McBtsUserAllInfo> queryAllInfoFromCache(Long btsId)
			throws Exception;

	/**
	 * 向基站发送用户数据查询请求，等待基站上传数据到FTP服务器，然后从FTP服务器下载用户数据文件
	 * 
	 * @param btsId
	 * @return
	 * @throws Exception
	 */
	public List<McBtsUserAllInfo> queryUserInfo(Long btsId) throws Exception;

	/**
	 * 配置用户数据，将用户数据文件上传到FTP服务器，然后通知基站下载
	 * 
	 * @param btsId
	 * @throws Exception
	 */
	public void configUserInfo(Long btsId) throws Exception;

	/**
	 * 查询指定基站的用户组列表
	 * 
	 * @param btsId
	 * @return
	 * @throws Exception
	 */
	public List<McBtsUserGroup> queryAllGroups(Long btsId) throws Exception;
}