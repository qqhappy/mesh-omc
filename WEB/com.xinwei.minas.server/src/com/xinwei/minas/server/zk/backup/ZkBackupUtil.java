/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-7-5	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.zk.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.xinwei.minas.zk.core.basic.ZkBackup;
import com.xinwei.minas.zk.core.basic.ZkBackupConstant;
import com.xinwei.minas.zk.core.xnode.common.ZkNode;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * NK数据备份助手
 * 
 * @author fanhaoyu
 * 
 */

public class ZkBackupUtil {

	/**
	 * 备份丢失
	 */
	public static final byte ERROR_CODE_BACKUP_MISSING = -1;

	/**
	 * 删除失败
	 */
	public static final byte ERROR_CODE_DEL_FAILED = 0;

	/**
	 * 删除成功
	 */
	public static final byte DEL_SUCCESS = 1;

	private static boolean isBackupDirExist() {
		return ZkBackupConstant.BACKUP_DIR.exists();
	}

	public static boolean createBackupDir() {
		return ZkBackupConstant.BACKUP_DIR.mkdir();
	}

	/**
	 * 计算备份所占空间大小
	 * 
	 * @param dir
	 * @return
	 * @throws Exception
	 */
	public static double calBackupSpaceSize(File dir) throws Exception {
		double size = 0;
		if (dir.exists()) {
			for (File file : dir.listFiles()) {
				if (file.isDirectory()) {
					size += calBackupSpaceSize(file);
				} else if (file.isFile()) {
					size += getFileSize(file);
				}
			}
		}
		return size;
	}

	/**
	 * 获取本地磁盘上的所有备份
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<ZkBackup> getBackupsOnDisk() throws Exception {
		if (!isBackupDirExist()) {
			if (!createBackupDir()) {
				return null;
			}
		}
		List<ZkBackup> backupList = new LinkedList<ZkBackup>();
		// 获取备份目录中所有文件
		File[] subDirs = ZkBackupConstant.BACKUP_DIR.listFiles();
		for (File subDir : subDirs) {
			// 如果是目录，则创建一个备份
			if (subDir.isDirectory()) {
				ZkBackup backup = new ZkBackup();
				backup.setName(subDir.getName());
				backup.setCreateTime(new Date(subDir.lastModified()));
				double size = calBackupSpaceSize(subDir);
				backup.setSpaceSize(size);
				// 获取sagGroup节点数据文件名列表
				List<String> fileList = new LinkedList<String>();
				for (String file : subDir.list()) {
					fileList.add(file);
				}
				backup.setFileList(fileList);
				backupList.add(backup);
			}
		}
		return backupList;
	}

	/**
	 * 在本地磁盘上创建备份
	 * 
	 * @param backupName
	 * @param sagRoot
	 * @return
	 * @throws Exception
	 */
	public static ZkBackup createBackupOnDisk(String backupName, ZkNode sagRoot)
			throws Exception {
		// 如果备份根目录不存在，则创建备份根目录，如果创建失败，则备份同样创建失败，返回null
		if (!isBackupDirExist()) {
			if (!createBackupDir()) {
				return null;
			}
		}

		ZkBackup backup = new ZkBackup();
		backup.setName(backupName);
		backup.setCreateTime(new Date());
		String dir = new StringBuffer()
				.append(ZkBackupConstant.BACKUP_DIR_NAME)
				.append(File.separator).append(backupName).toString();
		File newBackupDir = new File(dir);
		// 如果路径已存在，则先删除，因为此时已有的备份系统中无记录，有可能是客户无意间创建的
		if (newBackupDir.exists()) {
			newBackupDir.delete();
		}
		// 创建新建备份文件夹
		if (newBackupDir.mkdir()) {
			Set<ZkNode> sagGroups = sagRoot.getChildren();
			for (ZkNode sagGroup : sagGroups) {
				String fileName = sagGroup.getName()
						+ ZkBackupExcelUtil.EXCEL_POSTFIX;
				String filePath = dir + File.separator + fileName;
				ZkBackupExcelUtil.createNewXLSFile(filePath);
				ZkBackupExcelUtil backupExcelUtil = new ZkBackupExcelUtil(
						sagGroup, filePath);
				backupExcelUtil.exportSagGroup();
				backup.addFilePath(fileName);
			}
		} else {
			throw new IOException(MessageFormat.format(
					OmpAppContext.getMessage("create_backup_failed"),
					MessageFormat.format(OmpAppContext.getMessage(""), dir)));
		}
		double size = calBackupSpaceSize(newBackupDir);
		backup.setSpaceSize(size);
		return backup;
	}

