package com.xinwei.lte.web.lte.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.domain.JSONResult;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.lte.model.HaInfo;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.oss.adapter.OssAdapter;

public class HaInfoAction extends ActionSupport{

	//记录日志
	private static Logger logger = LoggerFactory.getLogger(HaInfoAction.class);
	
	@Resource
	private OssAdapter ossAdapter;
	
	private JSONResult jsonResult;
	
	private List<HaInfo> haInfoList;
	
	private OnlinePage onlinePage = new OnlinePage();
	
	private HaInfo haInfo;
	
	/**
	 * 查询列表
	 * @return
	 */
	public String showConfigInfo(){
		
		jsonResult = new JSONResult();
		haInfoList = new ArrayList<HaInfo>();
		haInfo = new HaInfo();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			
			Map<String,Object> resultMap = ossAdapter.invoke(0xba, 0x05, map);
			
			//int lteTotalQueryCount = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			if(resultMap == null || resultMap.isEmpty()){
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				return SUCCESS;
			}
			
			if(resultMap.get("haTcn1000Type") != null){
				haInfo.setHaTcn1000Type(Integer.valueOf(resultMap.get("haTcn1000Type")+""));
			}
			
			if(resultMap.get("haEnable") != null){
				haInfo.setHaEnable(Integer.valueOf(resultMap.get("haEnable")+""));
			}
			
			if(resultMap.get("haSdcIp") != null){
				haInfo.setHaSdcIp(resultMap.get("haSdcIp")+"");
			}
			
			if(resultMap.get("haSdcPort") != null){
				haInfo.setHaSdcPort(Integer.valueOf(resultMap.get("haSdcPort")+""));
			}
			
			if(resultMap.get("haXgwIp") != null){
				haInfo.setHaXgwIp(resultMap.get("haXgwIp")+"");
			}
			
			if(resultMap.get("haXgwPort") != null){
				haInfo.setHaXgwPort(Integer.valueOf(resultMap.get("haXgwPort")+""));
			}
			
			if(resultMap.get("haRemoteSdcIp") != null){
				haInfo.setHaRemoteSdcIp(resultMap.get("haRemoteSdcIp")+"");
			}
			
			if(resultMap.get("haRemoteSdcPort") != null){
				haInfo.setHaRemoteSdcPort(Integer.valueOf(resultMap.get("haRemoteSdcPort")+""));
			}
			
			if(resultMap.get("haRemoteXgwIp") != null){
				haInfo.setHaRemoteXgwIp(resultMap.get("haRemoteXgwIp")+"");
			}
			
			if(resultMap.get("haRemoteXgwPort") != null){
				haInfo.setHaRemoteXgwPort(Integer.valueOf(resultMap.get("haRemoteXgwPort")+""));
			}
			haInfoList.add(haInfo);
			
		} catch (Exception e) {
			jsonResult.setErrorCode("-1");
			jsonResult.setErrorMsg(e.getMessage());
			logger.debug("showConfigInfo error.",e);
			e.printStackTrace();
		}
		
		return "SHOW_CONFIG_INFO";
	}
	
	/**
	 * 跳转至修改页面
	 * @return
	 */
	public String toModify(){
		
		try {
			haInfo = new HaInfo();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			Map<String,Object> resultMap = ossAdapter.invoke(0xba, 0x05, map);
			
			//int lteTotalQueryCount = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			
			if(resultMap.get("haTcn1000Type") != null){
				haInfo.setHaTcn1000Type(Integer.valueOf(resultMap.get("haTcn1000Type")+""));
			}
			
			if(resultMap.get("haEnable") != null){
				haInfo.setHaEnable(Integer.valueOf(resultMap.get("haEnable")+""));
			}
			
			if(resultMap.get("haSdcIp") != null){
				haInfo.setHaSdcIp(resultMap.get("haSdcIp")+"");
			}
			
			if(resultMap.get("haSdcPort") != null){
				haInfo.setHaSdcPort(Integer.valueOf(resultMap.get("haSdcPort")+""));
			}
			
			if(resultMap.get("haXgwIp") != null){
				haInfo.setHaXgwIp(resultMap.get("haXgwIp")+"");
			}
			
			if(resultMap.get("haXgwPort") != null){
				haInfo.setHaXgwPort(Integer.valueOf(resultMap.get("haXgwPort")+""));
			}
			
			if(resultMap.get("haRemoteSdcIp") != null){
				haInfo.setHaRemoteSdcIp(resultMap.get("haRemoteSdcIp")+"");
			}
			
			if(resultMap.get("haRemoteSdcPort") != null){
				haInfo.setHaRemoteSdcPort(Integer.valueOf(resultMap.get("haRemoteSdcPort")+""));
			}
			
			if(resultMap.get("haRemoteXgwIp") != null){
				haInfo.setHaRemoteXgwIp(resultMap.get("haRemoteXgwIp")+"");
			}
			
			if(resultMap.get("haRemoteXgwPort") != null){
				haInfo.setHaRemoteXgwPort(Integer.valueOf(resultMap.get("haRemoteXgwPort")+""));
			}
		} catch (Exception e) {
			logger.debug("toModifyPage error.",e);
			e.printStackTrace();
		}
		
		return "TO_MODIFY_PAGE";
	}
	
	/**
	 * 修改
	 * @return
	 */
	public String modify(){
		jsonResult = new JSONResult();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			/*map.put("haTcn1000Type", haInfo.getHaTcn1000Type());*/
			int haTcn1000Type = haInfo.getHaTcn1000Type();
			int haEnable = haInfo.getHaEnable();
			map.put("haEnable", haInfo.getHaEnable());
			if(haEnable == 1){
				if(haTcn1000Type == 0){
					map.put("haSdcIp", haInfo.getHaSdcIp());
					map.put("haRemoteSdcIp", haInfo.getHaRemoteSdcIp());
				}else{
					map.put("haSdcIp", haInfo.getHaSdcIp());
					map.put("haXgwIp", haInfo.getHaXgwIp());
					map.put("haRemoteSdcIp", haInfo.getHaRemoteSdcIp());
					map.put("haRemoteXgwIp", haInfo.getHaRemoteXgwIp());
				}
			}
			
			Map<String,Object> resultMap = ossAdapter.invoke(0xba, 0x03, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				jsonResult.setErrorCode("-1");
				jsonResult.setErrorMsg(LteFlag.flagReturn(flag));
			}
		} catch (Exception e) {
			logger.debug("add error.",e);
			e.printStackTrace();
			jsonResult.setErrorCode("-1");
			jsonResult.setErrorMsg(e.getMessage());
		}
		
		returnJsonObject(jsonResult);
		return NONE;
	}
	
	//异步请求返回单个对象
	private void returnJsonObject(Object content){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/jsp; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			
			JSONObject object = new JSONObject();
			object = JSONObject.fromObject(content);
			out.println(object.toString());
			out.flush();
		} catch (Exception e){
			e.printStackTrace();
		} finally{
			if (out != null)
				out.close();
		}
	}

	public JSONResult getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(JSONResult jsonResult) {
		this.jsonResult = jsonResult;
	}

	public List<HaInfo> getHaInfoList() {
		return haInfoList;
	}

	public void setHaInfoList(List<HaInfo> haInfoList) {
		this.haInfoList = haInfoList;
	}

	public HaInfo getHaInfo() {
		return haInfo;
	}

	public void setHaInfo(HaInfo haInfo) {
		this.haInfo = haInfo;
	}

	public OnlinePage getOnlinePage() {
		return onlinePage;
	}

	public void setOnlinePage(OnlinePage onlinePage) {
		this.onlinePage = onlinePage;
	}
	
}

