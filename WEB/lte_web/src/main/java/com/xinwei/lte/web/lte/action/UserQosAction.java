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
import com.xinwei.lte.web.lte.model.QosModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * lte系统配置QOSaction
 * 
 * <p>
 * lte系统配置QOSaction
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class UserQosAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	private OnlinePage onlinePage;
	
	private QosModel qosModel;
	
	private List<QosModel> qosModelList;
	//记录日志
	private static Logger logger = LoggerFactory.getLogger(UserQosAction.class);

	private String showMessage = "暂无相关数据";
	/**
	 * 跳转到QOS页面
	 * @return
	 */
	public String turntoQos(){
		logger.debug("turntoQos - start");
		qosModelList = new ArrayList<QosModel>();
		try{
			if(null == onlinePage){
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);				
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			//map.put("ipId", systemAddressModel.getIp_id());
			Map<String,Object> resultMap = ossAdapter.invoke(0xad, 0x05, map);			
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
			
			List<Map> resultList = (List<Map>) resultMap.get("qosInfo");
			if(resultList != null){
				for(Map rMap : resultList){
					qosModel = new QosModel();
					if(rMap.get("qosID") != null){
						qosModel.setQos_id(((String)rMap.get("qosID")).trim());
					}
					if(rMap.get("qosType") != null){
						qosModel.setQos_type(((String)rMap.get("qosType")).trim());
					}
					if(rMap.get("qosPriority") != null){
						qosModel.setQos_priority(((String)rMap.get("qosPriority")).trim());
					}
					if(rMap.get("qosUplinkMBR") != null){
						qosModel.setQos_uplink_mbr(((String)rMap.get("qosUplinkMBR")).trim());
					}
					if(rMap.get("qosDownlinkMBR") != null){
						qosModel.setQos_downlink_mbr(((String)rMap.get("qosDownlinkMBR")).trim());
					}
					if(rMap.get("qosUplinkGBR") != null){
						qosModel.setQos_uplink_gbr(((String)rMap.get("qosUplinkGBR")).trim());
					}
					if(rMap.get("qosDownlinkGBR") != null){
						qosModel.setQos_downlink_gbr(((String)rMap.get("qosDownlinkGBR")).trim());
					}
					
					qosModelList.add(qosModel);
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
		logger.debug("turntoQos - end");
		return SUCCESS;
	}

	/**
	 * 跳转到修改QOS
	 * @return
	 */
	public String toModifyQos(){
		logger.debug("toModifyQos");
	
		return SUCCESS;
	}
	
	/**
	 * 修改QOS
	 * @return
	 */
	public String modifyQos(){
		logger.debug("modifyQos-start");
		JSONObject json = new JSONObject();
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("qosID", qosModel.getQos_id());
			//map.put("qosType",qosModel.getQos_type());
			//map.put("qosPriority",qosModel.getQos_priority());
			map.put("qosUplinkMBR",qosModel.getQos_uplink_mbr());
			map.put("qosDownlinkMBR",qosModel.getQos_downlink_mbr());
			map.put("qosUplinkGBR", qosModel.getQos_uplink_gbr());
			map.put("qosDownlinkGBR",qosModel.getQos_downlink_gbr());
			Map<String,Object> resultMap = ossAdapter.invoke(0xad, 0x03, map);
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
			logger.error("modifyQos error:"+e.toString());
		}
		logger.debug("modifyQos-end");
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

	public QosModel getQosModel()
	{
		return qosModel;
	}

	public void setQosModel(QosModel qosModel)
	{
		this.qosModel = qosModel;
	}

	public List<QosModel> getQosModelList()
	{
		return qosModelList;
	}

	public void setQosModelList(List<QosModel> qosModelList)
	{
		this.qosModelList = qosModelList;
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
