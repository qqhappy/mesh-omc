package com.xinwei.minas.enb.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 压缩文件工具类
 * @author sunzhangbin
 *
 */
public class ZipFileUtils {


	/**
	 * 把文件压缩成.Zip格式的压缩文件
	 * @param fileList 待压缩文件的集合
	 * @return 压缩文件
	 * @throws IOException 
	 */
	public File Zip(List<File> fileList) throws Exception{
		File tempZipFile = File.createTempFile("tempZipFile", ".zip");
		zip(tempZipFile.getAbsolutePath(), fileList);
		return tempZipFile;
	}

	
	private void zip(String absolutePath, List<File> fileList) throws Exception {
		// 构造zip输出流
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				absolutePath));
		// 设置压缩的方式
		 out.setMethod(ZipOutputStream.DEFLATED);
		  // 压缩文件
			if (fileList != null && !fileList.isEmpty()) {
				for (File tempFile : fileList) {
					zip(out, tempFile, tempFile.getName());
				}
			}
			out.close();
	}


	private void zip(ZipOutputStream out, File tempFile, String name) throws Exception {
		out.putNextEntry(new ZipEntry(name));
		FileInputStream in = new FileInputStream(tempFile);
		int count;
		byte[] data = new byte[2048000];
		while ((count = in.read(data, 0, 2048000)) != -1) {
			out.write(data, 0, count);
		}
		in.close();
	}
}
