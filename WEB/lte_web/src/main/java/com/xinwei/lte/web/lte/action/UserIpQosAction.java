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
import com.xinwei.lte.web.lte.model.CallQos;
import com.xinwei.lte.web.lte.model.IpQos;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.QosModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * lte系统配置呼叫QOSaction
 * 
 * <p>
 * lte系统配置QOSaction
 * </p> 
 * 
 * @author yinyuelin
 * 
 */

public class UserIpQosAction extends ActionSupport{
	
	@Resource
	private OssAdapter ossAdapter;
	
	private OnlinePage onlinePage;
	
	private IpQos ipQos;
	
	private List<IpQos> ipQosList;
	//记录日志
	private static Logger logger = LoggerFactory.getLogger(UserIpQosAction.class);

	private String showMessage = "暂无相关数据";
	/**
	 * 跳转到QOS页面
	 * @return
	 */
	public String turntoIpQos(){
		logger.debug("turntoQos - start");
		ipQosList = new ArrayList<IpQos>();
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
			Map<String,Object> resultMap = ossAdapter.invoke(0xb2, 0x05, map);			
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				return "toIpQos";
			}
			int lteTotalQueryCount = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			if(lteTotalQueryCount%LteConstant.PageSize == 0){
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize);
			}else{
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize+1);
			}	
			
			List<Map> resultList = (List<Map>) resultMap.get("ipqosInfo");
			if(resultList != null){
				for(Map rMap : resultList){
					ipQos = new IpQos();
					if(rMap.get("ipqosIndex") != null){
						ipQos.setIpqos_index(((String)rMap.get("ipqosIndex")).trim());
					}
					if(rMap.get("ipqosIp") != null){
						ipQos.setIpqos_ip(((String)rMap.get("ipqosIp")).trim());
					}
					if(rMap.get("ipqosMaxPort") != null){
						ipQos.setIpqos_max_port(((String)rMap.get("ipqosMaxPort")).trim());
					}
					if(rMap.get("ipqosMinPort") != null){
						ipQos.setIpqos_min_port(((String)rMap.get("ipqosMinPort")).trim());
					}
					if(rMap.get("ipqosQci") != null){
						ipQos.setIpqos_qci(((String)rMap.get("ipqosQci")).trim());
					}
					if(rMap.get("ipqosUplinkMbr") != null){
						ipQos.setIpqos_uplink_mbr(((String)rMap.get("ipqosUplinkMbr")).trim());
					}
					if(rMap.get("ipqosDownlinkMbr") != null){
						ipQos.setIpqos_downlink_mbr(((String)rMap.get("ipqosDownlinkMbr")).trim());
					}
					if(rMap.get("ipqosUplinkGbr") != null){
						ipQos.setIpqos_uplink_gbr(((String)rMap.get("ipqosUplinkGbr")).trim());
					}
					
					if(rMap.get("ipqosDownlinkGbr") != null){
						ipQos.setIpqos_downlink_gbr(((String)rMap.get("ipqosDownlinkGbr")).trim());
					}
					
					ipQosList.add(ipQos);
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
		return "toIpQos";
	}
	
	/**
	 * 跳转到新增页面
	 * @return
	 */
	public String toIpQosAdd(){
		
		return "toIpQosAdd";
	}
	
	/**
	 * 增加
	 * @return
	 */
	public String addIpQos(){
		JSONObject json = new JSONObject();
		try
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ipqosIp", ipQos.getIpqos_ip());
			//map.put("qosType",qosModel.getQos_type());
			//map.put("qosPriority",qosModel.getQos_priority());
			map.put("ipqosMaxPort",ipQos.getIpqos_max_port());
			map.put("ipqosMinPort",ipQos.getIpqos_min_port());
			map.put("ipqosQci", ipQos.getIpqos_qci());
			map.put("ipqosUplinkMbr",ipQos.getIpqos_uplink_mbr());
			map.put("ipqosDownlinkMbr",ipQos.getIpqos_downlink_mbr());
			map.put("ipqosUplinkGbr",ipQos.getIpqos_uplink_gbr());
			map.put("ipqosDownlinkGbr",ipQos.getIpqos_downlink_gbr());
			Map<String,Object> resultMap = ossAdapter.invoke(0xb2, 0x01, map);
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
		}
		catch (Exception e)
		{
			logger.error("",e);
		}
		
		return NONE;
	}

	/**
	 * 跳转到修改QOS
	 * @return
	 */
	public String toModifyQos(){
		logger.debug("toModifyQos");
	
		return "toModifyQos";
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
			map.put("ipqosIndex", ipQos.getIpqos_index());
			map.put("ipqosIp", ipQos.getIpqos_ip());
			//map.put("qosType",qosModel.getQos_type());
			//map.put("qosPriority",qosModel.getQos_priority());
			map.put("ipqosMaxPort",ipQos.getIpqos_max_port());
			map.put("ipqosMinPort",ipQos.getIpqos_min_port());
			map.put("ipqosQci", ipQos.getIpqos_qci());
			map.put("ipqosUplinkMbr",ipQos.getIpqos_uplink_mbr());
			map.put("ipqosDownlinkMbr",ipQos.getIpqos_downlink_mbr());
			map.put("ipqosUplinkGbr",ipQos.getIpqos_uplink_gbr());
			map.put("ipqosDownlinkGbr",ipQos.getIpqos_downlink_gbr());
			Map<String,Object> resultMap = ossAdapter.invoke(0xb2, 0x03, map);
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
	
	/**
	 * 删除
	 * @return
	 */
	public String deleteIpQos(){
		logger.debug("deleteSysAddress-start");
		JSONObject json = new JSONObject();
		try{
			
			String ipqos_index = ipQos.getIpqos_index();
			String[] ipqos_indexArray = ipqos_index.split(",");
			if(ipqos_indexArray.length == 1){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("ipqosIndex", ipqos_index);
				Map<String,Object> resultMap = ossAdapter.invoke(0xb2, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					return NONE;
				}
			}else{
				for(int i = 0 ; i <  ipqos_indexArray.length; i ++){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("ipqosIndex", ipqos_indexArray[i]);
					Map<String,Object> resultMap = ossAdapter.invoke(0xb2, 0x02, map);
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
			json.put("status", 1);
			json.put("message", e.getMessage());
			ajaxMethod(json.toString());
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

	public IpQos getIpQos()
	{
		return ipQos;
	}

	public void setIpQos(IpQos ipQos)
	{
		this.ipQos = ipQos;
	}

	public List<IpQos> getIpQosList()
	{
		return ipQosList;
	}

	public void setIpQosList(List<IpQos> ipQosList)
	{
		this.ipQosList = ipQosList;
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
