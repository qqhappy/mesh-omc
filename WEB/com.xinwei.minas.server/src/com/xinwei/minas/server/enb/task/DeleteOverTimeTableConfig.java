package com.xinwei.minas.server.enb.task;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.xinwei.minas.server.enb.service.EnbCache;
import com.xinwei.minas.server.mcbts.utils.FtpClient;
import com.xinwei.omp.core.utils.DateUtils;

/**
 * 删除过期tableconfig文件
 * @author gaoyu
 *
 */

public class DeleteOverTimeTableConfig extends QuartzJobBean {
	private Log log = LogFactory.getLog(DeleteOverTimeTableConfig.class);

	private int overTime;

	public int getOverTime() {
		return overTime;
	}

	public void setOverTime(int overTime) {
		this.overTime = overTime;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		String localpath = EnbFullTableTaskManager.getInstance()
				.getConfigLocalDirectory();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 0 - overTime);
		long limitTime = calendar.getTimeInMillis();
		
		File localfile = new File(localpath);
		
		File[] localfiles = localfile.listFiles();

		String ftpPath = EnbFullTableTaskManager.getInstance()
		.getConfigFtpdDrectory();
		String ftpIp = EnbFullTableTaskManager.getInstance().getFtpServerIp();
		int ftpPort = EnbFullTableTaskManager.getInstance().getFtpPort();
		String ftpUsername = EnbFullTableTaskManager.getInstance().getFtpUsername();
		String ftpPassword = EnbFullTableTaskManager.getInstance().getFtpPassword();
		if(localfiles !=null && localfiles.length > 0){
			Pattern p = Pattern.compile("[0-9a-fA-F]{8}");
			for(File subfilepath : localfiles){
				if(subfilepath.isDirectory() && p.matcher(subfilepath.getName()).find()){
				    String tableconfigpath = localpath + "/" + subfilepath.getName();
				    log.error("begin to delete local tableconfig files.filePath="+ tableconfigpath);
				    File tableconfigFile= new File(tableconfigpath);
				    File[] tableconfigFiles = tableconfigFile.listFiles();
				    deletefiles(tableconfigFiles, limitTime);
				}
			}
			
		}
			
			
			
		try {
			List<String> ftpfiles = FtpClient.getInstance().listAll(
					ftpPath,ftpIp,ftpPort,ftpUsername,ftpPassword);
			if(ftpfiles!=null && ftpfiles.size()>0){
				log.error("begin to delete ftp tableconfig files");
				int count = 0;
				for (String fileName : ftpfiles) {
					String preName = fileName.split("\\.")[0];
					long fileTime = Long.parseLong(preName.split("_")[1]);
					long dataTimeftp = DateUtils
							.getMillisecondTimeFromBriefTime(fileTime);
					if (dataTimeftp > limitTime)
						continue;
					String remotePath = ftpPath + "/" +fileName;
					FtpClient.getInstance().delete(remotePath, ftpIp, ftpPort,
							ftpUsername, ftpPassword);
					count++;

				}
				log.error("delete overtime tableconfig files. fileCount="
						+ count);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deletefiles(File[] files, long limitTime) {
		int count = 0;
		for (File file : files) {
			long modifiedTime = file.lastModified();
			long dataTimeftp = DateUtils
					.getMillisecondTimeFromBriefTime(modifiedTime);
			if (dataTimeftp > limitTime)
				continue;
			// 删除过期的文件
			try {
				if (!file.delete()) {
					log.error("delete  tableconfig file failed. filepath:"
							+ file.getAbsolutePath());
				}
				count++;
			} catch (Exception e) {
				log.error("delete tableconfig file failed. filepath:"
						+ file.getAbsolutePath());
			}
		}
		log.debug("delete overtime tableconfig files. fileCount=" + count);
	}
}
