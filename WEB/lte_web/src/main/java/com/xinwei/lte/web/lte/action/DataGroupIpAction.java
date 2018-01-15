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
import com.xinwei.lte.web.domain.JSONResult;
import com.xinwei.lte.web.domain.LteFlag;
import com.xinwei.lte.web.lte.model.DataGroupIp;
import com.xinwei.lte.web.lte.model.OnlinePage;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.oss.adapter.OssAdapter;

public class DataGroupIpAction extends ActionSupport{
	
	//记录日志
	private static Logger logger = LoggerFactory.getLogger(PdtConfigInfoAction.class);
	
	@Resource
	private OssAdapter ossAdapter;
	
	private JSONResult jsonResult;
	
	private OnlinePage onlinePage = new OnlinePage();
	
	private List<DataGroupIp> dataGroupIpList;
	
	private DataGroupIp dataGroupIp;

	/**
	 * 查询列表
	 * @return
	 */
	public String showDataGroupIp(){
		
		jsonResult = new JSONResult();
		dataGroupIpList = new ArrayList<DataGroupIp>();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ltePageSize", onlinePage.getPageSize()+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			DataGroupIp dataGroupIp = null;
			Map<String,Object> resultMap = ossAdapter.invoke(0xc2, 0x05, map);
			
			int lteTotalQueryCount = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				return "SHOW_CONFIG_INFO";
			}
			if(lteTotalQueryCount%LteConstant.PageSize == 0){
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize);
			}else{
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize+1);
			}	
			List<Map> resultList = (List<Map>) resultMap.get("tagdatagroupcfginfo");
			
			if(null != resultList){
				for(Map rMap : resultList){
					
					dataGroupIp = new DataGroupIp();
					
					if(rMap.get("gdn") != null){
						dataGroupIp.setGdn(rMap.get("gdn")+"");
					}
					
					if(rMap.get("ipAddr") != null){
						dataGroupIp.setIpAddr(rMap.get("ipAddr")+"");
					}
					if(rMap.get("bmType") != null){
						dataGroupIp.setBmType(rMap.get("bmType")+"");
					}
					if(rMap.get("bmQci") != null){
						dataGroupIp.setBmQci(rMap.get("bmQci")+"");
					}
					
					if(rMap.get("bmUplinkMbr") != null){
						dataGroupIp.setBmUplinkMbr(rMap.get("bmUplinkMbr")+"");
					}
					
					if(rMap.get("bmDownlinkMbr") != null){
						dataGroupIp.setBmDownlinkMbr(rMap.get("bmDownlinkMbr")+"");
					}
					
					if(rMap.get("bmUplinkGbr") != null){
						dataGroupIp.setBmUplinkGbr(rMap.get("bmUplinkGbr")+"");
					}
					
					if(rMap.get("bmDownlinkGbr") != null){
						dataGroupIp.setBmDownlinkGbr(rMap.get("bmDownlinkGbr")+"");
					}
					
					if(rMap.get("comment") != null){
						dataGroupIp.setComment(rMap.get("comment")+"");
					}
					dataGroupIpList.add(dataGroupIp);
				}
			}else{
				if(onlinePage.getCurrentPageNum() == 1 ){
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}	
			}	
			
		} catch (Exception e) {
			jsonResult.setErrorCode("-1");
			jsonResult.setErrorMsg(e.getMessage());
			logger.debug("showConfigInfo error.",e);
			e.printStackTrace();
		}
		
		return "SHOW_CONFIG_INFO";
	}
	
	/**
	 * 跳转至新增页面
	 * @return
	 */
	public String toDataGroupIpAddPage(){
		
		return "TO_ADD_PAGE";
	}
	
	/**
	 * 新增
	 * @return
	 */
	public String addDataGroupIp(){
		jsonResult = new JSONResult();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("gdn", dataGroupIp.getGdn());
			map.put("ipAddr", dataGroupIp.getIpAddr());
			map.put("bmType", dataGroupIp.getBmType());
			map.put("bmQci", dataGroupIp.getBmQci());
			map.put("bmUplinkMbr", dataGroupIp.getBmUplinkMbr());
			map.put("bmDownlinkMbr", dataGroupIp.getBmDownlinkMbr());
			map.put("bmUplinkGbr", dataGroupIp.getBmUplinkGbr());
			map.put("bmDownlinkGbr", dataGroupIp.getBmDownlinkGbr());
			map.put("comment", dataGroupIp.getComment());
			Map<String,Object> resultMap = ossAdapter.invoke(0xc2, 0x01, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				jsonResult.setErrorCode("-1");
				jsonResult.setErrorMsg(LteFlag.flagReturn(flag));
			}
		} catch (Exception e) {
			logger.debug("add error.",e);
			e.printStackTrace();
			jsonResult.setErrorCode("-1");
			jsonResult.setErrorMsg(e.getMessage());
		}
		
		returnJsonObject(jsonResult);
		return NONE;
	}
	
	
	
	/**
	 * 删除
	 * @return
	 */
	public String deleteDataGroupIps(){
		jsonResult = new JSONResult();
		try {
			String gdnStr = dataGroupIp.getGdn();
			String[] gdns = gdnStr.split(",");
			for(String gdn : gdns){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("gdn", gdn);
				Map<String,Object> resultMap = ossAdapter.invoke(0xc2, 0x02, map);
				String flag = (String) resultMap.get("lteFlag");
				if(!"0".equals(flag)){
					jsonResult.setErrorCode("-1");
					jsonResult.setErrorMsg(LteFlag.flagReturn(flag));
					break;
				}
			}
		} catch (Exception e) {
			logger.debug("delete error.",e);
			e.printStackTrace();
			jsonResult.setErrorCode("-1");
			jsonResult.setErrorMsg(e.getMessage());
		}
		returnJsonObject(jsonResult);
		return NONE;
	}
	
	/**
	 * 跳转至修改页面
	 * @return
	 */
	public String toDataGroupIpModifyPage(){
		
		return "TO_MODIFY_PAGE";
	}
	/**
	 * 修改
	 * @return
	 */
	public String modifyDataGroupIp(){
		jsonResult = new JSONResult();
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("gdn", dataGroupIp.getGdn());
			map.put("ipAddr", dataGroupIp.getIpAddr());
			map.put("bmType", dataGroupIp.getBmType());
			map.put("bmQci", dataGroupIp.getBmQci());
			map.put("bmUplinkMbr", dataGroupIp.getBmUplinkMbr());
			map.put("bmDownlinkMbr", dataGroupIp.getBmDownlinkMbr());
			map.put("bmUplinkGbr", dataGroupIp.getBmUplinkGbr());
			map.put("bmDownlinkGbr", dataGroupIp.getBmDownlinkGbr());
			map.put("comment", dataGroupIp.getComment());
			Map<String,Object> resultMap = ossAdapter.invoke(0xc2, 0x03, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				jsonResult.setErrorCode("-1");
				jsonResult.setErrorMsg(LteFlag.flagReturn(flag));
			}
		} catch (Exception e) {
			logger.debug("add error.",e);
			e.printStackTrace();
			jsonResult.setErrorCode("-1");
			jsonResult.setErrorMsg(e.getMessage());
		}
		
		returnJsonObject(jsonResult);
		return NONE;
	}
	
	//异步请求返回单个对象
	private void returnJsonObject(Object content){
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
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		DataGroupIpAction.logger = logger;
	}

	public OssAdapter getOssAdapter() {
		return ossAdapter;
	}

	public void setOssAdapter(OssAdapter ossAdapter) {
		this.ossAdapter = ossAdapter;
	}

	public JSONResult getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(JSONResult jsonResult) {
		this.jsonResult = jsonResult;
	}

	public OnlinePage getOnlinePage() {
		return onlinePage;
	}

	public void setOnlinePage(OnlinePage onlinePage) {
		this.onlinePage = onlinePage;
	}

	public List<DataGroupIp> getDataGroupIpList() {
		return dataGroupIpList;
	}

	public void setDataGroupIpList(List<DataGroupIp> dataGroupIpList) {
		this.dataGroupIpList = dataGroupIpList;
	}

	public DataGroupIp getDataGroupIp() {
		return dataGroupIp;
	}

	public void setDataGroupIp(DataGroupIp dataGroupIp) {
		this.dataGroupIp = dataGroupIp;
	}
	
	

}
