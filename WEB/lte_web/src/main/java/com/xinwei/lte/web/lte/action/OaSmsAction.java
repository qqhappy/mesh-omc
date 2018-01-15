package com.xinwei.lte.web.lte.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.lte.model.OaSmsModel;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.oss.adapter.model.OssAdapterInputMessage;
import com.xinwei.oss.adapter.model.OssAdapterOutputMessage;

public class OaSmsAction  extends ActionSupport{

private Log log = LogFactory.getLog(getClass());
	
	@Resource
	private OssAdapter ossAdapter;
	
	private OaSmsModel oaSmsModel;
	
	private String failedReason;
	
public String queryOaSms(){
		
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			log.debug("OaSmsAction.queryOaSms() -- start ");
			oaSmsModel=new OaSmsModel();
			Map<String,Object> resultMap = ossAdapter.invoke(0xc3, 0x04, map);
			
			if(resultMap.get("oaName") != null) {
				oaSmsModel.setOaName(resultMap.get("oaName") +"");
			}if(resultMap.get("oaPassowrd") != null) {
				oaSmsModel.setOaPassowrd(resultMap.get("oaPassowrd") +"");
			}if(resultMap.get("oaIp") != null) {
				oaSmsModel.setOaIp(resultMap.get("oaIp") +"");
			}if(resultMap.get("oaPort") != null) {
				oaSmsModel.setOaPort(resultMap.get("oaPort") +"");
			}if(resultMap.get("oadbName") != null) {
				oaSmsModel.setOadbName(resultMap.get("oadbName") +"");
			}if(resultMap.get("cycTime") != null) {
				oaSmsModel.setCycTime(resultMap.get("cycTime") +"");
			}if(resultMap.get("oaNum") != null) {
				oaSmsModel.setOaNum(resultMap.get("oaNum") +"");
			}if(resultMap.get("oaStatus") != null) {
				oaSmsModel.setOaStatus(resultMap.get("oaStatus") +"");
			}if(resultMap.get("comment") != null) {
				oaSmsModel.setComment(resultMap.get("comment") +"");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("OaSmsAction.queryOaSms() -- failure ", e);
			failedReason = e.getLocalizedMessage();
		}
		
		return SUCCESS;
	}
public String configOASms() {
	log.debug("OaSmsActio.config() -- start ");
	try {
		// 构造请求消息
		Map<String, Object> data = new HashMap();
		data.put("oaName", oaSmsModel.getOaName());
		data.put("oaPassowrd", oaSmsModel.getOaPassowrd());
		data.put("oaIp", oaSmsModel.getOaIp());
		data.put("oaPort", oaSmsModel.getOaPort());
		data.put("oadbName", oaSmsModel.getOadbName());
		data.put("cycTime", oaSmsModel.getCycTime());
		data.put("oaNum", oaSmsModel.getOaNum());
		data.put("comment", oaSmsModel.getComment());
		OssAdapterInputMessage req = new OssAdapterInputMessage(0xc3, 0x03,
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
		failedReason = e.getLocalizedMessage();
	}
	log.debug("OaSmsActio.config() -- end ");
	return SUCCESS;
}



	public OaSmsModel getOaSmsModel() {
		return oaSmsModel;
	}

	public void setOaSmsModel(OaSmsModel oaSmsModel) {
		this.oaSmsModel = oaSmsModel;
	}

	public String getFailedReason() {
		return failedReason;
	}

	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}
	
	
	
	
}
