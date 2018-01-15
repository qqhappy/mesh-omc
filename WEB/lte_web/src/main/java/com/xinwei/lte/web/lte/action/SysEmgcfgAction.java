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
import com.xinwei.lte.web.lte.model.SysEmgcfgModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * 系统紧急呼叫
 * 
 * <p>
 * 系统紧急呼叫
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class SysEmgcfgAction extends ActionSupport{
	
	//记录日志
	private static Logger logger = LoggerFactory.getLogger(SysEmgcfgAction.class);
	
	@Resource
	private OssAdapter ossAdapter;
	
	private OnlinePage onlinePage;
	
	private SysEmgcfgModel sysEmgcfgModel;
	
	private List<SysEmgcfgModel> sysEmgcfgModelList;
	
	private String showMessage = "暂无相关数据";
	
	private String numbers;
	/**
	 * 跳转到系统紧急呼叫
	 * @return
	 */
	public String turntoSysEmgcfg(){
		logger.info("turntoSysEmgcfg - start");
		try{
			sysEmgcfgModelList = new ArrayList<SysEmgcfgModel>();
			Map<String,Object> map = new HashMap<String,Object>();
			if(null == onlinePage){
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);
				onlinePage.setTotalPages(1);
			}	
			
			map.put("ltePageSize", LteConstant.PageSize);
			map.put("ltePageIndex", onlinePage.getCurrentPageNum());
			Map<String,Object> resultMap = ossAdapter.invoke(0xb0, 0x05, map);
			
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				return "toSysEmgcfg";
			}
			int lteTotalQueryCount = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			if(lteTotalQueryCount%LteConstant.PageSize == 0){
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize);
			}else{
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize+1);
			}	
			
			List<Map> resultList = (List<Map>) resultMap.get("emgSys");
			SysEmgcfgModel sysEmgcfgModel;
			if(null != resultList){
				for(Map rMap : resultList){
					sysEmgcfgModel = new SysEmgcfgModel();
					if(rMap.get("emgsysIndex") != null){
						sysEmgcfgModel.setEmgsys_index(String.valueOf(rMap.get("emgsysIndex")));
					}					
					if(rMap.get("emgsysCategory") != null){
						sysEmgcfgModel.setEmgsys_category(String.valueOf(rMap.get("emgsysCategory")).trim());
					}					
					if(rMap.get("emgsysNum") != null){
						sysEmgcfgModel.setEmgsys_num(String.valueOf(rMap.get("emgsysNum")).trim());
					}
					if(rMap.get("emgsysRedirectNum") != null){
						sysEmgcfgModel.setEmg_redicnum(String.valueOf(rMap.get("emgsysRedirectNum")).trim());
					}
					if(rMap.get("emgsysRedirectPri") != null){
						sysEmgcfgModel.setEmg_redicpri(String.valueOf(rMap.get("emgsysRedirectPri")).trim());
					}
					sysEmgcfgModelList.add(sysEmgcfgModel);
				}
			}			
		}catch (Exception e){
			e.printStackTrace();
			logger.error("turntoSysEmgcfg - error:"+e);
			showMessage = e.getMessage();
		}
		logger.info("turntoSysEmgcfg - end");
		return "toSysEmgcfg";
	}
	
	/**
	 * 跳转到增加系统紧急呼叫
	 * @return
	 */
	public String turntoSysEmgcfgAdd(){
		logger.info("turntoSysEmgcfgAdd");
		if(!numbers.equals("")){
		numbers = numbers.substring(0, numbers.lastIndexOf(","));
		}
		return "toSysEmgcfgAdd";
	}
	
	/**
	 * 增加系统紧急呼叫
	 * @return
	 */
	public String addSysEmgcfg(){
		logger.info("addSysEmgcfg - start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("emgsysCategory", sysEmgcfgModel.getEmgsys_category());
			map.put("emgsysNum", sysEmgcfgModel.getEmgsys_num());
			map.put("emgsysRedirectNum", sysEmgcfgModel.getEmg_redicnum());
			map.put("emgsysRedirectPri", sysEmgcfgModel.getEmg_redicpri());
			
			Map<String,Object> resultMap = ossAdapter.invoke(0xb0, 0x01, map);
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
		}catch (Exception e){
			e.printStackTrace();
			logger.error("addSysEmgcfg error:"+e);
		}
		logger.info("addSysEmgcfg - end");
		return NONE;
	}
	
	/**
	 * 跳转到修改系统紧急呼叫
	 * @return
	 */
	public String turntoSysEmgcfgModify(){
		logger.info("turntoSysEmgcfgModify");
		return "toSysEmgcfgModify";
	}
	
	/**
	 * 修改系统紧急呼叫
	 * @return
	 */
	public String modifySysEmgcfg(){
		
		logger.info("modifySysEmgcfg - start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("emgsysIndex", sysEmgcfgModel.getEmgsys_index());
			map.put("emgsysCategory", sysEmgcfgModel.getEmgsys_category());
			map.put("emgsysNum", sysEmgcfgModel.getEmgsys_num());
			map.put("emgsysRedirectNum", sysEmgcfgModel.getEmg_redicnum());
			map.put("emgsysRedirectPri", sysEmgcfgModel.getEmg_redicpri());
			
			Map<String,Object> resultMap = ossAdapter.invoke(0xb0, 0x03, map);
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
		}catch (Exception e){
			e.printStackTrace();
			logger.error("modifySysEmgcfg error:"+e);
		}
		logger.info("modifySysEmgcfg - end");
		return NONE;
	}
	
	/**
	 * 删除系统紧急呼叫
	 * @return
	 */
	public String deleteSysEmg(){
		logger.debug("deleteSysAddress-start");
		JSONObject json = new JSONObject();
		try{
			String emgsys_index = sysEmgcfgModel.getEmgsys_index();
			String[] emgsys_indexArray = emgsys_index.split(",");
			if(emgsys_indexArray.length == 1){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("emgsysIndex", emgsys_index);
				Map<String,Object> resultMap = ossAdapter.invoke(0xb0, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					return NONE;
				}
			}else{
				for(String str : emgsys_indexArray){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("emgsysIndex", str);
					Map<String,Object> resultMap = ossAdapter.invoke(0xb0, 0x02, map);
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

	public SysEmgcfgModel getSysEmgcfgModel()
	{
		return sysEmgcfgModel;
	}

	public void setSysEmgcfgModel(SysEmgcfgModel sysEmgcfgModel)
	{
		this.sysEmgcfgModel = sysEmgcfgModel;
	}

	public List<SysEmgcfgModel> getSysEmgcfgModelList()
	{
		return sysEmgcfgModelList;
	}

	public void setSysEmgcfgModelList(List<SysEmgcfgModel> sysEmgcfgModelList)
	{
		this.sysEmgcfgModelList = sysEmgcfgModelList;
	}

	public String getShowMessage()
	{
		return showMessage;
	}

	public void setShowMessage(String showMessage)
	{
		this.showMessage = showMessage;
	}

	public String getNumbers() {
		return numbers;
	}

	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}
	
}
