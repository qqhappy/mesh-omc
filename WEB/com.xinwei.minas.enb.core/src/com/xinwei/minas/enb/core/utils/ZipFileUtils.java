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
 * ѹ���ļ�������
 * @author sunzhangbin
 *
 */
public class ZipFileUtils {


	/**
	 * ���ļ�ѹ����.Zip��ʽ��ѹ���ļ�
	 * @param fileList ��ѹ���ļ��ļ���
	 * @return ѹ���ļ�
	 * @throws IOException 
	 */
	public File Zip(List<File> fileList) throws Exception{
		File tempZipFile = File.createTempFile("tempZipFile", ".zip");
		zip(tempZipFile.getAbsolutePath(), fileList);
		return tempZipFile;
	}

	
	private void zip(String absolutePath, List<File> fileList) throws Exception {
		// ����zip�����
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				absolutePath));
		// ����ѹ���ķ�ʽ
		 out.setMethod(ZipOutputStream.DEFLATED);
		  // ѹ���ļ�
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
