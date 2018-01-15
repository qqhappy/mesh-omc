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
import com.xinwei.lte.web.lte.model.UserIpResourceModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * lte系统配置用户IP资源action
 * 
 * <p>
 * lte系统配置用户IP资源action
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class UserIpResourceAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	private OnlinePage onlinePage;
	
	private UserIpResourceModel userIpResourceModel;
	
	private List<UserIpResourceModel> userIpResourceModelList;
	//记录日志
	private static Logger logger = LoggerFactory.getLogger(UserIpResourceAction.class);

	private String showMessage = "暂无相关数据";
	/**
	 * 跳转到用户IP资源页面
	 * @return
	 */
	public String turntoIpResource(){
		logger.debug("turntoIpResource");
		userIpResourceModelList = new ArrayList<UserIpResourceModel>();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			if(null == onlinePage){
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);				
			}

			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			Map<String,Object> resultMap = ossAdapter.invoke(0xae, 0x05, map);

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
			
			List<Map> resultList = (List<Map>) resultMap.get("snetInfo");
			if(resultList != null){
				for(Map rMap : resultList){
					userIpResourceModel = new UserIpResourceModel();
					if(rMap.get("snetID") != null){
						userIpResourceModel.setSnet_id(((String)rMap.get("snetID")).trim());
					}
					if(rMap.get("snetID") != null){
						userIpResourceModel.setSnet_startip(((String)rMap.get("snetStartIP")).trim());
					}
					if(rMap.get("snetID") != null){
						userIpResourceModel.setSnet_endip(((String)rMap.get("snetEndIP")).trim());
					}
					if(rMap.get("ippoolType") != null){
						userIpResourceModel.setIppoll_type(((String)rMap.get("ippoolType")).trim());
					}
					userIpResourceModelList.add(userIpResourceModel);
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
		return SUCCESS;
	}
	
	/**
	 * 跳转到用户IP资源配置页面
	 * @return
	 */
	public String turntoIpResourceAdd(){
		logger.debug("turntoIpResourceAdd");
		return SUCCESS;
	}
	
	/**
	 * 增加用户IP资源
	 * @return
	 */
	public String addIpResource(){
		logger.debug("addIpResource-start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("snetID", userIpResourceModel.getSnet_id());
			map.put("snetStartIP",userIpResourceModel.getSnet_startip());
			map.put("snetEndIP",userIpResourceModel.getSnet_endip());
			map.put("ippoolType",userIpResourceModel.getIppoll_type());
			Map<String,Object> resultMap = ossAdapter.invoke(0xae, 0x01, map);
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
			logger.error("addIpResource error:"+e.toString());
		}
		logger.debug("addIpResource-end");
		return NONE;
	}
	
	/**
	 * 跳转到修改用户IP资源
	 * @return
	 */
	public String toModifyIpResource(){
		logger.debug("toModifyIpResource-start");
		try{
		
		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("toModifyIpResource error:"+e.toString());
		}
		logger.debug("toModifyIpResource-end");
		return SUCCESS;
	}
	
	/**
	 * 修改修改用户IP资源
	 * @return
	 */
	public String modifyIpResource(){
		logger.debug("modifyIpResource-start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("snetID", userIpResourceModel.getSnet_id());
			map.put("snetStartIP",userIpResourceModel.getSnet_startip());
			map.put("snetEndIP",userIpResourceModel.getSnet_endip());
			map.put("ippoolType",userIpResourceModel.getIppoll_type());
			Map<String,Object> resultMap = ossAdapter.invoke(0xae, 0x03, map);
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
			logger.error("modifyIpResource error:"+e.toString());
		}
		logger.debug("toModifyIpResource-end");
		return NONE;
	}
	
	/**
	 * 删除用户IP资源
	 * @return
	 */
	public String deleteIpResource(){
		logger.debug("deleteIpResource-start");
		JSONObject json = new JSONObject();
		try{
			String snet_id = userIpResourceModel.getSnet_id();
			String[] snet_idArray = snet_id.split(",");
			if(snet_idArray.length == 1){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("snetID", snet_id);
				Map<String,Object> resultMap = ossAdapter.invoke(0xae, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					return NONE;
				}
			}else{
				for(String str : snet_idArray){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("snetID", str);
					Map<String,Object> resultMap = ossAdapter.invoke(0xae, 0x02, map);
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
			logger.error("deleteIpResource error:"+e.toString());
		}
		logger.debug("deleteIpResource-end");
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

	public UserIpResourceModel getUserIpResourceModel()
	{
		return userIpResourceModel;
	}

	public void setUserIpResourceModel(UserIpResourceModel userIpResourceModel)
	{
		this.userIpResourceModel = userIpResourceModel;
	}

	public List<UserIpResourceModel> getUserIpResourceModelList()
	{
		return userIpResourceModelList;
	}

	public void setUserIpResourceModelList(
			List<UserIpResourceModel> userIpResourceModelList)
	{
		this.userIpResourceModelList = userIpResourceModelList;
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
