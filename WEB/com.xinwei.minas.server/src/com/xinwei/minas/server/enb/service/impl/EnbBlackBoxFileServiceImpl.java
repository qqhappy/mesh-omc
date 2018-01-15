package com.xinwei.minas.server.enb.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.xinwei.minas.enb.core.model.Enb;
import com.xinwei.minas.enb.core.model.EnbBlackBoxFileCondition;
import com.xinwei.minas.enb.core.model.EnbBlackBoxFileModel;
import com.xinwei.minas.enb.core.utils.ZipFileUtils;
import com.xinwei.minas.server.enb.service.EnbBlackBoxFileService;
import com.xinwei.minas.server.enb.task.EnbBlackBoxFileTask;
import com.xinwei.omp.core.model.biz.PagingData;
import com.xinwei.omp.server.OmpAppContext;

public class EnbBlackBoxFileServiceImpl implements EnbBlackBoxFileService {

	// 黑匣子文件在FTP服务器的一级目录
	private String firstFilePath;
	// 黑匣子文件在本地的一级目录
	private String localFirstFilePath;

	private ResourceBundle resourceBundle;
	// 复位原因并排序
	private TreeMap<String, String> resetReasonMap = new TreeMap<String, String>();

	private String causePrefix = "enb.reset.reason.";

	public String getFirstFilePath() {
		return firstFilePath;
	}

	public void setFirstFilePath(String firstFilePath) {
		this.firstFilePath = firstFilePath;
	}

	public String getLocalFirstFilePath() {
		return localFirstFilePath;
	}

	public void setLocalFirstFilePath(String localFirstFilePath) {
		this.localFirstFilePath = localFirstFilePath;
	}

