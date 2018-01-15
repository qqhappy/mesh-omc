package com.xinwei.lte.web.lte.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.domain.JSONResult;
import com.xinwei.lte.web.lte.model.HaInfo;
import com.xinwei.lte.web.lte.model.LicenseModel;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * license查询和导入
 * @author sunzhangbin
 *
 */
public class LicenseAction extends ActionSupport{

	private Log log = LogFactory.getLog(getClass());
	
	@Resource
	private OssAdapter ossAdapter;
	
	private LicenseModel licenseModel;
	
	private String failedReason;
	
	private String deviceType="0";
	

	public String queryLicense(){
		
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			log.debug("LicenseAction.queryLicense() -- start ");
			map.put("deviceType", deviceType);
			Map<String,Object> resultMap = ossAdapter.invoke(0xc0, 0x04, map);
			licenseModel=new LicenseModel();
			if (resultMap.get("deviceType") != null) {
				licenseModel.setDeviceType(Integer.valueOf(resultMap.get("deviceType") +""));
			}if (resultMap.get("hardwareSignature") != null) {
				licenseModel.setHardwareSignature(resultMap.get("hardwareSignature") +"");
			}if (resultMap.get("versionType") != null) {
				licenseModel.setVersionType(Integer.valueOf(resultMap.get("versionType") +""));
			}if (resultMap.get("curUserNum") != null) {
				licenseModel.setCurUserNum(resultMap.get("curUserNum") +"");
			}if (resultMap.get("maxUserNum") != null) {
				licenseModel.setMaxUserNum(resultMap.get("maxUserNum") +"");
			}if (resultMap.get("expire") != null) {
				licenseModel.setExpire(resultMap.get("expire") +"");
			}if (resultMap.get("authCode") != null) {
				licenseModel.setAuthCode(resultMap.get("authCode") +"");
			}if (resultMap.get("descrition") != null) {
				licenseModel.setDescrition(resultMap.get("descrition") +"");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("LicenseAction.queryLicense() -- failure ", e);
			failedReason = e.getLocalizedMessage();
		}
		
		return SUCCESS;
	}
	
	public String toImportLicense(){
		
		return SUCCESS;
	}
	
	public String importLicense(){
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			log.debug("LicenseAction.queryLicense() -- start ");
			map.put("deviceType", licenseModel.getDeviceType());
			map.put("authCode", licenseModel.getAuthCode());
			Map<String,Object> resultMap = ossAdapter.invoke(0xc0, 0x03, map);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("LicenseAction.queryLicense() -- failure ", e);
			failedReason = e.getLocalizedMessage();
			return "SYS_CONFIG_LICENSE_ADD";
		}
		return SUCCESS;
	}
	
	
	
	
	public LicenseModel getLicenseModel() {
		return licenseModel;
	}

	public void setLicenseModel(LicenseModel licenseModel) {
		this.licenseModel = licenseModel;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	
}
