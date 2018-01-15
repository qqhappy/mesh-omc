package com.xinwei.lte.web.uem.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.lte.web.uem.utils.AbstractInnerMessage;
import com.xinwei.lte.web.uem.utils.RPCClient;
import com.xinwei.lte.web.uem.utils.UemConstantUtils;

public class GisEnbManage extends ActionSupport {
	private String EnbIDList;
	private RPCClient Client;
	private Log log = LogFactory.getLog(GisEnbManage.class);
	
	public void QueryEnbInfo(){
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		AbstractInnerMessage ResMes = null;		
		JSONObject QueryParas = new JSONObject();
		String[] EnbIDList = null;
		
		if(getEnbIDList() ==null){
			log.warn("EnbIDList is null");
			return;
		}
		EnbIDList = getEnbIDList().split(",");
		
		if(EnbIDList.length ==0){
			log.warn("EnbIDList is empty");
			return;
		}
		
		QueryParas.put("EnbIDList", JSONArray.fromObject(EnbIDList));
		
		Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
		Mes.setMessageId(UemConstantUtils.ACTION_QUERYGIS_ENBINFO);		
		Mes.setBody(QueryParas.toString());
		
		try {
			ResMes =  Client.InvokeMethod(Mes);
			Util.ajaxSimpleUtil(ResMes.getBody());
			
		} catch (Throwable e) {			
			
		}
	}

	public String getEnbIDList() {
		return EnbIDList;
	}

	public void setEnbIDList(String enbIDList) {
		EnbIDList = enbIDList;
	}

	public RPCClient getClient() {
		return Client;
	}

	public void setClient(RPCClient client) {
		Client = client;
	}
	
	
}
