package com.xinwei.lte.web.uem.action;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.uem.utils.RPCClientProxy;
import com.xinwei.lte.web.uem.utils.UemConstantUtils;

public class ConfigServer extends ActionSupport {
	
	private RPCClientProxy Client;
	private String submitMap;
	private String ServerType;
	private String error;
	private String DataServerList;
	
	public String CfgServer(){
		String MesType = null;		
		if(submitMap == null){
			setError("提交字段为空");
			return ERROR;
		}
		if(ServerType == null||ServerType.length()==0){
			setError("提交服务器类型为空");
			return ERROR;
		}
		if(ServerType.equals("FtpServer")){
			MesType = UemConstantUtils.ACTION_CONFIGALLUE_FTP;
		}
		else if(ServerType.equals("NtpServer")){
			MesType = UemConstantUtils.ACTION_CONFIGALLUE_NTP;
			
		}
		else if(ServerType.equals("DataServer")){
			MesType = UemConstantUtils.ACTION_CONFIGALLUE_DATASERVER;			
		}
		else if(ServerType.equals("AddDataServer")){
			MesType = UemConstantUtils.ACTION_CONFIGALLUE_ADDDATASERVER;			
		}
		else if(ServerType.equals("Im6000Server")){
			MesType = UemConstantUtils.ACTION_CONFIGALLUE_IMD6000;
		}
		else if(ServerType.equals("GisInit")){
			MesType = UemConstantUtils.ACTION_CONFIGGIS_INIT;
		}
		
		else{
			error = "未知的服务器类型,ServerType="+ServerType;
			return ERROR;
		}
		
		try {
			Client.SendMes(UemConstantUtils.MODEL_CONFIG, MesType, submitMap);
		} catch (Exception e) {

			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return SUCCESS;
	}

	public String Delete(){
		JSONObject submitObj = new JSONObject();
		
		if(DataServerList ==null || DataServerList.length() ==0){
			error = "ServerID is null";
			return ERROR;
		}
		submitObj.put("DataServerList",DataServerList);
		
		try {
			Client.SendMes(UemConstantUtils.MODEL_CONFIG,UemConstantUtils.ACTION_CONFIGALLUE_DELDATASERVER,submitObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		//return SUCCESS;
		return SUCCESS;
	}
	
	public void setSubmitObj(String submitObj) {
		submitMap = submitObj;
	}

	public String getSubmitObj() {
		return submitMap;
	}

	public void setServerType(String serverType) {
		ServerType = serverType;
	}

	public String getServerType() {
		return ServerType;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public String getSubmitMap() {
		return submitMap;
	}

	public void setSubmitMap(String submitMap) {
		this.submitMap = submitMap;
	}

	public void setClient(RPCClientProxy client) {
		Client = client;
	}

	public RPCClientProxy getClient() {
		return Client;
	}

	public String getDataServerList() {
		return DataServerList;
	}

	public void setDataServerList(String dataServerList) {
		DataServerList = dataServerList;
	}
}
