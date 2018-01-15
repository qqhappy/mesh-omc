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
import com.xinwei.lte.web.lte.model.VmgNumModel;
import com.xinwei.lte.web.lte.model.constant.LteConstant;
import com.xinwei.lte.web.lte.service.VmgNumService;
import com.xinwei.minas.core.exception.NoAuthorityException;
import com.xinwei.oss.adapter.OssAdapter;

/**
 * 
 * 视频监控号码action
 * 
 * <p>
 * 视频监控号码action
 * </p>
 * 
 * @author yinyuelin
 * 
 */

public class VmgNumAction extends ActionSupport {

	@Resource
	private OssAdapter ossAdapter;

	@Resource
	private VmgNumService vmgNumService;

	// 日志
	private static Logger logger = LoggerFactory.getLogger(VmgNumAction.class);

	private OnlinePage onlinePage;

	private VmgNumModel vmgNumModel;

	// 保存查询模型
	private VmgNumModel queryVmgNumModel;

	private List<VmgNumModel> vmgNumModelList;
	private List<VmgCfgModel> vmgCfgModelList;
	private String showMessage = "暂无相关数据";

	/**
	 * 跳转到用户参数模板页面
	 * 
	 * @return
	 */
	public String turntoVmgNum(){
		logger.debug("turntoVmgNum - start");
		
		
		
		vmgNumModelList = new ArrayList<VmgNumModel>();
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
			if(queryVmgNumModel != null){
				
				//若不是第一次登陆
				if(0 != queryVmgNumModel.getFirst()){
						map.put("monitorName",new String(queryVmgNumModel.getQueryValue().getBytes("iso-8859-1"),"utf-8"));	
						queryVmgNumModel.setQueryValue(new String(queryVmgNumModel.getQueryValue().getBytes("iso-8859-1"),"utf-8"));
				}					
			}else{
				queryVmgNumModel = new VmgNumModel();
				//map.put("imsiUEStatus",wirelessImsiModel.getImsi_uestatus());
			}
			
			//设置查询条件
			map.put("ltePageSize", LteConstant.PageSize+"");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum()+"");
			Map<String,Object> resultMap = ossAdapter.invoke(0xb5, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if(!"0".equals(flag)){
				//查询失败
				onlinePage.setTotalPages(1);
				onlinePage.setCurrentPageNum(1);
				logger.warn("ossAdapter.invoke error, flag = " + flag);
				return "turntoVmgNum";
			}
			
			//查询到的数据总个数
			int lteTotalQueryCount = Integer.parseInt((String)resultMap.get("lteTotalQueryCount"));
			if(lteTotalQueryCount%LteConstant.PageSize == 0){
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize);
			}else{
				onlinePage.setTotalPages(lteTotalQueryCount/LteConstant.PageSize+1);
			}		
			
