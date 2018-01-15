package com.xinwei.lte.web.uem.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xinwei.lte.web.uem.model.ParaConverter;
import com.xinwei.lte.web.uem.model.UEConfigModel;
import com.xinwei.lte.web.uem.utils.AbstractInnerMessage;
import com.xinwei.lte.web.uem.utils.AbstractMessage;
import com.xinwei.lte.web.uem.utils.RPCClient;
import com.xinwei.lte.web.uem.utils.UemConstantUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.enb.util.Util;

public class UEManage extends ActionSupport {	

	private List<Map<String,String>> UEList;	
	private String error = "";
	private Map<String,String> UEMap = null;	
	private String submitMap;
	private RPCClient Client;
	private String UEQueryFilter = null;
	private String currentPage = "1";
	private String UECountPerPage = "15";
	private String TotalPage = null;
	private String UEIDList;
	private String RecordNum;
	private LinkedList<Map<String, String>> DataServerList = new LinkedList<Map<String,String>>(){};
	

	private Log log = LogFactory.getLog(UEManage.class);
	
	private Map<String,String> UEConfigInst;
	
	/**
	 * 
	 * 返回左侧边栏页面
	 * 
	 * @author xiaoxu
	 * 
	 */

	public String TurnToLeftMenu(){
		return SUCCESS;
	}
	
	/**
	 * 
	 * 返回UE查询页面
	 * 
	 * @author xiaoxu
	 * 
	 */
	public String TurnToQueryUEPage(){
		//TODO:请求Config返回UEList
		
		try {			
			AbstractMessage<String> ResMes = QueryStaticUeInfo();
			HandleResultInfo(UemConstantUtils.MSG_INFO_STATIC,ResMes.getBody());
			
			/*UEManageDebug UEManageDebugInst = new UEManageDebug();
			HandleResultInfo(UemConstantUtils.MSG_INFO_STATIC, UEManageDebugInst.QueryStaticUeInfoDebug(getCurrentPage(),getUECountPerPage()));
			*/
			
		} catch (Exception e) {
		
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}	
	
		return SUCCESS;		
	}

	
	
