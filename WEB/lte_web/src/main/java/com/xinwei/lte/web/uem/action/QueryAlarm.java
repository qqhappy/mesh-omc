package com.xinwei.lte.web.uem.action;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.model.AlarmListModel;
import com.xinwei.lte.web.enb.util.Util;
import com.xinwei.lte.web.uem.utils.AbstractInnerMessage;
import com.xinwei.lte.web.uem.utils.RPCClientProxy;
import com.xinwei.lte.web.uem.utils.UemConstantUtils;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.system.action.web.WebConstants;

public class QueryAlarm extends ActionSupport{
	
	private RPCClientProxy Client;
	/**
	 * 复选框状态
	 */
	private String checkStateArray;	

	/**
	 * 0:确认 1:恢复
	 */
	private String alarmOperType;

	/**
	 * 告警ID
	 */
	private String alarmIdArray;
	
	/**
	 * 异常
	 */
	private String error = "";
	
	private String submitObj;
	/**
	 * 查询到的告警集合
	 */
	private AlarmListModel alarmListModel = new AlarmListModel();

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String queryCurrentAlarm() throws Exception {
		JSONObject Res = new JSONObject();
		JSONObject AlarmContent = new JSONObject();
		JSONObject ResJson = null;
/*		queryAlarmDebug inst = new queryAlarmDebug();
		String ResJson = inst.getAlarm(10);
		JSONObject Res = new JSONObject();
		
		Res.put("alarmListModel",ResJson);*/
		//确认
		if (Integer.parseInt(alarmOperType) == 0 || 
				Integer.parseInt(alarmOperType) == 1){
			
			LoginUser loginUser = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT));
			JSONObject JsonParas = new JSONObject();			
			JsonParas.put("alarmId", alarmIdArray);
			JsonParas.put("username", loginUser.getUsername());
			
			//确认
			if (Integer.parseInt(alarmOperType) == 0){		
				ResJson = JSONObject.fromObject(Client.SendMes(UemConstantUtils.MODEL_CONFIG, UemConstantUtils.ACTION_CONFIRM_ALARM,
						JsonParas.toString()));
			}
			
			//恢复
			if (Integer.parseInt(alarmOperType) == 1){
				ResJson = JSONObject.fromObject(Client.SendMes(UemConstantUtils.MODEL_CONFIG, UemConstantUtils.ACTION_RESTORE_ALARM,
						JsonParas.toString()));			
			}
		}
				
		try {
			ResJson = JSONObject.fromObject(Client.SendMes(UemConstantUtils.MODEL_CONFIG, UemConstantUtils.ACTION_QUERYALARM_CURRENT, submitObj));
			if(!ResJson.get("result").equals("success")){
				Res.put("result", "-1");
				Res.put("Message", "当前告警查询失败");
			}
			else{
				Res.put("result", "0");
				Res.put("alarmListModel",ResJson.get("DataBody"));				
			}
			Res.put("checkStateArray",checkStateArray);
			Util.ajaxSimpleUtil(Res.toString());
		} catch (Exception e) {

			e.printStackTrace();
			Res.put("result", "-1");			
			Res.put("Message", e.getLocalizedMessage());
			Util.ajaxSimpleUtil(Res.toString());
			return ERROR;
		}
	
		
		return NONE;
	}
	public String queryHistoryAlarm(){
		JSONObject Res = new JSONObject();
		JSONObject AlarmContent = new JSONObject();
		JSONObject ResJson = null;
		/*queryAlarmDebug inst = new queryAlarmDebug();
		String ResJson = inst.getAlarm(10);
		JSONObject Res = new JSONObject();
		
		Res.put("alarmListModel",ResJson);		*/
		try {
			ResJson = JSONObject.fromObject(Client.SendMes(UemConstantUtils.MODEL_CONFIG, UemConstantUtils.ACTION_QUERYALARM_HISTORY, submitObj));
			if(!ResJson.get("result").equals("success")){
				Res.put("result", "-1");
				Res.put("Message", "当前告警查询失败");
			}
			else{
				Res.put("result", "0");
				Res.put("alarmListModel",ResJson.get("DataBody"));				
			}					
			Util.ajaxSimpleUtil(Res.toString());
		} catch (Exception e) {

			e.printStackTrace();
			Res.put("result", "-1");			
			Res.put("Message", e.getLocalizedMessage());
			Util.ajaxSimpleUtil(Res.toString());
			return ERROR;
		}
		
		return NONE;
	}
	
	public String turnUECurrentAlarm(){
		return SUCCESS;
	}
	public String turnUEHistoryAlarm(){
		return SUCCESS;
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

	public AlarmListModel getAlarmListModel() {
		return alarmListModel;
	}

	public void setAlarmListModel(AlarmListModel alarmListModel) {
		this.alarmListModel = alarmListModel;
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
	public String getAlarmOperType() {
		return alarmOperType;
	}
	public void setAlarmOperType(String alarmOperType) {
		this.alarmOperType = alarmOperType;
	}
	public String getAlarmIdArray() {
		return alarmIdArray;
	}
	public void setAlarmIdArray(String alarmIdArray) {
		this.alarmIdArray = alarmIdArray;
	}


}
