/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2013-12-28	|  yinyuelin 	    |  create the file                       
 */

package com.xinwei.lte.web.lte.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.lte.model.CallQos;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.QosModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * lte系统配置呼叫QOSaction
 * 
 * <p>
 * lte系统配置QOSaction
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class UserCallQosAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	private OnlinePage onlinePage;
	
	private CallQos callQos;
	
	private List<CallQos> callQosList;
	//记录日志
	private static Logger logger = LoggerFactory.getLogger(UserCallQosAction.class);

	private String showMessage = "暂无相关数据";
	/**
	 * 跳转到QOS页面
	 * @return
	 */
	public String turntoCallQos(){
		logger.debug("turntoQos - start");
		callQosList = new ArrayList<CallQos>();
		try{
			if(null == onlinePage){
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);				
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			//map.put("ipId", systemAddressModel.getIp_id());
			Map<String,Object> resultMap = ossAdapter.invoke(0xb1, 0x05, map);			
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				return "toCallQos";
			}
			int lteTotalQueryCount = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			if(lteTotalQueryCount%LteConstant.PageSize == 0){
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize);
			}else{
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize+1);
			}	
			
			List<Map> resultList = (List<Map>) resultMap.get("callqosInfo");
			if(resultList != null){
				for(Map rMap : resultList){
					callQos = new CallQos();
					if(rMap.get("callqosType") != null){
						callQos.setCallqos_type(((String)rMap.get("callqosType")).trim());
					}
					if(rMap.get("callqosQci") != null){
						callQos.setCallqos_qci(((String)rMap.get("callqosQci")).trim());
					}
					if(rMap.get("callqosUplinkMbr") != null){
						callQos.setCallqos_uplink_mbr(((String)rMap.get("callqosUplinkMbr")).trim());
					}
					if(rMap.get("callqosDownlinkMbr") != null){
						callQos.setCallqos_downlink_mbr(((String)rMap.get("callqosDownlinkMbr")).trim());
					}
					if(rMap.get("callqosUplinkGbr") != null){
						callQos.setCallqos_uplink_gbr(((String)rMap.get("callqosUplinkGbr")).trim());
					}
					if(rMap.get("callqosDownlinkGbr") != null){
						callQos.setCallqos_downlink_gbr(((String)rMap.get("callqosDownlinkGbr")).trim());
					}
				
					callQosList.add(callQos);
				}
			}else{
				if(onlinePage.getCurrentPageNum() == 1 ){
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}
			}
			
		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("turntoQos error:"+e.toString());
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			showMessage = e.getMessage();
		}
		logger.debug("turntoQos - end");
		return "toCallQos";
	}

	/**
	 * 跳转到增加页面
	 * @return
	 */
	public String turntoCallQosAdd(){
		
		return "toCallQosAdd";
	}
	
	/**
	 * 增加
	 * @return
	 */
	public String addCallQos(){
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("callqosType", callQos.getCallqos_type());
			map.put("callqosQci",callQos.getCallqos_qci());
			map.put("callqosUplinkMbr",callQos.getCallqos_uplink_mbr());
			map.put("callqosDownlinkMbr", callQos.getCallqos_downlink_mbr());
			map.put("callqosUplinkGbr",callQos.getCallqos_uplink_gbr());
			map.put("callqosDownlinkGbr",callQos.getCallqos_downlink_gbr());
			Map<String,Object> resultMap = ossAdapter.invoke(0xb1, 0x01, map);
			String flag = (String) resultMap.get("lteFlag");
			if("0".equals(flag)){
				json.put("status", 0);
				ajaxMethod(json.toString());
			}else{
				json.put("status", 1);
				json.put("message", LteFlag.flagReturn(flag));
				ajaxMethod(json.toString());
			}
		}catch(NoAuthorityException e){
			json.put("status", 1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
			logger.error("addSysAddress error:"+e);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return NONE;
	}
	/**
	 * 跳转到修改QOS
	 * @return
	 */
	public String toModifyCallQos(){
		logger.debug("toModifyQos");
	
		return "toModifyCallQos";
	}
	
	/**
	 * 修改QOS
	 * @return
	 */
	public String modifyCallQos(){
		logger.debug("modifyQos-start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("callqosType", callQos.getCallqos_type());
			map.put("callqosQci",callQos.getCallqos_qci());
			map.put("callqosUplinkMbr",callQos.getCallqos_uplink_mbr());
			map.put("callqosDownlinkMbr", callQos.getCallqos_downlink_mbr());
			map.put("callqosUplinkGbr",callQos.getCallqos_uplink_gbr());
			map.put("callqosDownlinkGbr",callQos.getCallqos_downlink_gbr());
			Map<String,Object> resultMap = ossAdapter.invoke(0xb1, 0x03, map);
			String flag = (String) resultMap.get("lteFlag");
			if("0".equals(flag)){
				json.put("status", 0);
				ajaxMethod(json.toString());
			}else{
				json.put("status", 1);
				json.put("message", LteFlag.flagReturn(flag));
				ajaxMethod(json.toString());
			}
		}catch(NoAuthorityException e){
			json.put("status", 1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
			logger.error("addSysAddress error:"+e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("modifyQos error:"+e.toString());
		}
		logger.debug("modifyQos-end");
		return NONE;
	}
	
	/**
	 * 删除系统地址
	 * @return
	 */
	public String deleteCallQos(){
		logger.debug("deleteSysAddress-start");
		JSONObject json = new JSONObject();
		try{
			
			String callqos_type = callQos.getCallqos_type();
			String[] callqos_typeArray = callqos_type.split(",");
			if(callqos_typeArray.length == 1){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("callqosType", callqos_type);
				Map<String,Object> resultMap = ossAdapter.invoke(0xb1, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					return NONE;
				}
			}else{
				for(String str : callqos_typeArray){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("callqosType", str);
					Map<String,Object> resultMap = ossAdapter.invoke(0xb1, 0x02, map);
					String flag = (String) resultMap.get("lteFlag");
					if(!"0".equals(flag)){
						json.put("status", 1);
						json.put("message", LteFlag.flagReturn(flag));
						ajaxMethod(json.toString());
						return NONE;
					}
				}
			}
			
			json.put("status", 0);
			ajaxMethod(json.toString());
		}catch(NoAuthorityException e){
			json.put("status", 1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
			logger.error("addSysAddress error:"+e);
		}catch(Exception e){
			e.printStackTrace();
			ajaxMethod("error");
			logger.error("deleteSysAddress error:"+e.toString());
		}
		logger.debug("deleteSysAddress-end");
		return NONE;
	}
	
	//异步请求返回字符串
	private void ajaxMethod(String content){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/jsp; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(content);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null)
				out.close();
		}

	}

	//异步请求返回单个对象
	private void ajaxJsonMethod(Object content){
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
	
	//异步请求返回数组
	private void ajaxArrayMethod(Object content){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/jsp; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			
			JSONArray object = new JSONArray();
			object = JSONArray.fromObject(content);			
			out.println(object.toString());
			out.flush();
		} catch (Exception e){
			e.printStackTrace();
		} finally{
			if (out != null)
				out.close();
		}
	}

	public OnlinePage getOnlinePage()
	{
		return onlinePage;
	}

	public void setOnlinePage(OnlinePage onlinePage)
	{
		this.onlinePage = onlinePage;
	}

	public CallQos getCallQos()
	{
		return callQos;
	}

	public void setCallQos(CallQos callQos)
	{
		this.callQos = callQos;
	}

	public List<CallQos> getCallQosList()
	{
		return callQosList;
	}

	public void setCallQosList(List<CallQos> callQosList)
	{
		this.callQosList = callQosList;
	}

	public String getShowMessage()
	{
		return showMessage;
	}

	public void setShowMessage(String showMessage)
	{
		this.showMessage = showMessage;
	}
		
}
