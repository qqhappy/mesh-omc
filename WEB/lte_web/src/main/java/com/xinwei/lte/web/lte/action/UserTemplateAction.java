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

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.UserTemplateModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * 用户参数模板action
 * 
 * <p>
 * 用户参数模板action
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class UserTemplateAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	//日志
	private static Logger logger = LoggerFactory.getLogger(UserInfoAction.class);
	
	private OnlinePage onlinePage;
	
	private UserTemplateModel userTemplateModel;
	
	//保存查询条件
	//private UserTemplateModel queryuserTemplateModel;
	private List<UserTemplateModel> userTemplateModelList;
	
	private String showMessage = "暂无相关数据";
	/**
	 * 跳转到用户参数模板页面
	 * @return
	 */
	public String turntoUserTemplate(){
		logger.debug("turntoUserTemplate - start");
		userTemplateModelList = new ArrayList<UserTemplateModel>();
		
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			if(null == onlinePage){
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);				
			}
			if(onlinePage.getCurrentPageNum() == 0){
				onlinePage.setCurrentPageNum(1);
			}
			
			/*if(null != queryuserTemplateModel){
				if(0 != queryuserTemplateModel.getFirst()){
					if(null != queryuserTemplateModel.getTmp_id()){
						if(!"".equals(queryuserTemplateModel.getTmp_id())){
							map.put("tmpID", queryuserTemplateModel.getTmp_id());
						}						
					}
					
					if(null != queryuserTemplateModel.getTmp_name()){
						if(!"".equals(queryuserTemplateModel.getTmp_name())){
							map.put("tmpName", queryuserTemplateModel.getTmp_name());	
						}						
					}
					
					if(null != queryuserTemplateModel.getTmp_default()){
						if(!"".equals(queryuserTemplateModel.getTmp_default())){
							map.put("tmpDefault", queryuserTemplateModel.getTmp_default());
						}						
					}				
					
				}
			}else{
				queryuserTemplateModel = new UserTemplateModel();
			}*/
						
			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			//map.put("tmpID", systemAddressModel.getIp_id());
			//map.put("tmpName", systemAddressModel.getIp_id());
			//map.put("tmpDefault", systemAddressModel.getIp_id());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa2, 0x05, map);
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
			
			List<Map> resultList = (List<Map>) resultMap.get("tmpInfo");
			UserTemplateModel userTempModel;
			if(null != resultList){
				for(Map rMap : resultList){
					userTempModel = new UserTemplateModel();
					if(rMap.get("tmpIndex") != null){
						userTempModel.setTmp_index(((String)rMap.get("tmpIndex")).trim());
					}
					if(rMap.get("tmpID") != null){
						userTempModel.setTmp_id(((String)rMap.get("tmpID")).trim());
					}
					if(rMap.get("tmpName") != null){
						userTempModel.setTmp_name(((String)rMap.get("tmpName")).trim());
					}
					if(rMap.get("tmpULBW") != null){
						userTempModel.setTmp_ul_bw(((String)rMap.get("tmpULBW")).trim());
					}
					if(rMap.get("tmpDLBW") != null){
						userTempModel.setTemp_dl_bw(((String)rMap.get("tmpDLBW")).trim());
					}
					if(rMap.get("tmpPDNCat") != null){
						userTempModel.setTmp_pdn_cat(((String)rMap.get("tmpPDNCat")).trim());
					}
					if(rMap.get("tmpAPNULBW") != null){
						userTempModel.setTmp_apn_ulbw(((String)rMap.get("tmpAPNULBW")).trim());
					}
					if(rMap.get("tmpAPNDLBW") != null){
						userTempModel.setTmp_apn_dlbw(((String)rMap.get("tmpAPNDLBW")).trim());
					}
					if(rMap.get("tmpQCI") != null){
						userTempModel.setTmp_qci(((String)rMap.get("tmpQCI")).trim());
					}
					if(rMap.get("tmpDefault") != null){
						userTempModel.setTmp_default(((String)rMap.get("tmpDefault")).trim());
					}
					if(rMap.get("tmpARPPri") != null){
						userTempModel.setTmp_arp_pri(((String)rMap.get("tmpARPPri")).trim());
					}
					if(rMap.get("tmpARPReave") != null){
						userTempModel.setTmp_arp_reave(((String)rMap.get("tmpARPReave")).trim());
					}
					
					if(rMap.get("tmpARPReaved") != null){
						userTempModel.setTmp_arp_reaved(((String)rMap.get("tmpARPReaved")).trim());	
					}
								
					userTemplateModelList.add(userTempModel);
				}
			}else{
				if(onlinePage.getCurrentPageNum() == 1 ){
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}
			}
			
		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("turntoUserTemplate error:"+e.toString());
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			showMessage = e.getMessage();
		}
		logger.debug("turntoUserTemplate - end");
		return SUCCESS;
	}
	
	/**
	 * 跳转到用户参数模板配置页面
	 * @return
	 */
	public String turntoUserTemplateAdd(){
		logger.debug("turntoUserTemplateAdd");
		return SUCCESS;
	}
	
	/**
	 * 增加用户参数模板
	 * @return
	 */
	public String addUserTemplate(){
		logger.debug("addUserTemplate-start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("tmpID", userTemplateModel.getTmp_id());
			map.put("tmpName", userTemplateModel.getTmp_name());
			map.put("tmpULBW", userTemplateModel.getTmp_ul_bw());
			map.put("tmpDLBW", userTemplateModel.getTemp_dl_bw());
			map.put("tmpPDNCat",userTemplateModel.getTmp_pdn_cat());
			map.put("tmpAPNULBW",userTemplateModel.getTmp_apn_ulbw());
			map.put("tmpAPNDLBW",userTemplateModel.getTmp_apn_dlbw());
			map.put("tmpQCI", userTemplateModel.getTmp_qci());
			map.put("tmpDefault", userTemplateModel.getTmp_default());
			map.put("tmpARPPri", userTemplateModel.getTmp_arp_pri());
			map.put("tmpARPReave", userTemplateModel.getTmp_arp_reave());
			map.put("tmpARPReaved", userTemplateModel.getTmp_arp_reaved());
			
			Map<String,Object> resultMap = ossAdapter.invoke(0xa2, 0x01, map);
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
			logger.error("addUserTemplate error:"+e.toString());
		}
		logger.debug("addUserTemplate-end");
		return NONE;
	}
	
	/**
	 * 跳转到修改用户参数模板
	 * @return
	 */
	public String toModifyUserTemplate(){
		logger.debug("modifyUserTemplate-start");
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			/*if(null != queryuserTemplateModel.getTmp_id()){
				if(!"".equals(userTemplateModel.getTmp_id())){
					map.put("tmpID", userTemplateModel.getTmp_id());
				}						
			}			*/
						
			map.put("ltePageSize", "1");
			map.put("ltePageIndex", "1");
			map.put("tmpID", userTemplateModel.getTmp_id());
			//map.put("tmpName", systemAddressModel.getIp_id());
			//map.put("tmpDefault", systemAddressModel.getIp_id());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa2, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				onlinePage.setTotalPages(0);
				onlinePage.setCurrentPageNum(0);
				return SUCCESS;
			}
			int totalPages = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			onlinePage.setTotalPages(totalPages);
			
			List<Map> resultList = (List<Map>) resultMap.get("tmpInfo");
			if(null != resultList){
				Map rMap = resultList.get(0);	
				if(rMap.get("tmpIndex") != null){
					userTemplateModel.setTmp_index(((String)rMap.get("tmpIndex")).trim());
				}
				if(rMap.get("tmpID") != null){
					userTemplateModel.setTmp_id(((String)rMap.get("tmpID")).trim());
				}
				if(rMap.get("tmpName") != null){
					userTemplateModel.setTmp_name(((String)rMap.get("tmpName")).trim());
				}
				if(rMap.get("tmpULBW") != null){
					userTemplateModel.setTmp_ul_bw(((String)rMap.get("tmpULBW")).trim());
				}
				if(rMap.get("tmpDLBW") != null){
					userTemplateModel.setTemp_dl_bw(((String)rMap.get("tmpDLBW")).trim());
				}
				if(rMap.get("tmpPDNCat") != null){
					userTemplateModel.setTmp_pdn_cat(((String)rMap.get("tmpPDNCat")).trim());
				}
				if(rMap.get("tmpAPNULBW") != null){
					userTemplateModel.setTmp_apn_ulbw(((String)rMap.get("tmpAPNULBW")).trim());
				}
				if(rMap.get("tmpAPNDLBW") != null){
					userTemplateModel.setTmp_apn_dlbw(((String)rMap.get("tmpAPNDLBW")).trim());
				}
				if(rMap.get("tmpQCI") != null){
					userTemplateModel.setTmp_qci(((String)rMap.get("tmpQCI")).trim());
				}
				if(rMap.get("tmpDefault") != null){
					userTemplateModel.setTmp_default(((String)rMap.get("tmpDefault")).trim());
				}
				if(rMap.get("tmpARPPri") != null){
					userTemplateModel.setTmp_arp_pri(((String)rMap.get("tmpARPPri")).trim());
				}
				if(rMap.get("tmpARPReave") != null){
					userTemplateModel.setTmp_arp_reave(((String)rMap.get("tmpARPReave")).trim());
				}				
				if(rMap.get("tmpARPReaved") != null){
					userTemplateModel.setTmp_arp_reaved(((String)rMap.get("tmpARPReaved")).trim());	
				}			
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("modifyUserTemplate error:"+e.toString());
		}
		logger.debug("modifyUserTemplate-end");
		return SUCCESS;
	}
	
	/**
	 * 修改用户参数模板
	 * @return
	 */
	public String modifyUserTemplate(){
		logger.debug("modifyUserTemplate-start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("tmpID", userTemplateModel.getTmp_id());
			map.put("tmpName", userTemplateModel.getTmp_name());
			map.put("tmpULBW", userTemplateModel.getTmp_ul_bw());
			map.put("tmpDLBW", userTemplateModel.getTemp_dl_bw());
			map.put("tmpPDNCat",userTemplateModel.getTmp_pdn_cat());
			map.put("tmpAPNULBW",userTemplateModel.getTmp_apn_ulbw());
			map.put("tmpAPNDLBW",userTemplateModel.getTmp_apn_dlbw());
			map.put("tmpQCI", userTemplateModel.getTmp_qci());
			map.put("tmpDefault", userTemplateModel.getTmp_default());
			map.put("tmpARPPri", userTemplateModel.getTmp_arp_pri());
			map.put("tmpARPReave", userTemplateModel.getTmp_arp_reave());
			map.put("tmpARPReaved", userTemplateModel.getTmp_arp_reaved());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa2, 0x03, map);
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
			logger.error("modifyUserTemplate error:"+e.toString());
		}
		logger.debug("modifyUserTemplate-end");
		return NONE;
	}
	
	/**
	 * 删除用户参数模板
	 * @return
	 */
	public String deleteUserTemplate(){
		logger.debug("deleteUserTemplate-start");
		JSONObject json = new JSONObject();
		try{
			String tmp_id = userTemplateModel.getTmp_id();
			String[] tmp_idArray = tmp_id.split(",");
			if(tmp_idArray.length == 1){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("tmpID", tmp_id);
				Map<String,Object> resultMap = ossAdapter.invoke(0xa2, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					return NONE;
				}
			}else{
				for(String str : tmp_idArray){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("tmpID", str);
					Map<String,Object> resultMap = ossAdapter.invoke(0xa2, 0x02, map);
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
			logger.error("deleteUserTemplate error:"+e.toString());
		}
		logger.debug("deleteUserTemplate-end");
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
	public OnlinePage getOnlinePage()
	{
		return onlinePage;
	}

	public void setOnlinePage(OnlinePage onlinePage)
	{
		this.onlinePage = onlinePage;
	}

	public UserTemplateModel getUserTemplateModel()
	{
		return userTemplateModel;
	}

	public void setUserTemplateModel(UserTemplateModel userTemplateModel)
	{
		this.userTemplateModel = userTemplateModel;
	}

	public List<UserTemplateModel> getUserTemplateModelList()
	{
		return userTemplateModelList;
	}

	public void setUserTemplateModelList(
			List<UserTemplateModel> userTemplateModelList)
	{
		this.userTemplateModelList = userTemplateModelList;
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
