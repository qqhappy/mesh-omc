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
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.SysAllInfoModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * lte系统配置系统全局信息action
 * 
 * <p>
 * lte系统配置系统全局信息action
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class SysConfigAllInfoAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	private OnlinePage onlinePage;
	
	private SysAllInfoModel sysAllInfoModel;
	
	private List<SysAllInfoModel> sysAllInfoModelList;
	//记录日志
	private static Logger logger = LoggerFactory.getLogger(SysConfigAllInfoAction.class);
	
	private String showMessage = "暂无相关数据";
	/**
	 * 跳转到系统全局信息页面
	 * @return
	 */
	public String turntoWholeInfo(){
		logger.debug("turntoWholeInfo-start");
		sysAllInfoModelList = new ArrayList<SysAllInfoModel>();
		try{
			if(null == onlinePage){
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);				
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");

			Map<String,Object> resultMap = ossAdapter.invoke(0xaa, 0x05, map);
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
			
			List<Map> resultList = (List<Map>) resultMap.get("sysInfo");
			if(resultList != null){
				for(Map rMap : resultList){
					sysAllInfoModel = new SysAllInfoModel();
					
					if(rMap.get("sysInfoName") != null){
						sysAllInfoModel.setSysinfo_name(((String)rMap.get("sysInfoName")).trim());
					}
					if(rMap.get("sysInfoValue") != null){
						sysAllInfoModel.setSysinfo_value(((String)rMap.get("sysInfoValue")).trim());	
					}
					
					sysAllInfoModelList.add(sysAllInfoModel);
				}
			}else{
				if(onlinePage.getCurrentPageNum() == 1 ){
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}	
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.toString());
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			showMessage = e.getMessage();
		}
		
		logger.debug("turntoWholeInfo-end");
		return SUCCESS;
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

	public SysAllInfoModel getSysAllInfoModel()
	{
		return sysAllInfoModel;
	}

	public void setSysAllInfoModel(SysAllInfoModel sysAllInfoModel)
	{
		this.sysAllInfoModel = sysAllInfoModel;
	}

	public List<SysAllInfoModel> getSysAllInfoModelList()
	{
		return sysAllInfoModelList;
	}

	public void setSysAllInfoModelList(List<SysAllInfoModel> sysAllInfoModelList)
	{
		this.sysAllInfoModelList = sysAllInfoModelList;
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
