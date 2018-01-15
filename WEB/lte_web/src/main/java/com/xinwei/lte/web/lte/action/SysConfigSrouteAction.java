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
import com.xinwei.lte.web.lte.model.SRouteModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * lte系统配置静态路由action
 * 
 * <p>
 * lte系统配置静态路由action
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class SysConfigSrouteAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	private OnlinePage onlinePage;
	
	private SRouteModel srouteModel;
	
	//保存查询条件
	private List<SRouteModel> srouteModelList;
	//记录日志
	private static Logger logger = LoggerFactory.getLogger(SysConfigSrouteAction.class);
	
	private String showMessage = "暂无相关数据";
	/**
	 * 跳转到静态路由页面
	 * @return
	 */
	public String turntoSRoute(){
		logger.debug("turntoSysAddress");
		
		srouteModelList = new ArrayList<SRouteModel>();
		
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
			Map<String,Object> resultMap = ossAdapter.invoke(0xb3, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				return "toSroute";
			}
			int lteTotalQueryCount = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			if(lteTotalQueryCount%LteConstant.PageSize == 0){
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize);
			}else{
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize+1);
			}	
			
			List<Map> resultList = (List<Map>) resultMap.get("srouteInfo");
			SRouteModel sRouteModel;
			if(null != resultList){
				for(Map rMap : resultList){
					sRouteModel = new SRouteModel();
					if(rMap.get("srouteIndex") != null){
						sRouteModel.setSroute_index(((String)rMap.get("srouteIndex")).trim());
					}
					if(rMap.get("srouteNet") != null){
						sRouteModel.setSroute_net(((String)rMap.get("srouteNet")).trim());
					}
					if(rMap.get("srouteNetMask") != null){
						sRouteModel.setSroute_netmask(((String)rMap.get("srouteNetMask")).trim());
					}
					if(rMap.get("srouteGw") != null){
						sRouteModel.setSroute_gw(((String)rMap.get("srouteGw")).trim());
					}
					if(rMap.get("srouteMetirc") != null){
						sRouteModel.setSroute_metirc(((String)rMap.get("srouteMetirc")).trim());	
					}				
									
					srouteModelList.add(sRouteModel);
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
			logger.error("turntoSRoute error:"+e.toString());
			showMessage = e.getMessage();
		}
		return "toSroute";
	}
	
	/**
	 * 跳转到静态路由配置页面
	 * @return
	 */
	public String turntoSRouteAdd(){
		logger.debug("turntoSRouteAdd");
		return "toSrouteAdd";
	}
	
	/**
	 * 增加静态路由
	 * @return
	 */
	public String addSRoute(){
		logger.debug("addSRoute-start");
		JSONObject json = new JSONObject();
		try{
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("srouteNet", srouteModel.getSroute_net());
			map.put("srouteNetMask", srouteModel.getSroute_netmask());
			map.put("srouteGw", srouteModel.getSroute_gw());
			map.put("srouteMetirc", srouteModel.getSroute_metirc());
			Map<String,Object> resultMap = ossAdapter.invoke(0xb3, 0x01, map);
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
			logger.error("addSRoute error:"+e.toString());
			json.put("status", 1);
			json.put("message", "操作失败");
			ajaxMethod(json.toString());
		}
		logger.debug("addSRoute-end");
		return NONE;
	}
	
	/**
	 * 删除静态路由
	 * @return
	 */
	public String deleteSRoute(){
		logger.debug("deleteSRoute-start");
		
		JSONObject json = new JSONObject();
		try{
					
			String sroute_net = srouteModel.getSroute_net();
			String sroute_netmask = srouteModel.getSroute_netmask();
			String sroute_gw = srouteModel.getSroute_gw();
			String sroute_metirc = srouteModel.getSroute_metirc();
			
			String[] sroute_netArray = sroute_net.split(",");
			String[] sroute_netmaskArray = sroute_netmask.split(",");
			String[] sroute_gwArray = sroute_gw.split(",");
			String[] sroute_metircArray = sroute_metirc.split(",");
			
			if(sroute_netArray.length == 1){
				Map<String,Object> map = new HashMap<String,Object>();
				
				map.put("srouteNet", sroute_net);
				map.put("srouteNetMask", sroute_netmask);
				map.put("srouteGw", sroute_gw);
				map.put("srouteMetirc", sroute_metirc);
				
				Map<String,Object> resultMap = ossAdapter.invoke(0xb3, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					return NONE;
				}
			}else{
				for(int i = 0; i < sroute_netArray.length; i ++){
					Map<String,Object> map = new HashMap<String,Object>();

					map.put("srouteNet", sroute_netArray[i]);
					map.put("srouteNetMask", sroute_netmaskArray[i]);
					map.put("srouteGw", sroute_gwArray[i]);
					map.put("srouteMetirc", sroute_metircArray[i]);
					
					Map<String,Object> resultMap = ossAdapter.invoke(0xb3, 0x02, map);
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
			logger.error("deleteSRoute error:"+e.toString());
			json.put("status", 1);
			json.put("message", "操作失败");
			ajaxMethod(json.toString());
		}
		logger.debug("deleteSRoute-end");
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

	public SRouteModel getSrouteModel()
	{
		return srouteModel;
	}

	public void setSrouteModel(SRouteModel srouteModel)
	{
		this.srouteModel = srouteModel;
	}

	public List<SRouteModel> getSrouteModelList()
	{
		return srouteModelList;
	}

	public void setSrouteModelList(List<SRouteModel> srouteModelList)
	{
		this.srouteModelList = srouteModelList;
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