	/**
	 * 删除本地磁盘上的指定备份
	 * 
	 * @param backup
	 * @return
	 * @throws Exception
	 */
	public static byte deleteBackupOnDisk(ZkBackup backup) throws Exception {
		String dirPath = new StringBuffer()
				.append(ZkBackupConstant.BACKUP_DIR_NAME)
				.append(File.separator).append(backup.getName()).toString();
		File backupDir = new File(dirPath);
		// 备份文件丢失
		if (!backupDir.exists()) {
			return ERROR_CODE_BACKUP_MISSING;
		}
		boolean ok = false;
		for (File file : backupDir.listFiles()) {
			ok = file.delete();
			// 删除失败
			if (!ok) {
				return ERROR_CODE_DEL_FAILED;
			}
			backup.getFileList().remove(file.getName());
		}
		// 备份文件夹删除失败
		if (!backupDir.delete()) {
			return ERROR_CODE_DEL_FAILED;
		}
		return DEL_SUCCESS;
	}

	/**
	 * 获取指定备份中的SAG群组数据列表
	 * 
	 * @param backup
	 * @return
	 * @throws Exception
	 */
	public static List<ZkNode> getSagGroupsofBackup(ZkBackup backup)
			throws Exception {
		List<ZkNode> sagGroupList = new LinkedList<ZkNode>();
		String filePath = new StringBuffer()
				.append(ZkBackupConstant.BACKUP_DIR_NAME)
				.append(File.separator).append(backup.getName())
				.append(File.separator).toString();
		for (String fileName : backup.getFileList()) {
			// 依次导入sagGroup
			ZkBackupExcelUtil excelUtil = new ZkBackupExcelUtil(filePath
					+ fileName);
			ZkNode sagGroup = excelUtil.importSagGroup();
			sagGroupList.add(sagGroup);
		}
		return sagGroupList;
	}

	/**
	 * 获取备份文件的字节流
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static byte[] getBackupFileBytes(String filePath) throws IOException {
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(filePath);
			int fileLength = fileIn.available();
			byte[] fileBytes = new byte[fileLength];
			fileIn.read(fileBytes);
			return fileBytes;
		} catch (IOException e) {
			throw e;
		} finally {
			if (fileIn != null) {
				fileIn.close();
			}
		}
	}

	/**
	 * 把备份任务执行间隔字符串转换为具体时间，单位毫秒</br> intvalStr格式为“时间-单位”</br>
	 * 单位：m代表分钟，h代表小时，d代表天
	 * 
	 * @param intvalStr
	 *            执行间隔字符串
	 * @return
	 */
	public static long getInterval(String intvalStr) {
		String[] str = intvalStr.split("-");
		int number = Integer.valueOf(str[0]);
		long unit = 0;
		if (str[1].equals("m"))
			unit = ZkBackupConstant.ONE_MIN;
		else if (str[1].equals("h"))
			unit = ZkBackupConstant.ONE_HOUR;
		else if (str[1].equals("d"))
			unit = ZkBackupConstant.ONE_DAY;
		return number * unit;
	}

	/**
	 * 获取单个文件所占空间大小
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private static double getFileSize(File file) throws Exception {
		FileInputStream fileIn = null;
		try {
			fileIn = new FileInputStream(file);
			double size = fileIn.available();
			return size;
		} catch (Exception e) {
			throw e;
		} finally {
			if (fileIn != null) {
				fileIn.close();
			}
		}
	}
}