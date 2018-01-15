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
 * Enb整表业务任务管理器
 * 
 * 
 * @author zhuxiaozhan
 * 
 */

public class EnbFullTableTaskManager {

	private Log log = LogFactory.getLog(EnbFullTableTaskManager.class);

	public static final String CONFIG_SUCCESS_FLAG = "success";

	// FTP地址
	private String ftpServerIp;

	// FTP用户名
	private String ftpUsername;

	// FTP密码
	private String ftpPassword;

	// 整表配置ftp目录
	private String configFtpdDrectory;

	// 整表配置local目录
	private String configLocalDirectory;

	// 整表反构ftp目录
	private String reverseFtpdDrectory;

	// 整表反构local目录
	private String reverseLocalDirectory;

	private int overTime;

	// FTP端口
	private int ftpPort;

	private static final EnbFullTableTaskManager instance = new EnbFullTableTaskManager();

	// 整表配置任务缓存
	private static final Map<Long, FutureResult> fullTableConfigTasks = new ConcurrentHashMap<Long, FutureResult>();

	// 整表反构任务缓存
	private static final Map<Long, FutureResult> fullTableReverseTasks = new ConcurrentHashMap<Long, FutureResult>();

	// 整表反构对应的文件名称
	private static final Map<Long, String> reverseTaskData = new ConcurrentHashMap<Long, String>();

	private EnbFullTableTaskManager() {

	}

	public static EnbFullTableTaskManager getInstance() {
		return instance;
	}

	/**
	 * 处理整表配置结果通知
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
			// 包含错误原因和error code
			result.setErrorMessage(OmpAppContext.getMessage(
					"full_table_config_result_error",
					new Object[] { message.getStringValue(TagConst.ERR_MSG),
							message.getResult() }));
		}
		task.set(result);
	}

	/**
	 * 处理整表反构结果通知
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
			// 解析反构文件
			processReverseResult(enbId);
		} else {
			// 包含错误原因和error code
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
				// 生成EMS上enb当前的业务配置情况
				Map<String, List<String>> emsSqlMap = service
						.generateFullTableSql(moId, enb.getProtocolVersion());

				// 从ftp上获取生成的反构的文件
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
							// 获取表名
							String tableName = getTableName(sql);
							// 根据表名称获取EMS上对应的上配置信息,如果EMS上不包含该配置信息，则说明配置信息不同，产生告警
							List<String> emsSqlList = emsSqlMap.get(tableName);
							boolean alarmFlag = false;
							if (emsSqlList == null) {
								alarmFlag = true;
							} else {
								// 因为EMS上生成的sql语句中表名称没有带双引号，所以要去掉
								String enbSqlValues = getTableValues(sql);
								boolean findFlag = false;
								for (String emsSql : emsSqlList) {
									String emsSqlValues = getTableValues(emsSql);
									if (emsSqlValues.equals(enbSqlValues)) {
										findFlag = true;
										break;
									}
								}
								// 没有找到对应的sql语句则更改告警标识；
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

					// 如果有告警，则生成告警内容
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
						// 进行告警恢复
						enbAlarmHelper.fireFullTableReverseAlarmRestored(enb);
					}

				}
			} catch (Exception e) {
				log.error(e);
				// 产生反构失败的
				enbAlarmHelper.fireFullTableReverseAlarm(enb, OmpAppContext
						.getMessage("full_table_reverse_failed",
								new Object[] { e.getLocalizedMessage() }));
			}
		}
	}

	/**
	 * 往整表配置缓存里面添加任务
	 * 
	 * @param task
	 */
	public void addFullTableConfigTask(long enbId, FutureResult task) {
		fullTableConfigTasks.put(enbId, task);
	}

	/**
	 * 从整表配置缓存中移除任务
	 * 
	 * @param enbId
	 */
	public void removeFullTableConfigTask(long enbId) {
		fullTableConfigTasks.remove(enbId);
	}

	/**
	 * 往整表反构缓存里面添加任务
	 * 
	 * @param task
	 */
	public void addFullTableReverseTask(long enbId, FutureResult task,
			String fileName) {
		fullTableReverseTasks.put(enbId, task);
		reverseTaskData.put(enbId, fileName);
	}

	/**
	 * 从整表反构缓存中移除任务
	 * 
	 * @param enbId
	 */
	public void removeFullTableReverseTask(long enbId) {
		fullTableReverseTasks.remove(enbId);
		reverseTaskData.remove(enbId);
	}

	/**
	 * 
	 * 将文件上传到ftp上
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
	 * 将文件上传到ftp上
	 * 
	 * @param ftpdDrectory
	 * @param localDirectory
	 * @param fileName
	 * @throws IOException
	 */
	public void upFileToFtp(String ftpdDrectory, String localDirectory,
			String fileName, String fileContent) throws Exception {
		// 创建路径
		File content = new File(localDirectory);
		if (!content.exists()) {
			content.mkdirs();
		}
		// 要上传的文件路径
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

		// 上传到ftp
		FtpClient.getInstance().uploadFile(ftpdDrectory, filePath, ftpServerIp,
				ftpPort, ftpUsername, ftpPassword);

	}

	/**
	 * 从ftp上获取文件
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
	 * 从ftp上获取文件
	 * 
	 * @param ftpdDrectory
	 * @param localDirectory
	 * @param fileName
	 * @throws Exception
	 */
	public void fetchFileFromFtp(String ftpdDrectory, String localDirectory,
			String fileName) throws Exception {
		// 创建路径
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
	 * 构造文件名称
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
	 * 获取sql语句中表名
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
	 * 获取sql语句中values
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
