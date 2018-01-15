package com.xinwei.lte.web.enb.model;

/**
 * 下载模型
 * 
 * @author zhangqiang
 * 
 */
public class VersionDownloadPara {

	/**
	 * 1： 下载完成
	 */
	private int downloadSuccess;

	/**
	 * 下载异常
	 */
	private String error = "";

	/**
	 * 是否可以开始新的下载任务，0：否 1：是
	 */
	private int downLoadPara;

	/**
	 * 下载百分比
	 */
	private int percent;

	/**
	 * 版本号
	 */
	private String versionName;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public int getDownLoadPara() {
		return downLoadPara;
	}

	public void setDownLoadPara(int downLoadPara) {
		this.downLoadPara = downLoadPara;
	}

	public int getDownloadSuccess() {
		return downloadSuccess;
	}

	public void setDownloadSuccess(int downloadSuccess) {
		this.downloadSuccess = downloadSuccess;
	}

}
