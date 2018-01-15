/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-5-14	| zhuxiaozhan 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.FullTableConfigInfo;
import com.xinwei.minas.server.enb.helper.EnbAlarmHelper;
import com.xinwei.minas.server.enb.net.EnbAppMessage;
import com.xinwei.minas.server.enb.net.EnbMessageConstants;
import com.xinwei.minas.server.enb.net.TagConst;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.enb.service.EnbFullTableConfigService;
import com.xinwei.minas.server.mcbts.utils.FtpClient;
import com.xinwei.minas.server.platform.AppContext;
import com.xinwei.omp.server.OmpAppContext;

/**
 * 
 * Enb����ҵ�����������
 * 
 * 
 * @author zhuxiaozhan
 * 
 */

public class EnbFullTableTaskManager {

	private Log log = LogFactory.getLog(EnbFullTableTaskManager.class);

	public static final String CONFIG_SUCCESS_FLAG = "success";

	// FTP��ַ
	private String ftpServerIp;

	// FTP�û���
	private String ftpUsername;

	// FTP����
	private String ftpPassword;

	// ��������ftpĿ¼
	private String configFtpdDrectory;

	// ��������localĿ¼
	private String configLocalDirectory;

	// ������ftpĿ¼
	private String reverseFtpdDrectory;

	// ������localĿ¼
	private String reverseLocalDirectory;

	private int overTime;

	// FTP�˿�
	private int ftpPort;

	private static final EnbFullTableTaskManager instance = new EnbFullTableTaskManager();

	// �����������񻺴�
	private static final Map<Long, FutureResult> fullTableConfigTasks = new ConcurrentHashMap<Long, FutureResult>();

	// ���������񻺴�
	private static final Map<Long, FutureResult> fullTableReverseTasks = new ConcurrentHashMap<Long, FutureResult>();

	// ��������Ӧ���ļ�����
	private static final Map<Long, String> reverseTaskData = new ConcurrentHashMap<Long, String>();

	private EnbFullTableTaskManager() {

	}

	public static EnbFullTableTaskManager getInstance() {
		return instance;
	}

	/**
	 * �����������ý��֪ͨ
	 * 
	 * @param message
	 */
	public void handleFullTableConfigResultNotify(EnbAppMessage message) {
		FullTableConfigInfo result = new FullTableConfigInfo();
		long enbId = message.getEnbId();
		FutureResult task = fullTableConfigTasks.get(enbId);
		if (task == null) {
			return;
		}
		if (message.isSuccessful()) {
			result.setConfigStatus(FullTableConfigInfo.CONFIG_SUCCESS);
		} else {
			result.setConfigStatus(FullTableConfigInfo.CONFIG_FAIL);
			// ��������ԭ���error code
			result.setErrorMessage(OmpAppContext.getMessage(
					"full_table_config_result_error",
					new Object[] { message.getStringValue(TagConst.ERR_MSG),
							message.getResult() }));
		}
		task.set(result);
	}

	/**
	 * �������������֪ͨ
	 * 
	 * @param message
	 */
	public void handleFullTableReverseResultNotify(EnbAppMessage message) {
		long enbId = message.getEnbId();
		FutureResult task = fullTableReverseTasks.get(enbId);
		if (task == null) {
			return;
		}
		if (message.isSuccessful()) {
			task.set(CONFIG_SUCCESS_FLAG);
			// ���������ļ�
			processReverseResult(enbId);
		} else {
			// ��������ԭ���error code
			task.set(OmpAppContext.getMessage(
					"full_table_reverse_result_error",
					new Object[] { message.getStringValue(TagConst.ERR_MSG),
							message.getResult() }));
		}
	}

