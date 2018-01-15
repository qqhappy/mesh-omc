package com.xinwei.lte.web.uem.action;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.lte.web.uem.utils.AbstractInnerMessage;
import com.xinwei.lte.web.uem.utils.RPCClient;
import com.xinwei.lte.web.uem.utils.UemConstantUtils;

public class GisCommonManage extends ActionSupport {
	
	private RPCClient Client;
	private String submitMap;
	private String error;
	
	public void QueryStat(){
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		AbstractInnerMessage ResMes = null;		
		
		Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
		Mes.setMessageId(UemConstantUtils.ACTION_QUERYGIS_STAT);
				
		try {
			ResMes =  Client.InvokeMethod(Mes);
			Util.ajaxSimpleUtil(ResMes.getBody());
			
		} catch (Throwable e) {			
			
		}
	}

	public void Init(){
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		AbstractInnerMessage ResMes = null;		
		
		Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
		Mes.setMessageId(UemConstantUtils.ACTION_QUERYGIS_INIT);
				
		try {
			ResMes =  Client.InvokeMethod(Mes);
			Util.ajaxSimpleUtil(ResMes.getBody());
			
		} catch (Throwable e) {			
			
		}
	}
	
	public String Config(){
		if(submitMap ==null){
			error = "提交字段为空";
			return ERROR;
		}
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		
		
		Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
		Mes.setMessageId(UemConstantUtils.ACTION_CONFIGGIS_INIT);
		
		try {
			Client.InvokeMethod(Mes);		
			
		} catch (Throwable e) {			
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return NONE;
	}
	public void setClient(RPCClient client) {
		Client = client;
	}

	public RPCClient getClient() {
		return Client;
	}

	public void setSubmitMap(String submitMap) {
		this.submitMap = submitMap;
	}

	public String getSubmitMap() {
		return submitMap;
	}
	
	
}
