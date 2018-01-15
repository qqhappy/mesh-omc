/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-4-20	| tiance 	| 	create the file                       
 */

package com.xinwei.minas.server.mcbts.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.jibble.simpleftp.SimpleFTP;

/**
 * 
 * Ftp的客户端接口封装
 * 
 * @author tiance
 * 
 */

public class FtpClient {
	private static Logger logger = Logger.getLogger(FtpClient.class);
	private static FtpClient mInstance = new FtpClient();

	private FtpClient() {
	}

	public static FtpClient getInstance() {
		return mInstance;
	}

	/**
	 * 连接Server,取数据到本地，并删除服务器端文件
	 * 
	 * @param remote
	 * @param local
	 * @param ip
	 * @param port
	 * @param username
	 * @param psw
	 * @throws Exception
	 */
	public void fetchFile(String remote, String local, String ip, int port,
			String username, String psw) throws Exception {
		// ftp get
		URL url = new URL("ftp://" + username + ":" + psw + "@" + ip + ":"
				+ port + "/" + remote);
		URLConnection urlc = url.openConnection();
		InputStream in = null;
		FileOutputStream fout = null;
		try {
			in = urlc.getInputStream(); // To download
			fout = new FileOutputStream(new File(local));
			byte[] buff = new byte[4096];
			while (true) {
				int count = in.read(buff);
				if (count == -1) {
					break;
				}
				fout.write(buff, 0, count);
			}
			fout.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (fout != null) {
				fout.close();
			}
			if (in != null) {
				in.close();
			}
		}

		SimpleFTP ftp = new SimpleFTP();
		try {
			// Connect to an FTP server on port 21.
			ftp.connect(ip, port, username, psw);
			// delete file
			ftp.del(remote);

		} catch (Exception e) {
			// Quit from the FTP server.
			ftp.disconnect();
		}
	}

	/**
	 * 检查某个目录在Ftp上是否存在，没有的话创建一个
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public void mkdir(String path, String ip, int port, String username,
			String psw) throws Exception {
		SimpleFTP ftp = new SimpleFTP();
		// Connect to an FTP server on port 21.
		ftp.connect(ip, port, username, psw);
		// Set binary mode.
		ftp.mkdir(path);
		// Quit from the FTP server.
		ftp.disconnect();
	}

	/**
	 * 删除一个文件
	 * 
	 * @param filename
	 *            文件的路径名+文件名
	 * @param ip
	 * @param port
	 * @param username
	 * @param psw
	 */
	public void delete(String filename, String ip, int port, String username,
			String psw) throws Exception {
		SimpleFTP ftp = new SimpleFTP();
		ftp.connect(ip, port, username, psw);

		ftp.del(filename);

		ftp.disconnect();
	}

	/**
	 * 删除某个ftp目录下的所有文件
	 * 
	 * @throws Exceptin
	 */
	public void delallFiles(String path, String ip, int port, String username,
			String psw) throws Exception {
		SimpleFTP ftp = new SimpleFTP();
		// Connect to an FTP server on port 21.
		ftp.connect(ip, port, username, psw);
		// Set binary mode.
		ftp.delall(path);
		// Quit from the FTP server.
		ftp.disconnect();
	}

	/**
	 * 上传一个文件
	 * 
	 * @param remotepath
	 *            远端ftp路径
	 * @param local
	 *            本地文件位置
	 * @param ip
	 *            ftp server的Ip
	 * @param port
	 *            ftp服务的端口
	 * @param username
	 *            用户名
	 * @param psw
	 *            密码
	 * @throws Exception
	 */
	public void uploadFile(String remotepath, String local, String ip,
			int port, String username, String psw) throws Exception {
		SimpleFTP ftp = new SimpleFTP();
		try {
			// Connect to an FTP server on port 21.
			ftp.connect(ip, port, username, psw);
			// Set binary mode.
			ftp.bin();
			// Change to a new working directory on the FTP server.
			ftp.cwd(remotepath);
			// Upload some files.
			ftp.stor(new File(local));
		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			// Quit from the FTP server.
			ftp.disconnect();
		}
	}

	public List<String> listAll(String path, String ip, int port,
			String username, String psw) throws Exception {
		CustomFTP ftp = new CustomFTP();
		// Set binary mode.
		List<String> filenames = Collections.EMPTY_LIST;
		try {
			// Connect to an FTP server on port 21.
			ftp.connect(ip, port, username, psw);
			filenames = ftp.listAll(path);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (ftp != null) {
				// Quit from the FTP server.
				ftp.disconnect();
			}
		}

		return filenames;
	}

	public static void main(String args[]) throws Exception {
		List<String> filenames = FtpClient.getInstance().listAll("system/softwareversion/",
				"172.16.20.180", 21, "admin", "xinwei");
		for (String f : filenames) {
			System.out.println(f);
		}
		FtpClient f = FtpClient.getInstance();
		f.fetchFile("system/softwareversion/BBU_RRU.2.6.10.19.BIN", "1.bin", "172.16.20.180", 21, "admin", "xinwei");
		// FtpClient.getInstance().delallFiles("/", "127.0.0.1", 21, "tiance",
		// "123456");
		// FtpClient.getInstance().mkdir("","192.168.2.77",21,"Admin","arrowping");
		// FtpClient.getInstance().uploadFile("a",
		// "workdir/mcbts/version/utversion/Work_w128_1.6.10.9.bin",
		// "127.0.0.1", 21, "tiance", "123456");
	}
}
