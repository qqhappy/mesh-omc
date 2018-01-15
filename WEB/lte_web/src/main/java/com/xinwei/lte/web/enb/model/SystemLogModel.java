package com.xinwei.lte.web.enb.model;

import com.xinwei.minas.core.model.secu.syslog.SystemLog;

/**
 * 日志模型，为获取日期的String
 * 
 * @author zhangqiang
 * 
 */

public class SystemLogModel {

	/**
	 * 操作时间的字符串形式
	 */
	private String operTimeString;

	/**
	 * 操作详细
	 */
	private String dataDesc;

	/**
	 * 后台日志模型
	 */
	private SystemLog log = new SystemLog();

	public String getOperTimeString() {
		return operTimeString;
	}

	public void setOperTimeString(String operTimeString) {
		this.operTimeString = operTimeString;
	}

	public SystemLog getLog() {
		return log;
	}

	public void setLog(SystemLog log) {
		this.log = log;
	}

	public String getDataDesc() {
		return dataDesc;
	}

	public void setDataDesc(String dataDesc) {
		this.dataDesc = dataDesc;
	}

}
