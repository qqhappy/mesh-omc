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

	// ɾ��������ǰ�ĺ�ϻ���ļ�
	private String persistDay;

	// ��ϻ���ļ�����
	private String persistCount;

	private static final String SEPARATOR = File.separator;

	// ���ر����ϻ���ļ���һ��Ŀ¼
	private String localFirstFilePath;

	// ftpServer�����ϻ���ļ���һ��Ŀ¼
	private String firstFilePath;

	// public static void main(String[] a){
	// EnbBlackBoxFileDeleteTask b = new EnbBlackBoxFileDeleteTask();
	// b.deleteTask();
	// }

	/**
	 * ��ʱ����ִ��ɾ����ϻ���ļ�
	 */
	public void deleteTask() {
		log.debug("��ʼɾ����ϻ���ļ�");
		deleteOverTimeFile();
		deleteOverCountFile();
		log.debug("ɾ����ϻ���ļ�����");

	}

	/**
	 * ɾ����ʱ���ļ�
	 */
	public void deleteOverTimeFile() {
		// ��ȡenb�ļ�Ŀ¼
		List<File> enbDirList = getLocalEnbFile();
		for (File enbDir : enbDirList) {
			// ��ȡenb�ļ�Ŀ¼�еĺ�ϻ���ļ�
			List<File> blackBoxFileList = getLocalBlackBoxFile(enbDir);
			for (File blackBoxFile : blackBoxFileList) {
				Date fileDate = getFileDateByFile(blackBoxFile);
				Date deleteTime = getOverTimeDate();
				// ɾ�����ڱ����������ļ�
				if (fileDate.before(deleteTime)) {
					deleteFile(blackBoxFile);
				}
			}

		}
	}

	/**
	 * ��ȡָ��enbĿ¼�µĺ�ϻ���ļ�
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
	 * ɾ��enb�ļ����ڳ���ָ�������ĺ�ϻ���ļ�
	 */
	public void deleteOverCountFile() {
		// ��ȡenbĿ¼
		List<File> enbDirList = getLocalEnbFile();
		for (File enbDir : enbDirList) {
			// ��ȡenbĿ¼�е��ļ�
			List<File> blackBoxFileList = getLocalBlackBoxFile(enbDir);
			List<File> blackBoxFileOrderedList = getFileListByOrder(blackBoxFileList);
			int count = Integer.parseInt(persistCount);
			// ɾ������ָ���������ļ�
			if (blackBoxFileOrderedList.size() > count) {
				for (int index = count; index < blackBoxFileOrderedList.size(); index++) {
					deleteFile(blackBoxFileOrderedList.get(index));
				}
			}

		}

	}

	/**
	 * ɾ���ļ��Ĳ���
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
	 * ��ȡ��ʱ��ʱ��
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
	 * �����ļ��ı���������ȡʱ��
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
	 * ��ȡ���ص�enbĿ¼
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
	 * �����ļ�����ȡʱ��
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
