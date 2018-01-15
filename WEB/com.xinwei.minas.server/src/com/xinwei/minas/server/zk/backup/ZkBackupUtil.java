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
 * NK���ݱ�������
 * 
 * @author fanhaoyu
 * 
 */

public class ZkBackupUtil {

	/**
	 * ���ݶ�ʧ
	 */
	public static final byte ERROR_CODE_BACKUP_MISSING = -1;

	/**
	 * ɾ��ʧ��
	 */
	public static final byte ERROR_CODE_DEL_FAILED = 0;

	/**
	 * ɾ���ɹ�
	 */
	public static final byte DEL_SUCCESS = 1;

	private static boolean isBackupDirExist() {
		return ZkBackupConstant.BACKUP_DIR.exists();
	}

	public static boolean createBackupDir() {
		return ZkBackupConstant.BACKUP_DIR.mkdir();
	}

	/**
	 * ���㱸����ռ�ռ��С
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
	 * ��ȡ���ش����ϵ����б���
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
		// ��ȡ����Ŀ¼�������ļ�
		File[] subDirs = ZkBackupConstant.BACKUP_DIR.listFiles();
		for (File subDir : subDirs) {
			// �����Ŀ¼���򴴽�һ������
			if (subDir.isDirectory()) {
				ZkBackup backup = new ZkBackup();
				backup.setName(subDir.getName());
				backup.setCreateTime(new Date(subDir.lastModified()));
				double size = calBackupSpaceSize(subDir);
				backup.setSpaceSize(size);
				// ��ȡsagGroup�ڵ������ļ����б�
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
	 * �ڱ��ش����ϴ�������
	 * 
	 * @param backupName
	 * @param sagRoot
	 * @return
	 * @throws Exception
	 */
	public static ZkBackup createBackupOnDisk(String backupName, ZkNode sagRoot)
			throws Exception {
		// ������ݸ�Ŀ¼�����ڣ��򴴽����ݸ�Ŀ¼���������ʧ�ܣ��򱸷�ͬ������ʧ�ܣ�����null
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
		// ���·���Ѵ��ڣ�����ɾ������Ϊ��ʱ���еı���ϵͳ���޼�¼���п����ǿͻ�����䴴����
		if (newBackupDir.exists()) {
			newBackupDir.delete();
		}
		// �����½������ļ���
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
	 * ɾ�����ش����ϵ�ָ������
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
		// �����ļ���ʧ
		if (!backupDir.exists()) {
			return ERROR_CODE_BACKUP_MISSING;
		}
		boolean ok = false;
		for (File file : backupDir.listFiles()) {
			ok = file.delete();
			// ɾ��ʧ��
			if (!ok) {
				return ERROR_CODE_DEL_FAILED;
			}
			backup.getFileList().remove(file.getName());
		}
		// �����ļ���ɾ��ʧ��
		if (!backupDir.delete()) {
			return ERROR_CODE_DEL_FAILED;
		}
		return DEL_SUCCESS;
	}

	/**
	 * ��ȡָ�������е�SAGȺ�������б�
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
			// ���ε���sagGroup
			ZkBackupExcelUtil excelUtil = new ZkBackupExcelUtil(filePath
					+ fileName);
			ZkNode sagGroup = excelUtil.importSagGroup();
			sagGroupList.add(sagGroup);
		}
		return sagGroupList;
	}

	/**
	 * ��ȡ�����ļ����ֽ���
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
	 * �ѱ�������ִ�м���ַ���ת��Ϊ����ʱ�䣬��λ����</br> intvalStr��ʽΪ��ʱ��-��λ��</br>
	 * ��λ��m������ӣ�h����Сʱ��d������
	 * 
	 * @param intvalStr
	 *            ִ�м���ַ���
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
	 * ��ȡ�����ļ���ռ�ռ��С
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