			//返回的数据集合
			List<Map> resultList = (List<Map>) resultMap.get("vmgNumInfo");
			VmgNumModel vmgNumModel;
			if(null != resultList){
				for(Map rMap : resultList){
					vmgNumModel = new VmgNumModel();
					if(rMap.get("callNumberIndex") != null){
						vmgNumModel.setCall_number_index(((String)rMap.get("callNumberIndex")).trim());
					}
					if(rMap.get("callNumber") != null){
						vmgNumModel.setCall_number(((String)rMap.get("callNumber")).trim());
					}
					if(rMap.get("authPassword") != null){
						vmgNumModel.setAuth_password(((String)rMap.get("authPassword")).trim());
					}
					if(rMap.get("monitorName") != null){
						vmgNumModel.setMonitor_name(((String)rMap.get("monitorName")).trim());
					}
					if(rMap.get("channelId") != null){
						vmgNumModel.setChannel_id(((String)rMap.get("channelId")).trim());
					}
					if(rMap.get("comment") != null){
						vmgNumModel.setComment(((String)rMap.get("comment")).trim());
					}
													
					vmgNumModelList.add(vmgNumModel);
				}
			}else{
				if(onlinePage.getCurrentPageNum() == 1 ){
					onlinePage.setCurrentPageNum(1);
					onlinePage.setTotalPages(1);
				}
			}
			
		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("turntoVmgNum error:"+e.toString());
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			showMessage = e.getMessage();
		}
		logger.debug("turntoVmgNum - end");
		return "turntoVmgNum";
	}

	/**
	 * 跳转到用户参数模板配置页面
	 * 
	 * @return
	 */
	public String turntoVmgNumAdd() {
		logger.debug("turntoVmgNumAdd");
		//获取视频监控设备
		vmgCfgModelList = new ArrayList<VmgCfgModel>();
		try{
			Map<String, Object> templateMap = new HashMap<String, Object>();
			templateMap.put("ltePageSize", "50");
			templateMap.put("ltePageIndex", "1");
			Map<String, Object> templateResultMap = ossAdapter.invoke(0xb4, 0x05, templateMap);
			String templateFlag = (String) templateResultMap.get("lteFlag");
			if ("0".equals(templateFlag)) {
				List<Map> templateresultList = (List<Map>) templateResultMap.get("vmgCfgInfo");
				VmgCfgModel vmgCfgModel;
				if (templateresultList != null) {
					for(Map rMap : templateresultList){
						vmgCfgModel = new VmgCfgModel();
						
						if(rMap.get("monitorName") != null){
							vmgCfgModel.setMonitor_name(((String)rMap.get("monitorName")).trim());
						}
						
														
						vmgCfgModelList.add(vmgCfgModel);
				}
			}
		
			}	
		
		}catch(Exception e){
			e.printStackTrace();
			logger.error("turntoVmgNum error:"+e.toString());
			onlinePage.setCurrentPageNum(1);
			onlinePage.setTotalPages(1);
			showMessage = e.getMessage();
		}
		return "turntoVmgNumAdd";
	}

	/**
	 * 增加用户参数模板
	 * 
	 * @return
	 */
	public String addVmgNum() {
		logger.debug("addVmgNum-start");
		JSONObject json = new JSONObject();
		try {

			// 数据校验
			OperResult operResult = vmgNumService.checkVmgNumModel(vmgNumModel);
			if (!operResult.isSuccess()) {
				json.put("status", 1);
				json.put("message", operResult.getRetMsg());
				ajaxMethod(json.toString());
				return NONE;
			}

			// 赋值
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("callNumber", vmgNumModel.getCall_number());
			map.put("authPassword", vmgNumModel.getAuth_password());
			map.put("monitorName", vmgNumModel.getMonitor_name());
			map.put("channelId", vmgNumModel.getChannel_id());
			map.put("comment", vmgNumModel.getComment());

			Map<String, Object> resultMap = ossAdapter.invoke(0xb5, 0x01, map);
			String flag = (String) resultMap.get("lteFlag");
			if ("0".equals(flag)) {
				json.put("status", 0);
				ajaxMethod(json.toString());
			} else {
				// 操作失败
				json.put("status", 1);
				json.put("message", LteFlag.flagReturn(flag));
				ajaxMethod(json.toString());
				logger.warn("ossAdapter.invoke error, flag = " + flag);
			}
		} catch (NoAuthorityException e) {
			json.put("status", 1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
			logger.error("addSysAddress error:" + e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("addVmgNum error:" + e.toString());
		}
		logger.debug("addVmgNum-end");
		return NONE;
	}

	/**
	 * 跳转到修改用户参数模板
	 * 
	 * @return
	 */
	public String toModifyVmgNum() {
		logger.debug("toModifyVmgNum-start");
		//获取视频监控设备
		vmgCfgModelList = new ArrayList<VmgCfgModel>();
		try {
			Map<String, Object> templateMap = new HashMap<String, Object>();
			templateMap.put("ltePageSize", "50");
			templateMap.put("ltePageIndex", "1");
			Map<String, Object> templateResultMap = ossAdapter.invoke(0xb4, 0x05, templateMap);
			String templateFlag = (String) templateResultMap.get("lteFlag");
			if ("0".equals(templateFlag)) {
				List<Map> templateresultList = (List<Map>) templateResultMap.get("vmgCfgInfo");
				VmgCfgModel vmgCfgModel;
				if (templateresultList != null) {
					for(Map rMap : templateresultList){
						vmgCfgModel = new VmgCfgModel();
						
						if(rMap.get("monitorName") != null){
							vmgCfgModel.setMonitor_name(((String)rMap.get("monitorName")).trim());
						}
						
														
						vmgCfgModelList.add(vmgCfgModel);
				}
			}
		
			}	
			Map<String, Object> map = new HashMap<String, Object>();

			// 查询参数
			map.put("ltePageSize", "1");
			map.put("ltePageIndex", "1");
			map.put("callNumberIndex", vmgNumModel.getCall_number_index());
			Map<String, Object> resultMap = ossAdapter.invoke(0xb5, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if (!"0".equals(flag)) {
				// 操作失败
				onlinePage.setTotalPages(0);
				onlinePage.setCurrentPageNum(0);
				logger.warn("ossAdapter.invoke error, flag = " + flag);
				return SUCCESS;
			}
			int totalPages = Integer.parseInt((String) resultMap
					.get("lteTotalQueryCount"));
			onlinePage.setTotalPages(totalPages);

			List<Map> resultList = (List<Map>) resultMap.get("vmgNumInfo");
			if (null != resultList) {
				Map rMap = resultList.get(0);
				if (rMap.get("callNumberIndex") != null) {
					vmgNumModel.setCall_number_index(((String) rMap
							.get("callNumberIndex")).trim());
				}
				if (rMap.get("callNumber") != null) {
					vmgNumModel
							.setCall_number(((String) rMap.get("callNumber"))
									.trim());
				}
				if (rMap.get("authPassword") != null) {
					vmgNumModel.setAuth_password(((String) rMap
							.get("authPassword")).trim());
				}
				if (rMap.get("monitorName") != null) {
					vmgNumModel.setMonitor_name(((String) rMap
							.get("monitorName")).trim());
				}
				if (rMap.get("channelId") != null) {
					vmgNumModel.setChannel_id(((String) rMap.get("channelId"))
							.trim());
				}
				if (rMap.get("comment") != null) {
					vmgNumModel.setComment(((String) rMap.get("comment"))
							.trim());
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("toModifyVmgNum error:" + e.toString());
		}
		logger.debug("toModifyVmgNum-end");
		return "toModifyVmgNum";
	}

	/**
	 * 修改用户参数模板
	 * 
	 * @return
	 */
	public String modifyVmgNum() {
		logger.debug("modifyVmgNum-start");
		JSONObject json = new JSONObject();
		try {

			// 验证参数
			OperResult operResult = vmgNumService.checkVmgNumModel(vmgNumModel);
			if (!operResult.isSuccess()) {
				json.put("status", 1);
				json.put("message", operResult.getRetMsg());
				ajaxMethod(json.toString());
				return NONE;
			}

			// 属性赋值
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("callNumberIndex", vmgNumModel.getCall_number_index());
			map.put("callNumber", vmgNumModel.getCall_number());
			map.put("authPassword", vmgNumModel.getAuth_password());
			map.put("monitorName", vmgNumModel.getMonitor_name());
			map.put("channelId", vmgNumModel.getChannel_id());
			map.put("comment", vmgNumModel.getComment());

			Map<String, Object> resultMap = ossAdapter.invoke(0xb5, 0x03, map);
			String flag = (String) resultMap.get("lteFlag");
			if ("0".equals(flag)) {
				json.put("status", 0);
				ajaxMethod(json.toString());
			} else {
				// 操作失败
				json.put("status", 1);
				json.put("message", LteFlag.flagReturn(flag));
				ajaxMethod(json.toString());
				logger.warn("ossAdapter.invoke error, flag = " + flag);
			}
		} catch (NoAuthorityException e) {
			json.put("status", 1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
			logger.error("addSysAddress error:" + e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("modifyVmgNum error:" + e.toString());
		}
		logger.debug("modifyVmgNum - end");
		return NONE;
	}

	/**
	 * 删除用户参数模板
	 * 
	 * @return
	 */
	public String deleteVmgNum() {
		logger.debug("deleteVmgNum-start");
		JSONObject json = new JSONObject();
		try {
			String callNumberIndex = vmgNumModel.getCall_number_index();
			String[] callNumberIndexArray = callNumberIndex.split(",");
			if (callNumberIndexArray.length == 1) {
				// 删除单个数据
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("callNumberIndex", callNumberIndex);
				Map<String, Object> resultMap = ossAdapter.invoke(0xb5, 0x02,
						map);
				String flag = (String) resultMap.get("lteFlag");
				if (!"0".equals(flag)) {
					// 操作失败
					json.put("status", 1);
					json.put("message", LteFlag.flagReturn(flag));
					ajaxMethod(json.toString());
					logger.warn("ossAdapter.invoke error, flag = " + flag);
					return NONE;
				}
			} else {
				for (String str : callNumberIndexArray) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("callNumberIndex", str);
					Map<String, Object> resultMap = ossAdapter.invoke(0xb5,
							0x02, map);
					String flag = (String) resultMap.get("lteFlag");
					if (!"0".equals(flag)) {
						// 操作失败
						json.put("status", 1);
						json.put("message", LteFlag.flagReturn(flag));
						ajaxMethod(json.toString());
						logger.warn("ossAdapter.invoke error, flag = " + flag);
						return NONE;
					}
				}
			}

			json.put("status", 0);
			ajaxMethod(json.toString());
		} catch (NoAuthorityException e) {
			json.put("status", 1);
			json.put("message", LteFlag.NO_AUTHORITY);
			ajaxMethod(json.toString());
			logger.error("addSysAddress error:" + e);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("deleteVmgNum error:" + e.toString());
		}
		logger.debug("deleteVmgNum-end");
		return NONE;
	}

	/**
	 * 检查视频监控设备是否存在
	 * 
	 * @return
	 */
	public String checkMonitorNameExist() {

		JSONObject json = new JSONObject();
		try {

			Map<String, Object> map = new HashMap<String, Object>();
			if (null == onlinePage) {
				onlinePage = new OnlinePage();
				onlinePage.setCurrentPageNum(1);
				onlinePage.setPageSize(LteConstant.PageSize);
			}
			if (onlinePage.getCurrentPageNum() == 0) {
				onlinePage.setCurrentPageNum(1);
			}

			// 设置查询条件
			map.put("ltePageSize", LteConstant.PageSize + "");
			map.put("ltePageIndex", onlinePage.getCurrentPageNum() + "");
			map.put("monitorName", new String(vmgNumModel.getMonitor_name()
					.getBytes("iso-8859-1"), "utf-8"));
			Map<String, Object> resultMap = ossAdapter.invoke(0xb4, 0x05, map);
			String flag = (String) resultMap.get("lteFlag");
			if (!"0".equals(flag)) {
				// 查询失败
				json.put("status", -1);
				logger.warn("ossAdapter.invoke error, flag = " + flag);
				return "turntoVmgNum";
			}

			// 查询到的数据总个数
			int lteTotalQueryCount = Integer.parseInt((String) resultMap
					.get("lteTotalQueryCount"));
			if (lteTotalQueryCount == 0) {
				json.put("status", -1);
				logger.warn("ossAdapter.lteTotalQueryCount error, flag = "
						+ flag);
			} else {
				json.put("status", 0);
			}
			ajaxMethod(json.toString());
		} catch (Exception e) {
			json.put("status", -1);
			logger.error("checkMonitorNameExist error:" + e.toString());
		}

		return NONE;
	}

	// 异步请求返回字符串
	private void ajaxMethod(String content) {
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

	public OnlinePage getOnlinePage() {
		return onlinePage;
	}

	public void setOnlinePage(OnlinePage onlinePage) {
		this.onlinePage = onlinePage;
	}

	public String getShowMessage() {
		return showMessage;
	}

	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}

	public VmgNumModel getVmgNumModel() {
		return vmgNumModel;
	}

	public void setVmgNumModel(VmgNumModel vmgNumModel) {
		this.vmgNumModel = vmgNumModel;
	}

	public List<VmgNumModel> getVmgNumModelList() {
		return vmgNumModelList;
	}

	public void setVmgNumModelList(List<VmgNumModel> vmgNumModelList) {
		this.vmgNumModelList = vmgNumModelList;
	}

	public VmgNumModel getQueryVmgNumModel() {
		return queryVmgNumModel;
	}

	public void setQueryVmgNumModel(VmgNumModel queryVmgNumModel) {
		this.queryVmgNumModel = queryVmgNumModel;
	}

	public List<VmgCfgModel> getVmgCfgModelList() {
		return vmgCfgModelList;
	}

	public void setVmgCfgModelList(List<VmgCfgModel> vmgCfgModelList) {
		this.vmgCfgModelList = vmgCfgModelList;
	}

}
