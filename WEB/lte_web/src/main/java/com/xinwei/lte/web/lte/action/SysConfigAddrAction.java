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
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.SystemAddressModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * lte系统配置系统地址action
 * 
 * <p>
 * lte系统配置系统地址action
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class SysConfigAddrAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	private OnlinePage onlinePage;
	
	private SystemAddressModel systemAddressModel;
	
	//保存查询条件
	private List<SystemAddressModel> systemAddressModelList;
	//记录日志
	private static Logger logger = LoggerFactory.getLogger(SysConfigAddrAction.class);
	
	private String showMessage = "暂无相关数据";
	
	/**
	 * 跳转到系统地址页面
	 * @return
	 */
	public String turntoSysAddress(){
		logger.debug("turntoSysAddress");
		systemAddressModelList = new ArrayList<SystemAddressModel>();
		try{
			
			Map<String,Object> map = new HashMap<String,Object>();
			if(null == onlinePage){
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);		
			}		
			/*if(null != systemAddressModel){
				if(0 != systemAddressModel.getFirst()){
					if(null != systemAddressModel.getIp_id()){
						map.put("ipID", systemAddressModel.getIp_id());
					}					
				}
			}else{
				systemAddressModel = new SystemAddressModel();
			}*/
			
			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			//map.put("ipID", systemAddressModel.getIp_id());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa6, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				return SUCCESS;
			}
			int lteTotalQueryCount = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			if(lteTotalQueryCount%LteConstant.PageSize == 0){
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize);
			}else{
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize+1);
			}	
			
			List<Map> resultList = (List<Map>) resultMap.get("ipInfo");
			SystemAddressModel sysAddressModel;
			if(null != resultList){
				for(Map rMap : resultList){
					sysAddressModel = new SystemAddressModel();
					if(rMap.get("ipID") != null){
						sysAddressModel.setIp_id(((String)rMap.get("ipID")).trim());
					}
					if(rMap.get("ipAddr") != null){
						sysAddressModel.setIp_addr(((String)rMap.get("ipAddr")).trim());
					}
					if(rMap.get("ipMask") != null){
						sysAddressModel.setIp_mask(((String)rMap.get("ipMask")).trim());
					}
					if(rMap.get("ipGateway") != null){
						sysAddressModel.setIp_gateway(((String)rMap.get("ipGateway")).trim());
					}
					if(rMap.get("ipComment") != null){
						sysAddressModel.setIp_comment(((String)rMap.get("ipComment")).trim());	
					}				
									
					systemAddressModelList.add(sysAddressModel);
				}
			}else{
				if(onlinePage.getCurrentPageNum() == 1 ){
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}	
			}			
		}catch(Exception e){
			e.printStackTrace();
			onlinePage.setTotalPages(1);
			onlinePage.setCurrentPageNum(1);
			logger.error("turntoSysAddress error:"+e);
			showMessage = e.getMessage();
		}
		return SUCCESS;
	}
	
	/**
	 * 跳转到系统地址配置页面
	 * @return
	 */
	public String turntoSysAddressAdd(){
		logger.debug("turntoSysAddressAdd");
		return SUCCESS;
	}
	
	/**
	 * 增加系统地址
	 * @return
	 */
	public String addSysAddress(){
		logger.debug("addSysAddress-start");
		JSONObject json = new JSONObject();
		try{
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ipID", systemAddressModel.getIp_id());
			map.put("ipAddr", systemAddressModel.getIp_addr());
			map.put("ipMask", systemAddressModel.getIp_mask());
			map.put("ipGateway", systemAddressModel.getIp_gateway());
			map.put("ipComment", systemAddressModel.getIp_comment());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa6, 0x01, map);
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
			logger.error("addSysAddress error:"+e);
		}
		logger.debug("addSysAddress-end");
		return NONE;
	}
	/**
	 * 跳转到修改系统地址
	 * @return
	 */
	public String toModifySysAddress(){
		logger.debug("tiModifySysAddress-start");
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ltePageSize", "1");
			map.put("ltePageIndex", "1");
			map.put("ipID", systemAddressModel.getIp_id());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa6, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				
				return SUCCESS;
			}
			
			List<Map> resultList = (List<Map>) resultMap.get("ipInfo");
			if(resultList.size() != 0){
				Map rMap = resultList.get(0);
				if(rMap.get("ipID") != null){
					systemAddressModel.setIp_id(((String)rMap.get("ipID")).trim());
				}
				if(rMap.get("ipAddr") != null){
					systemAddressModel.setIp_addr(((String)rMap.get("ipAddr")).trim());
				}
				if(rMap.get("ipMask") != null){
					systemAddressModel.setIp_mask(((String)rMap.get("ipMask")).trim());
				}
				if(rMap.get("ipGateway") != null){
					systemAddressModel.setIp_gateway(((String)rMap.get("ipGateway")).trim());
				}
				if(rMap.get("ipComment") != null){
					systemAddressModel.setIp_comment(((String)rMap.get("ipComment")).trim());
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("tiModifySysAddress error:"+e);
		}
		logger.debug("modifySysAddress-end");
		return SUCCESS;
	}
	/**
	 * 修改系统地址
	 * @return
	 */
	public String modifySysAddress(){
		logger.debug("modifySysAddress-start");
		JSONObject json = new JSONObject();
		try{			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ipID", systemAddressModel.getIp_id());
			map.put("ipAddr", systemAddressModel.getIp_addr());
			map.put("ipMask", systemAddressModel.getIp_mask());
			map.put("ipGateway", systemAddressModel.getIp_gateway());
			map.put("ipComment", systemAddressModel.getIp_comment());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa6, 0x03, map);
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
			logger.error("modifySysAddress error:"+e);
		}
		logger.debug("modifySysAddress-end");
		return NONE;
	}
	
	/**
	 * 删除系统地址
	 * @return
	 */
	public String deleteSysAddress(){
		logger.debug("deleteSysAddress-start");
		JSONObject json = new JSONObject();
		try{			
			String ip_id = systemAddressModel.getIp_id();
			String[] ipArray = ip_id.split(",");
			if(ipArray.length == 1){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("ipID", ip_id);
				Map<String,Object> resultMap = ossAdapter.invoke(0xa6, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					return NONE;
				}
			}else{
				for(String str : ipArray){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("ipID", str);
					Map<String,Object> resultMap = ossAdapter.invoke(0xa6, 0x02, map);
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
			logger.error("deleteSysAddress error:"+e);
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

	public List<SystemAddressModel> getSystemAddressModelList()
	{
		return systemAddressModelList;
	}

	public void setSystemAddressModelList(
			List<SystemAddressModel> systemAddressModelList)
	{
		this.systemAddressModelList = systemAddressModelList;
	}

	public SystemAddressModel getSystemAddressModel()
	{
		return systemAddressModel;
	}

	public void setSystemAddressModel(SystemAddressModel systemAddressModel)
	{
		this.systemAddressModel = systemAddressModel;
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
