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
	 * 设置从ftp上取文件的路径
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
	 * 设置文件存放到本地的路径
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
	 * 设置ftp服务器IP
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
	 * 设置ftp服务器端口
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
	 * 设置ftp登录用户名
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
	 * 设置ftp登录密码
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
