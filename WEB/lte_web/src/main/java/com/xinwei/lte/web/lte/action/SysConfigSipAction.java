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
import com.xinwei.lte.web.lte.model.SipRouteModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * lte系统配置SIP路由action
 * 
 * <p>
 * lte系统配置SIP路由action
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class SysConfigSipAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	private OnlinePage onlinePage;
	
	private SipRouteModel sipRouteModel;
	
	//保存查询条件
	private SipRouteModel querysipRouteModel;
	private List<SipRouteModel> sipRouteModelList;
	//记录日志
	private static Logger logger = LoggerFactory.getLogger(SysConfigSipAction.class);
	
	private String showMessage = "暂无相关数据";
	/**
	 * 跳转到SIP路由页面
	 * @return
	 */
	public String turntoSip(){
		logger.debug("turntoSip-start");
		sipRouteModelList = new ArrayList<SipRouteModel>();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			if(null == onlinePage){
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);	
			}
			if(null != querysipRouteModel){
				if(null != querysipRouteModel.getSip_id()){
					if(!"".equals(querysipRouteModel.getSip_id())){
						map.put("sipID",  querysipRouteModel.getSip_id());
					}					
				}
			}else{
				querysipRouteModel = new SipRouteModel();
			}
			
			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			
			Map<String,Object> resultMap = ossAdapter.invoke(0xa7, 0x05, map);
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
			List<Map> resultList = (List<Map>) resultMap.get("sipInfo");
			
			SipRouteModel sipRoModel ;
			if(null != resultList){
				for(Map rMap : resultList){
					sipRoModel = new SipRouteModel();
					if(rMap.get("sipID") != null){
						sipRoModel.setSip_id(((String)rMap.get("sipID")).trim());
					}
					if(rMap.get("sipAddr") != null){
						sipRoModel.setSip_addr(((String)rMap.get("sipAddr")).trim());
					}
					if(rMap.get("sipPort") != null){
						sipRoModel.setSip_port(((String)rMap.get("sipPort")).trim());
					}
					if(rMap.get("sipComment") != null){
						sipRoModel.setSip_comment(((String)rMap.get("sipComment")).trim());
					}
					if(rMap.get("sipIsDefault") != null){
						sipRoModel.setSip_isdefault(((String)rMap.get("sipIsDefault")).trim());
					}
					
					if(rMap.get("sipState") != null){
						sipRoModel.setSip_state(((String)rMap.get("sipState")).trim());
					}
				
					sipRouteModelList.add(sipRoModel);			
				}
			}else{
				if(onlinePage.getCurrentPageNum() == 1 ){
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}	
			}			
	
		}catch(Exception e){
			e.printStackTrace();
			logger.error("turntoSip error:"+e.toString());
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			showMessage = e.getMessage();
		}
		logger.debug("turntoSip-end");
		return SUCCESS;
	}
	
	/**
	 * 跳转到SIP路由配置页面
	 * @return
	 */
	public String turntoSipAdd(){
		logger.debug("turntoSipAdd");		
		return SUCCESS;
	}
	
	/**
	 * 增加SIP路由
	 * @return
	 */
	public String addSip(){
		logger.debug("addSip-start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("sipID", sipRouteModel.getSip_id());
			map.put("sipAddr", sipRouteModel.getSip_addr());
			map.put("sipPort", sipRouteModel.getSip_port());
			map.put("sipIsDefault", sipRouteModel.getSip_isdefault());
			map.put("sipComment", sipRouteModel.getSip_comment());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa7, 0x01, map);
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
			logger.error("addSip error:"+e.toString());
		}
		logger.debug("addSip-end");
		return NONE;
	}
	/**
	 * 跳转到修改SIP路由
	 * @return
	 */
	public String toModifySip(){
		logger.debug("modifySip-start");
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ltePageSize", "1");
			map.put("ltePageIndex", "1");
			map.put("sipID", sipRouteModel.getSip_id());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa7, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				return ERROR;
			}
			List<Map> resultList = (List<Map>) resultMap.get("sipInfo");
			if(resultList.size() != 0){
				Map rMap = resultList.get(0);
				if(rMap.get("sipID") != null){
					sipRouteModel.setSip_id(((String)rMap.get("sipID")).trim());
				}
				if(rMap.get("sipAddr") != null){
					sipRouteModel.setSip_addr(((String)rMap.get("sipAddr")).trim());
				}
				if(rMap.get("sipPort") != null){
					sipRouteModel.setSip_port(((String)rMap.get("sipPort")).trim());
				}
				if(rMap.get("sipComment") != null){
					sipRouteModel.setSip_comment(((String)rMap.get("sipComment")).trim());
				}
				if(rMap.get("sipIsDefault") != null){
					sipRouteModel.setSip_isdefault(((String)rMap.get("sipIsDefault")).trim());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("modifySip error:"+e.toString());
		}
		logger.debug("modifySip-end");
		return SUCCESS;
	}
	/**
	 * 修改SIP路由
	 * @return
	 */
	public String modifySip(){
		logger.debug("modifySip-start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("sipID", sipRouteModel.getSip_id());
			map.put("sipAddr", sipRouteModel.getSip_addr());
			map.put("sipPort", sipRouteModel.getSip_port());
			map.put("sipIsDefault", sipRouteModel.getSip_isdefault());
			map.put("sipComment", sipRouteModel.getSip_comment());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa7, 0x03, map);
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
			logger.error("modifySip error:"+e.toString());
		}
		logger.debug("modifySip-end");
		return NONE;
	}
	
	/**
	 * 删除SIP路由
	 * @return
	 */
	public String deleteSip(){
		logger.debug("deleteSip-start");
		JSONObject json = new JSONObject();
		try{
			String sip_id = sipRouteModel.getSip_id();
			String[] idArray = sip_id.split(",");
			if(idArray.length == 1){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("sipID", sip_id);
				Map<String,Object> resultMap = ossAdapter.invoke(0xa7, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					return NONE;
				}
			}else{
				for(String str : idArray){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("sipID", str);
					Map<String,Object> resultMap = ossAdapter.invoke(0xa7, 0x02, map);
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
			logger.error("deleteSip error:"+e.toString());
		}
		logger.debug("deleteSip-end");
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

	public List<SipRouteModel> getSipRouteModelList()
	{
		return sipRouteModelList;
	}

	public void setSipRouteModelList(List<SipRouteModel> sipRouteModelList)
	{
		this.sipRouteModelList = sipRouteModelList;
	}

	public SipRouteModel getSipRouteModel()
	{
		return sipRouteModel;
	}

	public void setSipRouteModel(SipRouteModel sipRouteModel)
	{
		this.sipRouteModel = sipRouteModel;
	}

	public SipRouteModel getQuerysipRouteModel()
	{
		return querysipRouteModel;
	}

	public void setQuerysipRouteModel(SipRouteModel querysipRouteModel)
	{
		this.querysipRouteModel = querysipRouteModel;
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
