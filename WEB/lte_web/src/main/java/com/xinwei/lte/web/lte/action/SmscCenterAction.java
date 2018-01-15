package com.xinwei.lte.web.lte.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.lte.model.CpuThreshold;
import com.xinwei.lte.web.lte.model.SmscModel;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.oss.adapter.model.OssAdapterInputMessage;
import com.xinwei.oss.adapter.model.OssAdapterOutputMessage;

public class SmscCenterAction extends ActionSupport {
	// 记录日志
	private static Log log = LogFactory.getLog(SmscCenterAction.class);

	// 模型
	private SmscModel smscModel;

	// 显示给用户的错误提示信息
	private String failedReason;

	@Resource
	private OssAdapter ossAdapter;

	// 异常信息
	private String exception;

	public String query() {
		log.debug("SmscCenterAction.query() -- start ");
		smscModel = new SmscModel();
		// 构造请求参数
		Map<String, Object> data = new HashMap<String, Object>();
		// 调用适配层发送消息
		try {
			Map<String, Object> resultMap = ossAdapter.invoke(0xbd, 0x04, data);
			if (resultMap.get("smscUserName") != null) {
				smscModel.setSmscUserName(resultMap.get("smscUserName") + "");
			}
			if (resultMap.get("smscPassWord") != null) {
				smscModel.setSmscPassWord(resultMap.get("smscPassWord") + "");
			}

			if (resultMap.get("smscIp") != null) {
				smscModel.setSmscIp(resultMap.get("smscIp") + "");
			}
			if (resultMap.get("smscPort") != null) {
				smscModel.setSmscPort(Integer.valueOf(resultMap.get("smscPort")
						+ ""));
			}

			if (resultMap.get("smscComment") != null) {
				smscModel.setSmscComment(resultMap.get("smscComment") + "");
			}
			if (resultMap.get("smscStatus") != null) {
				smscModel.setSmscStatus(Integer.valueOf(resultMap
						.get("smscStatus") + ""));
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("SmscCenterAction.query() -- failure ", e);
			failedReason = e.getMessage();
		}
		log.debug("SmscCenterAction.query() -- end ");
		return SUCCESS;
	}

	/**
	 * cup门限修改
	 * 
	 * @return
	 */
	public String config() {
		log.debug("SmscCenterAction.config() -- start ");
		try {
			// 构造请求消息
			Map<String, Object> data = new HashMap();
			data.put("smscUserName", smscModel.getSmscUserName());
			data.put("smscPassWord", smscModel.getSmscPassWord());
			data.put("smscIp", smscModel.getSmscIp());
			data.put("smscPort", smscModel.getSmscPort());
			data.put("smscComment", smscModel.getSmscComment());

	
			// 调用适配层
			Map<String,Object> resultMap = ossAdapter.invoke(0xbd, 0x03,
					data);
		
			if(Integer.valueOf(resultMap.get("lteFlag") +"") !=0){
				failedReason = LteFlag.flagReturn(resultMap.get("lteFlag")+"");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("SmscCenterAction.config() -- failure ", e);
			failedReason = e.getLocalizedMessage();
		}
		log.debug("SmscCenterAction.config() -- end ");
		return SUCCESS;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public SmscModel getSmscModel() {
		return smscModel;
	}

	public void setSmscModel(SmscModel smscModel) {
		this.smscModel = smscModel;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}
}