	// 获取国际化复位原因
	@Override
   synchronized	public TreeMap<String, String> getAllResetReason() {
		if (!resetReasonMap.isEmpty()) {
			return resetReasonMap;
		} else {
			String baseName = "com.xinwei.minas.server.enb.EnbBlackBox";
			setResourceBundle(ResourceBundle.getBundle(baseName,
					OmpAppContext.getLocale()));
			Enumeration keys = resourceBundle.getKeys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				if (key.contains(causePrefix)) {
					String value = resourceBundle.getString(key);
					resetReasonMap.put(key, value);
				}
			}

			return resetReasonMap;
		}

	}

	// 获取本地的黑匣子文件
	public LinkedHashMap<String, List<File>> listLocalFile() {
		File file = new File(localFirstFilePath + File.separator
				+ firstFilePath);
		File[] arrFile = file.listFiles();
		List<File> fileList = new ArrayList<File>(Arrays.asList(arrFile));
		LinkedHashMap<String, List<File>> fileMap = new LinkedHashMap<String, List<File>>();
		for (File boxFile : fileList) {
			File[] boxFileArray = boxFile.listFiles();
			List<File> boxFileList = new ArrayList<File>(
					Arrays.asList(boxFileArray));
			fileMap.put(boxFile.getName(), boxFileList);
		}

		return fileMap;
	}

	// 对黑匣子文件进行分页和排序,筛选
	@Override
	public PagingData<EnbBlackBoxFileModel> getBlackBoxFile(
			EnbBlackBoxFileCondition enbBlackBoxFileCondition) {
		ArrayList<EnbBlackBoxFileModel> blackBoxFileModelList = queryBlackBoxFile(enbBlackBoxFileCondition);
		int lastPNum = blackBoxFileModelList.size()
				% enbBlackBoxFileCondition.getNumPerPage();
		int page = 0;
		page = blackBoxFileModelList.size()
				/ enbBlackBoxFileCondition.getNumPerPage();
		if (lastPNum > 0) {
			page = page + 1;
		}
		PagingData<EnbBlackBoxFileModel> pagingData = new PagingData<EnbBlackBoxFileModel>();
		pagingData.setTotalPages(page);
		pagingData.setCurrentPage(enbBlackBoxFileCondition.getCurrentPage());
		pagingData.setNumPerPage(enbBlackBoxFileCondition.getNumPerPage());
		List<EnbBlackBoxFileModel> fileList = currentList(
				enbBlackBoxFileCondition.getCurrentPage(),
				enbBlackBoxFileCondition.getNumPerPage(), blackBoxFileModelList);
		pagingData.setResults(fileList);
		return pagingData;
	}

	// 获取当前页的EnbBlackBoxFileModel
	private List<EnbBlackBoxFileModel> currentList(int currentPage,
			int numPerPage,
			ArrayList<EnbBlackBoxFileModel> blackBoxFileModelList) {
		List<EnbBlackBoxFileModel> fileList = new ArrayList<EnbBlackBoxFileModel>();
		for (int number = 0; number < numPerPage; number++) {
			int index = ((currentPage - 1) * numPerPage) + number;
			if (index < blackBoxFileModelList.size()) {
				fileList.add(blackBoxFileModelList.get(index));
			} else {
				break;
			}
		}
		return fileList;
	}

	// 获取本地的黑匣子文件封装到EnbBlackBoxFileModel并进行排序筛选。
	private ArrayList<EnbBlackBoxFileModel> queryBlackBoxFile(
			EnbBlackBoxFileCondition enbBlackBoxFileCondition) {
		LinkedHashMap<String, List<File>> fileMap = listLocalFile();
		List<String> enbids = new ArrayList<String>(
				Arrays.asList(enbBlackBoxFileCondition.getEnbids().trim()
						.split(",")));
		List<String> resetReasonList = new ArrayList<String>(
				Arrays.asList(enbBlackBoxFileCondition.getResetReason().trim()
						.split(",")));
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date begindate = null;
		Date enddate = null;
		try {
			begindate = format.parse(enbBlackBoxFileCondition.getBegindate());
			enddate = format.parse(enbBlackBoxFileCondition.getEnddate());
		} catch (ParseException e3) {
			e3.printStackTrace();
		}
		ArrayList<EnbBlackBoxFileModel> blackBoxFileList = new ArrayList<EnbBlackBoxFileModel>();
		// 遍历map把黑匣子文件封装到EnbBlackBoxFileModel模型中。
		for (String fileKey : fileMap.keySet()) {
			for (String enbid : enbids) {
				if (fileKey.equals(enbid.trim())) {
					
					for (File blackBoxFile : fileMap.get(fileKey)) {
						String fileName = blackBoxFile.getName();
						if(matcherFileName(fileName)){
						String name = fileName.substring(0,
								fileName.indexOf(".tar.gz"));
						String[] nameParts = name.trim().split("_");
						EnbBlackBoxFileModel model = new EnbBlackBoxFileModel();
						model.setEnbId(nameParts[1]);
						SimpleDateFormat sf = new SimpleDateFormat(
								"yyMMddHHmmss");
						Date date;
						try {
							date = sf.parse(nameParts[2]);
							String cause = nameParts[3];
							model.setFileTime(date);
							String realCause = null;
							for (String key : resetReasonMap.keySet()) {
								if ((causePrefix + cause).equals(key)) {
									realCause = resetReasonMap.get(key);
								}
							}
							model.setCause(realCause);
							model.setFileName(fileName);

							if (date.after(begindate)
									&& date.before(enddate)
									&& resetReasonList.contains(causePrefix
											+ cause.trim())) {
								blackBoxFileList.add(model);
							}
							
						} catch (ParseException e) {
							e.printStackTrace();
						}
						}
					}
				}
			}

		}
		// 根據時間排序
		Collections.sort(blackBoxFileList,
				new Comparator<EnbBlackBoxFileModel>() {

					@Override
					public int compare(EnbBlackBoxFileModel e1,
							EnbBlackBoxFileModel e2) {
						if (e1.getFileTime().before(e2.getFileTime())) {
							return 1;
						} else if (e1.getFileTime().after(e2.getFileTime())) {
							return -1;
						}
						return 0;
					}
				});
		return blackBoxFileList;
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	//
	public File getFileFormMap(String name) {
		LinkedHashMap<String, List<File>> fileMap = listLocalFile();
		File theFile = null;
		for (String key : fileMap.keySet()) {
			for (File file : fileMap.get(key)) {
				if (file.getName().equals(name)) {
					theFile = file;
				}
			}
		}
		return theFile;
	}

	public byte[] getBytesByFile(File file) {
		byte[] buffer = null;
		if (file == null) {
			return null;
		}
		FileInputStream stream = null;
		ByteArrayOutputStream out = null;
		try {
			stream = new FileInputStream(file);
			out = new ByteArrayOutputStream(1000);
			byte[] bytes = new byte[1000];
			int n;
			while ((n = stream.read(bytes)) != -1) {
				out.write(bytes, 0, n);

			}
			buffer = out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return buffer;
	}

	@Override
	public byte[] getBytesByFileName(String name) {
		String path = getFileFormMap(name).getPath();
		File file = new File(path);
		return getBytesByFile(file);

	}

	@Override
	public List<File> getFileListByFileName(String name) {
		String[] names = name.trim().split(",");
		List<File> fileList = new ArrayList<File>();
		for (String fileName : names) {
			File file = getFileFormMap(fileName);
			fileList.add(file);
		}
		return fileList;
	}

	@Override
	public byte[] getZipFile(String name) {
		List<File> fileList = getFileListByFileName(name);
		File tempFileZip = null;
		try {
			tempFileZip = new ZipFileUtils().Zip(fileList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getBytesByFile(tempFileZip);
	}
	/**
	 * 校验黑匣子文件名称，过滤掉不正常的文件
	 * @param fileName
	 * @return
	 */
	private boolean matcherFileName(String fileName){
		Pattern pattern=java.util.regex.Pattern.compile("^BBX_[0-9a-zA-Z]{8}_[0-9]{12}_[0-9]{1,}\\.tar.gz$");
		Matcher matcher=pattern.matcher(fileName);
		return matcher.matches();

	}
	
}



