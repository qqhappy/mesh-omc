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

public class GisUEManage extends ActionSupport {
	private RPCClient Client;
	private Log log = LogFactory.getLog(GisUEManage.class);
	
	private String IMSI;
	private String PCI;
	private String Status;
	private String BussinessType;
	private String Location;
	
	public void QueryUEInfo(){
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		AbstractInnerMessage ResMes = null;		
		JSONObject QueryParas = new JSONObject();
		
		QueryParas.put("IMSI", getIMSI());			
		QueryParas.put("Status", getStatus());
		QueryParas.put("PCI", getPCI());
		QueryParas.put("BussinessType", getBussinessType());
		QueryParas.put("Location", getLocation());		
		
		if(QueryParas.isEmpty()){
			QueryParas.put("IsAll",true);
		}
		else{
			QueryParas.put("IsAll",false);
		}
		
		Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
		Mes.setMessageId(UemConstantUtils.ACTION_QUERYGIS_UEINFO);		
		Mes.setBody(QueryParas.toString());
		
		try {
			ResMes =  Client.InvokeMethod(Mes);
			Util.ajaxSimpleUtil(ResMes.getBody());
			
		} catch (Throwable e) {			
			
		}
	}

	public String getIMSI() {
		return IMSI;
	}

	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}
	
	public RPCClient getClient() {
		return Client;
	}

	public void setClient(RPCClient client) {
		Client = client;
	}

	public void setPCI(String pCI) {
		PCI = pCI;
	}

	public String getPCI() {
		return PCI;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getStatus() {
		return Status;
	}

	public void setBussinessType(String bussinessType) {
		BussinessType = bussinessType;
	}

	public String getBussinessType() {
		return BussinessType;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getLocation() {
		return Location;
	}
	
	
}
