package com.xinwei.lte.web.uem.action;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ServerConfigDebug {
	public static String getFtpDebug(){
		Map<String,String> Cfg = new HashMap<String, String>(){{
			put("FtpIPAddress","172.31.4.230");
			put("FtpPort","8000");
			put("UserName","aa");
			put("Password","bb");
			put("FilePathName","/");
		}};
		
		return JSONObject.fromObject(Cfg).toString();
	}
	
	public static String getNtpDebug(){
		Map<String,String> Cfg = new HashMap<String, String>(){{
			put("NTP_Server_IP","172.31.4.230");
			put("NTP_Server_Port","8001");			
		}};
		
		return JSONObject.fromObject(Cfg).toString();
	}
	
	public static String getDataServerDebug(){
		int i = 0;
		List<Map<String,String>> list = new LinkedList<Map<String,String>>(){};
		
		Map<String,String> Cfg = new HashMap<String, String>(){{
			put("IPAddress","172.31.4.230");
			put("IPAddress","172.31.4.230");
			put("Port","8000");
			put("Protocolmode","1");
		}};
		for(i=0;i<4;i++){
			Cfg = new HashMap<String, String>(){};
			Cfg.put("DataServerID",i+"");
			Cfg.put("IPAddress","172.31.4."+i);
			Cfg.put("Port","800"+i);
			Cfg.put("Protocolmode","1");
			list.add(Cfg);
		}
		return JSONArray.fromObject(list).toString();		
	}
	
	public static String getIm6000Debug(){
		Map<String,String> Cfg = new HashMap<String, String>(){{
			put("UpgradeNum","3");
			put("DisplayNum","4");
			for(int i = 101;i < 116;i++){
				put("T"+i,i+"");
			}			
		}};
		
		return JSONObject.fromObject(Cfg).toString();
	}
}
