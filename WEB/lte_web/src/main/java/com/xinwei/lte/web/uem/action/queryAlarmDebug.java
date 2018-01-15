package com.xinwei.lte.web.uem.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.janino.Java.NewAnonymousClassInstance;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class queryAlarmDebug {
	public String getAlarm(int UENum){
		JSONObject res = new JSONObject();
		JSONArray AlarmList = new JSONArray();
		JSONObject Alarm = new JSONObject();
		int count = 0;		
		
		Map<String,String> AlarmMap = new HashMap<String, String>(){{
			put("alarmContent","小区退服告警-手动闭塞");
			put("alarmDefId","562949953749057540");
			put("alarmLevel","1");
			put("alarmState","1");
			put("alarmTimes","1");
			put("alarmType","0");
			put("confirmState","0");
			put("confirmTime","0");
			put("confirmUser","0");
			put("IMSI","46000058122");
			put("id","2000578231");
			put("restoreFlag","0");
			put("restoreUser","2000578231");
	
		}};
		
		for(count=0;count < UENum;count++){
			Alarm.put("alarm",JSONObject.fromObject(AlarmMap));
			Alarm.put("confirmTimeString", null);
			Alarm.put("UEId", count);
			Alarm.put("firstAlarmTimeString", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
			AlarmList.add(Alarm);
		}		
		res.put("totalPage",10);
		res.put("alarmList",AlarmList);
		
		return res.toString();
	}
}
