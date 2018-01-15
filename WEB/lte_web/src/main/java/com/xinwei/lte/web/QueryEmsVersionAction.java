package com.xinwei.lte.web;

import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;

public class QueryEmsVersionAction extends ActionSupport {
	
	@Value("${ems_version}")
	private String ems_version;
	
	@Value("${tcn1000_version}")
	private String tcn1000_version;
	
	@Value("${xw7400_version}")
	private String xw7400_version;
	
	@Value("${xw7102_version}")
	private String xw7102_version;
	
	public String queryEmsVersion(){
		return SUCCESS;
	}
	public String turnEmsVersionLeft(){
		return SUCCESS;
	}

	public String getEms_version() {
		return ems_version;
	}

	public void setEms_version(String ems_version) {
		this.ems_version = ems_version;
	}

	public String getTcn1000_version() {
		return tcn1000_version;
	}

	public void setTcn1000_version(String tcn1000_version) {
		this.tcn1000_version = tcn1000_version;
	}

	public String getXw7400_version() {
		return xw7400_version;
	}

	public void setXw7400_version(String xw7400_version) {
		this.xw7400_version = xw7400_version;
	}

	public String getXw7102_version() {
		return xw7102_version;
	}

	public void setXw7102_version(String xw7102_version) {
		this.xw7102_version = xw7102_version;
	}
	
	
}
