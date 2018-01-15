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
import com.xinwei.lte.web.lte.model.WirelessImsiModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * 用户信息无线IMSIaction
 * 
 * <p>
 * 用户信息无线IMSIaction
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class UserImsiAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	private OnlinePage onlinePage;
	
	private WirelessImsiModel wirelessImsiModel;
	
	//保存查询条件
	private WirelessImsiModel querywirelessImsiModel;
	
	private List<WirelessImsiModel> wirelessImsiModelList;
	//记录日志
	private static Logger logger = LoggerFactory.getLogger(UserImsiAction.class);
	
	private String showMessage = "暂无相关数据";
	/**
	 * 跳转到无线IMSI页面并查询数据
	 * @return
	 */
	public String turntoImsi(){
		logger.debug("turntoImsi-start");
		wirelessImsiModelList = new ArrayList<WirelessImsiModel>();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			if(null == onlinePage){
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);
			}
			
			//若通过查询按钮查询
			if(querywirelessImsiModel != null){
				
				//若不是第一次登陆
				if(0 != querywirelessImsiModel.getFirst()){
					if(querywirelessImsiModel.getQueryType().equals("1")){
						map.put("imsi",querywirelessImsiModel.getQueryValue());	
					}else{
						map.put("imsiUEStatus",querywirelessImsiModel.getQueryValue());	
					}
				}					
			}else{
				querywirelessImsiModel = new WirelessImsiModel();
				//map.put("imsiUEStatus",wirelessImsiModel.getImsi_uestatus());
			}
			
			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			
			Map<String,Object> resultMap = ossAdapter.invoke(0xa0, 0x05, map);
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
			
			List<Map> resultList = (List<Map>) resultMap.get("imsiInfo");
			WirelessImsiModel wireImsiModel;
			if(null != resultList){
				for(Map rMap : resultList){
					wireImsiModel = new WirelessImsiModel();
					if(rMap.get("imsiIndex") != null){
						wireImsiModel.setImsi_index(((String)rMap.get("imsiIndex")).trim());
					}
					if(rMap.get("imsi") != null){
						wireImsiModel.setImsi(((String)rMap.get("imsi")).trim());
					}
					if(rMap.get("imsiK") != null){
						wireImsiModel.setImsi_k(((String)rMap.get("imsiK")).trim());
					}
					if(rMap.get("imsiUEStatus") != null){
						wireImsiModel.setImsi_uestatus(((String)rMap.get("imsiUEStatus")).trim());
					}
					
					wirelessImsiModelList.add(wireImsiModel);
				}
			}else{
				if(onlinePage.getCurrentPageNum() == 1 ){
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}	
			}			
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("turntoImsi error:" + e.toString());	
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			showMessage = e.getMessage();
		}		
		logger.debug("turntoImsi-end");
		return SUCCESS;
	}
	
	/**
	 * 查询IMSI
	 * @return
	 */
	public String searchImsi(){
		logger.debug("searchImsi - start");		
		wirelessImsiModelList = new ArrayList<WirelessImsiModel>();
		try{
			Map<String,Object> map = new HashMap<String,Object>();			
			map.put("ltePageSize", onlinePage.getPageSize()+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			if(0 != wirelessImsiModel.getFirst()){
				map.put("imsi",wirelessImsiModel.getImsi());
				map.put("imsiUEStatus",wirelessImsiModel.getImsi_uestatus());
			}				
		
			Map<String,Object> resultMap = ossAdapter.invoke(0xa0, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				onlinePage.setTotalPages(0);
				return turntoImsi();
			}
				int totalPages = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
				onlinePage.setTotalPages(totalPages);
				
				List<Map> resultList = (List<Map>) resultMap.get("imsiInfo");
				WirelessImsiModel wirelessImsiModel;
				if(null != resultList){
					for(Map rMap : resultList){
						wirelessImsiModel = new WirelessImsiModel();
						wirelessImsiModel.setImsi_index((String)rMap.get("imsiIndex"));
						wirelessImsiModel.setImsi((String)rMap.get("imsi"));
						wirelessImsiModel.setImsi_k((String)rMap.get("imsiK"));
						wirelessImsiModel.setImsi_uestatus((String)rMap.get("imsiUEStatus"));
						wirelessImsiModelList.add(wirelessImsiModel);
					}
				}			
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.toString());
		}
		logger.debug("searchImsi - end");
		return SUCCESS;
	}
	
	/**
	 * 跳转到无线IMSI配置页面
	 * @return
	 */
	public String turntoImsiAdd(){
		logger.debug("turntoImsiAdd");
		
		return SUCCESS;
	}
	
	/**
	 * 跳转到imsi批量导入
	 * @return
	 */
	public String turntoImsiBatchImport(){
		
		return SUCCESS;
	}
	
	
	/**增加无线IMSI
	 * 
	 * @return
	 */
	public String addImsi(){
		logger.debug("addImsi-start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("imsi",wirelessImsiModel.getImsi());
			map.put("imsiK",wirelessImsiModel.getImsi_k());
			map.put("imsiUEStatus",wirelessImsiModel.getImsi_uestatus());
			
			Map<String,Object> resultMap =  ossAdapter.invoke(0xa0,0x01,map);
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
			logger.error("AddImsi Error:"+e.toString());
		}
		logger.debug("addImsi-end");
		return NONE;
	}
	
	/**跳转到修改无线IMSI
	 * 
	 * @return
	 */
	public String toModifyImsi(){
		logger.debug("modifyImsi-start");
		
		try{
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ltePageSize", "1");
			map.put("ltePageIndex", "1");
			map.put("imsiIndex",wirelessImsiModel.getImsi_index());
			//map.put("imsiUEStatus",wirelessImsiModel.getImsi_uestatus());
			
			Map<String,Object> resultMap =  ossAdapter.invoke(0xa0,0x05,map);
			String flag = (String) resultMap.get("lteFlag");
			List<Map> resultList = (List<Map>) resultMap.get("imsiInfo");
			wirelessImsiModel = new WirelessImsiModel();
			if(resultList.size() != 0){
				Map rMap = resultList.get(0);
				if(rMap.get("imsiIndex") != null){
					wirelessImsiModel.setImsi_index(((String)rMap.get("imsiIndex")).trim());
				}
				if(rMap.get("imsi") != null){
					wirelessImsiModel.setImsi(((String)rMap.get("imsi")).trim());
				}
				if(rMap.get("imsiK") != null){
					wirelessImsiModel.setImsi_k(((String)rMap.get("imsiK")).trim());
				}
				if(rMap.get("imsiUEStatus") != null){
					wirelessImsiModel.setImsi_uestatus(((String)rMap.get("imsiUEStatus")).trim());
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("ModifyImsi Error:"+e.toString());
		}
		logger.debug("modifyImsi-end");
		return SUCCESS;
	}
	
	/**修改无线IMSI
	 * 
	 * @return
	 */
	public String modifyImsi(){
		logger.debug("modifyImsi-start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("imsi",wirelessImsiModel.getImsi());
			map.put("imsiK",wirelessImsiModel.getImsi_k());
			map.put("imsiUEStatus",wirelessImsiModel.getImsi_uestatus());
			
			Map<String,Object> resultMap =  ossAdapter.invoke(0xa0,0x03,map);
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
			logger.error("ModifyImsi Error:"+e.toString());
		}
		logger.debug("modifyImsi-end");
		return NONE;
	}
	
	/**删除无线IMSI
	 * 
	 * @return
	 */
	public String deleteImsi(){
		logger.debug("deleteImsi-start");
		JSONObject json = new JSONObject();
		try{
			String imsi = wirelessImsiModel.getImsi();
			String[] imsiArray = imsi.split(",");
			int errorCount = 0;
			if(imsiArray.length != 1){
				//若为批量删除
				for(String str : imsiArray){
					Map<String,Object> map = new HashMap<String,Object>();
					  map.put("imsi",str);
					  Map<String,Object> resultMap =  ossAdapter.invoke(0xa0,0x02,map);
					  String flag = (String) resultMap.get("lteFlag");
					  if(!"0".equals(flag)){
						  json.put("status", 1);
						  json.put("message", LteFlag.flagReturn(flag));
						  ajaxMethod(json.toString());
						  return NONE;
					  }
				}
			}else{
				//若为单个删除
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("imsi",imsi);
				Map<String,Object> resultMap =  ossAdapter.invoke(0xa0,0x02,map);
				 String flag = (String) resultMap.get("lteFlag");
				  if(!"0".equals(flag)){
					  json.put("status", 1);
					  json.put("message", LteFlag.flagReturn(flag));
					  ajaxMethod(json.toString());
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
			logger.error("DeleteImsi Error:"+e.toString()); 
		}
		logger.debug("deleteImsi-end");
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
	public WirelessImsiModel getWirelessImsiModel(){
		return wirelessImsiModel;
	}

	public void setWirelessImsiModel(WirelessImsiModel wirelessImsiModel){
		this.wirelessImsiModel = wirelessImsiModel;
	}

	public OnlinePage getOnlinePage()
	{
		return onlinePage;
	}

	public void setOnlinePage(OnlinePage onlinePage)
	{
		this.onlinePage = onlinePage;
	}

	public List<WirelessImsiModel> getWirelessImsiModelList()
	{
		return wirelessImsiModelList;
	}

	public void setWirelessImsiModelList(
			List<WirelessImsiModel> wirelessImsiModelList)
	{
		this.wirelessImsiModelList = wirelessImsiModelList;
	}

	public WirelessImsiModel getQuerywirelessImsiModel()
	{
		return querywirelessImsiModel;
	}

	public void setQuerywirelessImsiModel(WirelessImsiModel querywirelessImsiModel)
	{
		this.querywirelessImsiModel = querywirelessImsiModel;
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
