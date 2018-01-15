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
 * ��վ�û�����ҵ�����ʵ��
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
		// �·�
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
		// �·�
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
		// �·�
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
		// �·�
		configUserInfo(btsId);
	}

	@Override
	public List<McBtsUserAllInfo> queryAllInfoFromCache(Long btsId)
			throws Exception {
		return McBtsUserInfoCache.getInstance().getInfoList(btsId);
	}

	@Override
	public List<McBtsUserAllInfo> queryUserInfo(Long btsId) throws Exception {
		// �������վ��������Ȼ��ȴ���վ�ظ�����վ�ظ����FTP���������������ļ���csv��
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		StringBuilder sb = new StringBuilder();
		String remotePath = sb.append(ftpUserDataPath).append(File.separator)
				.append("bts_").append(mcBts.getHexBtsId()).toString();
		// ֪ͨ��վ�ϴ�����
		config(btsId, remotePath, 1);

		sb = new StringBuilder();
		String localPath = sb.append(localUserDataPath).append(File.separator)
				.append("bts_").append(mcBts.getHexBtsId()).toString();
		File localDir = new File(localPath);
		if (!localDir.exists()) {
			localDir.mkdir();
		}
		// �����ļ�
		downloadFileFromFtp(remotePath, localPath);
		// �����û������б�
		List<McBtsUserAllInfo> infoList = McBtsUserInfoUtil
				.getUserAllInfoListFromDir(localPath);
		// ���»���
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
		// �����ϴ������ļ���FTP��������Ȼ��֪ͨ��վ
		// ��ȡ���ػ���
		List<McBtsUserAllInfo> infoList = McBtsUserInfoCache.getInstance()
				.getInfoList(btsId);
		// ���浽����
		String localPath = localUserDataPath + "\\bts_"
				+ StringUtils.to8HexString(btsId);
		List<String> fileList = McBtsUserInfoUtil.saveUserAllInfoOnDisk(
				infoList, localPath);
		// �ϴ���ftp
		String remotePath = ftpUserDataPath + "\\bts_"
				+ StringUtils.to8HexString(btsId);
		FtpClient.getInstance().mkdir(remotePath, ftpServerIp, ftpServerPort,
				ftpUserName, ftpPassword);
		for (String file : fileList) {
			FtpClient.getInstance().uploadFile(remotePath, file, ftpServerIp,
					ftpServerPort, ftpUserName, ftpPassword);
		}
		// ֪ͨ��վ��������
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
	 * ֪ͨ��վ�����û�����
	 * 
	 * @param btsId
	 * @param remotePath
	 * @throws Exception
	 */
	private void config(Long btsId, String remotePath, int operateMode)
			throws Exception {
		// ֪ͨ��վ
		McBts mcBts = McBtsCache.getInstance().queryByBtsId(btsId);
		if (mcBts == null) {
			throw new Exception(OmpAppContext.getMessage("mcbts_not_exist"));
		}
		if (mcBts != null && mcBts.isOnlineManage()) {
			if (mcBts.isConnected()) {
				GenericBizData data = new GenericBizData("mcbts_userinfo");
				// ����ģʽΪ֪ͨ��վ��������
				data.addProperty(new GenericBizProperty("operateMode",
						operateMode));
				data.addProperty(new GenericBizProperty("fileDir", remotePath));
				// ��������֪ͨ
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

		// ���������ļ�
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
	 * �ж��Ƿ���Ҫ�����û�����ͬ��ҵ��
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
		// ����Ҫʵ��,Ⱥ��ҵ��

	}

	@Override
	public void addOrUpdateBusiness(String hexBtsId, Business business)
			throws Exception {
		// ����Ҫʵ��,Ⱥ��ҵ��

	}

}