	public void processReverseResult(long enbId) {
		Enb enb = EnbCache.getInstance().queryByEnbId(enbId);
		if (enb != null) {
			EnbAlarmHelper enbAlarmHelper = OmpAppContext.getCtx().getBean(
					EnbAlarmHelper.class);
			try {
				Long moId = enb.getMoId();
				EnbFullTableConfigService service = AppContext.getCtx()
						.getBean(EnbFullTableConfigService.class);
				// ����EMS��enb��ǰ��ҵ���������
				Map<String, List<String>> emsSqlMap = service
						.generateFullTableSql(moId, enb.getProtocolVersion());

				// ��ftp�ϻ�ȡ���ɵķ������ļ�
				String currentLocalD = getReverseLocalDirectory()
						+ File.separator + enb.getHexEnbId();
				String fileName = reverseTaskData.get(enbId);
				fetchFileFromFtp(getReverseFtpdDrectory(), currentLocalD,
						fileName);
				File reverseFile = new File(currentLocalD + File.separator
						+ fileName);
				ArrayList<String> alarm = new ArrayList<String>();
				if (reverseFile.exists()) {
					BufferedReader reader = null;
					try {
						reader = new BufferedReader(new FileReader(reverseFile));
						String sql = null;
						while ((sql = reader.readLine()) != null) {
							// ��ȡ����
							String tableName = getTableName(sql);
							// ���ݱ����ƻ�ȡEMS�϶�Ӧ����������Ϣ,���EMS�ϲ�������������Ϣ����˵��������Ϣ��ͬ�������澯
							List<String> emsSqlList = emsSqlMap.get(tableName);
							boolean alarmFlag = false;
							if (emsSqlList == null) {
								alarmFlag = true;
							} else {
								// ��ΪEMS�����ɵ�sql����б�����û�д�˫���ţ�����Ҫȥ��
								String enbSqlValues = getTableValues(sql);
								boolean findFlag = false;
								for (String emsSql : emsSqlList) {
									String emsSqlValues = getTableValues(emsSql);
									if (emsSqlValues.equals(enbSqlValues)) {
										findFlag = true;
										break;
									}
								}
								// û���ҵ���Ӧ��sql�������ĸ澯��ʶ��
								alarmFlag = !findFlag;
							}
							if (alarmFlag) {
								if (!alarm.contains(tableName)) {
									alarm.add(tableName);
								}
							}
						}

					} finally {
						if (reader != null) {
							reader.close();
						}
					}

					// ����и澯�������ɸ澯����
					if (alarm.size() != 0) {
						StringBuilder alarmDesc = new StringBuilder();
						for (String tn : alarm) {
							if (alarmDesc.length() != 0) {
								alarmDesc.append(",");
							}
							alarmDesc.append(tn);
						}
						enbAlarmHelper.fireFullTableReverseAlarm(enb,
								alarmDesc.toString());

					} else {
						// ���и澯�ָ�
						enbAlarmHelper.fireFullTableReverseAlarmRestored(enb);
					}

				}
			} catch (Exception e) {
				log.error(e);
				// ��������ʧ�ܵ�
				enbAlarmHelper.fireFullTableReverseAlarm(enb, OmpAppContext
						.getMessage("full_table_reverse_failed",
								new Object[] { e.getLocalizedMessage() }));
			}
		}
	}

	/**
	 * ���������û��������������
	 * 
	 * @param task
	 */
	public void addFullTableConfigTask(long enbId, FutureResult task) {
		fullTableConfigTasks.put(enbId, task);
	}

	/**
	 * ���������û������Ƴ�����
	 * 
	 * @param enbId
	 */
	public void removeFullTableConfigTask(long enbId) {
		fullTableConfigTasks.remove(enbId);
	}

	/**
	 * �����������������������
	 * 
	 * @param task
	 */
	public void addFullTableReverseTask(long enbId, FutureResult task,
			String fileName) {
		fullTableReverseTasks.put(enbId, task);
		reverseTaskData.put(enbId, fileName);
	}

	/**
	 * ���������������Ƴ�����
	 * 
	 * @param enbId
	 */
	public void removeFullTableReverseTask(long enbId) {
		fullTableReverseTasks.remove(enbId);
		reverseTaskData.remove(enbId);
	}

	/**
	 * 
	 * ���ļ��ϴ���ftp��
	 * 
	 * @param ftpdDrectory
	 * @param localDirectory
	 * @param fileName
	 * @param fileContent
	 * @param hexEnbId
	 * @throws Exception
	 */
	public void upFiletoFtp(String ftpdDrectory, String localDirectory,
			String fileName, String fileContent, String hexEnbId)
			throws Exception {
		String currentLocalD = localDirectory + File.separator + hexEnbId;
		upFileToFtp(ftpdDrectory, currentLocalD, fileName, fileContent);
	}

