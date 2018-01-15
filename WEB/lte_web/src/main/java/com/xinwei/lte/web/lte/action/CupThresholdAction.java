package com.xinwei.lte.web.lte.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.lte.model.CpuThreshold;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.oss.adapter.model.OssAdapterInputMessage;
import com.xinwei.oss.adapter.model.OssAdapterOutputMessage;

public class CupThresholdAction extends ActionSupport{
	// 记录日志
	private static Log log = LogFactory.getLog(CupThresholdAction.class);
	
	//模型
	private CpuThreshold cpuThreshold;
	
	// 显示给用户的错误提示信息

	@Resource
	private OssAdapter ossAdapter;
	
	//异常信息
	private String exception;

	public String queryCpuThreshold(){
		log.debug("CupThresholdAction.queryCpuThreshold() -- start ");
		cpuThreshold = new CpuThreshold();
		//构造请求参数
		Map<String ,Object> data = new HashMap<String,Object>();
		// 调用适配层发送消息
		try {	
			OssAdapterInputMessage req = new OssAdapterInputMessage(0xbc, 0x05,
				data);
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);
			if(resp.isSuccesful()){
				int cpuUp = resp.getIntValue("cpuUp");
				int cupDown = resp.getIntValue("cupDown");
				cpuThreshold.setCpuUp(cpuUp);
				cpuThreshold.setCupDown(cupDown);
			}else{
				exception = resp.getReason();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("CupThresholdAction.query() -- failure ", e);
			exception = e.getMessage();
		}
		log.debug("CupThresholdAction.query() -- end ");
		return SUCCESS;
	}
	
	/**
	 * cup门限修改
	 * @return
	 */
	public String config() {
		log.debug("CupThresholdAction.config() -- start ");
		try {
			// 构造请求消息
			Map<String, Object> data = new HashMap();
			data.put("cpuUp", cpuThreshold.getCpuUp());
			data.put("cupDown", cpuThreshold.getCupDown());
			OssAdapterInputMessage req = new OssAdapterInputMessage(0xbc, 0x03,
					data);
			// 调用适配层
			OssAdapterOutputMessage resp = ossAdapter.invoke(req);
			if (resp.isFailed()) {
				// 应答失败
				exception = resp.getReason();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("CupThresholdAction.config() -- failure ", e);
			exception = e.getLocalizedMessage();
		}
		log.debug("CupThresholdAction.config() -- end ");
		return SUCCESS;
	}
	
	
	
	
	
	
	
	
	
	
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public CpuThreshold getCpuThreshold() {
		return cpuThreshold;
	}

	public void setCpuThreshold(CpuThreshold cpuThreshold) {
		this.cpuThreshold = cpuThreshold;
	}
}
