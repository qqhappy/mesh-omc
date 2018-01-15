/**
 * 
 */
package com.xinwei.minas.xstat.core.model;

/**
 * @author fanhaoyu
 * 
 */
public class FtpConfig {

	private String remotePath;

	private String localPath;

	private String ftpServerIp;

	private int ftpPort;

	private String username;

	private String password;

	/**
	 * ���ô�ftp��ȡ�ļ���·��
	 * 
	 * @param remotePath
	 */
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getRemotePath() {
		return remotePath;
	}

	/**
	 * �����ļ���ŵ����ص�·��
	 * 
	 * @param localPath
	 */
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getLocalPath() {
		return localPath;
	}

	/**
	 * ����ftp������IP
	 * 
	 * @param ftpServerIp
	 */
	public void setFtpServerIp(String ftpServerIp) {
		this.ftpServerIp = ftpServerIp;
	}

	public String getFtpServerIp() {
		return ftpServerIp;
	}

	/**
	 * ����ftp�������˿�
	 * 
	 * @param ftpPort
	 */
	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	public int getFtpPort() {
		return ftpPort;
	}

	/**
	 * ����ftp��¼�û���
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	/**
	 * ����ftp��¼����
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

}
