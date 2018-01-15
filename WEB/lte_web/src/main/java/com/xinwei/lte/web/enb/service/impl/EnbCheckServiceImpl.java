/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-11-3	| Administrator 	| 	create the file                       
 */

package com.xinwei.lte.web.enb.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xinwei.lte.web.enb.model.check.HealthCheckShow;
import com.xinwei.lte.web.enb.service.EnbCheckService;
import com.xinwei.lte.web.enb.service.ExcelStyleManager;
import com.xinwei.minas.enb.core.model.Enb;

/**
 * 
 * 
 * @author chenlong
 * 
 */

public class EnbCheckServiceImpl implements EnbCheckService {

	private int MAX_SAVE_FILE_NUM = 5;

	private Logger logger = LoggerFactory.getLogger(EnbCheckServiceImpl.class);
	
	@Override
	public int doCheck(List<Enb> enbList, String realPath) {
		if(EnbCheckTaskManager.getInstance().isFree()) {
			Thread thread = new Thread(new EnbCheckTask(enbList, realPath));
			thread.start();
			return 0;
		}
		return 1;
	}

	@Override
	public List<HealthCheckShow> queryCheckFile(String realPath) {
		List<HealthCheckShow> healthCheckShows = new ArrayList<HealthCheckShow>();
		String filePath = realPath + "check";
		File file = new File(filePath);
		File[] files = file.listFiles();
		// 只保留最近的文件,其他删除
		List<File> fileList = deleteExtraFiles(files);
		// 按照时间排序
		sortFilesByTime(fileList);
		
		// 封装文件显示对象
		for (int i = 0; i < fileList.size(); i++) {
			String fileName = fileList.get(i).getName();
			HealthCheckShow show = new HealthCheckShow();
			String fileNameContent = fileName.split("\\.")[0];
			String[] fileInfo = fileNameContent.split("_");
			show.setFileName(fileName);
			show.setCheckDate(ExcelStyleManager.getInstance().getTimeFromLong(fileInfo[1]));
			show.setCheckEnbNum(Integer.valueOf(fileInfo[2]));
			show.setBreakEnbNum(Integer.valueOf(fileInfo[3]));
			healthCheckShows.add(show);
		}
		return healthCheckShows;
	}

	/**
	 * 根据日期排序
	 * @param deleteExtraFiles
	 * @return
	 */
	private void sortFilesByTime(List<File> fileList) {
		if(null != fileList) {
			for (int i = 0; i < fileList.size(); i++) {
				int max = i;
				for (int j = i + 1; j < fileList.size(); j++) {
					if(!comparableCheckFile(fileList.get(max), fileList.get(j))) {
						max = j;
					}
				}
				File maxFile = fileList.get(max);
				fileList.set(max, fileList.get(i));
				fileList.set(i, maxFile);
			}
		}
	}

	/**
	 * 删除超过最大限制数量中较早的文件
	 * 
	 * @param files
	 */
	private List<File> deleteExtraFiles(File[] files) {
		List<File> checkFiles = new ArrayList<File>();
		List<File> checkFilesSort = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i].getName();
			if (fileName.startsWith("enbHealthCheck_")
					&& fileName.endsWith(".xls")) {
				checkFiles.add(files[i]);
			}
		}
		// 如果健康检查文件数量大于最大限制,则删除时间较早的文件
		if (checkFiles.size() > MAX_SAVE_FILE_NUM) {
			// 按照时间排序
			int checkFilesSize = checkFiles.size();
			for (int i = 0; i < checkFilesSize; i++) {
				int min = 0;
				for (int j = 1; j < checkFiles.size(); j++) {
					if (comparableCheckFile(checkFiles.get(min),
							checkFiles.get(j))) {
						min = j;
					}
				}
				File minFile = checkFiles.get(min);
				checkFilesSort.add(minFile);
				checkFiles.remove(minFile);
			}

			int checkSortSize = checkFilesSort.size();
			for (int i = 0; i < checkSortSize - MAX_SAVE_FILE_NUM; i++) {
				File deleteFile = checkFilesSort.get(0);
				checkFilesSort.remove(0);
				deleteFile.delete();
			}
			return checkFilesSort;
		}
		return checkFiles;
	}

	/**
	 * 按照检查时间比较健康检查文件
	 * 
	 * @param file1
	 * @param file2
	 * @return
	 */
	private boolean comparableCheckFile(File file1, File file2) {
		String file1Date = file1.getName().split("\\.")[0].split("_")[1];
		String file2Date = file2.getName().split("\\.")[0].split("_")[1];
		if (Long.valueOf(file1Date) > Long.valueOf(file2Date)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(6);
		list.add(10);
		list.add(4);
		list.add(2);
		list.add(8);
		
		for (int i = 0; i < list.size(); i++) {
			int min = i;
			for (int j = i + 1; j < list.size(); j++) {
				if(list.get(min) > list.get(j)) {
					min = j;
				}
			}
			Integer minElement = list.get(min);
			list.set(min, list.get(i));
			list.set(i, minElement);
		}
		
		for (Integer integer : list) {
			System.out.println(integer);
		}
		
	}
	
}
