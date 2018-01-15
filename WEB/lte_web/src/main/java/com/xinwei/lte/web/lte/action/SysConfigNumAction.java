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
import com.xinwei.lte.web.lte.model.NumAnalyseModel;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * lte系统配置号码分析action
 * 
 * <p>
 * lte系统配置号码分析action
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class SysConfigNumAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	private OnlinePage onlinePage;
	
	private NumAnalyseModel numAnalyseModel;
	
	private NumAnalyseModel querynumAnalyseModel;
	private List<NumAnalyseModel> numAnalyseModelList;
	//记录日志
	private static Logger logger = LoggerFactory.getLogger(SysConfigNumAction.class);

	private String showMessage = "暂无相关数据";
	/**
	 * 跳转到号码分析页面
	 * @return
	 */
	public String turntoNumAnalyse(){
		logger.debug("turntoNumAnalyse-start");
		numAnalyseModelList = new ArrayList<NumAnalyseModel>();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			if(null == onlinePage){
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);	
			}

			if(null != querynumAnalyseModel){
				if(null != querynumAnalyseModel.getNa_prefix()){
					if(!"".equals(querynumAnalyseModel.getNa_prefix())){
						map.put("naPrefix", querynumAnalyseModel.getNa_prefix());
					}
					
				}
			}else{
				querynumAnalyseModel = new NumAnalyseModel();
				//map.put("naPrefix", "0");
			}
			
			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			
			Map<String,Object> resultMap = ossAdapter.invoke(0xa8, 0x05, map);
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
			
			List<Map> resultList = (List<Map>) resultMap.get("naInfo");
			NumAnalyseModel NumAnaModel;
			if(null != resultList){
				for(Map rMap : resultList){
					NumAnaModel = new NumAnalyseModel();
					if(rMap.get("naPrefix") != null){
						NumAnaModel.setNa_prefix(((String)rMap.get("naPrefix")).trim());
					}
					if(rMap.get("naMinLen") != null){
						NumAnaModel.setNa_minlen(((String)rMap.get("naMinLen")).trim());
					}
					if(rMap.get("naMaxLen") != null){
						NumAnaModel.setNa_maxlen(((String)rMap.get("naMaxLen")).trim());
					}
					if(rMap.get("naAttr") != null){
						NumAnaModel.setNa_attr(((String)rMap.get("naAttr")).trim());
					}
					if(rMap.get("sipID") != null){
						NumAnaModel.setSip_id(((String)rMap.get("sipID")).trim());
					}
	
					numAnalyseModelList.add(NumAnaModel);
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
		
		logger.debug("turntoNumAnalyse-end");
		return SUCCESS;
	}
	/**
	 * 跳转到号码分析配置页面
	 * @return
	 */
	public String turntoNumAnalyseAdd(){
		logger.debug("turntoNumAnalyse");
		
		return SUCCESS;
	}
	
	/**
	 * 增加号码分析
	 * @return
	 */
	public String addNumAnalyse(){
		logger.debug("addNumAnalyse-start");
		JSONObject json = new JSONObject();
		try{			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("naPrefix",numAnalyseModel.getNa_prefix());
			map.put("naMinLen", numAnalyseModel.getNa_minlen());
			map.put("naMaxLen", numAnalyseModel.getNa_maxlen());
			map.put("naAttr", numAnalyseModel.getNa_attr());
			map.put("sipID",numAnalyseModel.getSip_id());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa8, 0x01, map);
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
			logger.error("addNumAnalyse error:"+e.toString());		
		}
		logger.debug("addNumAnalyse-end");
		return NONE;
	}
	
	/**
	 * 跳转到修改号码分析
	 * @return
	 */
	public String toModifyNumAnalyse(){
		logger.debug("toModifyNumAnalyse-start");

		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ltePageSize", "1");
			map.put("ltePageIndex", "1");
			map.put("naPrefix", numAnalyseModel.getNa_prefix());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa8, 0x05, map);
			
			List<Map> resultList = (List<Map>) resultMap.get("naInfo");
						
			if(resultList.size() != 0){
				Map rMap = resultList.get(0);				
				if(rMap.get("naPrefix") != null){
					numAnalyseModel.setNa_prefix(((String)rMap.get("naPrefix")).trim());
				}
				if(rMap.get("naMinLen") != null){
					numAnalyseModel.setNa_minlen(((String)rMap.get("naMinLen")).trim());
				}
				if(rMap.get("naMaxLen") != null){
					numAnalyseModel.setNa_maxlen(((String)rMap.get("naMaxLen")).trim());
				}
				if(rMap.get("naAttr") != null){
					numAnalyseModel.setNa_attr(((String)rMap.get("naAttr")).trim());
				}
				if(rMap.get("sipID") != null){
					numAnalyseModel.setSip_id(((String)rMap.get("sipID")).trim());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("toModifyNumAnalyse error:"+e.toString());
			
		}
		logger.debug("toModifyNumAnalyse-end");
		return SUCCESS;
	}
	
	/**
	 * 修改号码分析
	 * @return
	 */
	public String modifyNumAnalyse(){
		logger.debug("modifyNumAnalyse-start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("naPrefix",numAnalyseModel.getNa_prefix());
			map.put("naMinLen", numAnalyseModel.getNa_minlen());
			map.put("naMaxLen", numAnalyseModel.getNa_maxlen());
			map.put("naAttr", numAnalyseModel.getNa_attr());
			map.put("sipID",numAnalyseModel.getSip_id());
			Map<String,Object> resultMap = ossAdapter.invoke(0xa8, 0x03, map);
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
			logger.error("modifyNumAnalyse error:"+e.toString());
		}
		logger.debug("modifyNumAnalyse-end");
		return NONE;
	}
	
	/**
	 * 删除号码分析
	 * @return
	 */
	public String deleteNumAnalyse(){
		logger.debug("deleteNumAnalyse-start");
		JSONObject json = new JSONObject();
		try{
			String na_prefix = numAnalyseModel.getNa_prefix();
			String[] na_prefixArray = na_prefix.split(",");
			if(na_prefixArray.length == 1){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("naPrefix",na_prefix);
				Map<String,Object> resultMap = ossAdapter.invoke(0xa8, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					return NONE;
				}
			}else{
				for(String str : na_prefixArray){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("naPrefix",str);
					Map<String,Object> resultMap = ossAdapter.invoke(0xa8, 0x02, map);
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
			logger.error("deleteNumAnalyse error:"+e.toString());
		}
		logger.debug("deleteNumAnalyse-end");
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
	public NumAnalyseModel getNumAnalyseModel()
	{
		return numAnalyseModel;
	}
	public void setNumAnalyseModel(NumAnalyseModel numAnalyseModel)
	{
		this.numAnalyseModel = numAnalyseModel;
	}
	public List<NumAnalyseModel> getNumAnalyseModelList()
	{
		return numAnalyseModelList;
	}
	public void setNumAnalyseModelList(List<NumAnalyseModel> numAnalyseModelList)
	{
		this.numAnalyseModelList = numAnalyseModelList;
	}
	public NumAnalyseModel getQuerynumAnalyseModel()
	{
		return querynumAnalyseModel;
	}
	public void setQuerynumAnalyseModel(NumAnalyseModel querynumAnalyseModel)
	{
		this.querynumAnalyseModel = querynumAnalyseModel;
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
