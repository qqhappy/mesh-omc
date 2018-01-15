package com.xinwei.lte.web.uem.action;

import java.awt.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.janino.Java.NewAnonymousClassInstance;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.uem.utils.RPCClientProxy;
import com.xinwei.lte.web.uem.utils.UemConstantUtils;

public class QueryServer extends ActionSupport {
	
	private RPCClientProxy Client;
	private Map<String,String> FtpServer = new HashMap<String, String>(){};
	private Map<String,String> NtpServer = new HashMap<String, String>(){};
	private LinkedList<Map<String, String>> DataServerList = new LinkedList<Map<String,String>>(){};
	private Map<String,String> Im6000Server = new HashMap<String, String>(){};
	private Map<String, String> Im6000Timer = new TreeMap<String, String>(){};
	private Map<String, String> GisInit = new TreeMap<String, String>(){};
	private String ServerIDList;
	private Log log = LogFactory.getLog(UEManage.class);
	private String error;
	private String ServerName;
	private String ServerID;
	
	public String Query(){		
		try{
			GenerateFtpMap();
			GenerateNtpMap();
			GenerateDataServerMap();
			GenerateGisInitMap();
			//GenerateIm6000sServerMap();
		}
		catch(Exception e){
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return SUCCESS;
	}
	
	
	public String GetFtpCfg() throws Exception{
		//return ServerConfigDebug.getFtpDebug();
		return Client.SendMes(UemConstantUtils.MODEL_CONFIG,UemConstantUtils.ACTION_QUERYALLUE_FTP,null);
	}
	
	public void GenerateFtpMap() throws Exception{	
		
		JSONObject ResObj = JSONObject.fromObject(GetFtpCfg());
		if(!ResObj.get("result").equals("success")){
			log.warn("FtpQuery result is fail!");
			throw new Exception("Ftp查询失败");
		}
		JSONObject FtpJsonObj = (JSONObject)ResObj.get("DataBody");
		if(FtpJsonObj.isEmpty()){
			log.warn("Ftp Query Result is empty");
			FtpServer = null;
		}
		else{
			InsertKVToMap(FtpJsonObj,FtpServer);			
		}
	}
	
	public void GenerateNtpMap() throws Exception{		
		JSONObject ResObj = JSONObject.fromObject(GetNtpCfg());
		if(!ResObj.get("result").equals("success")){
			log.warn("FtpQuery result is fail!");
			throw new Exception("Ntp查询失败");
		}
		JSONObject NtpJsonObj = (JSONObject)ResObj.get("DataBody");
		
		if(NtpJsonObj.isEmpty()){
			log.warn("Ntp Query Result is empty");
			NtpServer = null;
		}
		else{
			InsertKVToMap(NtpJsonObj,NtpServer);			
		}
	}
	
	public void GenerateDataServerMap() throws Exception{
		JSONObject ResObj = JSONObject.fromObject(GetDataServerCfg());
		if(!ResObj.get("result").equals("success")){
			log.warn("FtpQuery result is fail!");
			throw new Exception("DataServer查询失败");
		}
		
		JSONArray DataServerJsonArr = (JSONArray)ResObj.get("DataBody");
		Map<String,String> DataServer = null;
		
		int index = 0;
		if(DataServerJsonArr.isEmpty()){
			log.warn("DataServer Query Result is empty");
			DataServer = null;			
		}
		else{
			for(index=0;index<DataServerJsonArr.size();index++){
				JSONObject DataServerJson = (JSONObject)(DataServerJsonArr.get(index));
				DataServer = new HashMap<String, String>(){};
				InsertKVToMap(DataServerJson,DataServer);
				DataServerList.push(DataServer);
			}						
		}
	}
	
	public void GenerateGisInitMap() throws Exception{
		JSONObject ResObj = JSONObject.fromObject(GetGisInitCfg());
		if(!ResObj.get("result").equals("success")){
			log.warn("GisInit result is fail!");
			throw new Exception("GisInit查询失败");
		}
		
		JSONObject GisInitJson = (JSONObject)ResObj.get("DataBody");
		
		if(GisInitJson.isEmpty()){
			log.warn("GisInit Query Result is empty");
			GisInit = null;
		}
		else{
			InsertKVToMap(GisInitJson,GisInit);			
		}
	}
	
	private Object GetGisInitCfg() throws Exception {
		return Client.SendMes(UemConstantUtils.MODEL_CONFIG,UemConstantUtils.ACTION_QUERYGIS_INIT,null);
	}


	public void GenerateIm6000sServerMap() throws Exception{		
		JSONObject Im6000JsonObj = JSONObject.fromObject(GetIm6000ServerCfg());
		if(Im6000JsonObj.isEmpty()){
			log.warn("Im6000sServer Query Result is empty");
			Im6000Server = null;
			Im6000Timer = null;
		}
		else{
			Im6000Server.put("UpgradeNum",(String) Im6000JsonObj.get("UpgradeNum"));
			Im6000Server.put("DisplayNum",(String) Im6000JsonObj.get("DisplayNum"));
			Im6000JsonObj.remove("UpgradeNum");
			Im6000JsonObj.remove("DisplayNum");
			
			InsertKVToMap(Im6000JsonObj,Im6000Timer);			
		}
	}
	private void InsertKVToMap(JSONObject Json, Map<String, String> Map) {	
		Iterator it = null;
		it = Json.keys();
		while(it.hasNext()){
			String key = String.valueOf(it.next());
			String val = Json.get(key)+"";
			Map.put(key, val);
		}
	}

	public String GetNtpCfg() throws Exception{
		//return ServerConfigDebug.getNtpDebug();
		return Client.SendMes(UemConstantUtils.MODEL_CONFIG,UemConstantUtils.ACTION_QUERYALLUE_NTP,null);
	}
	
	public String GetDataServerCfg() throws Exception{
		//return ServerConfigDebug.getDataServerDebug();
		JSONObject filter = null;		
		if(ServerIDList !=null){
			filter = new JSONObject();
			filter.put("DataServerList",ServerIDList);
			return Client.SendMes(UemConstantUtils.MODEL_CONFIG,UemConstantUtils.ACTION_QUERYALLUE_DATASERVER,filter.toString());
		}
		else{
			return Client.SendMes(UemConstantUtils.MODEL_CONFIG,UemConstantUtils.ACTION_QUERYALLUE_DATASERVER,null);
		}
		
	}

	public String GetIm6000ServerCfg() throws Exception{
		//return ServerConfigDebug.getIm6000Debug();
		return Client.SendMes(UemConstantUtils.MODEL_CONFIG,UemConstantUtils.ACTION_QUERYALLUE_IMD6000,null);
	}
	
	public String TurnToPage() throws Exception{
		if(ServerName ==null){
			log.warn("ServerName to Config is null");
			error = "ServerName is null";
			return error;
		}
		else if(ServerName.equals("FtpServer")){
			GenerateFtpMap();
		}
		else if(ServerName.equals("NtpServer")){
			GenerateNtpMap();

		}
		else if(ServerName.equals("DataServer")){
			GenerateDataServerMap();

		}
		else if(ServerName.equals("GisInit")){
			GenerateGisInitMap();

		}
		else if(ServerName.equals("Im6000sUE") || ServerName.equals("Im6000sTimer")){
			GenerateIm6000sServerMap();
		}
		
		return ServerName;
	}
	
	public void setClient(RPCClientProxy client) {
		Client = client;
	}

	public RPCClientProxy getClient() {
		return Client;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setIm6000Timer(Map<String,String> im6000Timer) {
		Im6000Timer = im6000Timer;
	}

	public Map<String,String> getIm6000Timer() {
		return Im6000Timer;
	}

	public Map<String, String> getFtpServer() {
		return FtpServer;
	}

	public void setFtpServer(Map<String, String> ftpServer) {
		FtpServer = ftpServer;
	}

	public Map<String, String> getNtpServer() {
		return NtpServer;
	}

	public void setNtpServer(Map<String, String> ntpServer) {
		NtpServer = ntpServer;
	}


	public Map<String, String> getIm6000Server() {
		return Im6000Server;
	}

	public void setIm6000Server(Map<String, String> im6000Server) {
		Im6000Server = im6000Server;
	}

	public String getServerIDList() {
		return ServerIDList;
	}

	public Map<String, String> getGisInit() {
		return GisInit;
	}


	public void setGisInit(Map<String, String> gisInit) {
		GisInit = gisInit;
	}


	public void setServerIDList(String serverIDList) {
		ServerIDList = serverIDList;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public void setServerName(String serverName) {
		ServerName = serverName;
	}

	public String getServerName() {
		return ServerName;
	}


	public void setDataServerList(LinkedList<Map<String, String>> dataServerList) {
		DataServerList = dataServerList;
	}


	public LinkedList<Map<String, String>> getDataServerList() {
		return DataServerList;
	}


	public void setServerID(String serverID) {
		ServerID = serverID;
	}


	public String getServerID() {
		return ServerID;
	}	
	
}
