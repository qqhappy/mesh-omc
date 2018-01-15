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
 * Ftp�Ŀͻ��˽ӿڷ�װ
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
	 * ����Server,ȡ���ݵ����أ���ɾ�����������ļ�
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
	 * ���ĳ��Ŀ¼��Ftp���Ƿ���ڣ�û�еĻ�����һ��
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
	 * ɾ��һ���ļ�
	 * 
	 * @param filename
	 *            �ļ���·����+�ļ���
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
	 * ɾ��ĳ��ftpĿ¼�µ������ļ�
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
	 * �ϴ�һ���ļ�
	 * 
	 * @param remotepath
	 *            Զ��ftp·��
	 * @param local
	 *            �����ļ�λ��
	 * @param ip
	 *            ftp server��Ip
	 * @param port
	 *            ftp����Ķ˿�
	 * @param username
	 *            �û���
	 * @param psw
	 *            ����
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
