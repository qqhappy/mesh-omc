package com.xinwei.lte.web.uem.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.lte.web.uem.utils.AbstractInnerMessage;
import com.xinwei.lte.web.uem.utils.RPCClient;
import com.xinwei.lte.web.uem.utils.UemConstantUtils;

public class GisDevManage extends ActionSupport {

	private RPCClient Client;
	private Log log = LogFactory.getLog(GisDevManage.class);
	
	private String CenterLatitude = null;
	private String CenterLongitude = null;
	private String Zoom = null;
	
	public void QueryDevInfo(){
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		AbstractInnerMessage ResMes = null;		
		JSONObject QueryParas = new JSONObject();		
		
		QueryParas.put("CenterLatitude", CenterLatitude);
		QueryParas.put("CenterLongitude", CenterLongitude);
		QueryParas.put("Zoom", Zoom);
		
		if(QueryParas.isEmpty()){
			log.warn("UEInfo is empty");
			return;
		}
		Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
		Mes.setMessageId(UemConstantUtils.ACTION_QUERYGIS_DEVINFO);		
		Mes.setBody(QueryParas.toString());
		try {
			ResMes =  Client.InvokeMethod(Mes);
			Util.ajaxSimpleUtil(ResMes.getBody());
			
		} catch (Throwable e) {			
			
		}
	}

	public void setClient(RPCClient client) {
		Client = client;
	}

	public RPCClient getClient() {
		return Client;
	}

	public String getCenterLatitude() {
		return CenterLatitude;
	}

	public void setCenterLatitude(String centerLatitude) {
		CenterLatitude = centerLatitude;
	}

	public String getCenterLongitude() {
		return CenterLongitude;
	}

	public void setCenterLongitude(String centerLongitude) {
		CenterLongitude = centerLongitude;
	}

	public String getZoom() {
		return Zoom;
	}

	public void setZoom(String zoom) {
		Zoom = zoom;
	}
	
	
}