	/**
	 * 
	 * ���ļ��ϴ���ftp��
	 * 
	 * @param ftpdDrectory
	 * @param localDirectory
	 * @param fileName
	 * @throws IOException
	 */
	public void upFileToFtp(String ftpdDrectory, String localDirectory,
			String fileName, String fileContent) throws Exception {
		// ����·��
		File content = new File(localDirectory);
		if (!content.exists()) {
			content.mkdirs();
		}
		// Ҫ�ϴ����ļ�·��
		String filePath = localDirectory + File.separator + fileName;

		FileWriter fwrite = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
			}
			fwrite = new FileWriter(file);
			fwrite.write(fileContent);
		} catch (Exception e) {
			log.error("Fail Upload file to ftp!", e);
			throw e;
		} finally {
			if (fwrite != null) {
				try {
					fwrite.close();
				} catch (IOException e) {
					log.error("close stream fail!", e);
					throw e;
				}

			}
		}

		// �ϴ���ftp
		FtpClient.getInstance().uploadFile(ftpdDrectory, filePath, ftpServerIp,
				ftpPort, ftpUsername, ftpPassword);

	}

	/**
	 * ��ftp�ϻ�ȡ�ļ�
	 * 
	 * @param ftpdDrectory
	 * @param localDirectory
	 * @param fileName
	 * @param hexEnbId
	 * @throws Exception
	 */
	public void fetchFileFromFtp(String ftpdDrectory, String localDirectory,
			String fileName, String hexEnbId) throws Exception {
		String currentLocalD = localDirectory + File.separator + hexEnbId;
		fetchFileFromFtp(ftpdDrectory, currentLocalD, fileName);
	}

	/**
	 * ��ftp�ϻ�ȡ�ļ�
	 * 
	 * @param ftpdDrectory
	 * @param localDirectory
	 * @param fileName
	 * @throws Exception
	 */
	public void fetchFileFromFtp(String ftpdDrectory, String localDirectory,
			String fileName) throws Exception {
		// ����·��
		File content = new File(localDirectory);
		if (!content.exists()) {
			content.mkdirs();
		}
		try {
			FtpClient.getInstance().fetchFile(ftpdDrectory + "/" + fileName,
					localDirectory + File.separator + fileName, ftpServerIp,
					ftpPort, ftpUsername, ftpPassword);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * �����ļ�����
	 * 
	 * @param enbId
	 * @return
	 */
	public String createFileName(String hexEnbId) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = hexEnbId + "_" + df.format(new Date()) + ".cfg";
		return fileName;
	}

	/**
	 * ��ȡsql����б���
	 * 
	 * @param sql
	 * @return
	 */
	public static String getTableName(String sql) {
		Pattern pattern = Pattern
				.compile("^INSERT\\s+INTO\\s+(\\W)?\\w+(\\W)?");
		Matcher matcher = pattern.matcher(sql);
		String result = "";
		if (matcher.find()) {
			result = matcher.group();
		}

		pattern = Pattern.compile("(\\W)?\\w+(\\W)?$");
		matcher = pattern.matcher(result);
		if (matcher.find()) {
			result = matcher.group();
		}

		pattern = Pattern.compile("\\w+");
		matcher = pattern.matcher(result);
		if (matcher.find()) {
			result = matcher.group();
		}
		return result.trim();
	}

	/**
	 * ��ȡsql�����values
	 * 
	 * @param sql
	 * @return
	 */
	public static String getTableValues(String sql) {
		Pattern pattern = Pattern.compile("\\((\\w+\\s?,?\\s?)+\\);$");
		Matcher matcher = pattern.matcher(sql);
		String result = "";
		if (matcher.find()) {
			result = matcher.group();
		}
		return result.trim();
	}

	public String getFtpServerIp() {
		return ftpServerIp;
	}

	public void setFtpServerIp(String ftpServerIp) {
		this.ftpServerIp = ftpServerIp;
	}

	public String getFtpUsername() {
		return ftpUsername;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public int getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	public int getOverTime() {
		return overTime;
	}

	public void setOverTime(int overTime) {
		this.overTime = overTime;
	}

	public String getConfigFtpdDrectory() {
		return configFtpdDrectory;
	}

	public void setConfigFtpdDrectory(String configFtpdDrectory) {
		this.configFtpdDrectory = configFtpdDrectory;
	}

	public String getConfigLocalDirectory() {
		return configLocalDirectory;
	}

	public void setConfigLocalDirectory(String configLocalDirectory) {
		this.configLocalDirectory = configLocalDirectory;
	}

	public String getReverseFtpdDrectory() {
		return reverseFtpdDrectory;
	}

	public void setReverseFtpdDrectory(String reverseFtpdDrectory) {
		this.reverseFtpdDrectory = reverseFtpdDrectory;
	}

	public String getReverseLocalDirectory() {
		return reverseLocalDirectory;
	}

	public void setReverseLocalDirectory(String reverseLocalDirectory) {
		this.reverseLocalDirectory = reverseLocalDirectory;
	}

	public void testReverseBiz(Long enbId) {
		EnbAppMessage message = new EnbAppMessage();
		message.setEnbId(enbId);
		message.setMa(EnbMessageConstants.MA_CONF);
		message.setMoc(EnbMessageConstants.MOC_INCREMENTAL_CONFIG);
		message.addTagValue(TagConst.RESULT, 0);
		message.addTagValue(TagConst.ERR_MSG, "ok");
		handleFullTableReverseResultNotify(message);
	}

}
