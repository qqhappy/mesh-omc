package com.xinwei.minas.server.enb.task;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.minas.enb.core.model.EnbBlackBoxFileModel;

public class EnbBlackBoxFileDeleteTask {

	private static final Log log = LogFactory
			.getLog(EnbBlackBoxFileDeleteTask.class);

	// 删除多少天前的黑匣子文件
	private String persistDay;

	// 黑匣子文件个数
	private String persistCount;

	private static final String SEPARATOR = File.separator;

	// 本地保存黑匣子文件的一级目录
	private String localFirstFilePath;

	// ftpServer保存黑匣子文件的一级目录
	private String firstFilePath;

	// public static void main(String[] a){
	// EnbBlackBoxFileDeleteTask b = new EnbBlackBoxFileDeleteTask();
	// b.deleteTask();
	// }

	/**
	 * 定时任务执行删除黑匣子文件
	 */
	public void deleteTask() {
		log.debug("开始删除黑匣子文件");
		deleteOverTimeFile();
		deleteOverCountFile();
		log.debug("删除黑匣子文件结束");

	}

	/**
	 * 删除超时的文件
	 */
	public void deleteOverTimeFile() {
		// 获取enb文件目录
		List<File> enbDirList = getLocalEnbFile();
		for (File enbDir : enbDirList) {
			// 获取enb文件目录中的黑匣子文件
			List<File> blackBoxFileList = getLocalBlackBoxFile(enbDir);
			for (File blackBoxFile : blackBoxFileList) {
				Date fileDate = getFileDateByFile(blackBoxFile);
				Date deleteTime = getOverTimeDate();
				// 删除不在保留天数的文件
				if (fileDate.before(deleteTime)) {
					deleteFile(blackBoxFile);
				}
			}

		}
	}

	/**
	 * 获取指定enb目录下的黑匣子文件
	 * 
	 * @param enbFile
	 * @return
	 */
	public List<File> getLocalBlackBoxFile(File enbFile) {
		File[] fileArray = enbFile.listFiles();
		List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));
		return fileList;
	}

	/**
	 * 删除enb文件夹内超过指定个数的黑匣子文件
	 */
	public void deleteOverCountFile() {
		// 获取enb目录
		List<File> enbDirList = getLocalEnbFile();
		for (File enbDir : enbDirList) {
			// 获取enb目录中的文件
			List<File> blackBoxFileList = getLocalBlackBoxFile(enbDir);
			List<File> blackBoxFileOrderedList = getFileListByOrder(blackBoxFileList);
			int count = Integer.parseInt(persistCount);
			// 删除超过指定个数的文件
			if (blackBoxFileOrderedList.size() > count) {
				for (int index = count; index < blackBoxFileOrderedList.size(); index++) {
					deleteFile(blackBoxFileOrderedList.get(index));
				}
			}

		}

	}

	/**
	 * 删除文件的操作
	 * 
	 * @param file
	 */
	public void deleteFile(File file) {
		try {
			if (file.isFile() && file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			log.error("failed to delete file: " + file, e);
		}
	}

	/**
	 * 获取超时的时间
	 * 
	 * @return
	 */
	public List<File> getFileListByOrder(List<File> blackBoxFileList) {

		Collections.sort(blackBoxFileList, new Comparator<File>() {

			@Override
			public int compare(File f1, File f2) {
				Date date1 = getFileDateByFile(f1);
				Date date2 = getFileDateByFile(f2);
				if (date2.before(date1)) {
					return -1;
				} else if (date2.after(date1)) {
					return 1;
				}
				return 0;
			}
		});
		return blackBoxFileList;
	}

	/**
	 * 根据文件的保留天数获取时间
	 * 
	 * @return
	 */
	public Date getOverTimeDate() {
		int persistDays = Integer.parseInt(persistDay);
		SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -persistDays);
		return calendar.getTime();
	}

	/**
	 * 获取本地的enb目录
	 * 
	 * @return
	 */
	public List<File> getLocalEnbFile() {

		File firstFile = new File(localFirstFilePath + SEPARATOR
				+ firstFilePath);
		File[] fileArray = firstFile.listFiles();
		List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));
		return fileList;
	}

	/**
	 * 根据文件名获取时间
	 * 
	 * @param file
	 * @return
	 */
	public Date getFileDateByFile(File file) {
		String fileName = file.getName();
		String name = fileName.substring(0, fileName.indexOf(".tar.gz"));
		String[] nameParts = name.trim().split("_");
		SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmss");
		Date date = null;
		try {
			date = sf.parse(nameParts[2]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public String getPersistDay() {
		return persistDay;
	}

	public void setPersistDay(String persistDay) {
		this.persistDay = persistDay;
	}

	public String getPersistCount() {
		return persistCount;
	}

	public void setPersistCount(String persistCount) {
		this.persistCount = persistCount;
	}

	public String getLocalFirstFilePath() {
		return localFirstFilePath;
	}

	public void setLocalFirstFilePath(String localFirstFilePath) {
		this.localFirstFilePath = localFirstFilePath;
	}

	public String getFirstFilePath() {
		return firstFilePath;
	}

	public void setFirstFilePath(String firstFilePath) {
		this.firstFilePath = firstFilePath;
	}

}
