/*      						
 * Copyright 2012 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    		|  Who  		|  What  
 * 2015-1-22	| chenjunhua 	| 	create the file                       
 */
package com.xinwei.lte.web.lte.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.lte.model.TcnVmrLink;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.oss.adapter.model.OssAdapterInputMessage;
import com.xinwei.oss.adapter.model.OssAdapterOutputMessage;

/**
 * 
 * 路由服务器配置业务Action
 * 
 * @author chenjunhua
 * 
 */
public class TcnVmrLinkAction extends ActionSupport {

	// 记录日志
	private static Log log = LogFactory.getLog(TcnVmrLinkAction.class);

	// 模型
	private TcnVmrLink tcnVmrLink;

	@Resource
	private OssAdapter ossAdapter;

	// 显示给用户的错误提示信息
	private String failedReason = "暂无相关数据";

	// 异常信息
	private String exception;

	/**
	 * 查询路由服务器配置信息
	 * 
	 * @return
	 */
	public String query() {
		log.debug("TcnVmrLinkAction.query() -- start ");
		tcnVmrLink = new TcnVmrLink();
		try {
			// 构造请求数据
			Map<String, Object> data = new HashMap<String, Object>();
			// 调用适配层发送消息
			OssAdapterInputMessage req = new OssAdapterInputMessage(0xb8, 0x04,
					data);
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);

			if (resp.isSuccesful()) {
				// 应答成功，解析消息
				String vmrIp = resp.getStringValue("vmrIp");
				String vmrPort = resp.getStringValue("vmrPort");
				String username = resp.getStringValue("vmrUsername");
				String password = resp.getStringValue("vmrPassword");
				String comment = resp.getStringValue("comment");
				String linkStatus = resp.getStringValue("vmrLinkStatus");
				tcnVmrLink.setVmrIp(vmrIp);
				tcnVmrLink.setVmrPort(vmrPort);
				tcnVmrLink.setUsername(username);
				tcnVmrLink.setPassword(password);
				tcnVmrLink.setComment(comment);
				tcnVmrLink.setLinkStatus(linkStatus);
			} else {
				// 应答失败
				failedReason = resp.getReason();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TcnVmrLinkAction.query() -- failure ", e);
			exception = e.getMessage();
		}
		log.debug("TcnVmrLinkAction.query() -- end ");
		return SUCCESS;
	}

	/**
	 * 修改路由服务器配置信息
	 * 
	 * @return
	 */
	public String config() {
		log.debug("TcnVmrLinkAction.config() -- start ");
		try {
			// 构造请求消息
			Map<String, Object> data = new HashMap();
			data.put("vmrIp", tcnVmrLink.getVmrIp());
			data.put("vmrPort", tcnVmrLink.getVmrPort());
			data.put("vmrUsername", tcnVmrLink.getUsername());
			data.put("vmrPassword", tcnVmrLink.getPassword());
			data.put("comment", tcnVmrLink.getComment());
			OssAdapterInputMessage req = new OssAdapterInputMessage(0xb8, 0x03,
					data);
			// 调用适配层
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);
			if (resp.isFailed()) {
				// 应答失败
				failedReason = resp.getReason();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TcnVmrLinkAction.config() -- failure ", e);
			exception = e.getLocalizedMessage();
		}
		log.debug("TcnVmrLinkAction.config() -- end ");
		return SUCCESS;
	}

	public TcnVmrLink getTcnVmrLink() {
		return tcnVmrLink;
	}

	public void setTcnVmrLink(TcnVmrLink tcnVmrLink) {
		this.tcnVmrLink = tcnVmrLink;
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

