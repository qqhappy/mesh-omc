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
import com.xinwei.lte.web.lte.model.OperResult;
import com.xinwei.lte.web.lte.model.VmgCfgModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.lte.web.lte.service.VmgCfgService;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;
import com.xinwei.lte.web.lte.action.Utils.Util;

/**
 * 
 * 视频监控设备配置action
 * 
 * <p>
 * 视频监控设备配置action
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class VmgCfgAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	@Resource
	private VmgCfgService vmgCfgService;
	//日志
	private static Logger logger = LoggerFactory.getLogger(VmgCfgAction.class);
	
	private OnlinePage onlinePage;
	
	private VmgCfgModel vmgCfgModel;
	
	//保存查询条件
	private VmgCfgModel queryVmgCfgModel;	
	
	private List<VmgCfgModel> vmgCfgModelList;
	
	private String showMessage = "暂无相关数据";
	/**
	 * 跳转到用户参数模板页面
	 * @return
	 */
	public String turntoVmgCfg(){
		logger.debug("turntoVmgCfg - start");
		vmgCfgModelList = new ArrayList<VmgCfgModel>();
		
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
			
			//若通过查询按钮查询
			if(queryVmgCfgModel != null){
				
				//若不是第一次登陆
				if(0 != queryVmgCfgModel.getFirst()){
						map.put("monitorName",new String(queryVmgCfgModel.getQueryValue().getBytes("iso-8859-1"),"utf-8"));	
						queryVmgCfgModel.setQueryValue(new String(queryVmgCfgModel.getQueryValue().getBytes("iso-8859-1"),"utf-8"));
				}					
			}else{
				queryVmgCfgModel = new VmgCfgModel();
				//map.put("imsiUEStatus",wirelessImsiModel.getImsi_uestatus());
			}
						
			//设置查询条件
			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			
			//获取视频监控设备配置信息
			Map<String,Object> resultMap = ossAdapter.invoke(0xb4, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				//查询失败，直接返回
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				logger.warn("ossAdapter.invoke error, flag = " + flag);
				return "turntoVmgCfg";
			}
			
			//查询到的数据总个数
			int lteTotalQueryCount = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			if(lteTotalQueryCount%LteConstant.PageSize == 0){
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize);
			}else{
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize+1);
			}		
			
			//返回的数据集合
			List<Map> resultList = (List<Map>) resultMap.get("vmgCfgInfo");
			VmgCfgModel vmgCfgModel;
			if(null != resultList){
				for(Map rMap : resultList){
					vmgCfgModel = new VmgCfgModel();
					if(rMap.get("monitorIndex") != null){
						vmgCfgModel.setMonitor_index(((String)rMap.get("monitorIndex")).trim());
					}
					if(rMap.get("monitorName") != null){
						vmgCfgModel.setMonitor_name(((String)rMap.get("monitorName")).trim());
					}
					if(rMap.get("monitorType") != null){
						vmgCfgModel.setMonitor_type(((String)rMap.get("monitorType")).trim());
					}
					if(rMap.get("monitorIp") != null){
						vmgCfgModel.setMonitor_ip(((String)rMap.get("monitorIp")).trim());
					}
					if(rMap.get("monitorPort") != null){
						vmgCfgModel.setMonitor_port(((String)rMap.get("monitorPort")).trim());
					}
					if(rMap.get("userName") != null){
						vmgCfgModel.setUser_name(((String)rMap.get("userName")).trim());
					}
					if(rMap.get("userPassword") != null){
						vmgCfgModel.setUser_password(((String)rMap.get("userPassword")).trim());
					}
					if(rMap.get("comment") != null){
						vmgCfgModel.setComment(((String)rMap.get("comment")).trim());
					}
													
					vmgCfgModelList.add(vmgCfgModel);
				}
			}else{
				if(onlinePage.getCurrentPageNum() == 1 ){
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}
			}
			
		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("turntoVmgCfg error:"+e.toString());
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			showMessage = e.getMessage();
		}
		logger.debug("turntoVmgCfg - end");
		return "turntoVmgCfg";
	}
	
	public String queryMonitorName(){
		
		System.out.println("**************************************************************************");
		
		JSONObject json = new JSONObject();
		vmgCfgModelList = new ArrayList<VmgCfgModel>();		
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
			
			//若通过查询按钮查询
			if(queryVmgCfgModel != null){
				
				//若不是第一次登陆
				if(0 != queryVmgCfgModel.getFirst()){
						map.put("monitorName",queryVmgCfgModel.getQueryValue());	
				}					
			}else{
				queryVmgCfgModel = new VmgCfgModel();
				//map.put("imsiUEStatus",wirelessImsiModel.getImsi_uestatus());
			}
						
			//设置查询条件
			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			
			//获取视频监控设备配置信息
			Map<String,Object> resultMap = ossAdapter.invoke(0xb4, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				//查询失败，直接返回
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				json.put("status", -1);
				json.put("onlinePage", onlinePage);
				json.put("error","ossAdapter.invoke error, flag = " + flag);
				return NONE;
			}			
			//查询到的数据总个数
			int lteTotalQueryCount = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			if(lteTotalQueryCount%LteConstant.PageSize == 0){
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize);
			}else{
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize+1);
			}		
			
			//返回的数据集合
			List<Map> resultList = (List<Map>) resultMap.get("vmgCfgInfo");
			VmgCfgModel vmgCfgModel;
			if(null != resultList){
				for(Map rMap : resultList){
					vmgCfgModel = new VmgCfgModel();
					if(rMap.get("monitorIndex") != null){
						vmgCfgModel.setMonitor_index(((String)rMap.get("monitorIndex")).trim());
					}
					if(rMap.get("monitorName") != null){
						vmgCfgModel.setMonitor_name(((String)rMap.get("monitorName")).trim());
					}
					if(rMap.get("monitorType") != null){
						vmgCfgModel.setMonitor_type(((String)rMap.get("monitorType")).trim());
					}
					if(rMap.get("monitorIp") != null){
						vmgCfgModel.setMonitor_ip(((String)rMap.get("monitorIp")).trim());
					}
					if(rMap.get("monitorPort") != null){
						vmgCfgModel.setMonitor_port(((String)rMap.get("monitorPort")).trim());
					}
					if(rMap.get("userName") != null){
						vmgCfgModel.setUser_name(((String)rMap.get("userName")).trim());
					}
					if(rMap.get("userPassword") != null){
						vmgCfgModel.setUser_password(((String)rMap.get("userPassword")).trim());
					}
					if(rMap.get("comment") != null){
						vmgCfgModel.setComment(((String)rMap.get("comment")).trim());
					}													
					vmgCfgModelList.add(vmgCfgModel);					
				}
				json.put("status", 0);
				json.put("vmgCfgModelList", vmgCfgModelList);
			}else{
				if(onlinePage.getCurrentPageNum() == 1 ){
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}
			}
			
		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("turntoVmgCfg error:"+e.toString());
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			json.put("status", -1);
			json.put("error",e.getLocalizedMessage());
		}
		json.put("onlinePage", onlinePage);
		Util.ajaxSimpleUtil(json.toString());
		return NONE;
	}
	
	/**
	 * 跳转到用户参数模板配置页面
	 * @return
	 */
	public String turntoVmgCfgAdd(){
		logger.debug("turntoVmgCfgAdd");
		return "turntoVmgCfgAdd";
	}
	
	/**
	 * 增加用户参数模板
	 * @return
	 */
	public String addVmgCfg(){
		logger.debug("addVmgCfg-start");
		JSONObject json = new JSONObject();
		try{
			
			//数据校验
			OperResult operResult = vmgCfgService.checkVmgCfgModel(vmgCfgModel);
			if(!operResult.isSuccess()){
				json.put("status", 1);
				json.put("message",operResult.getRetMsg());
				ajaxMethod(json.toString());
				return NONE;
			}
			
			//赋值
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("monitorName", vmgCfgModel.getMonitor_name());
			map.put("monitorType", vmgCfgModel.getMonitor_type());
			map.put("monitorIp", vmgCfgModel.getMonitor_ip());
			map.put("monitorPort", vmgCfgModel.getMonitor_port());
			map.put("userName",vmgCfgModel.getUser_name());
			map.put("userPassword",vmgCfgModel.getUser_password());
			map.put("comment",vmgCfgModel.getComment());
			
			//新增数据
			Map<String,Object> resultMap = ossAdapter.invoke(0xb4, 0x01, map);
			String flag = (String) resultMap.get("lteFlag");
			if("0".equals(flag)){
				//返回成功
				json.put("status", 0);
				ajaxMethod(json.toString());
			}else{				
				json.put("status", 2);
				json.put("message", LteFlag.flagReturn(flag));
				ajaxMethod(json.toString());
				logger.warn("ossAdapter.invoke error, flag = " + flag);
			}		
		}catch(NoAuthorityException e){
			json.put("status", 1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
			logger.error("addSysAddress error:"+e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("addVmgCfg error:"+e.toString());
		}
		logger.debug("addVmgCfg-end");
		return NONE;
	}
	
	/**
	 * 跳转到修改用户参数模板
	 * @return
	 */
	public String toModifyVmgCfg(){
		logger.debug("toModifyVmgCfg-start");
		try{
			Map<String,Object> map = new HashMap<String,Object>();
						
			//设置查询参数
			map.put("ltePageSize", "1");
			map.put("ltePageIndex", "1");
			map.put("monitorIndex", vmgCfgModel.getMonitor_index());
			
			//调用接口查询数据
			Map<String,Object> resultMap = ossAdapter.invoke(0xb4, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				onlinePage.setTotalPages(0);
				onlinePage.setCurrentPageNum(0);
				return SUCCESS;
			}
			int totalPages = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			onlinePage.setTotalPages(totalPages);
			
			List<Map> resultList = (List<Map>) resultMap.get("vmgCfgInfo");
			if(null != resultList){
				Map rMap = resultList.get(0);	
				if(rMap.get("monitorIndex") != null){
					vmgCfgModel.setMonitor_index(((String)rMap.get("monitorIndex")).trim());
				}
				if(rMap.get("monitorName") != null){
					vmgCfgModel.setMonitor_name(((String)rMap.get("monitorName")).trim());
				}
				if(rMap.get("monitorType") != null){
					vmgCfgModel.setMonitor_type(((String)rMap.get("monitorType")).trim());
				}
				if(rMap.get("monitorIp") != null){
					vmgCfgModel.setMonitor_ip(((String)rMap.get("monitorIp")).trim());
				}
				if(rMap.get("monitorPort") != null){
					vmgCfgModel.setMonitor_port(((String)rMap.get("monitorPort")).trim());
				}
				if(rMap.get("userName") != null){
					vmgCfgModel.setUser_name(((String)rMap.get("userName")).trim());
				}
				if(rMap.get("userPassword") != null){
					vmgCfgModel.setUser_password(((String)rMap.get("userPassword")).trim());
				}
				if(rMap.get("comment") != null){
					vmgCfgModel.setComment(((String)rMap.get("comment")).trim());
				}
									
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("toModifyVmgCfg error:"+e.toString());
		}
		logger.debug("toModifyVmgCfg-end");
		return "toModifyVmgCfg";
	}
	
	/**
	 * 修改用户参数模板
	 * @return
	 */
	public String modifyVmgCfg(){
		logger.debug("modifyVmgCfg-start");
		JSONObject json = new JSONObject();
		try{
			//数据校验
			OperResult operResult = vmgCfgService.checkVmgCfgModel(vmgCfgModel);
			if(!operResult.isSuccess()){
				json.put("status", 1);
				json.put("message",operResult.getRetMsg());
				ajaxMethod(json.toString());
				return NONE;
			}
			
			
			Map<String,Object> map = new HashMap<String,Object>();
			//设置参数
			map.put("monitorIndex", vmgCfgModel.getMonitor_index());
			map.put("monitorName", vmgCfgModel.getMonitor_name());
			map.put("monitorType", vmgCfgModel.getMonitor_type());
			map.put("monitorIp", vmgCfgModel.getMonitor_ip());
			map.put("monitorPort",vmgCfgModel.getMonitor_port());
			map.put("userName",vmgCfgModel.getUser_name());
			map.put("userPassword",vmgCfgModel.getUser_password());
			map.put("comment", vmgCfgModel.getComment());
			
			
			Map<String,Object> resultMap = ossAdapter.invoke(0xb4, 0x03, map);
			String flag = (String) resultMap.get("lteFlag");
			if("0".equals(flag)){
				json.put("status", 0);
				ajaxMethod(json.toString());
			}else{
				//操作失败，返回失败flag
				json.put("status", 1);
				json.put("message", LteFlag.flagReturn(flag));
				ajaxMethod(json.toString());
				logger.warn("ossAdapter.invoke error, flag = " + flag);
			}
		}catch(NoAuthorityException e){
			json.put("status", 1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
			logger.error("addSysAddress error:"+e);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("modifyVmgCfg error:"+e.toString());
		}
		logger.debug("modifyVmgCfg - end");
		return NONE;
	}
	
	/**
	 * 删除用户参数模板
	 * @return
	 */
	public String deleteVmgCfg(){
		logger.debug("deleteUserTemplate-start");
		JSONObject json = new JSONObject();
		try{
			String monitorIndex = vmgCfgModel.getMonitor_index();
			String[] monitorIndexArray = monitorIndex.split(",");
			if(monitorIndexArray.length == 1){
				//删除单个数据
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("monitorIndex", monitorIndex);
				Map<String,Object> resultMap = ossAdapter.invoke(0xb4, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					//操作失败
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					logger.warn("ossAdapter.invoke error, flag = " + flag);
					return NONE;
				}
				json.put("status", 0);
				ajaxMethod(json.toString());
				return NONE;
			}
			
			//删除多个数据
			for(String str : monitorIndexArray){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("monitorIndex", str);
				Map<String,Object> resultMap = ossAdapter.invoke(0xb4, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					//操作失败
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					logger.warn("ossAdapter.invoke error, flag = " + flag);
					return NONE;
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
		response.setContentType("text/html; charset=UTF-8");
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

	public String getShowMessage()
	{
		return showMessage;
	}

	public void setShowMessage(String showMessage)
	{
		this.showMessage = showMessage;
	}

	public VmgCfgModel getVmgCfgModel()
	{
		return vmgCfgModel;
	}

	public void setVmgCfgModel(VmgCfgModel vmgCfgModel)
	{
		this.vmgCfgModel = vmgCfgModel;
	}

	public List<VmgCfgModel> getVmgCfgModelList()
	{
		return vmgCfgModelList;
	}

	public void setVmgCfgModelList(List<VmgCfgModel> vmgCfgModelList)
	{
		this.vmgCfgModelList = vmgCfgModelList;
	}

	public VmgCfgModel getQueryVmgCfgModel()
	{
		return queryVmgCfgModel;
	}

	public void setQueryVmgCfgModel(VmgCfgModel queryVmgCfgModel)
	{
		this.queryVmgCfgModel = queryVmgCfgModel;
	}
	
}
