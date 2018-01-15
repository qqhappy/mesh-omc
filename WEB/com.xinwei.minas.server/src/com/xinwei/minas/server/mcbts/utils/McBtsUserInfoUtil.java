/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-8-13	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.xinwei.minas.mcbts.core.model.simplenms.McBtsGroupUserInfo;
import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserACL;
import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserAllInfo;
import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserGroup;
import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserInfo;
import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserInfoConstant;
import com.xinwei.minas.mcbts.core.model.simplenms.McBtsUserIpInfo;

/**
 * 
 * 基站用户信息CSV解析助手
 * 
 * @author fanhaoyu
 * 
 */

public class McBtsUserInfoUtil {

	public static final String SEPARATOR = ",";

	/**
	 * 从目录中获取用户的所有信息列表
	 * 
	 * @param dirName
	 * @return
	 * @throws Exception
	 */
	public static List<McBtsUserAllInfo> getUserAllInfoListFromDir(
			String dirName) throws Exception {

		String str = dirName + File.separator;

		List<McBtsUserInfo> userInfoList = getUserInfoListFromFile(str
				+ McBtsUserInfoConstant.USER_INFO_FILE_NAME);

		if (userInfoList == null || userInfoList.isEmpty()) {
			return Collections.emptyList();
		}
		List<McBtsUserIpInfo> ipInfoList = getUserIpInfoListFromFile(str
				+ McBtsUserInfoConstant.USER_IP_FILE_NAME);

		List<McBtsUserGroup> groupList = getUserGroupListFromFile(str
				+ McBtsUserInfoConstant.GROUP_INFO_FILE_NAME);

		List<McBtsGroupUserInfo> groupUserInfoList = getGroupUserInfoListFromFile(str
				+ McBtsUserInfoConstant.GROUP_USER_INFO_FILE_NAME);

		List<McBtsUserAllInfo> userAllInfoList = new LinkedList<McBtsUserAllInfo>();

		for (int i = 0; i < userInfoList.size(); i++) {
			McBtsUserInfo userInfo = userInfoList.get(i);
			McBtsUserIpInfo ipInfo = ipInfoList.get(i);
			McBtsGroupUserInfo groupUserInfo = groupUserInfoList.get(i);

			McBtsUserAllInfo allInfo = new McBtsUserAllInfo();
			allInfo.setPid(userInfo.getPid());
			allInfo.setUid(userInfo.getUid());
			allInfo.setTelNo(userInfo.getTelNo());
			allInfo.setPriority(userInfo.getPriority());
			allInfo.setIp(ipInfo.getIp());
			allInfo.setMac(ipInfo.getMac());
			allInfo.setGroupId(groupUserInfo.getGroupId());
			allInfo.setPriorInGroup(groupUserInfo.getPriorInGroup());
			userAllInfoList.add(allInfo);
		}

		for (McBtsUserAllInfo allInfo : userAllInfoList) {
			for (McBtsUserGroup groupInfo : groupList) {
				if (allInfo.getGroupId().equals(groupInfo.getGroupId())) {
					allInfo.setGroupName(groupInfo.getGroupName());
					allInfo.setGroupPrior(groupInfo.getGroupPrior());
					break;
				}
			}
		}
		return userAllInfoList;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static List<McBtsUserInfo> getUserInfoListFromFile(String fileName)
			throws Exception {
		File userInfoFile = new File(fileName);
		if (!userInfoFile.exists()) {
			throw new Exception("McBtsUserInfoUtil.file_missing" + fileName);
		}
		if (!userInfoFile.isFile()) {
			throw new Exception("McBtsUserInfoUtil.invalid_csv_file" + fileName);
		}
		FileInputStream stream = null;
		InputStreamReader reader = null;
		BufferedReader bufReader = null;
		try {
			stream = new FileInputStream(userInfoFile);
			if (stream.available() <= 0) {
				stream.close();
				return Collections.emptyList();
			}

			reader = new InputStreamReader(stream);
			bufReader = new BufferedReader(reader);
			List<McBtsUserInfo> userInfoList = new LinkedList<McBtsUserInfo>();
			while (true) {
				String str = bufReader.readLine();
				if (str == null) {
					break;
				}
				String[] values = str.split(",");
				McBtsUserInfo info = new McBtsUserInfo();
				for (int i = 0; i < values.length; i++) {
					switch (i) {
					case 0:
						info.setPid(Long.valueOf(values[i]));
						break;
					case 1:
						info.setUid(Long.valueOf(values[i]));
						break;
					case 2:
						info.setTelNo(values[i]);
						break;
					case 3:
						info.setPriority(Integer.valueOf(values[i]));
						break;
					default:
						break;
					}
				}
				userInfoList.add(info);
			}
			return userInfoList;
		} catch (Exception e) {
			throw e;
		} finally {
			if (stream != null)
				stream.close();
			if (reader != null)
				reader.close();
			if (bufReader != null)
				bufReader.close();
		}
	}

	/**
	 * 获取用户组信息
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static List<McBtsUserGroup> getUserGroupListFromFile(String fileName)
			throws Exception {
		File groupFile = new File(fileName);
		if (!groupFile.exists()) {
			throw new Exception("McBtsUserInfoUtil.file_missing" + fileName);
		}
		if (!groupFile.isFile()) {
			throw new Exception("McBtsUserInfoUtil.not_valid_csv_file"
					+ fileName);
		}

		FileInputStream stream = null;
		InputStreamReader reader = null;
		BufferedReader bufReader = null;
		try {
			stream = new FileInputStream(groupFile);
			if (stream.available() <= 0) {
				stream.close();
				return Collections.emptyList();
			}

			reader = new InputStreamReader(stream);
			bufReader = new BufferedReader(reader);
			List<McBtsUserGroup> groupList = new LinkedList<McBtsUserGroup>();
			while (true) {
				String str = bufReader.readLine();
				if (str == null) {
					break;
				}
				String[] values = str.split(",");
				McBtsUserGroup group = new McBtsUserGroup();
				for (int i = 0; i < values.length; i++) {
					switch (i) {
					case 0:
						group.setGroupId(Long.valueOf(values[i]));
						break;
					case 1:
						group.setGroupName(values[i]);
						break;
					case 2:
						group.setGroupPrior(Integer.valueOf(values[i]));
						break;
					default:
						break;
					}
				}
				groupList.add(group);
			}
			return groupList;
		} catch (Exception e) {
			throw e;
		} finally {
			if (stream != null)
				stream.close();
			if (reader != null)
				reader.close();
			if (bufReader != null)
				bufReader.close();
		}
	}

	/**
	 * 获取组用户信息
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static List<McBtsGroupUserInfo> getGroupUserInfoListFromFile(
			String fileName) throws Exception {
		File infoFile = new File(fileName);
		if (!infoFile.exists()) {
			throw new Exception("McBtsUserInfoUtil.file_missing" + fileName);
		}
		if (!infoFile.isFile()) {
			throw new Exception("McBtsUserInfoUtil.not_valid_csv_file"
					+ fileName);
		}

		FileInputStream stream = null;
		InputStreamReader reader = null;
		BufferedReader bufReader = null;
		try {
			stream = new FileInputStream(infoFile);
			if (stream.available() <= 0) {
				stream.close();
				return Collections.emptyList();
			}

			reader = new InputStreamReader(stream);
			bufReader = new BufferedReader(reader);
			List<McBtsGroupUserInfo> infoList = new LinkedList<McBtsGroupUserInfo>();
			while (true) {
				String str = bufReader.readLine();
				if (str == null) {
					break;
				}
				String[] values = str.split(",");
				McBtsGroupUserInfo info = new McBtsGroupUserInfo();
				for (int i = 0; i < values.length; i++) {
					switch (i) {
					case 0:
						info.setGroupId(Long.valueOf(values[i]));
						break;
					case 1:
						info.setUid(Long.valueOf(values[i]));
						break;
					case 2:
						info.setPriorInGroup(Integer.valueOf(values[i]));
						break;
					default:
						break;
					}
				}
				infoList.add(info);
			}
			return infoList;
		} catch (Exception e) {
			throw e;
		} finally {
			if (stream != null)
				stream.close();
			if (reader != null)
				reader.close();
			if (bufReader != null)
				bufReader.close();
		}
	}

	/**
	 * 获取用户IP信息
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static List<McBtsUserIpInfo> getUserIpInfoListFromFile(
			String fileName) throws Exception {
		File infoFile = new File(fileName);
		if (!infoFile.exists()) {
			throw new Exception("McBtsUserInfoUtil.file_missing" + fileName);
		}
		if (!infoFile.isFile()) {
			throw new Exception("McBtsUserInfoUtil.not_valid_csv_file"
					+ fileName);
		}

		FileInputStream stream = null;
		InputStreamReader reader = null;
		BufferedReader bufReader = null;
		try {
			stream = new FileInputStream(infoFile);
			if (stream.available() <= 0) {
				stream.close();
				return Collections.emptyList();
			}

			reader = new InputStreamReader(stream);
			bufReader = new BufferedReader(reader);
			List<McBtsUserIpInfo> infoList = new LinkedList<McBtsUserIpInfo>();
			while (true) {
				String str = bufReader.readLine();
				if (str == null) {
					break;
				}
				String[] values = str.split(",");
				McBtsUserIpInfo info = new McBtsUserIpInfo();
				for (int i = 0; i < values.length; i++) {
					switch (i) {
					case 0:
						info.setPid(Long.valueOf(values[i]));
						break;
					case 1:
						info.setIp(values[i]);
						break;
					case 2:
						info.setMac(values[i]);
						break;
					default:
						break;
					}
				}
				infoList.add(info);
			}
			return infoList;
		} catch (Exception e) {
			throw e;
		} finally {
			if (stream != null)
				stream.close();
			if (reader != null)
				reader.close();
			if (bufReader != null)
				bufReader.close();
		}
	}

	/**
	 * 把用户所有信息分五个文件进行存储
	 * 
	 * @param allInfoList
	 * @param dirName
	 * @throws Exception
	 */
	public static List<String> saveUserAllInfoOnDisk(
			List<McBtsUserAllInfo> allInfoList, String dirName)
			throws Exception {
		List<String> fileList = new LinkedList<String>();
		// 从列表中拆出五个列表
		List<McBtsUserInfo> userInfoList = new LinkedList<McBtsUserInfo>();
		List<McBtsUserIpInfo> ipInfoList = new LinkedList<McBtsUserIpInfo>();
		List<McBtsUserACL> aclInfoList = new LinkedList<McBtsUserACL>();
		List<McBtsUserGroup> groupList = new LinkedList<McBtsUserGroup>();
		List<McBtsGroupUserInfo> groupUserList = new LinkedList<McBtsGroupUserInfo>();
		for (McBtsUserAllInfo allInfo : allInfoList) {
			McBtsUserInfo userInfo = new McBtsUserInfo();
			userInfo.setPid(allInfo.getPid());
			userInfo.setUid(allInfo.getUid());
			userInfo.setTelNo(allInfo.getTelNo());
			userInfo.setPriority(allInfo.getPriority());
			userInfoList.add(userInfo);
		}
		for (McBtsUserAllInfo allInfo : allInfoList) {
			McBtsUserACL aclInfo = new McBtsUserACL();
			aclInfo.setPid(allInfo.getPid());
			aclInfo.setUid(allInfo.getUid());
			aclInfoList.add(aclInfo);
		}
		for (McBtsUserAllInfo allInfo : allInfoList) {
			McBtsGroupUserInfo userInfo = new McBtsGroupUserInfo();
			userInfo.setUid(allInfo.getUid());
			userInfo.setGroupId(allInfo.getGroupId());
			userInfo.setPriorInGroup(allInfo.getPriorInGroup());
			groupUserList.add(userInfo);
		}
		for (McBtsUserAllInfo allInfo : allInfoList) {
			McBtsUserIpInfo ipInfo = new McBtsUserIpInfo();
			ipInfo.setPid(allInfo.getPid());
			ipInfo.setIp(allInfo.getIp());
			ipInfo.setMac(allInfo.getMac());
			ipInfoList.add(ipInfo);
		}
		for (McBtsUserAllInfo allInfo : allInfoList) {
			boolean repeat = false;
			for (McBtsUserGroup userGroup : groupList) {
				if (allInfo.getGroupId().equals(userGroup.getGroupId())) {
					repeat = true;
					break;
				}
			}
			if (!repeat) {
				McBtsUserGroup group = new McBtsUserGroup();
				group.setGroupId(allInfo.getGroupId());
				group.setGroupName(allInfo.getGroupName());
				group.setGroupPrior(allInfo.getGroupPrior());
				groupList.add(group);
			}
		}
		File dir = new File(dirName);
		dir.mkdir();

		String str = dirName + File.separator;

		String userInfoFile = str + McBtsUserInfoConstant.USER_INFO_FILE_NAME;
		saveUserInfos(userInfoList, userInfoFile);
		fileList.add(userInfoFile);

		String aclInfoFile = str + McBtsUserInfoConstant.USER_ACL_FILE_NAME;
		saveUserACLs(aclInfoList, aclInfoFile);
		fileList.add(aclInfoFile);

		String userGroupFile = str + McBtsUserInfoConstant.GROUP_INFO_FILE_NAME;
		saveUserGroups(groupList, userGroupFile);
		fileList.add(userGroupFile);

		String ipInfoFile = str + McBtsUserInfoConstant.USER_IP_FILE_NAME;
		saveUserIpInfos(ipInfoList, ipInfoFile);
		fileList.add(ipInfoFile);

		String groupUserFile = str
				+ McBtsUserInfoConstant.GROUP_USER_INFO_FILE_NAME;
		saveGroupUserInfos(groupUserList, groupUserFile);
		fileList.add(groupUserFile);
		return fileList;
	}

	public static void saveUserIpInfos(List<McBtsUserIpInfo> infoList,
			String savePath) throws Exception {
		File userIpFile = new File(savePath);
		if (userIpFile.exists()) {
			userIpFile.delete();
		}
		userIpFile.createNewFile();
		FileOutputStream stream = null;
		OutputStreamWriter writer = null;
		try {
			stream = new FileOutputStream(userIpFile);
			writer = new OutputStreamWriter(stream);
			for (McBtsUserIpInfo info : infoList) {
				writer.write(info.getPid() + SEPARATOR + info.getIp()
						+ SEPARATOR + info.getMac() + "\r\n");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
			if (stream != null)
				stream.close();
		}

	}

	public static void saveUserInfos(List<McBtsUserInfo> infoList,
			String savePath) throws Exception {
		File userIpFile = new File(savePath);
		if (userIpFile.exists()) {
			userIpFile.delete();
		}
		userIpFile.createNewFile();
		FileOutputStream stream = null;
		OutputStreamWriter writer = null;
		try {
			stream = new FileOutputStream(userIpFile);
			writer = new OutputStreamWriter(stream);
			for (McBtsUserInfo info : infoList) {
				writer.write(info.getPid() + SEPARATOR + info.getUid()
						+ SEPARATOR + info.getTelNo() + SEPARATOR
						+ info.getPriority() + "\r\n");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
			if (stream != null)
				stream.close();
		}
	}

	public static void saveUserGroups(List<McBtsUserGroup> groupList,
			String savePath) throws Exception {
		File userIpFile = new File(savePath);
		if (userIpFile.exists()) {
			userIpFile.delete();
		}
		userIpFile.createNewFile();
		FileOutputStream stream = null;
		OutputStreamWriter writer = null;
		try {
			stream = new FileOutputStream(userIpFile);
			writer = new OutputStreamWriter(stream);
			for (McBtsUserGroup group : groupList) {
				writer.write(group.getGroupId() + SEPARATOR
						+ group.getGroupName() + SEPARATOR
						+ group.getGroupPrior() + "\r\n");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
			if (stream != null)
				stream.close();
		}
	}

	public static void saveGroupUserInfos(List<McBtsGroupUserInfo> infoList,
			String savePath) throws Exception {
		File userIpFile = new File(savePath);
		if (userIpFile.exists()) {
			userIpFile.delete();
		}
		userIpFile.createNewFile();
		FileOutputStream stream = null;
		OutputStreamWriter writer = null;
		try {
			stream = new FileOutputStream(userIpFile);
			writer = new OutputStreamWriter(stream);
			for (McBtsGroupUserInfo info : infoList) {
				writer.write(info.getGroupId() + SEPARATOR + info.getUid()
						+ SEPARATOR + info.getPriorInGroup() + "\r\n");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
			if (stream != null)
				stream.close();
		}
	}

	public static void saveUserACLs(List<McBtsUserACL> aclList, String savePath)
			throws Exception {
		File userIpFile = new File(savePath);
		if (userIpFile.exists()) {
			userIpFile.delete();
		}
		userIpFile.createNewFile();
		FileOutputStream stream = null;
		OutputStreamWriter writer = null;
		try {
			stream = new FileOutputStream(userIpFile);
			writer = new OutputStreamWriter(stream);
			for (McBtsUserACL acl : aclList) {
				writer.write(acl.getPid() + SEPARATOR + acl.getUid() + "\r\n");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
			if (stream != null)
				stream.close();
		}
	}

	/**
	 * 判断一个文件是否是一个有效的csv文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean isValidCSVFile(String filePath) {
		int index = filePath.lastIndexOf(".");
		if (index < 0)
			return false;
		String postfix = filePath.substring(index);
		if (!postfix.toLowerCase().equals(McBtsUserInfoConstant.FILE_POSTFIX))
			return false;
		return true;
	}
}