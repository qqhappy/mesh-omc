package com.xinwei.lte.web.uem.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;


import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.uem.utils.RPCClientProxy;
import com.xinwei.lte.web.uem.utils.UEAlarmDataExeclUtils;
import com.xinwei.lte.web.uem.utils.UemConstantUtils;

public class ExportUEAlarm extends ActionSupport{
	
	private RPCClientProxy Client;
	/**
	 * 当前告警or历史告警标识
	 */
	private int timeFlag;
	public static int CURRENT_ALARM = 0;
	public static int HISTORY_ALARM = 1;
	
	/**
	 * 复选框状态
	 */
	private String checkStateArray;	

	/**
	 * 异常
	 */
	private String error = "";
	
	private String submitObj;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private UEAlarmDataExeclUtils utils = new UEAlarmDataExeclUtils(); 
	
	public String exportAlarmData(){
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String now = sf.format(new Date());
		
		String fileName = "UEAlarmExport" + now + ".xls"; 
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("Application/msexcel;charset=utf-8");		
		response.setHeader("Content-disposition", "attachment;filename="
				+ fileName);
		
		PrintWriter out = null;
		List<JSONObject> alarmObj = queryUEAlarmData();
		try {
			ServletOutputStream outPutStream = response.getOutputStream();
			utils.exportUEAlarm(alarmObj, outPutStream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("UE_ALARM_EXPORT: UE Export error!");
		}
		
		return NONE;
	}
	
	private List<JSONObject> queryUEAlarmData(){	
		JSONObject ResJson = null;	
		int AlarmIndex = 0;
		
		try {
			submitObj = java.net.URLDecoder.decode(submitObj, "UTF-8");
			// 查询告警信息
			if (timeFlag == CURRENT_ALARM) {
				ResJson = JSONObject.fromObject(Client.SendMes(UemConstantUtils.MODEL_CONFIG, UemConstantUtils.ACTION_EXPORT_CURRENTALARM, submitObj));
			}else if(timeFlag == HISTORY_ALARM){
				ResJson = JSONObject.fromObject(Client.SendMes(UemConstantUtils.MODEL_CONFIG, UemConstantUtils.ACTION_EXPORT_HISTORYALARM, submitObj));
			}
			
			if(!ResJson.get("result").equals("success")){
				return null;
			}
			else{
				JSONObject body = (JSONObject)(ResJson.get("DataBody"));
				if (body == null || body.isEmpty()){
					logger.warn("body is empty");
					return null;
				}
				List alarmListObj = (List<JSONObject>)(body.get("alarmList"));
				if(alarmListObj ==null || alarmListObj.isEmpty()){
					logger.warn("alarmListObj is empty");
					return null;
				}else{
					return  alarmListObj;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	


	public String getCheckStateArray() {
		return checkStateArray;
	}

	public void setCheckStateArray(String checkStateArray) {
		this.checkStateArray = checkStateArray;
	}

	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	public void setSubmitObj(String submitObj) {
		this.submitObj = submitObj;
	}
	public String getSubmitObj() {
		return submitObj;
	}
	public void setClient(RPCClientProxy client) {
		Client = client;
	}
	public RPCClientProxy getClient() {
		return Client;
	}

	public int getTimeFlag() {
		return timeFlag;
	}

	public void setTimeFlag(int timeFlag) {
		this.timeFlag = timeFlag;
	}


}
