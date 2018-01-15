/*      						
 * Copyright 2013 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-9-17	| fanhaoyu 	| 	create the file                       
 */

package com.xinwei.minas.mcbts.core.model.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xinwei.omp.core.model.biz.FieldProperty;
import com.xinwei.omp.core.model.biz.Listable;

/**
 * 
 * 性能日志配置实体类
 * 
 * @author fanhaoyu
 * 
 */

@SuppressWarnings("serial")
public class PerfLogConfig implements Serializable, Listable {

	private Long idx;

	private Long moId;
	// 4字节
	private String ftpServerIp;
	// 2字节
	private int ftpServerPort;
	// 长度1-10
	private String userName;
	// 长度1-10
	private String password;
	// 上报间隔（单位：秒） 2字节
	private int reportInterval;
	// 收集间隔（单位：秒） 2字节
	private int collectInterval;
	// 实时性能上报周期
	private int realTimeInterval;

	public String getFtpServerIp() {
		return ftpServerIp;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Long getIdx() {
		return idx;
	}

	public Long getMoId() {
		return moId;
	}

	public void setMoId(Long moId) {
		this.moId = moId;
	}

	public void setFtpServerIp(String ftpServerIp) {
		this.ftpServerIp = ftpServerIp;
	}

	public int getFtpServerPort() {
		return ftpServerPort;
	}

	public void setFtpServerPort(int ftpServerPort) {
		this.ftpServerPort = ftpServerPort;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getReportInterval() {
		return reportInterval;
	}

	public void setReportInterval(int reportInterval) {
		this.reportInterval = reportInterval;
	}

	public int getCollectInterval() {
		return collectInterval;
	}

	public void setCollectInterval(int collectInterval) {
		this.collectInterval = collectInterval;
	}

	public void setRealTimeInterval(int realTimeInterval) {
		this.realTimeInterval = realTimeInterval;
	}

	public int getRealTimeInterval() {
		return realTimeInterval;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + collectInterval;
		result = prime * result
				+ ((ftpServerIp == null) ? 0 : ftpServerIp.hashCode());
		result = prime * result + ftpServerPort;
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + reportInterval;
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PerfLogConfig))
			return false;
		PerfLogConfig other = (PerfLogConfig) obj;
		if (collectInterval != other.collectInterval)
			return false;
		if (ftpServerIp == null) {
			if (other.ftpServerIp != null)
				return false;
		} else if (!ftpServerIp.equals(other.ftpServerIp))
			return false;
		if (ftpServerPort != other.ftpServerPort)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (reportInterval != other.reportInterval)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public List<FieldProperty> listAll() {
		List<FieldProperty> allProperties = new ArrayList<FieldProperty>();
		allProperties.add(new FieldProperty(0,
				"listable.PerfLogConfig.ftpServerIp", String
						.valueOf(this.ftpServerIp)));
		allProperties.add(new FieldProperty(0,
				"listable.PerfLogConfig.ftpServerPort", String
						.valueOf(this.ftpServerPort)));
		allProperties.add(new FieldProperty(0,
				"listable.PerfLogConfig.userName", String
						.valueOf(this.userName)));
		allProperties.add(new FieldProperty(0,
				"listable.PerfLogConfig.password", String
						.valueOf(this.password)));
		allProperties.add(new FieldProperty(0,
				"listable.PerfLogConfig.reportInterval", String
						.valueOf(this.reportInterval)));
		allProperties.add(new FieldProperty(0,
				"listable.PerfLogConfig.collectInterval", String
						.valueOf(this.collectInterval)));

		return allProperties;
	}

	@Override
	public String getBizName() {
		return "mcbts_perfLog";
	}
}