	/**
	 * 
	 * 返回UE新增页面
	 * 
	 * @author xiaoxu
	 * 
	 */
	public String TurnToAddUEPage(){

		try {
			GetDataServerCfg();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			error = "DataServer查询失败:"+e.getLocalizedMessage();
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	/**
	 * 
	 * 返回UE修改页面
	 * @author xiaoxu
	 * 
	 * 
	 */
	public String TurnToModifyUEPage(){
		try {
			AbstractMessage<String> ResMes = QueryStaticUeInfo();
			HandleResultInfo(UemConstantUtils.MSG_INFO_CONFIG, ResMes.getBody());
			GetDataServerCfg();
			/*UEManageDebug UEManageDebugInst = new UEManageDebug();
			HandleResultInfo(UemConstantUtils.MSG_INFO_CONFIG, UEManageDebugInst.QueryConfigUeInfoDebug());
			*/
			
		} catch (Exception e) {

			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return SUCCESS;
		
	}
	
	
	
	/**
	 * 
	 * 根据筛选条件，查询UE性能信息 
	 * @author xiaoxu 
	 * @return 
	 * 
	 */
	public void QueryPerformanceUeInfo(){
		/*UEManageDebug UEManageDebugInst = new UEManageDebug();
		UEManageDebugInst.QueryDynamicUeInfoDebug();
		return;
		*/
		
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		AbstractInnerMessage ResMes = null;
		JSONObject QueryParas = new JSONObject();
		Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
		Mes.setMessageId(UemConstantUtils.ACTION_PERFORM);
		
		if(UEIDList !=null && UEIDList.length() !=0){
			QueryParas.put("UEList",JSONArray.fromObject(UEIDList.split(",")));
			QueryParas.put("RecordNum",RecordNum);			
		}
		else if(UEIDList ==null && currentPage !=null){
			QueryParas.put("page_num", currentPage);
			QueryParas.put("count", UECountPerPage);
		}
		else if(UEIDList ==null && currentPage ==null){
			log.warn("PageNum or UECountPerPage is null,Set Default PageNum=1,UECountPerPage=20");
			currentPage = "1";
			UECountPerPage = "15";
		}		
		
		if(UEQueryFilter !=null){
			QueryParas.put("filter", UEQueryFilter);
		}
		
		Mes.setBody(QueryParas.toString());
		try {
			ResMes = SendMes(Mes);
		} catch (Exception e) {
			JSONObject ResJson = new JSONObject();
			
			e.printStackTrace();
			ResJson.put("result","fail");
			ResJson.put("DataBody",  e.getLocalizedMessage());
			ResMes.setBody(ResJson.toString());
		}	
		finally{
			Util.ajaxSimpleUtil(ResMes.getBody());
		}
		
		
	}


	/**
	 * 
	 * 根据筛选条件，查询UE静态信息 
	 * @author xiaoxu 
	 * @return 
	 * 
	 */
	private AbstractInnerMessage QueryStaticUeInfo() throws Exception{		
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		AbstractInnerMessage ResMes = null;
		JSONObject QueryParas = new JSONObject();
		
		Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
		Mes.setMessageId(UemConstantUtils.ACTION_QUERYALLUE_QUERY);
		if(currentPage ==null ||UECountPerPage ==null){
			log.warn("PageNum or UECountPerPage is null,Set Default PageNum=1,UECountPerPage=20");
			currentPage = "1";
			UECountPerPage = "15";
		}
		
		QueryParas.put("page_num", currentPage);
		QueryParas.put("count", UECountPerPage);
		
		if(UEQueryFilter !=null){
			QueryParas.put("filter",UEQueryFilter);
		}
		
		Mes.setBody(QueryParas.toString());
		ResMes = SendMes(Mes);
		return ResMes;		
	}
	/**
	 * 
	 * 处理UE结果信息 
	 * @author xiaoxu 
	 * @throws Exception 
	 * 
	 */
	private void HandleResultInfo(int msgInfo, String body) throws Exception {
		
		HashMap<String, ParaConverter> HandleMap = null;	
		List<Map<String,String>> UEList = new ArrayList();
		Map<String,String> UEMap = null;
		int UEIndex = 0;
		int ParaIndex = 0;
		List UEListJson = null;
		
		if(body ==null || body.length()==0){
			log.warn("UE Dynamic Info is empty");
			return;
		}
		
		
		switch(msgInfo){
			case UemConstantUtils.MSG_INFO_STATIC:
				HandleMap = UemConstantUtils.DISPLAY_STATIC;				
				JSONObject Obj = JSONObject.fromObject(body);
				if(Obj.isEmpty()){
					log.warn("UEInfoObject is empty");
					return;
				}
				if(!(Obj.get("result").equals("success"))){
					log.warn("查询错误");					
					return;
				}
				Obj = (JSONObject)(Obj.get("DataBody"));
				
				//处理统计部分
				JSONObject UEStat = (JSONObject)(Obj.get("Statistics"));
				if(UEStat ==null ){
					log.warn("Statistics is empty");
					return;
				}				
				String TotalPage = UEStat.get("TotalPage")+"";
				if(TotalPage !=null && TotalPage.length() !=0){
					setTotalPage(TotalPage);
					setCurrentPage(currentPage);
				}
				
				//处理UE部分
				UEListJson = (List<JSONObject>)Obj.get("UE");	
				if(UEListJson ==null || UEListJson.size()==0){
					log.warn("UEList is empty");
					return;
				}				 
				
				for(UEIndex=0;UEIndex<UEListJson.size();UEIndex++){
					UEMap =  new HashMap();
					JSONObject UEInstJson = (JSONObject)UEListJson.get(UEIndex);
					for(Map.Entry<String, ParaConverter> entry:HandleMap.entrySet()){
						String ParaKey = entry.getKey();
						Object ParaValStr = UEInstJson.get(ParaKey);
						
						String ParaVal = entry.getValue().convert((ParaValStr instanceof JSONNull)?null:ParaValStr);
						UEMap.put(ParaKey,ParaVal);
					}
					UEList.add(UEMap);
				}
				setUEList(UEList);				
				
				break;
			case UemConstantUtils.MSG_INFO_CONFIG:
				
				JSONObject ResObj = (JSONObject)JSONObject.fromObject(body);
				if(!ResObj.get("result").equals("success")){
					log.warn("Config result is fail!");
					return;
				}
				JSONObject UEInstJson = (JSONObject) JSONArray.fromObject(JSONObject.fromObject(ResObj.get("DataBody")).get("UE")).get(0);
				String[] DisplayParas = UemConstantUtils.DISPLAY_CONFIG;
				Map<String,String> UEConfigInst = new HashMap<String, String>(){};
				for(ParaIndex=0;ParaIndex < DisplayParas.length;ParaIndex++){
					String ParaName = DisplayParas[ParaIndex];
					String ParaVal = UEInstJson.get(ParaName)+"";
					if(ParaVal==null||ParaVal.length()==0){
						log.warn("ParaVal is null or empty");
						ParaVal = "";
					}
					UEConfigInst.put(ParaName,ParaVal);
				}
				setUEConfigInst(UEConfigInst);
				break;
		}
		
		
	}
	
	/**
	 * 
	 * 修改UE配置接口 
	 * @author xiaoxu 
	 * @throws Exception 
	 * 
	 */
	public String ConfigUE(){
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		AbstractInnerMessage ResMes = null;
		JSONObject QueryParas = new JSONObject();
	
		if(submitMap ==null){
			error = "提交的结构为空";
			return ERROR;
		}
		
		if(Client ==null){
			error = "无法和后台建立连接";
			return ERROR;
		}
		Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
		JSONObject UEConfigJson = JSONObject.fromObject(submitMap);
		
		if(UEConfigJson.get("UEID") ==null){
			//UE新增			
			Mes.setMessageId(UemConstantUtils.ACTION_CONFIGUE_ADD);			
		}
		else{
			//UE修改
			Mes.setMessageId(UemConstantUtils.ACTION_CONFIGUE_UPDATE);
		}
		
		
		Mes.setBody(UEConfigJson.toString());
		try {
			ResMes = SendMes(Mes);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		
		return SUCCESS;
	}
	/**
	 * 
	 * 删除UE接口 
	 * @author xiaoxu 
	 * 
	 */
	
	public void DeleteUE(){
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		AbstractInnerMessage ResMes = null;
		JSONObject QueryParas = new JSONObject();
		
		Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
		Mes.setMessageId(UemConstantUtils.ACTION_CONFIGUE_DELETE);
		QueryParas.put("UEID", JSONArray.fromObject(UEIDList.split(",")));
		QueryParas.put("all", false);
		Mes.setBody(QueryParas.toString());
		try {
			ResMes = SendMes(Mes);
			Util.ajaxSimpleUtil(ResMes.getBody());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return;
		}
		
		return;
	}
	
	/**
	 * 
	 * 重启UE接口 
	 * @author xiaoxu 
	 * 
	 */
	
	public void RebootUE(){
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		AbstractInnerMessage ResMes = null;
		JSONObject QueryParas = new JSONObject();
		
		Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
		Mes.setMessageId(UemConstantUtils.ACTION_CONFIGUE_REBOOT);
		QueryParas.put("UEID", JSONArray.fromObject(UEIDList.split(",")));
		QueryParas.put("all", false);
		Mes.setBody(QueryParas.toString());
		try {
			ResMes = SendMes(Mes);
			Util.ajaxSimpleUtil(ResMes.getBody());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return;
		}
		
		return;
	}
	
	
	/*
	 * 查询DataServer，TODO:重构
	 * */
	private void GetDataServerCfg() throws Exception{
		//return ServerConfigDebug.getDataServerDebug();
		int index = 0;
		AbstractInnerMessage Mes = new AbstractInnerMessage();
		AbstractInnerMessage ResMes = null;
		JSONObject QueryParas = new JSONObject();
		Mes.setTarget(UemConstantUtils.MODEL_CONFIG);
		Mes.setMessageId(UemConstantUtils.ACTION_QUERYALLUE_DATASERVER);
		
		ResMes = SendMes(Mes);
		
		JSONObject ResObj = JSONObject.fromObject(ResMes.getBody());
		if(!ResObj.get("result").equals("success")){
			log.warn("FtpQuery result is fail!");
			throw new Exception("DataServer查询失败");
		}
		
		JSONArray DataServerJsonArr = (JSONArray)ResObj.get("DataBody");
		Map<String,String> DataServer = null;		
		
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
	private void InsertKVToMap(JSONObject Json, Map<String, String> Map) {	
		Iterator it = null;
		it = Json.keys();
		while(it.hasNext()){
			String key = String.valueOf(it.next());
			String val = Json.get(key)+"";
			Map.put(key, val);
		}
	}
	/**
	 * 
	 * 升级UE接口 
	 * @author xiaoxu 
	 * 
	 */
	
	
	private AbstractInnerMessage SendMes(AbstractInnerMessage mes) throws Exception{
		try {
			log.info("[SendMes] MesType="+mes.getMessageId()+" Mesbody="+mes.getBody());
			AbstractInnerMessage Res = Client.InvokeMethod(mes);
			log.info("[SendMes] ResMesBody= "+Res.getBody());
			if(Res.getMessageId().equals("ERROR")){
				throw new Exception("错误："+Res.getBody());
			}
			return Res;
			
		} catch (Throwable e) {			
			throw new Exception("Config响应错误:"+e.getLocalizedMessage());
		}
		
	}
	
	public List<Map<String, String>> getUEList() {
		return UEList;
	}

	public void setUEList(List<Map<String, String>> uEList) {
		UEList = uEList;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Map<String, String> getUEMap() {
		return UEMap;
	}

	public void setUEMap(Map<String, String> uEMap) {
		UEMap = uEMap;
	}
	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	
	public void setClient(RPCClient client) {
		Client = client;
	}

	public RPCClient getClient() {
		return Client;
	}

	public void setUEIDList(String uEIDList) {
		UEIDList = uEIDList;
	}

	public String getUEIDList() {
		return UEIDList;
	}

	public void setUEConfigInst(Map<String,String> uEConfigInst) {
		UEConfigInst = uEConfigInst;
	}

	public Map<String,String> getUEConfigInst() {
		return UEConfigInst;
	}	

	public String getUEQueryFilter() {
		return UEQueryFilter;
	}

	public void setUEQueryFilter(String uEQueryFilter) {
		UEQueryFilter = uEQueryFilter;
	}

	public String getUECountPerPage() {
		return UECountPerPage;
	}

	public void setUECountPerPage(String uECountPerPage) {
		UECountPerPage = uECountPerPage;
	}

	public void setTotalPage(String totalPage) {
		TotalPage = totalPage;
	}

	public String getTotalPage() {
		return TotalPage;
	}

	public void setRecordNum(String recordNum) {
		RecordNum = recordNum;
	}

	public String getRecordNum() {
		return RecordNum;
	}

	public void setSubmitMap(String submitMap) {
		this.submitMap = submitMap;
	}

	public String getSubmitMap() {
		return submitMap;
	}

	public LinkedList<Map<String, String>> getDataServerList() {
		return DataServerList;
	}

	public void setDataServerList(LinkedList<Map<String, String>> dataServerList) {
		DataServerList = dataServerList;
	}


	
}
