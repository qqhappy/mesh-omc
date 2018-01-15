/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.service.impl;

import java.io.File;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.McBts;
import com.xinwei.minas.mcbts.core.model.McBtsTypeDD;
import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserAllInfo;
import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserGroup;
import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserInfoConstant;
import com.xinwei.minas.mcbts.core.model.sysManage.Business;
import com.xinwei.minas.server.mcbts.proxy.McBtsBizProxy;
import com.xinwei.minas.server.mcbts.service.McBtsCache;
import com.xinwei.minas.server.mcbts.service.McBtsUserInfoBizService;
import com.xinwei.minas.server.mcbts.service.McBtsUserInfoCache;
import com.xinwei.minas.server.mcbts.utils.FtpClient;
import com.xinwei.minas.server.mcbts.utils.McBtsUserInfoUtil;
import com.xinwei.omp.core.model.biz.GenericBizData;
import com.xinwei.omp.core.model.biz.GenericBizProperty;
import com.xinwei.omp.core.utils.EnvironmentUtils;
import com.xinwei.omp.core.utils.StringUtils;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * 基站用户数据业务服务实现
 * 
 * @author fanhaoyu
 * 
 */

public class McBtsUserInfoBizServiceImpl implements McBtsUserInfoBizService {

	private String ftpServerIp;

	private int ftpServerPort;

	private String ftpUserName;

	private String ftpPassword;

	private String localUserDataPath;

	private String ftpUserDataPath;

	private McBtsBizProxy proxy;

	@Override
	public void add(Long btsId, McBtsUserAllInfo info) throws Exception {
		boolean result = McBtsUserInfoCache.getInstance().addInfo(btsId, info);
		if (!result) {
			throw new Exception(
					OmpAppContext
							.getMessage("McBtsUserInfoBizService.update_cache_failed"));
		}
		// 下发
		configUserInfo(btsId);
	}

	@Override
	public void modify(Long btsId, McBtsUserAllInfo info) throws Exception {
		boolean result = McBtsUserInfoCache.getInstance().modifyInfo(btsId,
				info);
		if (!result) {
			throw new Exception(
					OmpAppContext
							.getMessage("McBtsUserInfoBizService.update_cache_failed"));
		}
		// 下发
		configUserInfo(btsId);
	}

	@Override
	public void delete(Long btsId, Long pid) throws Exception {
		boolean result = McBtsUserInfoCache.getInstance()
				.deleteInfo(btsId, pid);
		if (!result) {
			throw new Exception(
					OmpAppContext
							.getMessage("McBtsUserInfoBizService.update_cache_failed"));
		}
		// 下发
		configUserInfo(btsId);
	}

	@Override
	public void importUserAllInfoList(Long btsId,
			List<McBtsUserAllInfo> infoList) throws Exception {
		McBtsUserInfoCache.getInstance().clearInfo(btsId);
		boolean result = McBtsUserInfoCache.getInstance().addInfoList(btsId,
				infoList);
		if (!result) {
			throw new Exception(
					OmpAppContext
							.getMessage("McBtsUserInfoBizService.update_cache_failed"));
		}
		// 下发
		configUserInfo(btsId);
	}

	@Override
	public List<McBtsUserAllInfo> queryAllInfoFromCache(Long btsId)
			throws Exception {
		return McBtsUserInfoCache.getInstance().getInfoList(btsId);
	}

	@Override
	public List<McBtsUserAllInfo> queryUserInfo(Long btsId) throws Exception {
		// 首先向基站发送请求，然后等待基站回复，基站回复后从FTP服务器下载数据文件（csv）
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		StringBuilder sb = new StringBuilder();
		String remotePath = sb.append(ftpUserDataPath).append(File.separator)
				.append("bts_").append(mcBts.getHexBtsId()).toString();
		// 通知基站上传数据
		config(btsId, remotePath, 1);

		sb = new StringBuilder();
		String localPath = sb.append(localUserDataPath).append(File.separator)
				.append("bts_").append(mcBts.getHexBtsId()).toString();
		File localDir = new File(localPath);
		if (!localDir.exists()) {
			localDir.mkdir();
		}
		// 下载文件
		downloadFileFromFtp(remotePath, localPath);
		// 构建用户数据列表
		List<McBtsUserAllInfo> infoList = McBtsUserInfoUtil
				.getUserAllInfoListFromDir(localPath);
		// 更新缓存
		McBtsUserInfoCache.getInstance().clearInfo(btsId);
		boolean result = McBtsUserInfoCache.getInstance().addInfoList(btsId,
				infoList);
		if (!result) {
			throw new Exception(
					OmpAppContext
							.getMessage("McBtsUserInfoBizService.update_cache_failed"));
		}
		return infoList;
	}

	@Override
	public void configUserInfo(Long btsId) throws Exception {
		// 首先上传数据文件到FTP服务器，然后通知基站
		// 获取本地缓存
		List<McBtsUserAllInfo> infoList = McBtsUserInfoCache.getInstance()
				.getInfoList(btsId);
		// 保存到本地
		String localPath = localUserDataPath + "\\bts_"
				+ StringUtils.to8HexString(btsId);
		List<String> fileList = McBtsUserInfoUtil.saveUserAllInfoOnDisk(
				infoList, localPath);
		// 上传到ftp
		String remotePath = ftpUserDataPath + "\\bts_"
				+ StringUtils.to8HexString(btsId);
		FtpClient.getInstance().mkdir(remotePath, ftpServerIp, ftpServerPort,
				ftpUserName, ftpPassword);
		for (String file : fileList) {
			FtpClient.getInstance().uploadFile(remotePath, file, ftpServerIp,
					ftpServerPort, ftpUserName, ftpPassword);
		}
		// 通知基站下载数据
		config(btsId, remotePath, 0);
	}

