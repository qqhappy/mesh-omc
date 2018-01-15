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
import com.xinwei.lte.web.lte.model.MmeTaModel;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * lte系统配置MME TA链路action
 * 
 * <p>
 * lte系统配置MME TA链路action
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class SysConfigTaAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	private OnlinePage onlinePage;
	
	private MmeTaModel mmeTaModel;
	
	private List<MmeTaModel> mmeTaModelList;
	//记录日志
	private static Logger logger = LoggerFactory.getLogger(SysConfigTaAction.class);
	
	private String showMessage = "暂无相关数据";
	
	/**
	 * 跳转到MME TA列表页面
	 * @return
	 */
	public String turntoTa(){
		logger.debug("turntoTa-start");
		mmeTaModelList = new ArrayList<MmeTaModel>();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			if(null == onlinePage){
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);				
			}
			

			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			Map<String,Object> resultMap = ossAdapter.invoke(0xac, 0x05, map);

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
			
			List<Map> resultList = (List<Map>) resultMap.get("mmeTAInfo");
			if(resultList != null){
				for(Map rMap : resultList){
					mmeTaModel = new MmeTaModel();
					if(rMap.get("mmeTAID") != null){
						mmeTaModel.setMmeta_id(((String)rMap.get("mmeTAID")).trim());
					}
					if(rMap.get("mmeTATAI") != null){
						mmeTaModel.setMmeta_tai(((String)rMap.get("mmeTATAI")).trim());
					}
					if(rMap.get("mmeTAComment") != null){
						mmeTaModel.setMmeta_comment(((String)rMap.get("mmeTAComment")).trim());
					}
					
					mmeTaModelList.add(mmeTaModel);
				}
			}else{
				if(onlinePage.getCurrentPageNum() == 1 ){
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}	
			}			
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("turntoTa error:"+e.toString());
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			showMessage = e.getMessage();
		}
		logger.debug("turntoTa-end");
		return SUCCESS;
	}
	
	/**
	 * 跳转到MME TA列表配置页面
	 * @return
	 */
	public String turntoTaAdd(){
		logger.debug("turntoTaAdd");
		return SUCCESS;
	}
	
	/**
	 * 增加MME TA列表
	 * @return
	 */
	public String addTa(){
		logger.debug("addTa-start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("mmeTAID", mmeTaModel.getMmeta_id());
			map.put("mmeTATAI",mmeTaModel.getMmeta_tai());
			map.put("mmeTAComment",mmeTaModel.getMmeta_comment());
			Map<String,Object> resultMap = ossAdapter.invoke(0xac, 0x01, map);
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
			logger.error("addTa error:"+e.toString());
		}
		logger.debug("addTa-end");
		return NONE;
	}
	
	/**
	 * 跳转到修改MME TA列表
	 * @return
	 */
	public String toModifyTa(){
		logger.debug("toModifyTa-start");
		try{
			mmeTaModel.setMmeta_comment(new String(mmeTaModel.getMmeta_comment().getBytes("iso-8859-1"),"utf-8"));
		}catch(Exception e){
			e.printStackTrace();
			logger.error("toModifyTa error:"+e.toString());
			
		}
		logger.debug("toModifyTa-end");
		return SUCCESS;
	}
	
	/**
	 * 修改MME TA列表
	 * @return
	 */
	public String modifyTa(){
		logger.debug("modifyTa-start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("mmeTAID", mmeTaModel.getMmeta_id());
			map.put("mmeTATAI",mmeTaModel.getMmeta_tai());
			map.put("mmeTAComment",mmeTaModel.getMmeta_comment());
			Map<String,Object> resultMap = ossAdapter.invoke(0xac, 0x03, map);
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
			logger.error("modifyTa error:"+e.toString());
		}
		logger.debug("modifyTa-end");
		return NONE;
	}
	
	/**
	 * 删除MME TA列表
	 * @return
	 */
	public String deleteTa(){
		logger.debug("deleteTa-start");
		JSONObject json = new JSONObject();
		try{			
			String mmeta_id = mmeTaModel.getMmeta_id();
			String[] mmeta_idArray = mmeta_id.split(",");
			if(mmeta_idArray.length == 1){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("mmeTAID", mmeta_id);
				Map<String,Object> resultMap = ossAdapter.invoke(0xac, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					return NONE;
				}
			}else{
				for(String str : mmeta_idArray){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("mmeTAID", str);
					Map<String,Object> resultMap = ossAdapter.invoke(0xac, 0x02, map);
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
			logger.error("deleteTa error:"+e.toString());
		}
		logger.debug("deleteTa-end");
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

	public MmeTaModel getMmeTaModel()
	{
		return mmeTaModel;
	}

	public void setMmeTaModel(MmeTaModel mmeTaModel)
	{
		this.mmeTaModel = mmeTaModel;
	}

	public List<MmeTaModel> getMmeTaModelList()
	{
		return mmeTaModelList;
	}

	public void setMmeTaModelList(List<MmeTaModel> mmeTaModelList)
	{
		this.mmeTaModelList = mmeTaModelList;
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
