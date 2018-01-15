/*      						
 * Copyright 2014 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-6-20	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.server.enb.xstat;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.xstat.EnbStatEntity;
import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.mcbts.utils.FtpClient;
import com.xinwei.minas.server.xstat.service.StatEntityProcessor;
import com.xinwei.minas.server.xstat.service.StatFileParser;
import com.xinwei.minas.server.xstat.task.ScheduledTask;
import com.xinwei.minas.xstat.core.model.FtpConfig;

/**
 * 
 * eNB�����ļ���������
 * 
 * @author fanhaoyu
 * 
 */

public class EnbStatFileProcessTask extends ScheduledTask {

	private Log log = LogFactory.getLog(EnbStatFileProcessTask.class);

	//private static final String SEPARATOR = File.separator;
	private static final String SEPARATOR = "/";

	private FtpConfig ftpConfig;

	private StatFileParser statFileParser;

	@SuppressWarnings("rawtypes")
	private StatEntityProcessor statEntityProcessor;

	public EnbStatFileProcessTask(FtpConfig ftpConfig) {
		this.ftpConfig = ftpConfig;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doWork() throws Exception {
		log.debug("start to process enb stat file.");
		List<String> fileList = null;
		try {
			// ��ȡftp�е������ļ��б�
			fileList = FtpClient.getInstance().listAll(
					ftpConfig.getRemotePath(), ftpConfig.getFtpServerIp(),
					ftpConfig.getFtpPort(), ftpConfig.getUsername(),
					ftpConfig.getPassword());

		} catch (Exception e) {
			log.error("list all telstat files on ftp failed.", e);
		}
		if (fileList == null || fileList.isEmpty()) {
			log.debug("no enb stat file to process");
			return;
		}
		Pattern p = Pattern.compile("eNB.[0-9a-fA-F]{8}.[0-9]{12}.perf");
		// ���δ����ļ�
		for (String fileName : fileList) {
			// ��֤�ļ�����ʽ
			if (!p.matcher(fileName).find()) {
				log.warn("format of stat file name is wrong. fileName="
						+ fileName);
				continue;
			}
			long enbId = 0l;
			// ����eNB ID
			try {
				enbId = getEnbId(fileName);
			} catch (Exception e) {
				log.error("get eNB ID from file name failed. fileName="
						+ fileName, e);
				continue;
			}
			// ���˵�enbId�����ڵ��ļ�
			if (!EnbCache.getInstance().enbExists(enbId)) {
				// log.warn("eNB of this stat file is not exists. fileName="
				// + fileName);
				continue;
			}
			String remoteFilePath = ftpConfig.getRemotePath() + SEPARATOR
					+ fileName;
			String localFilePath = ftpConfig.getLocalPath() + SEPARATOR
					+ fileName;
			try {
				// ���ļ�ȡ�����ز�ɾ��ftp�ϵĸ��ļ�
				log.debug("fetch telstat file from ftp. filePath="
						+ remoteFilePath);
				FtpClient.getInstance().fetchFile(remoteFilePath,
						localFilePath, ftpConfig.getFtpServerIp(),
						ftpConfig.getFtpPort(), ftpConfig.getUsername(),
						ftpConfig.getPassword());
			} catch (Exception e) {
				log.error("fetch telstat file from ftp failed. filePath="
						+ localFilePath, e);
				continue;
			}
			List<EnbStatEntity> entities = null;
			try {
				File file = new File(localFilePath);
				entities = statFileParser.parse(file);
			} catch (Exception e) {
				log.error("parse telstat file failed. filePath="
						+ localFilePath, e);
				continue;
			}
			if (entities != null && !entities.isEmpty()) {
				try {
					statEntityProcessor.process(entities);
				} catch (Exception e) {
					log.error("process EnbStatEntity failed. filePath="
							+ localFilePath, e);
				}
			}
		}
		log.debug("finish processing enb stat file.");
	}

	/**
	 * ���ļ����н�����enbId
	 * 
	 * @param fileName
	 * @return
	 */
	private long getEnbId(String fileName) {
		String enbId = fileName.split("\\.")[1];
		return Long.valueOf(enbId, 16);
	}

	public static void main(String[] args) {
		Pattern p = Pattern.compile("eNB.[0-9a-fA-F]{8}.[0-9]{12}.perf");
		Matcher m = p.matcher("eNB.0000000a.111111111111.perf");
		System.out.println(m.find());
	}

	/**
	 * ����ͳ���ļ�������
	 * 
	 * @param statFileParser
	 */
	public void setStatFileParser(StatFileParser statFileParser) {
		this.statFileParser = statFileParser;
	}

	public StatFileParser getStatFileParser() {
		return statFileParser;
	}

	/**
	 * ����ͳ��ʵ�崦����
	 * 
	 * @param statEntityProcessor
	 */
	@SuppressWarnings("rawtypes")
	public void setStatEntityProcessor(StatEntityProcessor statEntityProcessor) {
		this.statEntityProcessor = statEntityProcessor;
	}

	@SuppressWarnings("rawtypes")
	public StatEntityProcessor getStatEntityProcessor() {
		return statEntityProcessor;
	}

}