	@Override
	public List<McBtsUserGroup> queryAllGroups(Long btsId) throws Exception {
		return McBtsUserInfoCache.getInstance().getGroupList(btsId);
	}

	@Override
	public void syncFromEMSToNE(Long moId) throws Exception {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		if (needToConfig(mcBts)) {
			configUserInfo(mcBts.getBtsId());
		}
	}

	@Override
	public void syncFromNEToEMS(Long moId) throws Exception {
		McBts mcBts = McBtsCache.getInstance().queryByMoId(moId);
		if (needToConfig(mcBts)) {
			queryUserInfo(mcBts.getBtsId());
		}
	}

	/**
	 * 通知基站下载用户数据
	 * 
	 * @param btsId
	 * @param remotePath
	 * @throws Exception
	 */
	private void config(Long btsId, String remotePath, int operateMode)
			throws Exception {
		// 通知基站
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		if (mcBts == null) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		if (mcBts != null && mcBts.isOnlineManage()) {
			if (mcBts.isConnected()) {
				GenericBizData data = new GenericBizData("mcbts_userinfo");
				// 操作模式为通知基站下载数据
				data.addProperty(new GenericBizProperty("operateMode",
						operateMode));
				data.addProperty(new GenericBizProperty("fileDir", remotePath));
				// 发送下载通知
				proxy.query(mcBts.getMoId(), data);
			} else {
				throw new Exception(OmpAppContext.getMessage("bts_unconnected"));
			}
		} else {
			throw new Exception(
					OmpAppContext.getMessage("mcbts_is_offline_manage"));
		}
	}

	private void downloadFileFromFtp(String remotePath, String localPath)
			throws Exception {

		String localStr = localPath + File.separator;
		String remoteStr = remotePath + File.separator;

		// 下载数据文件
		FtpClient.getInstance().fetchFile(
				remoteStr + McBtsUserInfoConstant.USER_INFO_FILE_NAME,
				localStr + McBtsUserInfoConstant.USER_INFO_FILE_NAME,
				ftpServerIp, ftpServerPort, ftpUserName, ftpPassword);
		FtpClient.getInstance().fetchFile(
				remoteStr + McBtsUserInfoConstant.USER_ACL_FILE_NAME,
				localStr + McBtsUserInfoConstant.USER_ACL_FILE_NAME,
				ftpServerIp, ftpServerPort, ftpUserName, ftpPassword);
		FtpClient.getInstance().fetchFile(
				remoteStr + McBtsUserInfoConstant.GROUP_INFO_FILE_NAME,
				localStr + McBtsUserInfoConstant.GROUP_INFO_FILE_NAME,
				ftpServerIp, ftpServerPort, ftpUserName, ftpPassword);
		FtpClient.getInstance().fetchFile(
				remoteStr + McBtsUserInfoConstant.USER_IP_FILE_NAME,
				localStr + McBtsUserInfoConstant.USER_IP_FILE_NAME,
				ftpServerIp, ftpServerPort, ftpUserName, ftpPassword);
		FtpClient.getInstance().fetchFile(
				remoteStr + McBtsUserInfoConstant.GROUP_USER_INFO_FILE_NAME,
				localStr + McBtsUserInfoConstant.GROUP_USER_INFO_FILE_NAME,
				ftpServerIp, ftpServerPort, ftpUserName, ftpPassword);
	}

	/**
	 * 判断是否需要进行用户数据同步业务
	 * 
	 * @param mcBts
	 * @return
	 */
	private boolean needToConfig(McBts mcBts) {
		if (mcBts.getBtsType() != McBtsTypeDD.MICRO_BEEHIVE_MCBTS)
			return false;
		if (mcBts.getWorkMode() != McBts.WORK_MODE_SINGLE)
			return false;
		if (mcBts.getBootSource() != McBts.BOOT_SOURCE_BTS)
			return false;
		if (!EnvironmentUtils.isSimpleMinas())
			return false;
		return true;
	}

	public void setProxy(McBtsBizProxy proxy) {
		this.proxy = proxy;
	}

	public void setFtpServerIp(String ftpServerIp) {
		this.ftpServerIp = ftpServerIp;
	}

	public void setFtpServerPort(int ftpServerPort) {
		this.ftpServerPort = ftpServerPort;
	}

	public String getFtpUserName() {
		return ftpUserName;
	}

	public void setFtpUserName(String ftpUserName) {
		this.ftpUserName = ftpUserName;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public void setLocalUserDataPath(String localUserDataPath) {
		this.localUserDataPath = localUserDataPath;
	}

	public void setFtpUserDataPath(String ftpUserDataPath) {
		this.ftpUserDataPath = ftpUserDataPath;
	}

	@Override
	public void fillExportList(Business business, List<Long> moIdList)
			throws Exception {
		// 不需要实现,群配业务

	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// 不需要实现,群配业务

	}

}