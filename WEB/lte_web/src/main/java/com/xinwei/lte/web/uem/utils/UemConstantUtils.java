package com.xinwei.lte.web.uem.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

import com.xinwei.lte.web.uem.model.ParaConverter;

public class UemConstantUtils {
	//模块名 
	public static final String MODEL_CONFIG = "config";	
	
	
	//UE配置操作
	
	public static final String ACTION_CONFIGUE_ADD = "add ue";
	
	public static final String ACTION_CONFIGUE_UPDATE = "update ue";
	
	public static final String ACTION_CONFIGUE_DELETE = "delete ue";
	
	public static final String ACTION_CONFIGUE_REBOOT = "reboot ue";
	
	public static final String ACTION_CONFIGUE_DATASWITCH = "dataswitch ue";
	
	public static final String ACTION_CONFIGUE_ALARMSWITCH = "alarmswitch ue";
	
	public static final String ACTION_CONFIGUE_NTPSWITCH = "ntpswitch ue";
	
	public static final String ACTION_CONFIGALLUE_UPDATE = "update globalue";
	
	public static final String ACTION_CONFIGALLUE_FTP = "update ftp";
	
	public static final String ACTION_CONFIGALLUE_NTP = "update ntp";
	
	public static final String ACTION_CONFIGALLUE_DATASERVER = "update dataserver";
	
	public static final String ACTION_CONFIGALLUE_DELDATASERVER = "delete dataserver";
	
	public static final String ACTION_CONFIGALLUE_ADDDATASERVER = "add dataserver";
	
	public static final String ACTION_CONFIGALLUE_IMD6000 = "update imd6000";
	
	//UE查询操作
	
	public static final String ACTION_QUERYALLUE_QUERY = "list ue";
	
	public static final String ACTION_QUERYALLUE_FTP = "list ftp";
	
	public static final String ACTION_QUERYALLUE_NTP = "list ntp";
	
	public static final String ACTION_QUERYALLUE_DATASERVER = "list dataserver";
	
	public static final String ACTION_QUERYALLUE_IMD6000 = "list imd6000";
	
	public static final String ACTION_QUERYALLUE_ALARM = "list alarm";	
	
	public static final String ACTION_CONFIRM_ALARM  = "confirm alarm";
	
	public static final String ACTION_RESTORE_ALARM = "restore alarm";
	
	//UE版本操作
	public static final String ACTION_UEVERSION_UPLOAD = "upload ue";
	
	public static final String ACTION_UEVERSION_QUERY = "findversion ue";
	
	public static final String ACTION_UEVERSION_UPGRADE = "upgrade ue";
	
	public static final String ACTION_CONFIGUE_DELTET = "delversion ue";
	
	//UE动态
	public static final String ACTION_PERFORM = "perform ue";
	
	public static final String ACTION_DYNAMIC = "dynamic ue";
	
	//GIS
	public static final String ACTION_QUERYGIS_INIT = "init gis";	
	
	public static final String ACTION_QUERYGIS_DEVINFO = "listdev gis";
	
	public static final String ACTION_QUERYGIS_UEINFO = "listue gis";
	
	public static final String ACTION_QUERYGIS_ENBINFO = "listenb gis";
	
	public static final String ACTION_QUERYGIS_STAT = "querystat gis";
	
	public static final String ACTION_CONFIGGIS_INIT = "update gis";
	
	//告警
	public static final String ACTION_QUERYALARM_CURRENT = "listcurrent alarm";
	
	public static final String ACTION_QUERYALARM_HISTORY = "listhistory alarm";
	
	public static final String ACTION_EXPORT_CURRENTALARM = "exportcurrent alarm";
	
	public static final String ACTION_EXPORT_HISTORYALARM = "exporthistory alarm";
	
	//内部消息定义
	
	public static final int MSG_INFO_CONFIG = 1;
	
	public static final int MSG_INFO_STATIC = 2;
	
	//页面要展示的信息列表

	public static final HashMap<String, ParaConverter> DISPLAY_STATIC = new HashMap<String, ParaConverter>(){
		{
			put("UEID",new ParaConverter<Integer>(){

				@Override
				public String convert(Integer Para) {
					if(Para >0){
						return Para+"";
					}
					return "";
				}

				
								
			});
			
			put("IMSI",new ParaConverter<String>(){

				@Override
				public String convert(String Para) {
					if(Para != null && Para.length() !=0){
						return Para;
					}
					return "无";
				}				
			});
			
			put("Serial",new ParaConverter<String>(){

				@Override
				public String convert(String Para) {
					if(Para != null && Para.length() !=0){
						return Para;
					}
					return "无";
				}				
			});
			
			put("BussinessType",new ParaConverter<Integer>(){

				@Override
				public String convert(Integer Para) {
			
					int BussinessID = Para;
					switch(BussinessID){						
					case 1:
						return (String)"集抄";
					case 2:
						return (String)"配网自动化";
					case 3:
						return (String)"负控";
					case 4:
						return (String)"视频";
					case 5:
						return (String)"其它";
					default:
						return (String)"未知类型";
					}					
			
				}				
			});
			
			put("Version",new ParaConverter<String>(){

				@Override
				public String convert(String Para) {
					if(Para != null && Para.length() !=0){
						return Para;
					}
					return "无";
				}				
			});
			
			put("IP",new ParaConverter<String>(){

				@Override
				public String convert(String Para) {
					if(Para != null && Para.length() !=0){
						return Para;
					}
					return "无";
				}				
			});
			
			put("Location",new ParaConverter<String>(){

				@Override
				public String convert(String Para) {
					if(Para != null && Para.length() !=0){
						return Para;
					}
					return "无";
				}				
			});

		
	}
		};

	public static final String[] DISPLAY_CONFIG = {"UEID","IMSI", "BussinessType", "Location", "Latitude", "Longitude", "DataReportPeriod", "ControlReportPeriod", "BitRate", "DataBit", "StopBit", "CheckBit", "AlarmMaskBit","reportSwitch","NTPSwitch","DataServerID"};
}
