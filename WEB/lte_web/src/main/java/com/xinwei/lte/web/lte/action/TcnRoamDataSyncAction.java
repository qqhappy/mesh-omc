/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-3-19	|  yinyuelin 	    |  create the file                       
 */

package com.xinwei.lte.web.lte.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.lte.model.TcnRoamDataSyncModel;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.oss.adapter.model.OssAdapterInputMessage;
import com.xinwei.oss.adapter.model.OssAdapterOutputMessage;

/**
 * 
 * TCN1000漫游组网数据同步Action
 * 
 * @author chenjunhua
 * 
 */

public class TcnRoamDataSyncAction extends ActionSupport {

	private Log log = LogFactory.getLog(getClass());

	@Resource
	private OssAdapter ossAdapter;

	private TcnRoamDataSyncModel tcnRoamDataSyncModel;
	
	private String failedReason;
	
	private String exception;
	
	/**
	 * 查询数据
	 * 
	 * @return
	 */
	public String query() {
		log.debug("TcnRoamDataSyncAction.query() -- start ");
		tcnRoamDataSyncModel = new TcnRoamDataSyncModel();
		try{
			// 构造请求消息
			Map<String, Object> data = new HashMap();
			OssAdapterInputMessage req = new OssAdapterInputMessage(0xb7, 0x04, data);
			// 调用适配层
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);
			if (resp.isSuccesful()) {
				// 应答成功，解析消息
				String masterIp = resp.getStringValue("masterIp");
				String masterPort = resp.getStringValue("masterPort");
				String comment = resp.getStringValue("comment");
				String syncStatus = resp.getStringValue("syncStatus");
				tcnRoamDataSyncModel.setMasterIp(masterIp);
				tcnRoamDataSyncModel.setMasterPort(masterPort);		
				tcnRoamDataSyncModel.setComment(comment);
				tcnRoamDataSyncModel.setSyncStatus(syncStatus);
			}
			else {
				// 应答失败
				failedReason = resp.getReason();
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("TcnRoamDataSyncAction.query() -- failure ", e);
			exception = e.getLocalizedMessage();			
		}
		log.debug("TcnRoamDataSyncAction.query() -- end ");
		return SUCCESS;
	}

	/**
	 * 配置数据
	 * 
	 * @return
	 */
	public String config() {
		log.debug("TcnRoamDataSyncAction.config() -- start ");
		try{
			// 构造请求消息
			Map<String, Object> data = new HashMap();
			data.put("masterIp", tcnRoamDataSyncModel.getMasterIp());
			data.put("masterPort", tcnRoamDataSyncModel.getMasterPort());
			data.put("comment", tcnRoamDataSyncModel.getComment());			
			OssAdapterInputMessage req = new OssAdapterInputMessage(0xb7, 0x03, data);
			// 调用适配层
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);
			if (resp.isFailed()) {
				// 应答失败
				failedReason = resp.getReason();
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error("TcnRoamDataSyncAction.config() -- failure ", e);
			exception = e.getLocalizedMessage();			
		}
		log.debug("TcnRoamDataSyncAction.config() -- end ");
		return SUCCESS;
	}

	public TcnRoamDataSyncModel getTcnRoamDataSyncModel() {
		return tcnRoamDataSyncModel;
	}

	public void setTcnRoamDataSyncModel(TcnRoamDataSyncModel tcnRoamDataSyncModel) {
		this.tcnRoamDataSyncModel = tcnRoamDataSyncModel;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	
	

}
